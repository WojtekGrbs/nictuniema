package Ammo;
impo
import Ammo.Ammo;

public class Naboj extends Ammo {

    private Kaliber kaliber;
    private Double kaliberVal;
    private int id;
    private static int count;

    public Naboj() {
        count++;
        this.id = count;
        this.kaliberVal = RandomSupplier.provideRandomCaliberGenerator().get().getDlugosc();
    }

    public Double getKaliberVal() {
        return kaliberVal;
    }

    public void setKaliberVal(Double kaliberVal) {
        this.kaliberVal = kaliberVal;
    }
}
