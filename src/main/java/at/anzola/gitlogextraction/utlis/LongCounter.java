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
     * Standard-Vergleichsoperator für Keys, die Comparable implementieren,
     * wird z.B. für {@link #mostCommon(int)} verwendet
     */
    private Comparator<K> keyComparator = (a, b) -> ((Comparable) a).compareTo((Comparable) b);

    /**
     * Vergleichsoperator für Value- und Key-Paare,
     * so dass zuerst nach der Value und dann nach dem Key gereiht wird.<br>
     * Der Key-Comparator kann mittels {@link #LongCounter(Comparator)} gesetzt werden.
     * wird z.B. für {@link #mostCommon(int)} verwendet
     */
    private final Comparator<Entry<K, Long>> entryComparator = (a, b) -> {
        int d = Long.compare(a.getValue(), b.getValue());
        if (d != 0) {
            return d;
        }
        return keyComparator.compare(b.getKey(), a.getKey());
    };

    /**
     * Der keyComperator wird zum Sortieren für {@link #mostCommon(int)}} und
     * {@link #lessCommon(int)} vewendet.
     *
     * @param keyComparator zum Sortieren der Keys
     */
    public LongCounter(Comparator<K> keyComparator) {
        this.keyComparator = keyComparator;
    }

    /**
     * Erzeugt einen LongCounter mit der Standardkapazität und dem Standard-Loadfaktor<br>
     */
    public LongCounter() {
        // nothing to do
    }

    /**
     * Erzeugt eine LongCounter aus einer beliebigen Map die Long-Values hat
     *
     * @param m die Map mit den schon gezählten Werden
     */
    public LongCounter(Map<? extends K, Long> m) {
        super(m);
    }

    /**
     * Erzeugt einen LongCounter aus dem Objekt-Array
     *
     * @param k das Array mit den zu zählenden Elementen
     */
    public LongCounter(K... k) {
        for (K value : k) {
            this.put(value);
        }
    }

    /**
     * Erzeugt einen LongCounter aus der Collection
     *
     * @param k das Array mit den zu zählenden Elementen
     */
    public LongCounter(Collection<K> k) {
        Iterator<K> iterator = k.iterator();
        while (iterator.hasNext()) {
            this.put(iterator.next());
        }
    }

    /**
     * Erzeugt einen LongCounter aus einem String und zählt alle Zeichen in dem String.
     *
     * @param s die zu zählenden Zeichen
     * @return der LongCounter, der alle Zeichen gezählt hat
     */
    public static LongCounter<Character> fromString(CharSequence s) {
        LongCounter<Character> counter = new LongCounter<>();

        for (int i = 0; i < s.length(); i++) {
            counter.put(s.charAt(i));
        }
        return counter;
    }

    /**
     * Erhöht den Counter um "value" für den Key.
     *
     * @param key   der Key, dessen value verändert wird
     * @param value der Wert, um den der Counter verändert werden soll
     * @return der alte Wert des Counters oder null
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
     * Erhöht den Counter um Eins für den Key.
     *
     * @param key der Key, dessen value verändert wird
     * @return der alte Wert des Counters oder null
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
     * Erhöht (addiert) die Werte aus der Map "m" zum LongCounter.
     *
     * @param m die Map mit den zu addierenden Werten
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
     * Vermindert (subtrahiert) die Werte aus der Map "m" zum LongCounter.
     *
     * @param m die Map mit den zu subtrahierenden Werten
     */
    public void subtractAll(Map<? extends K, ? extends Long> m) {
        m.forEach((key, value) -> this.put(key, -value));
    }

    /**
     * Liefert (max.) die "n"-häufigsten Werte, sortiert nach Häufigkeit und danach nach den Keys.
     *
     * @param n die Anzahl der Werte
     * @return die Liste mit den Key/Counter-Paaren
     */
    public List<Entry<K, Long>> mostCommon(int n) {
        List<Entry<K, Long>> entrys = new ArrayList<>(entrySet());
        entrys.sort(entryComparator.reversed());

        return entrys.subList(0, Math.min(n, size()));
    }

    /**
     * Liefert (max.) die "n"-seltensten Werte, sortiert nach Häufigkeit und danach nach den Keys.
     *
     * @param n die Anzahl der Werte
     * @return die Liste mit den Key/Counter-Paaren
     */
    public List<Entry<K, Long>> lessCommon(int n) {
        List<Entry<K, Long>> entrys = new ArrayList<>(entrySet());
        entrys.sort(entryComparator);
        return entrys.subList(0, Math.min(n, size()));
    }

    /**
     * Löscht alle Einträge aus der Map, deren Counter == 0 ist.
     */
    public void clearZeros() {
        // Wichtig: man kann einer Schleife keine Elemente löschen -- verwende eine Schleife mit einem Iterator
        entrySet().removeIf(kLongEntry -> kLongEntry.getValue() == 0);
    }
}
