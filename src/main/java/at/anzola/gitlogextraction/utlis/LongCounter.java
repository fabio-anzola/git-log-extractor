package at.anzola.gitlogextraction.utlis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The LongCounter class
 *
 * @param <K> An Object
 * @author fabioanzola
 */
public class LongCounter<K> extends HashMap<K, Long> {
    /**
     * Standard Comparator for Keys
     */
    private Comparator<K> keyComparator = (a, b) -> ((Comparable) a).compareTo((Comparable) b);

    /**
     * Comparator for Value- and Key- pairs
     */
    private final Comparator<Entry<K, Long>> entryComparator = (a, b) -> {
        int d = Long.compare(a.getValue(), b.getValue());
        if (d != 0) {
            return d;
        }
        return keyComparator.compare(b.getKey(), a.getKey());
    };

    /**
     * Key-comparator used for sorting
     *
     * @param keyComparator for sorting of keys
     */
    public LongCounter(Comparator<K> keyComparator) {
        this.keyComparator = keyComparator;
    }

    /**
     * Generates a new LongCounter with Standard-capacity & load-factor
     */
    public LongCounter() {
        // nothing to do
    }

    /**
     * Creates LongCounter out of generic map with Long values
     *
     * @param m The map
     */
    public LongCounter(Map<? extends K, Long> m) {
        super(m);
    }

    /**
     * Creates LongCounter out of Object Array
     *
     * @param k an Array with elements to be counted
     */
    public LongCounter(K... k) {
        for (K value : k) {
            this.put(value);
        }
    }

    /**
     * Creates LongCounter out of Collection
     *
     * @param k The Collection to be counted from
     */
    public LongCounter(Collection<K> k) {
        Iterator<K> iterator = k.iterator();
        while (iterator.hasNext()) {
            this.put(iterator.next());
        }
    }

    /**
     * Generates a LongCounter out of a String (Counts characters)
     *
     * @param s The Symbols to be counted
     * @return The LongCounter object
     */
    public static LongCounter<Character> fromString(CharSequence s) {
        LongCounter<Character> counter = new LongCounter<>();

        for (int i = 0; i < s.length(); i++) {
            counter.put(s.charAt(i));
        }
        return counter;
    }

    /**
     * Increases the counter by "value" for the Key
     *
     * @param key   The Key to be incremented
     * @param value The Value to be increased by
     * @return The old value or null
     */
    public Long put(K key, long value) {
        if (this.containsKey(key)) {
            long old = this.get(key);
            super.put(key, old + value);
            return old;
        } else {
            super.put(key, value);
            return null;
        }
    }

    /**
     * Increases the counter for the Key by 1
     *
     * @param key The key which should e incremented
     * @return The old value of the Key or null
     */
    public Long put(K key) {
        if (this.containsKey(key)) {
            long old = this.get(key);
            super.put(key, old + 1);
            return old;
        } else {
            super.put(key, 1L);
            return null;
        }
    }

    /**
     * Increases the Values from Map "m" to LongCouter
     *
     * @param m The Map with the added values
     */
    public void putAll(Map<? extends K, ? extends Long> m) {
        for (Entry<? extends K, ? extends Long> entry : m.entrySet()) {
            if (this.containsKey(entry.getKey())) {
                super.put(entry.getKey(), this.get(entry.getKey()) + entry.getValue());
            } else {
                super.put(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Decreases the Values from Map "m" to LongCouter
     *
     * @param m The Map with the subtracted values
     */
    public void subtractAll(Map<? extends K, ? extends Long> m) {
        m.forEach((key, value) -> this.put(key, -value));
    }

    /**
     * Returns Key with max amount of value, sorts afterwards
     *
     * @param n The number of values
     * @return A list from Key/Counter
     */
    public List<Entry<K, Long>> mostCommon(int n) {
        List<Entry<K, Long>> entrys = new ArrayList<>(entrySet());
        entrys.sort(entryComparator.reversed());

        return entrys.subList(0, Math.min(n, size()));
    }

    /**
     * Returns Key with min amount of value, sorts afterwards
     *
     * @param n The number of values
     * @return A list from Key/Counter
     */
    public List<Entry<K, Long>> lessCommon(int n) {
        List<Entry<K, Long>> entrys = new ArrayList<>(entrySet());
        entrys.sort(entryComparator);
        return entrys.subList(0, Math.min(n, size()));
    }

    /**
     * Deletes all Values with Counter-value == 0
     */
    public void clearZeros() {
        entrySet().removeIf(kLongEntry -> kLongEntry.getValue() == 0);
    }
}
