package no.nav.sbl.dialogarena.mapping;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

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