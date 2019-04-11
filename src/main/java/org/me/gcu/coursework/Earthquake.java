package org.me.gcu.coursework;
//
// Name                 William Thomson
// Student ID           S1426481
// Programme of Study   Computing
//
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Earthquake {

    // private int id;
    private String title;
    private String description;
    private String pubDate;
    private String pubTime;
    private String category;
    private String geoLat;
    private String geoLong;

    private String magnitude;
    private String depth;
    private String location;
    private String link;




    // default constructor
    public Earthquake() {
        title = "World Earthquake Alert";
    }

    // constructor - parameters
//    public Earthquake(String lTitle,String lDescription, String lPubDate, String lCategory;) {
//        title = lTitle;
//        description = lDescription;
//        pubDate = lPubDate;
//        category = lCategory;
//        //geoLat = 0;
//        //geoLong = 0;
//    }


    public String determineColor(){

        String color = "white";

        if (Integer.valueOf(getMagnitude()) > 5 ){
            return "red";
        }

        return color;
    }

    public String parseDateToddMMyyyy(String time) {

        Log.e("output_string_date", "output_string_date: " + time);


        String inputPattern = "EEE, dd MMM yyyy HH:mm:ss";
        String outputPattern = "dd/MM/yyyy";
        String outputPatternTime = "HH:mm:ss";

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        SimpleDateFormat outputFormatTime = new SimpleDateFormat(outputPatternTime);

        Date date;
        Date timeFromDate;
        String str = null;
        Log.e("output_string_date", "2: " + time);

        try {
            date = inputFormat.parse(time);
            timeFromDate = inputFormat.parse(time);
            str = outputFormat.format(date);
            this.pubTime = outputFormatTime.format(timeFromDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

//    public String parseDateToddMMyyyy(String time) {
//
//        Log.e("output_string_date", "output_string_date: " + time);
//
//
//        String inputPattern = "EEE, dd MMM yyyy HH:mm:ss";
//        String outputPattern = "dd/MM/yyyy";
//        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
//        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
//
//        Date date;
//        String str = null;
//        Log.e("output_string_date", "2: " + time);
//
//        try {
//            date = inputFormat.parse(time);
//            Log.e("output_string_date_conv", "output_string_date: " + str);
//
//            str = outputFormat.format(date);
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return str;
//    }
//







    // toString() method
    public String toString()
    {
        String instanceDetails;
        instanceDetails = "Title: " + getTitle()
                //+ "\nDescription: " + getDescription()
                + "\nCategory: " + getCategory()
                + "\nPubDate: " + getPubDate()
                + "\nPubTime: " + getPubTime()
                + "\nLong: " + getLong()
                + "\nLat: " + getLat()
                + "\nMagnitude: " + getMagnitude()
                + "\nDepth: " + getDepth()
                + "\nLocation: " + getLocation()
                + "\nLink: " + getLink();

        return instanceDetails;
    }

    // getters and setters
//    public int getId(){
//        return id;
//    }
//    public void setId(int lId){
//        this.id = lId;
//    }
    public String getTitle(){
        return title;
    }
    public void setTitle(String lTitle){
        this.title = lTitle;
    }
    


    public String getDescription(){
        return description;
    }
    public void setDescription(String lDescription){
        this.description = lDescription;
    }



    public String getPubTime(){


        return pubTime;
    }

    public String getPubDate(){
        return pubDate;
    }
    public void setPubDate(String lPubDate){

        // set date and time
        String date = parseDateToddMMyyyy(lPubDate);
        this.pubDate = date;
    }


    public String getCategory(){
        return category;
    }
    public void setCategory(String lCategory) {
        this.category = lCategory;
    }

    public String getLong(){
        return geoLong;
    }
    public void setLong(String lGeoLong){
        this.geoLong = lGeoLong;
    }


    public String getLat(){
        return geoLat;
    }
    public void setLat(String lGeoLat) {
        this.geoLat = lGeoLat;
    }




    public String getMagnitude(){
        return magnitude;
    }
    public void setMagnitude(String lMagnitude) {
        this.magnitude = lMagnitude;
    }

    public String getDepth(){
        return depth;
    }
    public void setDepth(String lDepth) {
        String earthquakeDepth = lDepth.replaceAll("\\s+","");
        this.depth = earthquakeDepth;
    }

    public String getLocation(){
        return location;
    }
    public void setLocation(String llocation) {
        this.location = llocation;
    }



    public String getLink(){
        return link;
    }
    public void setLink(String llink) {
        this.link = llink;
    }

}
