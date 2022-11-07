package Places;

import Guests.Guest;
import Straszak.Straszak;

import java.util.Random;

public class MieszkanieWoda extends House {
    @Override
    protected void nawiedzPralnie(Guest guest) {
        DuchKomornika duch = new DuchKomornika();
        duch.nastraszKogos(guest);
    }

    @Override
    protected void nawiedzLazienke(Guest guest) {
        DuchPlywaka duch = new DuchPlywaka();
        duch.nastraszKogos(guest);
    }

    private class DuchPlywaka extends Straszak {
        @Override
        public void nastraszKogos(Guest guest) {
            Random random = new Random();
            int moc = 10+ random.nextInt(21);
            guest.przestraszMnie(moc);
        }
    }
}
