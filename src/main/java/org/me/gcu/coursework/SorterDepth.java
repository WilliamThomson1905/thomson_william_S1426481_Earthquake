package org.me.gcu.coursework;
//
// Name                 William Thomson
// Student ID           S1426481
// Programme of Study   Computing
//
import java.util.Comparator;

public class SorterDepth implements Comparator<Earthquake> {

    public int compare(Earthquake one, Earthquake another){
        int returnVal = 0;

        String depthOne = one.getDepth();
        String parsedString = depthOne.substring(0, depthOne.length() - 2);
        String depthAnother = another.getDepth();
        String parsedString2 = depthAnother.substring(0, depthAnother.length() - 2);


        int depth1 = Integer.valueOf(parsedString);
        int depth2 = Integer.valueOf(parsedString2);


        if(depth1 < depth2){
            returnVal =  1;
        }
        else if(depth1 > depth2){
            returnVal =  -1;

        }
        else if(depth1 == depth2){
            returnVal =  0;
        }
        return returnVal;
    }
}
