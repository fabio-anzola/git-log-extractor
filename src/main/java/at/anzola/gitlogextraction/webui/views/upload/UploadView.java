package at.anzola.gitlogextraction.webui.views.upload;

import at.anzola.gitlogextraction.reader.LogReader;
import at.anzola.gitlogextraction.webui.views.main.MainView;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
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
        createLayout();
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

        add(upload, output);
    }

    private void showOutput(String text, Component content, HasComponents outputContainer) {
        HtmlComponent p = new HtmlComponent(Tag.P);
        p.getElement().setText(text);
        outputContainer.add(p);
        outputContainer.add(content);
    }

    //TODO Design Page for Log uploading

}
