package at.anzola.gitlogextraction.utlis;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * The CommitDate class
 *
 * @author fabioanzola
 */
public class CommitDate {

    /**
     * A list of abbreviations of months
     */
    private final static List<String> MONTHS = Arrays.asList(
            new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"}
                    .clone());

    /**
     * @param s The Date from the logfile
     * @return The parsed LocalDateTime (UTC)
     */
    public static LocalDateTime of(String s) {
        String[] split = s.split(" ");
        String[] time = split[3].split(":");

        ZonedDateTime x = ZonedDateTime.of(
                Integer.parseInt(split[4]),
                MONTHS.indexOf(split[1]) + 1,
                Integer.parseInt(split[2]),
                Integer.parseInt(time[0]),
                Integer.parseInt(time[1]),
                Integer.parseInt(time[2]),
                0,
                ZoneId.of(split[5])
        );

        return LocalDateTime.ofInstant(x.toInstant(), ZoneOffset.UTC);
    }


}
