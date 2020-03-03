/*
 * * Project for C195
 * @author Jayson Valderrama <jvald74@wgu.edu>
 */
package util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Jayson Valderrama <jvald74@wgu.edu>
 */
public class TimeConverter {
    public static ZonedDateTime getUTCTime(String date) {
        String dateTime[] = date.split(" ", 2);
        String dateSplit[] = dateTime[0].split("-", 3);
        int year = Integer.parseInt(dateSplit[0]);
        int month = Integer.parseInt(dateSplit[1]);
        int day = Integer.parseInt(dateSplit[2]);
        String time[] = dateTime[1].split(":", 3);
        int hour = Integer.parseInt(time[0]);
        int minute = Integer.parseInt(time[1]);
        LocalDateTime ldt = LocalDateTime.of(year, month, day, hour, minute); 
        ZonedDateTime locZdt = ZonedDateTime.of(ldt, ZoneId.systemDefault());
        ZonedDateTime utcZdt = locZdt.withZoneSameInstant(ZoneOffset.UTC);
        return utcZdt;
    }
    
    public static ZonedDateTime getUTCFromDB(String date) {
        String dateTime[] = date.split(" ", 2);
        String dateSplit[] = dateTime[0].split("-", 3);
        int year = Integer.parseInt(dateSplit[0]);
        int month = Integer.parseInt(dateSplit[1]);
        int day = Integer.parseInt(dateSplit[2]);
        String time[] = dateTime[1].split(":", 3);
        int hour = Integer.parseInt(time[0]);
        int minute = Integer.parseInt(time[1]);
        LocalDateTime ldt = LocalDateTime.of(year, month, day, hour, minute); 
        ZonedDateTime utcZdt = ZonedDateTime.of(ldt, ZoneId.of("UTC"));
        return utcZdt;
    }
    
    public static ZonedDateTime getLocalTime(String date) {
        String dateTime[] = date.split(" ", 2);
        String dateSplit[] = dateTime[0].split("-", 3);
        int year = Integer.parseInt(dateSplit[0]);
        int month = Integer.parseInt(dateSplit[1]);
        int day = Integer.parseInt(dateSplit[2]);
        String time[] = dateTime[1].split(":", 3);
        int hour = Integer.parseInt(time[0]);
        int minute = Integer.parseInt(time[1]);
        LocalDateTime ldt = LocalDateTime.of(year, month, day, hour, minute); 
        ZonedDateTime utcZdt = ZonedDateTime.of(ldt, ZoneId.of("UTC"));
        ZonedDateTime locZdt = utcZdt.withZoneSameInstant(ZoneOffset.systemDefault());
        return locZdt;
    }
    
    public static String getDateTimeString(ZonedDateTime zdt) {
        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return customFormatter.format(zdt);
    }
    public static boolean conflictCheck(int userId, ZonedDateTime start, ZonedDateTime end, int ApptID) {
        if (end.compareTo(start) < 1) {
            return true;
        } else {
            try {
                String startTime = getDateTimeString(start);
                String endTime = getDateTimeString(end);
                DBConnection.connect();
                ResultSet rs = DBConnection.query("*", "appointment", "userId=" + userId + " AND ((("
                        + "start BETWEEN '" + startTime + "' AND '" + endTime + "') OR (end BETWEEN '" + 
                        startTime + "' AND '" + endTime + "')) OR ((start < '" + startTime + 
                        "') AND (end > '" + endTime + "'))) AND NOT appointmentId = " + ApptID);
                if (rs.next()) {
                    System.out.println("ApptID: " + ApptID);
                    System.out.println(rs.getInt("appointmentId"));
                    return true;
                }
                return false;
            } catch (SQLException e) {
                System.out.println("problem with db connection");
            } finally {
                DBConnection.closeConn();
            }
            return true;
        }
    }
}
