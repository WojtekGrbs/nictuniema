package Places;

import Guests.Guest;
import Straszak.Straszak;

import java.util.Random;

import static java.lang.Math.floor;

public abstract class House extends Place {

    private int kwotaZaleglosci;


    protected void nawiedzLazienke(Guest guest){};
    protected void nawiedzPralnie(Guest guest){};

    public House() {
        Random random = new Random();
        this.kwotaZaleglosci = random.nextInt(10000);
    }
    
    protected class DuchKomornika extends Straszak {

        @Override
        public void nastraszKogos(Guest guest) {
            guest.przestraszMnie((int) floor(House.this.kwotaZaleglosci/500));
        }
    }

    public int getKwotaZaleglosci() {
        return kwotaZaleglosci;
    }
}
