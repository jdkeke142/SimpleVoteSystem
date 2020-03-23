package fr.keke142.simplevotesystem.utils;

import java.util.Calendar;
import java.util.Date;

public final class DateUtil {

    private DateUtil() {
        throw new UnsupportedOperationException();
    }

    public static Date addSecondsToDate(Date date, int seconds) {
        Calendar calTimeNextVote = Calendar.getInstance();
        calTimeNextVote.setTime(date);
        calTimeNextVote.add(Calendar.SECOND, seconds);

        return calTimeNextVote.getTime();
    }
}
