package org.me.gcu.coursework;
//
// Name                 William Thomson
// Student ID           S1426481
// Programme of Study   Computing
//
import android.util.Log;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Arrays;
import java.util.LinkedList;



public class XMLPullParserHandler {

    LinkedList<Earthquake> allEarthquakes;
    private Earthquake earthquake;
    private String text;




    public XMLPullParserHandler() {
        // make all to parsing code
        // Note this is not the best location
        allEarthquakes = new LinkedList<>();

        // Write list to log for testing
//        if(allEarthquakes != null){
//            Log.e("MyTag", "List not null");
//            for (Object o : allEarthquakes){
//                Log.e("MyTag", o.toString());
//            }
//        }
//        else {
//            Log.e("MyTag", "List null");
//
//        }
    }


    // get all earthquakes
    public LinkedList<Earthquake> getEarthquakes() {
        return allEarthquakes;
    }



    public LinkedList<Earthquake> parse(String lDataToParse)  {
        //Earthquake widget = null;
        //LinkedList<Earthquake> allEarthquakes = null;


        Boolean itemsStart = false;
        XmlPullParserFactory factory;
        XmlPullParser xpparser;


        try {

            String dataToParse2 = lDataToParse.replaceAll("geo:lat", "lat");
            String dataToParse = dataToParse2.replaceAll("geo:long", "long");


            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);

            xpparser = factory.newPullParser();
            xpparser.setInput(new StringReader(dataToParse));
            Log.e("Start try - parsing","here");

            int eventType = xpparser.getEventType();
            //eventType = xpparser.next();

            // eventType.next();
            Log.i("Tag names are ", "EventType: " + eventType);


            while (eventType != XmlPullParser.END_DOCUMENT) {



                switch (eventType) {
                    case XmlPullParser.START_TAG:

                        // Check which Tag we have
                        if (xpparser.getName().equalsIgnoreCase("rss")) // maybe 'channel'
                        {
                            // allEarthquakes = new LinkedList<>();
                            Log.e("ItemStart","Start Tag: Rss");


                        } else if (xpparser.getName().equalsIgnoreCase("channel")) // maybe 'channel'
                        {
                            allEarthquakes = new LinkedList<>();
                            Log.e("ItemStart","Start Tag: Channel");


                        } else if (xpparser.getName().equalsIgnoreCase("image")) // maybe 'channel'
                        {
                            Log.e("ItemStart","Start Tag: image");

                        } else if (xpparser.getName().equalsIgnoreCase("item")) {
                            Log.e("ItemStart","Start Tag: item");
                            earthquake = new Earthquake();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = xpparser.getText();
                        Log.e("ItemText","Text Tag: " + xpparser.getText());

                        break;
                    case XmlPullParser.END_TAG:

                        if (xpparser.getName().equalsIgnoreCase("image")) {
                            // Log.e("ItemText","Text Tag: " + itemsStart);
                            itemsStart = true;

                        } else
                        if (xpparser.getName().equalsIgnoreCase("item")) {
                            //Log.e("MyTag","widget is " + earthquake.toString());
                            allEarthquakes.add(earthquake);
                        }
//                        else if (xpparser.getName().equalsIgnoreCase("title") && itemsStart == true) {
//                            // Now just get the associated text
//                            // String temp = xpparser.nextText();
//                            // Do something with text
////                            Log.e("MyTag","Title is " + temp);
//                            earthquake.setTitle(text);
//
//
//                        }
                        else if (xpparser.getName().equalsIgnoreCase("description") && itemsStart == true) {
                            //String temp = xpparser.nextText();
                            parseDescription(text);

                            earthquake.setDescription(text);
                        }
//                        else
//                        if (tagname.equalsIgnoreCase("link"))
//                        {
//                            earthquake.setLink(text);
//                        }
                        else if (xpparser.getName().equalsIgnoreCase("pubDate") && itemsStart == true) {
                            // String temp = xpparser.nextText();
                            earthquake.setPubDate(text);
                        }
                        else if (xpparser.getName().equalsIgnoreCase("category") && itemsStart == true) {
                            // String temp = xpparser.nextText();
                            earthquake.setCategory(text);
                        }
                        else if (xpparser.getName().equalsIgnoreCase("lat"))
                        {
                            Log.e("LatText","LatText Tag: " + xpparser.getText());
                            earthquake.setLat(text);
                        }
                        else if (xpparser.getName().equalsIgnoreCase("long"))
                        {
                            Log.e("LongText","LongText Tag: " + xpparser.getText());

                            earthquake.setLong(text);
                        }
                        else if (xpparser.getName().equalsIgnoreCase("link") && itemsStart == true)
                        {
                            //Log.e("LongText","LongText Tag: " + xpparser.getText());

                            earthquake.setLink(text);
                        }


                        break;

                    default:

                        break;
                }

                // Get the next event
                eventType = xpparser.next();

            } // End of while

            //return allEarthquakes;
        } catch (XmlPullParserException ae1) {
            Log.e("MyTags", "Parsing error: " + ae1.toString());
        } catch (IOException ae1) {
            Log.e("MyTagsss", "IO error during parsing");
        }

        Log.e("MyTag","End document");

        return allEarthquakes;

    }



    // parse the description to attain the:
    //  long, lat, location,
    public void parseDescription(String lDescriptionFromXML){
        // further parse title to get longitude
        // date/time: Sat, 29 Dec 2018 03:39:09 ,  Location: PHILIPPINES ,  Lat/long: 5.974,126.828 ,  Depth: 60 km ,  Magnitude: 7.0]
        // 0                                        1                       2                            3             4
        String magnitude = "";

        String[] listOfData = lDescriptionFromXML.split(";");
        String[] value;

        for(int x = 0; x <= 4; x++)
        {
            if( x==1 )
            {
                value = listOfData[x].split(":");
                // Log.e("MyTag","Description FParsed: \n" + value[1]);
                String earthquakeDetail = value[1];
                earthquake.setLocation(earthquakeDetail);
            }
            else if( x==3 ){
                value = listOfData[x].split(":");
                // Log.e("MyTag","Descrip FParsed: \n" + value[1]);
                String earthquakeDetail = value[1];
                earthquake.setDepth(earthquakeDetail);
            }
            else if( x==4 ){
                value = listOfData[x].split(":");
                // Log.e("MyTag","Descrip FParsed: \n" + value[1]);
                String earthquakeDetail = value[1];
                earthquake.setMagnitude(earthquakeDetail); }
        }
        //Log.e("MyTag","Descrpt FParsed: " + Arrays.toString(listOfData));
        // return "";
    }
}
