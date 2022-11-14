package Ammo;

import Ammo.Kaliber;

import java.util.Random;
import java.util.function.Supplier;

public class RandomSupplier {
    private static Random random = new Random();




    public static Supplier<Boolean> provideRandomSafeGenerator(boolean alwaysUnlocked){
        Supplier<Boolean> supplier = () -> {
            if (alwaysUnlocked){
                return false;
            }
            else {
                if(random.nextDouble() > 0.05){
                    return true;
                }
            }
            return false;
        };
        return supplier;
    }

    public static Supplier<Integer> provideRandomCO2EmissionGenerator(int a, int b){
        if (a > 30) {
            a=31;
        }
        if (b>30){
            b=31;
        } //zabezpieczenie przed błędnie podanymi danymi
        Integer[] lista = {a,b};
        Supplier<Integer> supplier = () ->{
            return lista[random.nextInt(2)];
        };
        return supplier;
    }

    public static Supplier<Kaliber> provideRandomCaliberGenerator(){
        Supplier<Kaliber> supplier = () -> {
            return Kaliber.values()[random.nextInt(3)];
        };
        return supplier;
    }


}
