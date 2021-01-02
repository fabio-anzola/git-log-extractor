package at.anzola.gitlogextraction.webui.views.list;

import at.anzola.gitlogextraction.response.Commit;
import at.anzola.gitlogextraction.response.Log;
import at.anzola.gitlogextraction.webui.views.main.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The ListView class
 *
 * @author fabioanzola
 */
@Route(value = "list", layout = MainView.class)
@PageTitle("List")
public class ListView extends Div {

    /**
     * Contains the Grid
     */
    private Grid<Commit> grid;

    /**
     * List (Data) for Grid
     */
    private ListDataProvider<Commit> dataProvider;

    /**
     * Grid-Column containing the Hash
     */
    private Grid.Column<Commit> hashColumn;

    /**
     * Grid-Column containing the Author
     */
    private Grid.Column<Commit> authorColumn;

    /**
     * Grid-Column containing the Author Date
     */
    private Grid.Column<Commit> authorDateColumn;

    /**
     * Grid-Column containing the Message
     */
    private Grid.Column<Commit> messageColumn;

    /**
     * Constructor for ListView
     */
    public ListView() {
        setId("list-view");
        setSizeFull();
        createGrid();
        add(grid);

        List<GridSortOrder<Commit>> orders = new ArrayList<GridSortOrder<Commit>>();
        orders.add(new GridSortOrder<>(authorDateColumn, SortDirection.ASCENDING));
        grid.sort(orders);
    }

    /**
     * Generates the Layout
     */
    private void createGrid() {
        createGridComponent();
        addColumnsToGrid();
        addFiltersToGrid();
    }

    /**
     * Creates Grid
     */
    private void createGridComponent() {
        grid = new Grid<>();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COLUMN_BORDERS);
        grid.setHeight("100%");

        grid.setMultiSort(true);

        dataProvider = new ListDataProvider<Commit>(getCommits());
        grid.setDataProvider(dataProvider);
    }

    /**
     * Gets Data
     *
     * @return the Data for the Grid
     */
    private Collection<Commit> getCommits() {
        try {
            return ((Log) UI.getCurrent().getSession().getAttribute("latestLog")).gitLog;
        } catch (NullPointerException e) {
            Notification.show("You have to upload a Log before it can be displayed here.");
            return new ArrayList<>();
        }
    }

    /**
     * Adds Columns to Grid
     */
    private void addColumnsToGrid() {
        createHashColumn();
        createAuthorColumn();
        createAuthorDateColumn();
        createMessageColumn();
    }

    /**
     * Creates the Grid-Column for the Hash
     */
    private void createHashColumn() {
        hashColumn = grid.addColumn(Commit::getHash, "hash").setHeader("Hash").setAutoWidth(true);
    }

    /**
     * Creates the Grid-Column for the Author
     */
    private void createAuthorColumn() {
        authorColumn = grid.addColumn(Commit::getAuthor, "author").setHeader("Author").setAutoWidth(true);
    }

    /**
     * Creates the Grid-Column for the Author Date
     */
    private void createAuthorDateColumn() {
        authorDateColumn = grid
                .addColumn(new LocalDateRenderer<>(commit -> commit.getAuthorDate().toLocalDate(),
                        DateTimeFormatter.ofPattern("M/d/yyyy")))
                .setComparator(commit -> commit.getAuthorDate().toLocalDate()).setHeader("Date").setAutoWidth(true);
    }

    /**
     * Creates the Grid-Column for the Message
     */
    private void createMessageColumn() {
        messageColumn = grid.addColumn(Commit::getMessage, "message").setHeader("Message").setAutoWidth(true);
    }

    /**
     * Adds the Filters to the Grid
     */
    private void addFiltersToGrid() {
        HeaderRow filterRow = grid.appendHeaderRow();

        TextField idFilter = new TextField();
        idFilter.setPlaceholder("Filter");
        idFilter.setClearButtonVisible(true);
        idFilter.setWidth("100%");
        idFilter.setValueChangeMode(ValueChangeMode.EAGER);
        idFilter.addValueChangeListener(event -> dataProvider.addFilter(
                commit -> StringUtils.containsIgnoreCase(commit.getHash(), idFilter.getValue())));
        filterRow.getCell(hashColumn).setComponent(idFilter);

        TextField clientFilter = new TextField();
        clientFilter.setPlaceholder("Filter");
        clientFilter.setClearButtonVisible(true);
        clientFilter.setWidth("100%");
        clientFilter.setValueChangeMode(ValueChangeMode.EAGER);
        clientFilter.addValueChangeListener(event -> dataProvider
                .addFilter(commit -> StringUtils.containsIgnoreCase(commit.getAuthor(), clientFilter.getValue())));
        filterRow.getCell(authorColumn).setComponent(clientFilter);

        TextField amountFilter = new TextField();
        amountFilter.setPlaceholder("Filter");
        amountFilter.setClearButtonVisible(true);
        amountFilter.setWidth("100%");
        amountFilter.setValueChangeMode(ValueChangeMode.EAGER);
        amountFilter.addValueChangeListener(event -> dataProvider.addFilter(commit -> StringUtils
                .containsIgnoreCase(commit.getMessage(), amountFilter.getValue())));
        filterRow.getCell(messageColumn).setComponent(amountFilter);

        DatePicker dateFilter = new DatePicker();
        dateFilter.setPlaceholder("Filter");
        dateFilter.setClearButtonVisible(true);
        dateFilter.setWidth("100%");
        dateFilter.addValueChangeListener(event -> dataProvider.addFilter(commit -> areDatesEqual(commit, dateFilter)));
        filterRow.getCell(authorDateColumn).setComponent(dateFilter);
    }

    /**
     * Checks if two Dates are equal
     *
     * @param commit     The Commit to be checked
     * @param dateFilter The DateFilter to be checked against
     * @return If they are equal / their relationship
     */
    private boolean areDatesEqual(Commit commit, DatePicker dateFilter) {
        LocalDate dateFilterValue = dateFilter.getValue();
        if (dateFilterValue != null) {
            LocalDate commitDate = commit.authorDate.toLocalDate();
            return dateFilterValue.equals(commitDate);
        }
        return true;
    }
}
