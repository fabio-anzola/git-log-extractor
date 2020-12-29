package at.anzola.gitlogextraction.ui;

import at.anzola.gitlogextraction.utlis.Diagrams;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

/**
 * The DiagramViews class
 *
 * @author fabioanzola
 */
public class DiagramViews {

    /**
     * Contains the titles for the different Charts
     */
    private static Map<Diagrams, String> titles = new HashMap<Diagrams, String>() {{
        put(Diagrams.year, "Commits per Year");
        put(Diagrams.month, "Commits per Month");
        put(Diagrams.day, "Commits per Day");
    }};

    /**
     * Contains the Data Series
     */
    private static XYChart.Series<String, Number> series;

    /**
     * Initiates chart creation process
     *
     * @param diagramsI The desired diagram type
     */
    public static void createChart(Diagrams diagramsI) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(App.stage);

        CategoryAxis xAxis1 = new CategoryAxis();
        xAxis1.setLabel(diagramsI.toString());
        NumberAxis yAxis1 = new NumberAxis();
        yAxis1.setLabel("commits");
        BarChart barchart = new BarChart(xAxis1, yAxis1);
        series = new XYChart.Series<>();
        series.setName(String.format("commits per %s", diagramsI.toString()));

        switch (diagramsI) {
            case day:
                chartPerDay();
                break;
            case month:
                chartPerMonth();
                break;
            case year:
                chartPerYear();
                break;
        }

        series.setName(titles.get(diagramsI));
        barchart.getData().add(series);

        Scene dialogScene = new Scene(barchart, 500, 500);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    /**
     * Fills charts with data of commit/day
     */
    private static void chartPerDay() {
        for (DayOfWeek value : DayOfWeek.values()) {
            if (App.analysis.getCommitsPerDay().containsKey(value)) {
                series.getData().add(new XYChart.Data<>(value.name(), App.analysis.getCommitsPerDay().get(value)));
            } else {
                series.getData().add(new XYChart.Data<>(value.name(), 0));
            }
        }
    }

    /**
     * Fills charts with data of commit/month
     */
    private static void chartPerMonth() {
        for (Month value : Month.values()) {
            if (App.analysis.getCommitsPerMonth().containsKey(value)) {
                series.getData().add(new XYChart.Data<>(value.name(), App.analysis.getCommitsPerMonth().get(value)));
            } else {
                series.getData().add(new XYChart.Data<>(value.name(), 0));
            }
        }
    }

    /**
     * Fills charts with data of commit/year
     */
    private static void chartPerYear() {
        for (Object o : App.analysis.getCommitsPerYear().keySet().stream().sorted().toArray()) {
            String value = String.valueOf((String) o);
            if (App.analysis.getCommitsPerYear().containsKey(value)) {
                series.getData().add(new XYChart.Data<>(value, App.analysis.getCommitsPerYear().get(value)));
            } else {
                series.getData().add(new XYChart.Data<>(value, 0));
            }
        }
    }
}
