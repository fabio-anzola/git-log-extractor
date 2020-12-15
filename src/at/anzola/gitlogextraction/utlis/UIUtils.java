package at.anzola.gitlogextraction.utlis;

import javafx.collections.ObservableList;

import java.util.Comparator;

/**
 * The UIUtils class
 *
 * @author fabioanzola
 */
public class UIUtils {

    /**
     * Sorting options
     */
    public enum lvsort {
        hash,
        author,
        date,
        message
    }

    /**
     * Sorts observableList depending on lvsortI
     *
     * @param observableList The list to sort
     * @param lvsortI The options to be sorted after
     * @return The sorted list
     */
    public static ObservableList<String> copareLV(ObservableList<String> observableList, lvsort lvsortI) {
        observableList.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {

                o1 = cutToWish(o1, lvsortI);
                o2 = cutToWish(o2, lvsortI);

                assert o1 != null;
                if (o1.equals(o2)) {
                    return 0;
                }
                if (o2 == null) {
                    return 1;
                }
                return o1.compareTo(o2);
            }
        });
        return observableList;
    }

    /**
     * Cuts given string to provided sorting option
     *
     * @param string The string to be cut
     * @param lvsort The option to be cut after
     * @return The cut string
     */
    public static String cutToWish(String string, lvsort lvsort) {
        switch (lvsort) {
            case hash:
                return string.split("\n")[0].trim();
            case author:
                return string.split("\n")[1].trim();
            case date:
                return string.split("\n")[2].trim();
            case message:
                return string.split("\n")[3].trim();
            default:
                return null;
        }
    }
}
