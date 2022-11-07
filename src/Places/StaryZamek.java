package Places;

import Guests.Guest;
import Straszak.Straszak;

public class StaryZamek extends Place {

    private static int bialaMoc = 20;

    public static class BialaDama extends Straszak {

        @Override
        public void nastraszKogos(Guest guest) {
            guest.przestraszMnie(StaryZamek.bialaMoc);
        }
    }

    @Override
    public void nawiedz(Guest guest) {
        class DuchZamku extends Straszak{

            @Override
            public void nastraszKogos(Guest guest) {
                guest.przestraszMnie(0);
            }
        }
        DuchZamku duch1 = new DuchZamku();
        DuchZamku duch2 = new DuchZamku();
        BialaDama bialaDama = new BialaDama();

        bialaDama.nastraszKogos(guest);
        duch1.nastraszKogos(guest);
        duch2.nastraszKogos(guest);


    }
}
