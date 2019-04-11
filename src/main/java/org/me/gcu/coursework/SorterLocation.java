package org.me.gcu.coursework;
//
// Name                 William Thomson
// Student ID           S1426481
// Programme of Study   Computing
//
import java.util.Comparator;

public class SorterLocation implements Comparator<Earthquake> {

    public int compare(Earthquake one, Earthquake another){


        return one.getLocation().compareTo(another.getLocation());
    }


}
