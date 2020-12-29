package at.anzola.gitlogextraction.webui.views.upload;

import at.anzola.gitlogextraction.reader.LogReader;
import at.anzola.gitlogextraction.response.Log;
import at.anzola.gitlogextraction.utlis.Anonym;
import at.anzola.gitlogextraction.webui.views.main.MainView;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import java.io.IOException;

@Route(value = "upload", layout = MainView.class)
@PageTitle("Upload")
@CssImport("./styles/views/upload/upload-view.css")
@RouteAlias(value = "", layout = MainView.class)
public class UploadView extends HorizontalLayout {

    public UploadView() {
        setId("upload-view");
        createHowTo();
        createLayout();
    }

    private void createHowTo() {
        Details component = new Details(
                "How to",
                new OrderedList(
                        new ListItem("Execute the following command in your bash: git log > log.txt"),
                        new ListItem("Upload the log.txt file by clicking on 'Upload File' are via drag&drop"),
                        new ListItem("(optional) Anonymize the log file via a simple click on 'Anonymize!'"),
                        new ListItem("Now you can easily examine you log either in the 'List' tab or in the 'Charts' tab")
                ));

        add(component);
    }

    private void createLayout() {
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setMaxFiles(1);
        upload.setDropLabel(new Label("Upload a 1 MiB (max) file in .txt (plaintext) format"));
        upload.setAcceptedFileTypes("text/plain");
        upload.setMaxFileSize(1048576);
        Div output = new Div();

        upload.addFileRejectedListener(event -> {
            Paragraph component = new Paragraph();
            showOutput(event.getErrorMessage(), component, output);
        });

        upload.addSucceededListener(succeededEvent -> {
            Notification.show("Upload successful!");
            try {
                UI.getCurrent().getSession().setAttribute("latestLog", LogReader.read(buffer.getInputStream().readAllBytes()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Button anonymize = new Button("Anonymize!");
        anonymize.addClickListener(buttonClickEvent -> {
            try {
                UI.getCurrent().getSession().setAttribute(
                        "latestLog",
                        Anonym.anonymize(((Log) UI.getCurrent().getSession().getAttribute("latestLog")))
                );
            } catch (NullPointerException e) {
                Notification.show("You have to upload a Log before it can be anonymized.");
            }
        });
        anonymize.setId("anonbutt");
        Label label = new Label("Click here to anonymize the log");
        label.setFor("anonbutt");

        add(upload, output, anonymize, label);
    }

    private void showOutput(String text, Component content, HasComponents outputContainer) {
        HtmlComponent p = new HtmlComponent(Tag.P);
        p.getElement().setText(text);
        outputContainer.add(p);
        outputContainer.add(content);
    }

    //TODO Design Page for Log uploading

}
