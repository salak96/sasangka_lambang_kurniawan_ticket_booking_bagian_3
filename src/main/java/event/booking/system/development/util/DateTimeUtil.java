package event.booking.system.development.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateTimeUtil {

    public static String formatLocalDateToString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd", Locale.of("id", "ID")));
    }

}


