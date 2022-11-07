package Places;

import Guests.Guest;
import Guests.Status;
import Straszak.Straszak;

public class OpuszczonySzpital extends Place {
    @Override
    public void nawiedz(Guest guest) {
        new Straszak(){

            @Override
            public void nastraszKogos(Guest guest) {
                if (guest.getStatus() == Status.NORMALNY) {
                    guest.przestraszMnie(15);
                }
            }
        }.nastraszKogos(guest);
    }
}
