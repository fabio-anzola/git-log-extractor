package at.anzola.gitlogextraction.webui.views.charts;

import at.anzola.gitlogextraction.response.Log;
import at.anzola.gitlogextraction.utlis.Analysis;
import at.anzola.gitlogextraction.utlis.LongCounter;
import at.anzola.gitlogextraction.webui.views.main.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.DayOfWeek;

@Route(value = "charts", layout = MainView.class)
@PageTitle("Charts")
public class ChartsView extends Div {

    private Log log;

    public ChartsView() {
        setId("charts-view");

        try {
            log = (Log) UI.getCurrent().getSession().getAttribute("latestLog");
            createLayout();
        } catch (NullPointerException e) {
            Notification.show("You have to upload a Log before it can be displayed here.");
        }
    }

    private void createLayout() {
        createPerDayChart();
    }

    private void createPerDayChart() {
        Chart chart = new Chart(ChartType.COLUMN);

        Configuration conf = chart.getConfiguration();
        conf.setTitle("Commits per Day");
        conf.setSubTitle("An absolute graph of all commits made, grouped by their day");

        ListSeries series = new ListSeries("Commits");

        Analysis analysis = new Analysis(log);

        series.setData(
                analysis.getCommitsPerDay().get(DayOfWeek.MONDAY),
                analysis.getCommitsPerDay().get(DayOfWeek.TUESDAY),
                analysis.getCommitsPerDay().get(DayOfWeek.WEDNESDAY),
                analysis.getCommitsPerDay().get(DayOfWeek.THURSDAY),
                analysis.getCommitsPerDay().get(DayOfWeek.FRIDAY),
                analysis.getCommitsPerDay().get(DayOfWeek.SATURDAY),
                analysis.getCommitsPerDay().get(DayOfWeek.SUNDAY)
        );
        conf.addSeries(series);

        XAxis xaxis = new XAxis();
        xaxis.setCategories("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
        xaxis.setTitle("Days");
        conf.addxAxis(xaxis);

        YAxis yaxis = new YAxis();
        yaxis.setTitle("Commits");
        conf.addyAxis(yaxis);

        Tooltip tooltip = new Tooltip();
        tooltip.setShared(true);
        conf.setTooltip(tooltip);

        add(chart);
    }

    //TODO Automatically generate and view the charts for the uploaded Log

}
