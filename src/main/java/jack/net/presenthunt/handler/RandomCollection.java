package jack.net.presenthunt.handler;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;


public class RandomCollection<E> {

    public final NavigableMap<Double, E> map = new TreeMap<>();
    private final Random random = new Random();
    private double total = 0;

    public void add(Double weight, E value) {
        if (weight > 0) {
            total += weight;
            map.put(total, value);
        }
    }


    public E getRandom() {
        if (total == 0) throw new RuntimeException("Trying to get random value for empty RandomCollection");
        final double value = random.nextDouble() * total;
        return map.higherEntry(value).getValue();
    }
}
