package no.nav.sbl.dialogarena.modiasoaprest.mapping;

import org.joda.time.DateTime;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class JodaDatetimeMapper {

    private static final DateTimeFormatter fmt = DateTimeFormatter.ISO_DATE_TIME;

    public static String jodaDatetimeToString(DateTime dt) {
        if(dt == null) {
            return null;
        }
        return LocalDateTime.ofInstant(dt.toDate().toInstant(), ZoneId.systemDefault()).format(fmt);
    }

}