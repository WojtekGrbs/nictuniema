import Ammo.Ammo;
import Ammo.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class AmmoChest {
    protected static List<Ammo> lista= new ArrayList<>();

    public List<Granat> getEcoArmourPiercingGrenades() {
        List<Granat> result = new ArrayList<>();
        Consumer<Ammo> consumer = (Granat) -> {
            if (Granat instanceof GranatPrzeciwpancerny) {
                if (((GranatPrzeciwpancerny) Granat).getEmisja() > 225) result.add((Granat) Granat);
            }
        };
        lista.forEach(consumer);
        return result;
    }
    public void findUnlockedGranades(){
        Consumer<Ammo> consumer = Granat ->{
            if(Granat instanceof GranatObronny || Granat instanceof GranatZaczepny){
                if (((Granat) Granat).isZabezpieczony()){
                    System.out.println("Uwaga");
                }
            }
        };
        lista.forEach(consumer);
    }
    public void getSummarizedCaliber(){

        AtomicInteger count = new AtomicInteger();
        AtomicReference<Double> sum = new AtomicReference<>((double) 0);

        Consumer<Ammo> consumer = naboj -> {
            if (naboj instanceof Naboj){
                count.getAndIncrement();
                sum.updateAndGet(v -> v + ((Naboj) naboj).getKaliberVal());
            }
            if (count.get() > 100){
                System.out.println(sum.get());
            }
        };
        lista.forEach(consumer);
    }

    public AmmoChest() {
        for (int i = 0; i < 20; i++) {
            lista.add(new GranatObronny());
            lista.add(new GranatPrzeciwpancerny(new Random().nextInt(30), new Random().nextInt(30)));
            lista.add(new GranatZaczepny());
        }
        for (int i = 0; i < 2000; i++) {
            lista.add(new Naboj());
        }
        Collections.shuffle(lista);
    }

    public static Ammo get(int i){
        return lista.get(i);
    };
}