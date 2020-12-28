package at.anzola.gitlogextraction.webui.views.charts;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import at.anzola.gitlogextraction.webui.views.main.MainView;

@Route(value = "charts", layout = MainView.class)
@PageTitle("Charts")
public class ChartsView extends Div {

    public ChartsView() {
        setId("charts-view");
        add(new Text("Content placeholder"));
    }

    //TODO Automatically generate and view the charts for the uploaded Log

}
