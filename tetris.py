import sys
import os
from PyQt5.QtWidgets import (QApplication, QMainWindow, QAction, QFileDialog, QGraphicsView, QGraphicsScene, QRubberBand, QVBoxLayout,
                             QWidget, QPushButton, QHBoxLayout, QTableWidget, QTableWidgetItem, QDialog, QHeaderView, QDialogButtonBox, QLabel)
from PyQt5.QtGui import QPixmap, QWheelEvent
from PyQt5.QtCore import Qt, QRect, QSize, QPoint
import pdfplumber
from pdf2image import convert_from_path

class Chunk:
    def __init__(self, current_page, file_name):

        self.context_table = []
        self.content_table = []

        self.file_name = file_name
        self.page = current_page + 1

    def save_chunk(self):
        self.context = "Konkteksty: " + "".join(self.context_table)
        self.content = "".join(self.content_table)
        print('----Zapisano Chunk------')
        print(self.page)
        print(self.file_name)
        print(self.content)
        print(self.context)

    def calculate_length(self):
        if len(self.context_table) and len(self.content_table):
            return len(("Konkteksty: " + "".join(self.context_table)+
            "".join(self.content_table)).split())
        elif len(self.context_table):
            return len(("Konkteksty: " + "".join(self.context_table)).split())
        elif len(self.context_table):
            return len(("".join(self.content_table)).split())
        return 0
class PDFViewer(QMainWindow):
    def __init__(self):
        super().__init__()
        self.initUI()

    def initUI(self):
        self.setWindowTitle('PDF Viewer')
        self.setGeometry(100, 100, 800, 600)

        self.centralWidget = QWidget(self)
        self.setCentralWidget(self.centralWidget)
        self.layout = QVBoxLayout(self.centralWidget)

        self.graphicsView = CustomGraphicsView(self)
        self.layout.addWidget(self.graphicsView)
        self.graphicsScene = QGraphicsScene(self)
        self.graphicsView.setScene(self.graphicsScene)

        self.navigationLayout = QHBoxLayout()
        self.prevButton = QPushButton('Previous', self)
        self.prevButton.clicked.connect(self.prevPage)
        self.nextButton = QPushButton('Next', self)
        self.nextButton.clicked.connect(self.nextPage)
        self.navigationLayout.addWidget(self.prevButton)
        self.navigationLayout.addWidget(self.nextButton)

        self.labl = QLabel(self)
        self.labl.setText('Długość Chunk\'a: 0')
        self.navigationLayout.addWidget(self.labl)
        self.layout.addLayout(self.navigationLayout)



        self.file_name = ''
        openFile = QAction('Open', self)
        openFile.setShortcut('Ctrl+O')
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

        self.zoom_factor = 1.0

        self.chunks = []
        self.current_chunk = None
        self.context_mode = False
        self.content_mode = False

    def showDialog(self):
        fname, _ = QFileDialog.getOpenFileName(self, 'Open file', '', "PDF files (*.pdf)")
        if fname:
            print(f"Selected file: {fname}")
            self.file_name = os.path.basename(fname)
            self.loadPDF(fname)

    def loadPDF(self, path):
        try:
            self.pdf_path = path
            self.images = convert_from_path(path, poppler_path=r'C:\Users\wojte\poppler-24.02.0\Library\bin')
            self.current_page = 0
            self.showPage(self.current_page)
        except Exception as e:
            print(f"Error loading PDF: {e}")

    def showPage(self, page_number):
        try:
            if page_number < 0 or page_number >= len(self.images):
                return
            image = self.images[page_number]
            image.save("temp_page.png")  # Save the image to use it with QPixmap
            self.pixmap = QPixmap("temp_page.png")
            self.graphicsScene.clear()
            self.graphicsScene.addPixmap(self.pixmap)
            self.graphicsScene.update()
            self.current_page = page_number
            self.zoom_factor = 1.0
            self.graphicsView.resetTransform()
        except Exception as e:
            print(f"Error showing page: {e}")

    def prevPage(self):
        if self.current_page > 0:
            self.showPage(self.current_page - 1)

    def nextPage(self):
        if self.current_page < len(self.images) - 1:
            self.showPage(self.current_page + 1)

    def keyPressEvent(self, event):
        if event.key() == Qt.Key_N:
            self.startNewChunk()
        elif event.key() == Qt.Key_Z and event.modifiers() == Qt.ControlModifier:
            if self.context_mode:
                if len(self.current_chunk.context_table):
                    self.current_chunk.context_table.pop(0)
                    self.labl.setText(f'Dlugosc chunk\'a: {self.current_chunk.calculate_length()}')
                    self.labl.adjustSize()
            elif self.content_mode:
                if len(self.current_chunk.content_table):
                    self.current_chunk.content_table.pop(0)
                    self.labl.setText(f'Dlugosc chunk\'a: {self.current_chunk.calculate_length()}')
                    self.labl.adjustSize()
        elif event.key() == Qt.Key_C:
            self.context_mode = True
            self.content_mode = False
        elif event.key() == Qt.Key_X:
            self.context_mode = False
            self.content_mode = True
        elif event.key() == Qt.Key_S and event.modifiers() == Qt.ControlModifier:
            self.current_chunk.save_chunk()
        event.accept()

    def eventFilter(self, source, event):
        if event.type() == event.MouseButtonPress and source is self.graphicsView.viewport():
            if event.button() == Qt.LeftButton:
                self.origin = event.pos()
                self.rubberBand.setGeometry(QRect(self.origin, QSize()))
                self.rubberBand.show()
            return True
        elif event.type() == event.MouseMove and source is self.graphicsView.viewport():
            if not self.origin.isNull():
                self.rubberBand.setGeometry(QRect(self.origin, event.pos()).normalized())
            return True
        elif event.type() == event.MouseButtonRelease and source is self.graphicsView.viewport():
            if event.button() == Qt.LeftButton:
                self.rubberBand.hide()
                self.extractText()
                self.origin = QPoint()
            return True

        return super().eventFilter(source, event)

    def startNewChunk(self):
        self.labl.setText(f'Dlugosc chunk\'a: 0')
        self.labl.adjustSize()
        self.current_chunk = Chunk(current_page=self.current_page,
                                   file_name=self.file_name)

    def handleZoom(self, event):
        delta = event.angleDelta().y()
        factor = 1.25 if delta > 0 else 0.8
        self.zoom_factor *= factor
        self.graphicsView.scale(factor, factor)
        print(f"Zoom factor: {self.zoom_factor}")

    def extractText(self):
        rect = self.rubberBand.geometry()
        x1, y1 = self.graphicsView.mapToScene(rect.topLeft()).toPoint().x(), self.graphicsView.mapToScene(rect.topLeft()).toPoint().y()
        x2, y2 = self.graphicsView.mapToScene(rect.bottomRight()).toPoint().x(), self.graphicsView.mapToScene(rect.bottomRight()).toPoint().y()

        pdf_page = pdfplumber.open(self.pdf_path).pages[self.current_page]
        pdf_width = pdf_page.width
        pdf_height = pdf_page.height

        scene_width = self.pixmap.width()
        scene_height = self.pixmap.height()

        pdf_x1 = min(max(x1 * (pdf_width / scene_width), 0), pdf_width)
        pdf_y1 = min(max(y1 * (pdf_height / scene_height), 0), pdf_height)
        pdf_x2 = min(max(x2 * (pdf_width / scene_width), 0), pdf_width)
        pdf_y2 = min(max(y2 * (pdf_height / scene_height), 0), pdf_height)

        print(f"Mapped coordinates to PDF: ({pdf_x1}, {pdf_y1}, {pdf_x2}, {pdf_y2})")

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
                    tables = self.showTableEditor(tables)
                    table_data = ""
                    for table in tables:
                        for row_idx, row in enumerate(table):
                            row_data = f"Wiersz{row_idx + 1}: " + ", ".join(
                                [f"Kolumna{col_idx + 1}:{col}" for col_idx, col in enumerate(row)])
                            table_data += row_data + "\n"
                    print("####### Tabele #######")
                    print(table_data)
                    print('@@@@@@@@@@@@@@@@@@@@@@')
                    print(f'Liczba słów: {len((text + table_data).split())}')
                if self.current_chunk:
                    if self.context_mode:
                        self.current_chunk.context_table.append(text)
                    elif self.content_mode:
                        if tables:
                            self.current_chunk.content_table.append(text + table_data)
                        else:
                            self.current_chunk.content_table.append(text)
                    self.labl.setText(f'Dlugosc chunk\'a: {self.current_chunk.calculate_length()}')
                    self.labl.adjustSize()
                if not text and not tables:
                    print("Brak tekstu w zaznaczonym obszarze!")
        except Exception as e:
            print(f"Error extracting text: {e}")

    def showTableEditor(self, tables):
        edited_tables = []
        for table in tables:
            dialog = TableEditorDialog(table, self)
            if dialog.exec_() == QDialog.Accepted:

                edited_table = dialog.getTableData()
                print(f'EDYTOWANA: {edited_table}')
                edited_tables.append(edited_table)
        return edited_tables

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

