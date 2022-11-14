import Ammo.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class AmmoChest2 extends AmmoChest{
    List<Ammo> lista = AmmoChest.lista;

    public void upgradeCaliber(Double newCaliber){
        lista.forEach(ammo -> {
            if (ammo instanceof Naboj && ((Naboj) ammo).getKaliberVal() > 5.56)
                ((Naboj) ammo).setKaliberVal(newCaliber);
        });
    }
    public void replaceLocked4All(){
        boolean podmiana = new Random().nextBoolean();
        lista.forEach(ammo -> {
            if (ammo instanceof GranatObronny){
                ((GranatObronny) ammo).setZabezpieczony(podmiana);
            }
        });
    }
    public void getSummarizedCO2Emission(){
        AtomicInteger suma = new AtomicInteger();
        lista.forEach(ammo ->{
            if(ammo instanceof GranatPrzeciwpancerny){
                suma.addAndGet(((GranatPrzeciwpancerny) ammo).getEmisja());
            }
        });
        System.out.println(suma.get());
    }

    public interface MyAmmoGetter{
        public Ammo getGeneralAmmoByIndex(int index);
    }
    public MyAmmoGetter createMyAmmmoGetter(){
        return AmmoChest::get();
    }
}
