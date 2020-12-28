package at.anzola.gitlogextraction.utlis;

import at.anzola.gitlogextraction.response.Commit;
import at.anzola.gitlogextraction.response.Log;

import java.time.DayOfWeek;
import java.time.Month;

/**
 * The Analysis class
 *
 * @author fabioanzola
 */
public class Analysis {

    /**
     * Tracks commits per day
     */
    private LongCounter<DayOfWeek> commitsPerDay;

    /**
     * Tracks commits per month
     */
    private LongCounter<Month> commitsPerMonth;

    /**
     * Tracks commits per year
     */
    private LongCounter<String> commitsPerYear;

    /**
     * The reference to the log
     */
    private Log log;

    /**
     * Default constructor
     *
     * @param log The log to be analysed
     */
    public Analysis(Log log) {
        this.log = log;
        this.commitsPerDay = new LongCounter<DayOfWeek>();
        this.commitsPerMonth = new LongCounter<Month>();
        this.commitsPerYear = new LongCounter<String>();
        fillAnalysisMaps();
    }

    /**
     * Method to analyze the given log
     */
    private void fillAnalysisMaps() {
        for (Commit commit : log.gitLog) {
            this.commitsPerDay.put(commit.authorDate.getDayOfWeek(), 1L);
            this.commitsPerMonth.put(commit.authorDate.getMonth(), 1L);
            this.commitsPerYear.put(String.valueOf(commit.authorDate.getYear()), 1L);
        }
    }

    /**
     * Get CommitsPerDay
     *
     * @return CommitsPerDay
     */
    public LongCounter<DayOfWeek> getCommitsPerDay() {
        return commitsPerDay;
    }

    /**
     * Get CommitsPerMonth
     *
     * @return CommitsPerMonth
     */
    public LongCounter<Month> getCommitsPerMonth() {
        return commitsPerMonth;
    }

    /**
     * Get CommitsPerYear
     *
     * @return CommitsPerYear
     */
    public LongCounter<String> getCommitsPerYear() {
        return commitsPerYear;
    }
}
