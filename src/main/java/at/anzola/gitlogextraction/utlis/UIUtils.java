package at.anzola.gitlogextraction.utlis;

import at.anzola.gitlogextraction.ui.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
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
    public enum Lvsort {
        hash,
        author,
        date,
        message
    }

    /**
     * Sorts observableList depending on lvsortI
     *
     * @param observableList The list to sort
     * @param lvsortI        The options to be sorted after
     * @return The sorted list
     */
    public static ObservableList<String> compareLV(ObservableList<String> observableList, Lvsort lvsortI) {
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
    public static String cutToWish(String string, Lvsort lvsort) {
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

    /**
     * Filter ListView after given String
     *
     * @param search The String to be sorted after
     */
    public static void filterListView(String search) {
        if (search.equals("")) {
            App.listv.setItems(App.listvItems);
            return;
        }

        ArrayList<String> filtered = new ArrayList<>();
        for (String listvItem : App.listvItems) {
            if (listvItem.contains(search)) {
                filtered.add(listvItem);
            }
        }
        App.listv.setItems(FXCollections.observableList(filtered));
        App.listv.refresh();
    }
}
