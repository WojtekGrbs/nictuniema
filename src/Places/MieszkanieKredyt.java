package Places;

import Guests.Guest;
import Places.House;

public class MieszkanieKredyt extends House {


    @Override
    protected void nawiedzLazienke(Guest guest) {

        DuchKomornika duch1 = new DuchKomornika();
        DuchKomornika duch2 = new DuchKomornika();

        duch1.nastraszKogos(guest);
        duch2.nastraszKogos(guest);
    }

    @Override
    protected void nawiedzPralnie(Guest guest) {
        DuchKomornika duch = new DuchKomornika();
        duch.nastraszKogos(guest);
    }
}
