package org.me.gcu.coursework;
//
// Name                 William Thomson
// Student ID           S1426481
// Programme of Study   Computing
//
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> implements Filterable {

    private Context lContext;
    //Two data sources, the original data and filtered data
    private LinkedList<Earthquake> originalData;
    private LinkedList<Earthquake> filteredData;

    public EarthquakeAdapter(Context context, LinkedList<Earthquake> earthquakes) {
        super(context, 0, earthquakes);
        Log.e("ContextTag", "Ahhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
        this.lContext = context;
        originalData = earthquakes;
        filteredData = earthquakes;
    }



    //For this helper method, return based on filteredData
    public LinkedList<Earthquake> getFilteredEarthquakes()
    {
        return filteredData;
    }


    //For this helper method, return based on filteredData
    public int getCount()
    {
        return filteredData.size();
    }

    //This should return a data object, not an int
    public Earthquake getItem(int position)
    {
        return filteredData.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }





    //The only change here is that we'll use filteredData.get(position)
    //Even better would be to use getItem(position), which uses the helper method (see the getItem() method above)
    public View getView(int position, View convertView, ViewGroup parent)
    {

        // Get the data item for this position
        Earthquake earthquake = filteredData.get(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Lookup view for data population
        final TextView buttonLocation = (TextView) convertView.findViewById(R.id.tvLocation);

        TextView tvPubDate = (TextView) convertView.findViewById(R.id.tvPubDate);
        TextView tvPubTime = (TextView) convertView.findViewById(R.id.tvPubTime);

//        TextView tvGeoLat = (TextView) convertView.findViewById(R.id.tvGeoLat);
//        TextView tvGeoLong = (TextView) convertView.findViewById(R.id.tvGeoLong);
        TextView tvDepth = (TextView) convertView.findViewById(R.id.tvDepth);
        TextView tvMagnitude = (TextView) convertView.findViewById(R.id.tvMagnitude);
        // TextView tvHome = (TextView) convertView.findViewById(R.id.tvHome);
        // Populate the data into the template view using the data object
        buttonLocation.setText(earthquake.getLocation());
        tvPubDate.setText(earthquake.getPubDate());
        tvPubTime.setText(earthquake.getPubTime());
//        tvGeoLat.setText(earthquake.getLat());
//        tvGeoLong.setText(earthquake.getLong());
        tvDepth.setText(earthquake.getDepth());
        tvMagnitude.setText(earthquake.getMagnitude());



        // setting color severity for each earthquake
        float magnitiude = Float.parseFloat(earthquake.getMagnitude());

        if(magnitiude >= 0 && magnitiude < 3) {
            setTextViewDrawableColor(buttonLocation, R.color.Cyan);
        }
        else if(magnitiude >= 3 && magnitiude < 5) {
            setTextViewDrawableColor(buttonLocation, R.color.colorC9A098);
        }
        else if(magnitiude >= 5 && magnitiude < 7) {
            setTextViewDrawableColor(buttonLocation, R.color.color967D6B);
        }
        else if(magnitiude >= 7) {
            setTextViewDrawableColor(buttonLocation, R.color.color3E3C3B);
        }


        // Return the completed view to render on screen
        return convertView;


    }

    // setting color severity for each earthquake
    private void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(textView.getContext(), color), PorterDuff.Mode.SRC_IN));
            }
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {
                FilterResults results = new FilterResults();

                //If there's nothing to filter on, return the original data for your list
                if(charSequence == null || charSequence.length() == 0)
                {
                    results.values = originalData;
                    results.count = originalData.size();
                }
                else
                {
                    LinkedList<Earthquake> filterResultsData = new LinkedList<Earthquake>();

                    for(Earthquake data : originalData)
                    {
                        //In this loop, you'll filter through originalData and compare each item to charSequence.
                        //If you find a match, add it to your new ArrayList
                        //I'm not sure how you're going to do comparison, so you'll need to fill out this conditional
                        if(data.getLocation().toLowerCase().contains(charSequence.toString().toLowerCase())) //data matches your filter criteria)
                        {
                            filterResultsData.add(data);
                        }
                    }

                    results.values = filterResultsData;
                    results.count = filterResultsData.size();

                }

                return results;
            }

            @Override
            @SuppressWarnings({"unchecked"})
            protected void publishResults(CharSequence charSequence, FilterResults filterResults)
            {
                filteredData = (LinkedList<Earthquake>)filterResults.values;

                notifyDataSetChanged();
            }
        };
    }

//    @Override
//    public Filter getFilter() {
//        return new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence charSequence) {
//                FilterResults results = new FilterResults();
//
//                //If there's nothing to filter on, return the original data for your list
//                if (charSequence == null || charSequence.length() == 0) {
//                    results.values = originalData;
//                    results.count = originalData.size();
//                } else {
//                    ArrayList<HashMap<String, String>> filterResultsData = new ArrayList<HashMap<String, String>>();
//
//                    for (HashMap<String, String> data : originalData) {
//                        //In this loop, you'll filter through originalData and compare each item to charSequence.
//                        //If you find a match, add it to your new ArrayList
//                        //I'm not sure how you're going to do comparison, so you'll need to fill out this conditional
//                        if (data matches your filter criteria)
//                        {
//                            filterResultsData.add(data);
//                        }
//                    }
//
//                    results.values = filterResultsData;
//                    results.count = filteredResultsData.size();
//                }
//
//                return results;
//            }
//        };
//    }

}