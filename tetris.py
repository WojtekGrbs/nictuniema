import sys
from PyQt5.QtWidgets import QApplication, QMainWindow, QAction, QFileDialog, QGraphicsView, QGraphicsScene, QRubberBand, QVBoxLayout, QWidget, QPushButton, QHBoxLayout
from PyQt5.QtGui import QPixmap, QKeyEvent
from PyQt5.QtCore import Qt, QRect, QSize, QPoint
from PyQt5 import QtCore
import pdfplumber
from pdf2image import convert_from_path

class PDFViewer(QMainWindow):
    def __init__(self):
        super().__init__()
        self.initUI()

    def initUI(self):
        self.setWindowTitle('PDF Chunker')

        # Wymiary okna - nie powinno wplywac na dalsze mapowanie zaznaczenia
        self.setGeometry(100, 100, 1920, 1080)

        self.centralWidget = QWidget(self)
        self.setCentralWidget(self.centralWidget)
        self.layout = QVBoxLayout(self.centralWidget)

        self.graphicsView = CustomGraphicsView(self)
        self.layout.addWidget(self.graphicsView)
        self.graphicsScene = QGraphicsScene(self)
        self.graphicsView.setScene(self.graphicsScene)

        # Przyciski nawigacji po dokumencie
        # Maksymalnie jedna strona na dokument - do zmiany?
        self.navigationLayout = QHBoxLayout()
        self.prevButton = QPushButton('Poprzednia Strona', self)
        self.prevButton.clicked.connect(self.prevPage)
        self.nextButton = QPushButton('Następna Strona', self)
        self.nextButton.clicked.connect(self.nextPage)
        self.navigationLayout.addWidget(self.prevButton)
        self.navigationLayout.addWidget(self.nextButton)
        self.layout.addLayout(self.navigationLayout)

        openFile = QAction('Open', self)
        openFile.setShortcut('O')
        openFile.setStatusTip('Open new PDF')
        openFile.triggered.connect(self.showDialog)


        menubar = self.menuBar()
        fileMenu = menubar.addMenu('&File')
        fileMenu.addAction(openFile)

        self.statusBar()
        self.show()

        self.origin = QPoint()
        self.rubberBand = QRubberBand(QRubberBand.Rectangle, self.graphicsView)
        self.graphicsView.viewport().installEventFilter(self)

    def showDialog(self):
        fname, _ = QFileDialog.getOpenFileName(self, 'Open file', '', "PDF files (*.pdf)")
        if fname:
            print(f"Wybrany plik: {fname}")
            self.loadPDF(fname)

    def loadPDF(self, path):
        try:
            self.pdf_path = path

            # Wymaga popplera'a
            self.images = convert_from_path(path, poppler_path=r'C:\Users\wojte\poppler-24.02.0\Library\bin')
            self.current_page = 0
            self.showPage(self.current_page)

        except Exception as e:
            print(f"Nie udalo sie wczytac PDF: {e}")

    def showPage(self, page_number):
        try:
            if page_number < 0 or page_number >= len(self.images):
                return

            # Wyswietlamy obraz aktualnej strony pdf-a
            image = self.images[page_number]

            # Zapisujemy go tymczasowo
            image.save("temp_page.png")

            # Tworzymy Pixmape na podstawie obrazu strony pdf-a
            self.pixmap = QPixmap("temp_page.png")
            self.graphicsScene.clear()
            self.graphicsScene.addPixmap(self.pixmap)
            self.graphicsScene.update()
            self.current_page = page_number
        except Exception as e:
            print(f"Nie udalo sie wyswietlic strony: {e}")

    def prevPage(self):
        if self.current_page > 0:
            self.showPage(self.current_page - 1)
    def nextPage(self):
        if self.current_page < len(self.images) - 1:
            self.showPage(self.current_page + 1)

    def keyPressEvent(self, event):
        if event.type() == event.KeyPress:
            key_text = event.text()
            print(f"Last Key Pressed: {key_text}")

    def keyReleaseEvent(self, event):
        if event.type() == event.KeyPress:
            key_text = event.text()
            print(f"Key Released: {key_text}")
    # Filtrujemy event'y zeby nie wywalalo nam aplikacji bo sledzimy ruch myszki :)
    def eventFilter(self, source, event):

        # LPM Triggeruje Rubberband'a
        if event.type() == event.MouseButtonPress and source is self.graphicsView.viewport():
            if event.button() == Qt.LeftButton:
                self.rubberBand.hide()
                self.origin = event.pos()
                self.rubberBand.setGeometry(QRect(self.origin, QSize()))
                self.rubberBand.show()
            return True

        # Trackujemy mouse move tylko po LPM i updatujemy rubberband
        elif event.type() == event.MouseMove and source is self.graphicsView.viewport():
            if not self.origin.isNull():
                self.rubberBand.setGeometry(QRect(self.origin, event.pos()).normalized())

            return True

        # LPM release triggeruje extract tekstu na zaznaczonym kawałku
        elif event.type() == event.MouseButtonRelease and source is self.graphicsView.viewport():
            if event.button() == Qt.LeftButton:

                self.extractText()
                self.origin = QPoint()
            return True

        return super().eventFilter(source, event)

    def extractText(self):
        rect = self.rubberBand.geometry()
        x1, y1 = self.graphicsView.mapToScene(rect.topLeft()).toPoint().x(), self.graphicsView.mapToScene(rect.topLeft()).toPoint().y()
        x2, y2 = self.graphicsView.mapToScene(rect.bottomRight()).toPoint().x(), self.graphicsView.mapToScene(rect.bottomRight()).toPoint().y()

        pdf_page = pdfplumber.open(self.pdf_path).pages[self.current_page]
        pdf_width = pdf_page.width
        pdf_height = pdf_page.height

        scene_width = self.pixmap.width()
        scene_height = self.pixmap.height()

        # Mapujemy wspolrzedne rubberband'a na wspolrzedne pdf'a
        pdf_x1 = min(max(x1 * (pdf_width / scene_width), 0), pdf_width)
        pdf_y1 = min(max(y1 * (pdf_height / scene_height), 0), pdf_height)
        pdf_x2 = min(max(x2 * (pdf_width / scene_width), 0), pdf_width)
        pdf_y2 = min(max(y2 * (pdf_height / scene_height), 0), pdf_height)

        try:
            with pdfplumber.open(self.pdf_path) as pdf:
                page = pdf.pages[self.current_page]
                bbox = (pdf_x1, pdf_y1, pdf_x2, pdf_y2)
                cropped_page = page.within_bbox(bbox)

                # Get the bounding boxes of the tables on the page.
                bboxes = [table.bbox for table in cropped_page.find_tables()]

                def not_within_bboxes(obj):
                    """Check if the object is in any of the table's bbox."""

                    def obj_in_bbox(_bbox):
                        """See https://github.com/jsvine/pdfplumber/blob/stable/pdfplumber/table.py#L404"""
                        v_mid = (obj["top"] + obj["bottom"]) / 2
                        h_mid = (obj["x0"] + obj["x1"]) / 2
                        xtemp0, top, xtemp1, bottom = _bbox
                        return (h_mid >= xtemp0) and (h_mid < xtemp1) and (v_mid >= top) and (v_mid < bottom)

                    return not any(obj_in_bbox(__bbox) for __bbox in bboxes)

                tables = cropped_page.extract_tables()
                text = cropped_page.filter(not_within_bboxes).extract_text()
                print('-------------Zaznaczenie--------------')

                if text:
                    print("####### Tekst #######")
                    print(text)
                    if not tables:
                        print('@@@@@@@@@@@@@@@@@@@@@@')
                        print(f'Liczba słów: {len(text.split())}')
                if tables:
                    table_data = ""
                    for table in tables:
                        for row_idx, row in enumerate(table):
                            row_data = f"Wiersz{row_idx + 1}: " + ", ".join([f"Kolumna{col_idx + 1}:{col}" for col_idx, col in enumerate(row)])
                            table_data += row_data + "\n"
                    print("####### Tabele #######")
                    print(table_data)
                    print('@@@@@@@@@@@@@@@@@@@@@@')
                    print(f'Liczba słów: {len((text + table_data).split())}')
                print()
                if not text and not tables:
                    print("Brak tekstu w zaznaczonym obszarze!")
        except Exception as e:
            print(f"Error extracting text: {e}")


class CustomGraphicsView(QGraphicsView):
    def __init__(self, parent=None):
        super().__init__(parent)
        self.setTransformationAnchor(QGraphicsView.AnchorUnderMouse)

    def wheelEvent(self, event):
        if event.modifiers() == Qt.ControlModifier:
            factor = 1.25 if event.angleDelta().y() > 0 else 0.8
            self.scale(factor, factor)
            print(f"Zoom factor: {factor}")
        else:
            super().wheelEvent(event)
if __name__ == '__main__':
    app = QApplication(sys.argv)
    viewer = PDFViewer()
    sys.exit(app.exec_())
