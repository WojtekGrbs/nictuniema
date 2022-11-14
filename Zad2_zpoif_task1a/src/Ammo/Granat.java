package Ammo;

import Ammo.Ammo;

public abstract class Granat extends Ammo {
    private static int count;
    private int id;
    private boolean zabezpieczony;

    public Granat() {
        count++;
        this.id = count;
        this.zabezpieczony = RandomSupplier.provideRandomSafeGenerator(false).get();
    }

    public boolean isZabezpieczony() {
        return zabezpieczony;
    }

    public void setZabezpieczony(boolean zabezpieczony) {
        this.zabezpieczony = zabezpieczony;
    }
}
