package at.anzola.gitlogextraction.reader;

import at.anzola.gitlogextraction.ui.App;
import at.anzola.gitlogextraction.utlis.Anonym;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * The LogReader class
 *
 * @author fabioanzola
 */
public class LogWriter {

    /**
     * Called to overwrite the current log
     *
     * @throws IOException Thrown if an error happens
     */
    public static void save() throws IOException {
        write(App.currentLog.toPath(), read(App.currentLog.toPath()));
    }

    /**
     * Called to save at a specific path
     *
     * @param file The File where to be saved
     * @throws IOException Thrown if an error happens
     */
    public static void saveAs(File file) throws IOException {
        write(file.toPath(), read(App.currentLog.toPath()));
    }

    /**
     * Reads and replaces from source
     *
     * @param path The path to get the log fom
     * @return The replaced File-content
     * @throws IOException Thrown if an error happens
     */
    private static String read(Path path) throws IOException {
        StringBuilder text = new StringBuilder();

        try (
                BufferedReader in = Files.newBufferedReader(path, StandardCharsets.UTF_8);

        ) {
            String line;

            while ((line = in.readLine()) != null) {
                for (String s : Anonym.users.keySet()) {
                    if (line.contains(s)) {
                        line = line.replace(s, Anonym.users.get(s));
                    }
                }
                text.append(line).append(System.lineSeparator());
            }
        }
        return text.toString();
    }

    /**
     * Writes text to specified file
     *
     * @param path    The path to get the log fom
     * @param content The content to be written
     * @throws IOException Thrown if an error happens
     */
    private static void write(Path path, String content) throws IOException {
        try (
                BufferedWriter out = Files.newBufferedWriter(path, StandardCharsets.UTF_8);

        ) {
            out.write(content);

            out.flush();
            out.close();
        }
    }
}
