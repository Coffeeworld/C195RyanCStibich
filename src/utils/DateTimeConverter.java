/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Date;

/**
 *
 * @author coffeeworld
 */
public class DateTimeConverter {
    
    public static LocalDate convertStringDateToLocalDate(String stringDate) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        //convert String to LocalDate
	LocalDate localDate = LocalDate.parse(stringDate, formatter);
        return localDate;
    }
    
    public static LocalDateTime convertLocaltoUTC(LocalDateTime ldt) {
        ZonedDateTime ldtZoned = ldt.atZone(ZoneId.systemDefault());
        ZonedDateTime utcZoned = ldtZoned.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime utcZonedLDT = utcZoned.toLocalDateTime();
        return utcZonedLDT;
    }
    
    public static LocalDateTime convertUTCtoLocal(LocalDateTime utc) {
        ZonedDateTime utcZoned = utc.atZone(ZoneId.of("UTC"));
        ZonedDateTime ldtZoned = utcZoned.withZoneSameInstant(ZoneId.systemDefault());
        LocalDateTime ldtZonedUTC = ldtZoned.toLocalDateTime();
        return ldtZonedUTC;
    }
}
