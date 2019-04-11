package org.me.gcu.coursework;
//
// Name                 William Thomson
// Student ID           S1426481
// Programme of Study   Computing
//
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class SorterDate implements Comparator<Earthquake> {



    public int compare(Earthquake p, Earthquake q) {

        DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateAsString = p.getPubDate();
        String dateAsString2 = q.getPubDate();
        try {
            Date date = sourceFormat.parse(dateAsString);
            Date date2 = sourceFormat.parse(dateAsString2);
            if (date.before(date2)) {
                return 1;
            } else if (date.after(date2)) {
                return -1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;


    }
}
