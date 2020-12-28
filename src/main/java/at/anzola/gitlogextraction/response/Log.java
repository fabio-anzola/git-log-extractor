package at.anzola.gitlogextraction.response;

import java.util.HashSet;
import java.util.Set;

/**
 * The Log class
 *
 * @author fabioanzola
 */
public class Log {
    /**
     * Contains all commits
     */
    public Set<Commit> gitLog = new HashSet<>();

    /**
     * toString method for the Log
     *
     * @return Nicely formatted message
     */
    @Override
    public String toString() {
        return "Log{"
                + "gitLog=" + gitLog
                + '}';
    }
}
