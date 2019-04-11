
//
// Name                 William Thomson
// Student ID           S1426481
// Programme of Study   Computing
//
package org.me.gcu.coursework;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

// import org.me.gcu.coursework.R;

public class MainActivity extends AppCompatActivity
{
    private TextView rawDataDisplay;
    //private Button startButton;
    private String result;
    private String stringMagic = "";
    private String urlSource="http://quakes.bgs.ac.uk/feeds/WorldSeismology.xml"; // change 'World' to 'Mh' to access data in Britain only.

    InputStream urlSourceInputStream = new ByteArrayInputStream(urlSource.getBytes(StandardCharsets.UTF_8));
    private ListView earthquakeListView;
    LinkedList<Earthquake> allEarthquakes;

    // String stringMagic = "";

    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    public void sendMessage(View view)
    {
        startActivity(new Intent(MainActivity.this, EarthquakeDetailedActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title_bar);



        if(getIntent().getStringExtra("updateUrlSource") != null)
        {
            String urlUpdate = getIntent().getStringExtra("updateUrlSource");
            urlSource = urlUpdate;
        }
        if(getIntent().getStringExtra("urlSource") != null)
        {
            String urlSourceGetIntent = getIntent().getStringExtra("urlSource");
            urlSource = urlSourceGetIntent;
        }

        final ImageButton btnListOfEarthquakes = (ImageButton) findViewById(R.id.btnListOfEarthquakes);
        btnListOfEarthquakes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(MainActivity.this, "List View Clicked", Toast.LENGTH_LONG).show();

                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });


        if(isServiceOK()) {
            Log.d(TAG, "isServiceOK: checking google service version");

            init();
        }



        // Set up the raw links to the graphical components
//        rawDataDisplay = (TextView)findViewById(R.id.rawDataDisplay);
//        startButton = (Button)findViewById(R.id.startButton);
//        startButton.setOnClickListener(this);
        startProgress();



        // More Code goes here -

    }





