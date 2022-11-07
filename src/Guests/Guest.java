package Guests;

import Guests.Status;

import java.util.Random;

public abstract class Guest {
    protected int odpornosc;
    protected Status status = Status.NORMALNY;

    public void przestraszMnie(int moc){

    };

    public Guest() {
        this.odpornosc = 10 + new Random().nextInt(11);
    }

    @Override
    public String toString() {
        return "Guests.Guest{" +
                "status=" + status +
                '}';
    }

    public Status getStatus() {
        return status;
    }
}
