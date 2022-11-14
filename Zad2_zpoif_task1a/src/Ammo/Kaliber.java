package Ammo;

public enum Kaliber {
    dwa(2.0), osiem(7.0), pol(0.57), siedem(7.0);
    private final Double dlugosc;

    Kaliber(Double i) {
        this.dlugosc = i;

    }

    public Double getDlugosc() {
        return dlugosc;
    }
}
