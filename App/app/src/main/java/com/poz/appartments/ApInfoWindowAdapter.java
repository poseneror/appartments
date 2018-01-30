package com.poz.appartments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Or on 31/07/2017.
 */

public class ApInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private Context context;

    public ApInfoWindowAdapter(Context context){
        this.context = context;
    }
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        // Inflate the layouts for the info window, title and snippet.
        LayoutInflater li;
        li = LayoutInflater.from(context);
        View infoWindow;
        // new apartment marker info layout
        if(marker.getTitle().compareTo("NEW") == 0) {
            infoWindow = li.inflate(R.layout.info_window_new, null);
            TextView title = ((TextView) infoWindow.findViewById(R.id.title));
            title.setText(marker.getTitle());
            TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
            snippet.setText(marker.getSnippet());
        }
        // default apartment layout
        else {
            infoWindow = li.inflate(R.layout.info_window_ap, null);
            TextView title = ((TextView) infoWindow.findViewById(R.id.title));
            title.setText(marker.getTitle());
            TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
            snippet.setText(marker.getSnippet());
        }
        return infoWindow;
    }


}
