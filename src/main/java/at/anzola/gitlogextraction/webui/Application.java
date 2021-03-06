package at.anzola.gitlogextraction.webui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.vaadin.artur.helpers.LaunchUtil;

/**
 * The entry point of the Spring Boot application.
 *
 * @author fabioanzola
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    /**
     * "This is the main method. It doesn't need JavaDoc." -Platon
     *
     * @param args args
     */
    public static void main(String[] args) {
        LaunchUtil.launchBrowserInDevelopmentMode(SpringApplication.run(Application.class, args));
    }

}
