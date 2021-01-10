package at.anzola.gitlogextraction.reader;

import at.anzola.gitlogextraction.Checkstyle.MagicNumber;
import at.anzola.gitlogextraction.response.Commit;
import at.anzola.gitlogextraction.response.Log;
import at.anzola.gitlogextraction.utlis.CommitDate;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

/**
 * The LogReader class
 *
 * @author fabioanzola
 */
public class LogReader {

    /**
     * Regex for the commit hash
     */
    private final static String REGEX_COMMIT = "^(commit\\s[a-f0-9]{40})";

    /**
     * Regex for the author line
     */
    private final static String REGEX_AUTHOR = "^(Author:\\s.+)";

    /**
     * Regex for the Date line
     */
    private final static String REGEX_DATE = "^(Date:"
            + "[\\s]{3}[A-Za-z]{3}\\s[A-Za-z]{3}\\s[0-9]{1,2}\\s[0-9:]{8}\\s[0-9]{4}\\s[0-9+]{5})";

    /**
     * Reads the log and outputs the object
     *
     * @param file The log file
     * @return The log object
     * @throws IOException Thrown if an error occurs
     */
    public static Log read(String file) throws IOException {
        Log log = new Log();

        try (
                BufferedReader in = Files.newBufferedReader(Paths.get(file), StandardCharsets.UTF_8)

        ) {
            String line;

            String hash = "";

            String author = "";

            LocalDateTime authorDate = null;

            String message = "";

            while ((line = in.readLine()) != null) {
                if (line.matches(REGEX_COMMIT) && !hash.equals("")) {
                    Commit commit = new Commit(hash, author, authorDate, message);
                    log.gitLog.add(commit);
                    hash = "";
                    author = "";
                    authorDate = null;
                    message = "";
                }

                if (line.matches(REGEX_COMMIT)) {
                    hash = line.split(" ")[1].trim();
                } else if (line.matches(REGEX_AUTHOR)) {
                    author = line.substring(MagicNumber.SEVEN).trim();
                } else if (line.matches(REGEX_DATE)) {
                    authorDate = CommitDate.of(line.substring(MagicNumber.SEVEN).trim());
                } else {
                    if (!line.equals("")) {
                        if (!message.equals("")) {
                            message += ':';
                            message += line.trim();
                        } else {
                            message += line.trim();
                        }
                    }
                }

            }
            Commit commit = new Commit(hash, author, authorDate, message);
            log.gitLog.add(commit);
        }
        return log;
    }

    /**
     * Reads the log and outputs the object
     *
     * @param plainLog The log file
     * @return The log object
     */
    public static Log read(byte[] plainLog) {
        Log log = new Log();
        String hash = "";
        String author = "";
        LocalDateTime authorDate = null;
        String message = "";
        String[] strings = new String(plainLog).split("\n");
        for (String line : strings) {
            if (line.matches(REGEX_COMMIT) && !hash.equals("")) {
                Commit commit = new Commit(hash, author, authorDate, message);
                log.gitLog.add(commit);
                hash = "";
                author = "";
                authorDate = null;
                message = "";
            }

            if (line.matches(REGEX_COMMIT)) {
                hash = line.split(" ")[1].trim();
            } else if (line.matches(REGEX_AUTHOR)) {
                author = line.substring(MagicNumber.SEVEN).trim();
            } else if (line.matches(REGEX_DATE)) {
                authorDate = CommitDate.of(line.substring(MagicNumber.SEVEN).trim());
            } else {
                if (!line.equals("")) {
                    if (!message.equals("")) {
                        message += ':';
                        message += line.trim();
                    } else {
                        message += line.trim();
                    }
                }
            }
        }
        Commit commit = new Commit(hash, author, authorDate, message);
        log.gitLog.add(commit);
        return log;
    }
}
