import Guests.Guest;
import Guests.Student;
import Guests.Turysta;
import Places.MieszkanieKredyt;
import Places.MieszkanieWoda;
import Places.OpuszczonySzpital;
import Places.StaryZamek;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Demonstrator {
    public static void main(String[] args) {
        Student student1 = new Student();
        Student student2 = new Student();
        Student student3 = new Student();
        Student student4 = new Student();
        Student student5 = new Student();

        Turysta turysta1 = new Turysta();
        Turysta turysta2 = new Turysta();
        Turysta turysta3 = new Turysta();
        Turysta turysta4 = new Turysta();
        Turysta turysta5 = new Turysta();
        List<Guest> guests = Arrays.asList(student1, student2, student3,student4,student5,turysta1,turysta2,turysta3,turysta4,turysta5);

        MieszkanieWoda mieszkanieWoda = new MieszkanieWoda();
        MieszkanieKredyt mieszkanieKredyt = new MieszkanieKredyt();
        OpuszczonySzpital opuszczonySzpital = new OpuszczonySzpital();
        StaryZamek staryZamek = new StaryZamek();
        for(Guest guest : guests){
            mieszkanieWoda.nawiedz(guest);
            mieszkanieKredyt.nawiedz(guest);
            opuszczonySzpital.nawiedz(guest);
            staryZamek.nawiedz(guest);
        }

    }
}
