package Ammo;

import Ammo.Granat;

public class GranatPrzeciwpancerny extends Granat {
    private int emisja;

    public GranatPrzeciwpancerny(int emisja, int emisja2) {
        super();
        this.emisja = 220 + RandomSupplier.provideRandomCO2EmissionGenerator(emisja,emisja2).get();
    }

    public int getEmisja() {
        return emisja;
    }
}
