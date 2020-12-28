package at.anzola.gitlogextraction.webui.views.upload;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import at.anzola.gitlogextraction.webui.views.main.MainView;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "upload", layout = MainView.class)
@PageTitle("Upload")
@CssImport("./styles/views/upload/upload-view.css")
@RouteAlias(value = "", layout = MainView.class)
public class UploadView extends HorizontalLayout {

    public UploadView() {
        setId("upload-view");
        add(new Text("Content placeholder"));
    }

    //TODO Page for Log uploading

}
