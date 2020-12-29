package at.anzola.gitlogextraction.webui.views.list;

import at.anzola.gitlogextraction.response.Commit;
import at.anzola.gitlogextraction.response.Log;
import at.anzola.gitlogextraction.webui.views.main.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
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

@Route(value = "list", layout = MainView.class)
@PageTitle("List")
@CssImport(value = "./styles/views/list/list-view.css", include = "lumo-badge")
@JsModule("@vaadin/vaadin-lumo-styles/badge.js")
public class ListView extends Div {

    private Grid<Commit> grid;
    private ListDataProvider<Commit> dataProvider;

    private Grid.Column<Commit> hashColumn;
    private Grid.Column<Commit> authorColumn;
    private Grid.Column<Commit> authorDateColumn;
    private Grid.Column<Commit> messageColumn;

    public ListView() {
        setId("list-view");
        setSizeFull();
        createGrid();
        add(grid);

        List<GridSortOrder<Commit>> orders = new ArrayList<GridSortOrder<Commit>>();
        orders.add(new GridSortOrder<>(authorDateColumn, SortDirection.ASCENDING));
        grid.sort(orders);
    }

    private void createGrid() {
        createGridComponent();
        addColumnsToGrid();
        addFiltersToGrid();
    }

    private void createGridComponent() {
        grid = new Grid<>();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COLUMN_BORDERS);
        grid.setHeight("100%");

        grid.setMultiSort(true);

        dataProvider = new ListDataProvider<Commit>(getCommits());
        grid.setDataProvider(dataProvider);
    }

    private Collection<Commit> getCommits() {
        try {
            return ((Log) UI.getCurrent().getSession().getAttribute("latestLog")).gitLog;
        } catch (NullPointerException e) {
            Notification.show("You have to upload a Log before it can be displayed here.");
            return new ArrayList<>();
        }
    }

    private void addColumnsToGrid() {
        createHashColumn();
        createAuthorColumn();
        createAuthorDateColumn();
        createMessageColumn();
    }

    private void createHashColumn() {
        hashColumn = grid.addColumn(Commit::getHash, "hash").setHeader("Hash").setAutoWidth(true);
    }

    private void createAuthorColumn() {
        authorColumn = grid.addColumn(Commit::getAuthor, "author").setHeader("Author").setAutoWidth(true);
    }

    private void createAuthorDateColumn() {
        authorDateColumn = grid
                .addColumn(new LocalDateRenderer<>(commit -> commit.getAuthorDate().toLocalDate(),
                        DateTimeFormatter.ofPattern("M/d/yyyy")))
                .setComparator(commit -> commit.getAuthorDate().toLocalDate()).setHeader("Date").setAutoWidth(true);
    }

    private void createMessageColumn() {
        messageColumn = grid.addColumn(Commit::getMessage, "message").setHeader("Message").setAutoWidth(true);
    }

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

    private boolean areDatesEqual(Commit commit, DatePicker dateFilter) {
        LocalDate dateFilterValue = dateFilter.getValue();
        if (dateFilterValue != null) {
            LocalDate commitDate = commit.authorDate.toLocalDate();
            return dateFilterValue.equals(commitDate);
        }
        return true;
    }
}