class TableEditorDialog(QDialog):
    def __init__(self, table_data, parent=None):
        super().__init__(parent)
        self.setWindowTitle("Edit Table")
        self.layout = QVBoxLayout(self)

        self.tableWidget = QTableWidget(self)
        self.tableWidget.setRowCount(len(table_data))
        self.tableWidget.setColumnCount(len(table_data[0]))
        for row_idx, row in enumerate(table_data):
            for col_idx, cell in enumerate(row):
                self.tableWidget.setItem(row_idx, col_idx, QTableWidgetItem(cell))

        self.layout.addWidget(self.tableWidget)
        self.tableWidget.horizontalHeader().setSectionResizeMode(QHeaderView.Stretch)
        self.tableWidget.verticalHeader().setSectionResizeMode(QHeaderView.Stretch)

        self.buttonBox = QDialogButtonBox(QDialogButtonBox.Ok | QDialogButtonBox.Cancel)
        self.buttonBox.accepted.connect(self.accept)
        self.buttonBox.rejected.connect(self.reject)
        self.layout.addWidget(self.buttonBox)

        self.setLayout(self.layout)

    def getTableData(self):
        row_count = self.tableWidget.rowCount()
        col_count = self.tableWidget.columnCount()
        table_data = []
        for row_idx in range(row_count):
            row_data = []
            for col_idx in range(col_count):
                item = self.tableWidget.item(row_idx, col_idx)
                row_data.append(item.text() if item else '')
            table_data.append(row_data)
        return table_data
if __name__ == '__main__':
    app = QApplication(sys.argv)
    viewer = PDFViewer()
    sys.exit(app.exec_())
