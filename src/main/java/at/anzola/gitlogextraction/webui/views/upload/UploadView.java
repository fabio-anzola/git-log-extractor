package at.anzola.gitlogextraction.webui.views.upload;

import at.anzola.gitlogextraction.Checkstyle.MagicNumber;
import at.anzola.gitlogextraction.reader.LogReader;
import at.anzola.gitlogextraction.response.Log;
import at.anzola.gitlogextraction.utlis.Anonym;
import at.anzola.gitlogextraction.webui.views.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.OrderedList;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * The UploadView class
 *
 * @author fabioanzola
 */
@Route(value = "upload", layout = MainView.class)
@PageTitle("Upload")
@CssImport("./styles/views/upload/upload-view.css")
@RouteAlias(value = "", layout = MainView.class)
public class UploadView extends HorizontalLayout {

    /**
     * Constructor for UploadView
     */
    public UploadView() {
        setId("upload-view");
        createHowTo();
        createLayout();
    }

    /**
     * Creates HowTo component
     */
    private void createHowTo() {
        Details component = new Details(
                "How to",
                new OrderedList(
                        new ListItem(
                   "Execute the following command in your bash: git log > log.txt"
                        ),
                        new ListItem(
                   "Upload the log.txt file by clicking on 'Upload File' or via drag&drop"
                        ),
                        new ListItem(
                   "(optional) Anonymize the log file via a simple click on 'Anonymize!'"
                        ),
                        new ListItem(
                   "Now simply examine you log either in the 'List' tab or in the 'Charts' tab"
                        )
                ));

        add(component);
    }

    /**
     * Creates Main Layout
     */
    private void createLayout() {
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setMaxFiles(1);
        upload.setDropLabel(new Label("Upload a 1 MiB (max) file in .txt (plaintext) format"));
        upload.setAcceptedFileTypes("text/plain");
        upload.setMaxFileSize(MagicNumber.MIB);
        Div output = new Div();

        upload.addFileRejectedListener(event -> {
            Paragraph component = new Paragraph();
            showOutput(event.getErrorMessage(), component, output);
        });

        upload.addSucceededListener(succeededEvent -> {
            Notification.show("Upload successful!");
            try {
                UI.getCurrent().getSession().setAttribute(
                        "latestLog", LogReader.read(buffer.getInputStream().readAllBytes())
                );
                UI.getCurrent().getSession().setAttribute(
                        "fullLog", new String(buffer.getInputStream().readAllBytes())
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Button anonymize = new Button("Anonymize your Log");
        anonymize.addClickListener(buttonClickEvent -> {
            try {
                //Anonymize Log to Display and work with
                UI.getCurrent().getSession().setAttribute(
                        "latestLog",
                        Anonym.anonymize(
                                ((Log) UI.getCurrent().getSession().getAttribute("latestLog")
                                ))
                );

                //Anonymize Full Log to be downloaded
                String log = (String) UI.getCurrent().getSession().getAttribute("fullLog");
                for (String s : Anonym.users.keySet()) {
                    if (log.contains(s)) {
                        log = log.replaceAll(s, Anonym.users.get(s));
                    }
                }
                UI.getCurrent().getSession().setAttribute("fullLog", log);


            } catch (NullPointerException e) {
                e.printStackTrace();
                Notification.show("You have to upload a Log before it can be anonymized.");
            }
        });

        Button download = new Button("Download your Log");
        download.addClickListener(buttonClickEvent -> {
            try {
                final String log = (String) UI.getCurrent().getSession().getAttribute("fullLog");

                if (null == log) {
                    throw new NullPointerException("Thrown bc log is null");
                }

                final StreamResource streamResource = new StreamResource(String.format("log%s.txt",
                        UI.getCurrent().getSession().getPushId()),
                    () -> new ByteArrayInputStream(log.getBytes(StandardCharsets.UTF_8)));
                streamResource.setContentType("application/x-unknown");
                streamResource.setCacheTime(0);

                final StreamRegistration registration = UI.getCurrent()
                        .getSession()
                        .getResourceRegistry()
                        .registerResource(streamResource);

                UI.getCurrent().getPage().open(
                        String.valueOf(registration.getResourceUri()), "_blank"
                );

            } catch (NullPointerException e) {
                e.printStackTrace();
                Notification.show("You have to upload a Log before it can be downloaded.");
            }
        });

        HorizontalLayout horizontalLayout = new HorizontalLayout(anonymize, download);
        add(upload, output, horizontalLayout);
    }

    /**
     * Shows output when File is uploaded
     *
     * @param text            The text to be shown
     * @param content         The content
     * @param outputContainer The component container
     */
    private void showOutput(String text, Component content, HasComponents outputContainer) {
        HtmlComponent p = new HtmlComponent(Tag.P);
        p.getElement().setText(text);
        outputContainer.add(p);
        outputContainer.add(content);
    }
}