//    public void onClick(View aview)
//    {
//        startProgress();
//    }

    public void startProgress()
    {
        // Run network access on a separate thread;
        new Thread(new Task(urlSource)).start();
    } //

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable
    {
        private String url;

        public Task(String aurl)
        {
            url = aurl;
        }
        @Override
        public void run()
        {

            URL aurl;
            URLConnection yc;
            BufferedReader in;
            String inputLine = "";


            Log.e("MyTag","in run");

            try
            {
                Log.e("MyTag","in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                Log.e("MyTag","url");

                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));


                //


                // Throw away the first 2 header lines before parsing

                //
                //
                while ((inputLine = in.readLine()) != null)
                {
                    result = result + inputLine;
                    Log.e("MyTagsss",inputLine);
                    stringMagic += inputLine;
                }

                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTaggg", "ioexception");
            }

            //
            // Now that you have the xml data you can parse it
            //earthquakeListView = (ListView)findViewById(R.id.earthquakeList);
//        try {
            XMLPullParserHandler parser = new XMLPullParserHandler();
            allEarthquakes = parser.parse(stringMagic);
            Log.e("URL1", "Ahh: " + stringMagic);

            //earthquakeListView.setAdapter(adapter);

//        }catch(IOException e) {
//            e.printStackTrace();
//        }


            // Now update the TextView to display raw XML data
            // Probably not the best way to update TextView
            // but we are just getting started !

            MainActivity.this.runOnUiThread(new Runnable()
            {
                public void run() {
                    Log.e("UI thread", "I am the UI thread");
                    //ArrayAdapter<Earthquake> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item, allEarthquakes);
                    //earthquakeListView.setAdapter(adapter);

                    // Construct the data source
                    //final ArrayList<Earthquake> arrayOfEarthquakes = new ArrayList<Earthquake>();



                    Spinner spinner = (Spinner) findViewById(R.id.earthquakeSpinner);
                    spinner.setPrompt("Sort");

                    // Create an ArrayAdapter using the string array and a default spinner layout
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MainActivity.this,
                            R.array.spinner_array, android.R.layout.simple_spinner_item);
                    // Specify the layout to use when the list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Apply the adapter to the spinner
                    spinner.setAdapter(adapter);


                    // Create the adapter to convert the array to views
                    final EarthquakeAdapter customAdapter = new EarthquakeAdapter(getApplicationContext(), allEarthquakes);


                    // Attach the adapter to a ListView
                    final ListView listView = (ListView) findViewById(R.id.earthquakeList);
                    EditText etSearchFilter = (EditText) findViewById(R.id.editTextSearchFilter);





                    Log.e("Earthquake Instnce: ", allEarthquakes.get(0).toString());


                    etSearchFilter.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            customAdapter.getFilter().filter(s);
                            customAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });


                    // Spinner click listener
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            // An item was selected. You can retrieve the selected item using
                            // parent.getItemAtPosition(pos)
                            String item = parent.getItemAtPosition(position).toString();


                            if(position == 1){
                                Collections.sort(allEarthquakes, new SorterLocation());
                            }
                            if(position == 2){
                                Collections.sort(allEarthquakes, new SorterMagnitude());
                            }
                            if(position == 3){
                                Collections.sort(allEarthquakes, new SorterDepth());
                            }
                            if(position == 4){
                                Collections.sort(allEarthquakes, new SorterDate());
                            }

                            // Showing selected spinner item
                            Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

                            // Collections.reverse(allEarthquakes);

                            customAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });



                    listView.setAdapter(customAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            //Earthquake earthquake = allEarthquakes.get(position);
                            LinkedList<Earthquake> earthquakess = customAdapter.getFilteredEarthquakes();
                            Earthquake earthquake = earthquakess.get(position);

                            Intent in = new Intent(view.getContext(), EarthquakeDetailedActivity.class);
                            in.putExtra("lvMagnitude", earthquake.getMagnitude());
                            in.putExtra("lvDepth", earthquake.getDepth());
                            in.putExtra("lvLocation", earthquake.getLocation());
                            in.putExtra("lvDate", earthquake.getPubDate());
                            in.putExtra("lvTime", earthquake.getPubTime());
                            in.putExtra("lvLong", earthquake.getLong());
                            in.putExtra("lvLat", earthquake.getLat());
                            startActivity(in);

                        }
                    });

                    listView.setTextFilterEnabled(true);

                    // rawDataDisplay.setText(result);



                }
            });
        }

    }

    public void getSlectedData(int i){

    }

    public void init(){

        final ImageButton btnListOfEarthquakes = (ImageButton) findViewById(R.id.btnRSSFeedChange);
        btnListOfEarthquakes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(MainActivity.this, "List View Clicked", Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);



                builder.setMessage("Select a Data Feed: ");
                builder.setPositiveButton("Britain", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(MainActivity.this, "Britain Data Feed", Toast.LENGTH_LONG).show();

                        Intent in = new Intent(MainActivity.this, MainActivity.class);
                        in.putExtra("updateUrlSource", "http://quakes.bgs.ac.uk/feeds/MhSeismology.xml");
                        startActivity(in);


                    }
                });
                builder.setNegativeButton("World", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(MainActivity.this, "World Data Feed", Toast.LENGTH_LONG).show();
                        Intent in = new Intent(MainActivity.this, MainActivity.class);
                        in.putExtra("updateUrlSource", "http://quakes.bgs.ac.uk/feeds/WorldSeismology.xml");
                        startActivity(in);
                    }
                });
                builder.show();
            }
        });



        final ImageButton btnEarthViewOfEarthquakes = (ImageButton) findViewById(R.id.btnEarthViewOfEarthquakes);
        btnEarthViewOfEarthquakes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(MainActivity.this, "Earth View Clicked", Toast.LENGTH_LONG).show();
                Intent in = new Intent(MainActivity.this, EarthquakeMapActivity.class);

                String[] earthquakesLongLat = new String[allEarthquakes.size()];
                String[] earthquakesLocation = new String[allEarthquakes.size()];
                String[] earthquakesMagnitude = new String[allEarthquakes.size()];
                String[] earthquakesDepth = new String[allEarthquakes.size()];
                String[] earthquakesPubDate = new String[allEarthquakes.size()];
                String[] earthquakesPubTime = new String[allEarthquakes.size()];



                Earthquake earthquake;

                for (int i = 0; i < allEarthquakes.size(); i++)
                {
                    earthquake = allEarthquakes.get(i);
                    earthquakesLongLat[i] = earthquake.getLat() + "," + earthquake.getLong();
                    earthquakesLocation[i] = earthquake.getLocation();
                    earthquakesMagnitude[i] = earthquake.getMagnitude();
                    earthquakesDepth[i] = earthquake.getDepth();
                    earthquakesPubDate[i] = earthquake.getPubDate();
                    earthquakesPubTime[i] = earthquake.getPubTime();



                    Log.e("LngLatString", "::: " + earthquakesLongLat[i]);

                }

                in.putExtra("earthquakesLongLat", earthquakesLongLat);
                in.putExtra("earthquakesLocation", earthquakesLocation);
                in.putExtra("earthquakesMagnitude", earthquakesMagnitude);
                in.putExtra("earthquakesDepth", earthquakesDepth);
                in.putExtra("earthquakesPubDate", earthquakesPubDate);
                in.putExtra("earthquakesPubTime", earthquakesPubTime);


                // the url feed being used
                in.putExtra("urlSource", urlSource);

                startActivity(in);
            }
        });


    }

    public boolean isServiceOK(){
        Log.d(TAG, "isServiceOK: checking google service version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //user can make map request
            Log.d(TAG, "isServiceOK: google play service is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            // an error occured that can be resolved
            Log.d(TAG, "isServiceOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else {
            Toast.makeText(this, "You can't make map request", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


}

//"<rss xmlns:geo=\"http://www.w3.org/2003/01/geo/wgs84_pos#\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" version=\"2.0\">\n" +
//        "<channel>\n" +
//        "<title>Recent UK earthquakes</title>\n" +
//        "<link>http://earthquakes.bgs.ac.uk/</link>\n" +
//        "<description>\n" +
//        "Recent UK seismic events recorded by the BGS Seismology team\n" +
//        "</description>\n" +
//        "<language>en-gb</language>\n" +
//        "<lastBuildDate>Mon, 25 Feb 2019 13:40:00</lastBuildDate>\n" +
//        "<image>\n" +
//        "<title>BGS Logo</title>\n" +
//        "<url>\n" +
//        "http://www.bgs.ac.uk/images/logos/bgs_c_w_227x50.gif\n" +
//        "</url>\n" +
//        "<link>http://earthquakes.bgs.ac.uk/</link>\n" +
//        "</image>\n" +
//        "<item>\n" +
//        "<title>\n" +
//        "UK Earthquake alert : M 1.9 :NEWDIGATE,SURREY, Tue, 19 Feb 2019 17:03:57\n" +
//        "</title>\n" +
//        "<description>\n" +
//        "Origin date/time: Tue, 19 Feb 2019 17:03:57 ; Location: NEWDIGATE,SURREY ; Lat/long: 51.161,-0.254 ; Depth: 2 km ; Magnitude: 1.9\n" +
//        "</description>\n" +
//        "<link>\n" +
//        "http://earthquakes.bgs.ac.uk/earthquakes/recent_events/20190219170228.html\n" +
//        "</link>\n" +
//        "<pubDate>Tue, 19 Feb 2019 17:03:57</pubDate>\n" +
//        "<category>EQUK</category>\n" +
//        "<geo:lat>51.161</geo:lat>\n" +
//        "<geo:long>-0.254</geo:long>\n" +
//        "</item>\n" +
//        "<item>\n" +
//        "<title>\n" +
//        "UK Earthquake alert : M 2.4 :NORWEGIAN SEA, Mon, 18 Feb 2019 01:13:32\n" +
//        "</title>\n" +
//        "<description>\n" +
//        "Origin date/time: Mon, 18 Feb 2019 01:13:32 ; Location: NORWEGIAN SEA ; Lat/long: 62.371,2.297 ; Depth: 10 km ; Magnitude: 2.4\n" +
//        "</description>\n" +
//        "<link>\n" +
//        "http://earthquakes.bgs.ac.uk/earthquakes/recent_events/20190218011330.html\n" +
//        "</link>\n" +
//        "<pubDate>Mon, 18 Feb 2019 01:13:32</pubDate>\n" +
//        "<category>EQUK</category>\n" +
//        "<geo:lat>62.371</geo:lat>\n" +
//        "<geo:long>2.297</geo:long>\n" +
//        "</item>\n" +
//        "<item>\n" +
//        "<title>\n" +
//        "UK Earthquake alert : M 0.2 :NEWDIGATE,SURREY, Thu, 14 Feb 2019 08:02:53\n" +
//        "</title>\n" +
//        "<description>\n" +
//        "Origin date/time: Thu, 14 Feb 2019 08:02:53 ; Location: NEWDIGATE,SURREY ; Lat/long: 51.161,-0.259 ; Depth: 2 km ; Magnitude: 0.2\n" +
//        "</description>\n" +
//        "<link>\n" +
//        "http://earthquakes.bgs.ac.uk/earthquakes/recent_events/20190214080252.html\n" +
//        "</link>\n" +
//        "<pubDate>Thu, 14 Feb 2019 08:02:53</pubDate>\n" +
//        "<category>EQUK</category>\n" +
//        "<geo:lat>51.161</geo:lat>\n" +
//        "<geo:long>-0.259</geo:long>\n" +
//        "</item>"+
//        "</channel>"+
//        "</rss>";