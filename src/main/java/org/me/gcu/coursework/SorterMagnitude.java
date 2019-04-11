package org.me.gcu.coursework;
//
// Name                 William Thomson
// Student ID           S1426481
// Programme of Study   Computing
//
import java.util.Comparator;

public class SorterMagnitude implements Comparator<Earthquake> {

    public int compare(Earthquake one, Earthquake another){
        int returnVal = 0;

        Double mag1 = Double.valueOf(one.getMagnitude());
        Double mag2 = Double.valueOf(another.getMagnitude());


        if(mag1 < mag2){
            returnVal =  1;
        }
        else if(mag1 > mag2){
            returnVal =  -1;

        }
        else if(mag1 == mag2){
            returnVal =  0;
        }
        return returnVal;
    }
}
