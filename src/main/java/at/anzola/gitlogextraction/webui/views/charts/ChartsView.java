package at.anzola.gitlogextraction.webui.views.charts;

import at.anzola.gitlogextraction.response.Log;
import at.anzola.gitlogextraction.utlis.Analysis;
import at.anzola.gitlogextraction.utlis.Diagrams;
import at.anzola.gitlogextraction.webui.views.main.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.DayOfWeek;
import java.time.Month;

/**
 * The ChartsView class
 *
 * @author fabioanzola
 */
@Route(value = "charts", layout = MainView.class)
@PageTitle("Charts")
public class ChartsView extends Div {

    /**
     * The private reference to the Log
     */
    private Log log;

    /**
     * Constructor, collects the log
     */
    public ChartsView() {
        setId("charts-view");

        try {
            log = (Log) UI.getCurrent().getSession().getAttribute("latestLog");
            createLayout();
        }
        catch (NullPointerException e) {
            Notification.show("You have to upload a Log before it can be displayed here.");
        }
    }

    /**
     * Creates the charts
     */
    private void createLayout() {
        createChart(Diagrams.day);
        createChart(Diagrams.month);
        createChart(Diagrams.year);
    }

    /**
     * Method for all kinds of diagrams
     *
     * @param diagrams Which kind of diagram
     */
    private void createChart(Diagrams diagrams) {
        Chart chart = new Chart(ChartType.COLUMN);

        Configuration conf = chart.getConfiguration();
        conf.setTitle(String.format(
                "Commits per %s",
                diagrams.name().toUpperCase().charAt(0)
                        + diagrams.name().toLowerCase().substring(1))
        );
        conf.setSubTitle(String.format("An absolute graph of all commits made, grouped by their %s", diagrams.name().toLowerCase()));

        ListSeries series = new ListSeries("Commits");

        Analysis analysis = new Analysis(log);

        switch (diagrams) {
            case day:
                for (DayOfWeek value : DayOfWeek.values()) {
                    series.addData(analysis.getCommitsPerDay().get(value));
                }
                break;
            case month:
                for (Month value : Month.values()) {
                    series.addData(analysis.getCommitsPerMonth().get(value));
                }
                break;
            case year:
                for (Object o : analysis.getCommitsPerYear().keySet().stream().sorted().toArray()) {
                    String value = String.valueOf((String) o);
                    series.addData(analysis.getCommitsPerYear().get(value));
                }
                break;
            default:
                break;
        }

        conf.addSeries(series);

        XAxis xaxis = new XAxis();

        switch (diagrams) {
            case day:
                for (DayOfWeek value : DayOfWeek.values()) {
                    xaxis.addCategory(""
                            + value.name().toUpperCase().charAt(0)
                            + value.name().toLowerCase().substring(1)
                    );
                }
                break;
            case month:
                for (Month value : Month.values()) {
                    xaxis.addCategory(""
                            + value.name().toUpperCase().charAt(0)
                            + value.name().toLowerCase().substring(1)
                    );
                }
                break;
            case year:
                for (Object o : analysis.getCommitsPerYear().keySet().stream().sorted().toArray()) {
                    String value = String.valueOf((String) o);
                    xaxis.addCategory(""
                            + value.toUpperCase().charAt(0)
                            + value.toLowerCase().substring(1)
                    );
                }
                break;
            default:
                break;
        }

        xaxis.setTitle(diagrams.name().toUpperCase().charAt(0) + diagrams.name().toLowerCase().substring(1) + 's');
        conf.addxAxis(xaxis);

        YAxis yaxis = new YAxis();
        yaxis.setTitle("Commits");
        conf.addyAxis(yaxis);

        Tooltip tooltip = new Tooltip();
        tooltip.setShared(true);
        conf.setTooltip(tooltip);

        add(chart);
    }
}
