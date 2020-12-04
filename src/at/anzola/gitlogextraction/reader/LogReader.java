package at.anzola.gitlogextraction.reader;

import at.anzola.gitlogextraction.response.Commit;
import at.anzola.gitlogextraction.utlis.CommitDate;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class LogReader {

    private final static String REGEX_COMMIT = "^(commit\\s[a-f0-9]{40})";

    private final static String REGEX_AUTHOR = "^(Author:\\s.+)";

    private final static String REGEX_DATE = "^(Date:[\\s]{3}[A-Za-z]{3}\\s[A-Za-z]{3}\\s[0-9]{1,2}\\s[0-9:]{8}\\s[0-9]{4}\\s[0-9+]{5})";

    private final static String REGEX_FULL_COMMIT = ""
            + "commit\\s[a-f0-9]{40}\\s+" +
            "(Merge: [a-f0-9 ]+)?\\s+" +
            "Author:\\s.+\\n" +
            "Date:[\\s]{3}[A-Za-z]{3}\\s[A-Za-z]{3}\\s[0-9]{2}\\s[0-9:]{8}\\s[0-9]{4}\\s[0-9+]{5}" +
            "\\s+" +
            ".+";

    public static void read(String file) throws IOException {

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
                    System.out.println(commit);
                    hash = "";
                    author = "";
                    authorDate = null;
                    message = "";
                }

                if (line.matches(REGEX_COMMIT)) {
                    hash = line.split(" ")[1].trim();
                }
                else if (line.matches(REGEX_AUTHOR)) {
                    author = line.substring(7).trim();
                }
                else if (line.matches(REGEX_DATE)) {
                    authorDate = CommitDate.of(line.substring(7).trim());
                }
                else {
                    if (!line.equals("")) {
                        if (!message.equals("")) {
                            message += ':';
                            message += line.trim();
                        }
                        else {
                            message += line.trim();
                        }
                    }
                }

            }
            Commit commit = new Commit(hash, author, authorDate, message);
            System.out.println(commit);
        }
    }


}
