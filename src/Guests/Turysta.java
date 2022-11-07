package Guests;

import Guests.Status;

public class Turysta extends Guest {


    @Override
    public void przestraszMnie(int Moc) {
        if(Moc > this.odpornosc) {
            if(this.status == Status.NORMALNY){
                this.status = Status.PRZERAZONY;
            }
            else if(this.status == Status.PRZERAZONY){
                this.status = Status.PANIKA;
            }
        }
        else {
            if(this.status == Status.PANIKA) {
                this.status = Status.PRZERAZONY;
            }
            else if(this.status == Status.PRZERAZONY) {
                this.status = Status.NORMALNY;
            }
        }
    }
}
