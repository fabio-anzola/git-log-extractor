package at.anzola.gitlogextraction.utlis;

import at.anzola.gitlogextraction.response.Commit;
import at.anzola.gitlogextraction.response.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * The Anonym class
 *
 * @author fabioanzola
 */
public class Anonym {

    /**
     * A Map of users. Exists for whole application time
     */
    public static Map<String, String> users = new HashMap<>();

    /**
     * Anonymizes a given Log (Via SHA-256 hash)
     *
     * @param log The log to be edited
     * @return The anonym log
     */
    public static Log anonymize(Log log) {
        for (Commit commit : log.gitLog) {
            MessageDigest messageDigest = null;
            try {
                messageDigest = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            messageDigest.update(commit.author.getBytes());
            users.put(
                    commit.author,
                    Base64.getEncoder().encodeToString(messageDigest.digest())
            );
        }
        for (Commit commit : log.gitLog) {
            commit.author = users.get(commit.author);
        }
        return log;
    }
}
