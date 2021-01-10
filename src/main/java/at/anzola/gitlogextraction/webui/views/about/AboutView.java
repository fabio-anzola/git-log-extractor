package at.anzola.gitlogextraction.webui.views.about;

import at.anzola.gitlogextraction.webui.views.main.MainView;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * The AboutView class
 *
 * @author fabioanzola
 */
@Route(value = "about", layout = MainView.class)
@PageTitle("About")
public class AboutView extends Div {

    /**
     * Contains the document path to the projects description
     */
    private String basePath = "https://raw.githubusercontent.com/fabio-anzola/git-log-extractor/master/project-documents/";

    /**
     * Contains the base filename for the pdfs (formatted for String.format())
     */
    private String baseFilename = "Projektdefinition_Anzola_v%d.pdf";

    /**
     * Constructor for AboutView
     */
    public AboutView() {
        setId("about-view");
        setHeight("100%");
        createView();
    }

    /**
     * Creates Main Layout
     */
    private void createView() {
        String url = latestDocs(1);
        String outerHtml = "<embed src=\"" + url + "\" type=\"application/pdf\" width=\"100%\" height=\"100%\"/>";
        Html html = new Html(outerHtml);
        add(html);
    }

    /**
     * Recursively gets latest projects description (pdf)
     *
     * @param i Index for url
     * @return Url to the latest description
     */
    private String latestDocs(int i) {
        try {
            String urlPath = basePath + String.format(baseFilename, i + 1);
            URL url = new URL(urlPath);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("HEAD");
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return latestDocs(i + 1);
            } else {
                return basePath + String.format(baseFilename, i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Non-Recursively gets latest projects description (pdf)
     *
     * @return Url to the latest description
     */
    private String getLatestDocs() {
        String returnUrl = null;
        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            try {
                String urlPath = basePath + String.format(baseFilename, i);
                URL url = new URL(urlPath);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("HEAD");
                httpURLConnection.connect();
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    returnUrl = urlPath;
                } else {
                    return returnUrl;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
