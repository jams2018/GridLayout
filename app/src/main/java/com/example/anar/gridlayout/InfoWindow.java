package com.example.anar.gridlayout;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

public class InfoWindow implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public InfoWindow(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = ((MapsActivity)context).getLayoutInflater()
                .inflate(R.layout.activity_info_window, null);

        TextView name =  view.findViewById(R.id.camera_name);
        ImageView img =  view.findViewById(R.id.camera_image);
        Camera infoWindowData = (Camera) marker.getTag();
        Picasso.with(context).load(infoWindowData.getImageUrl()).into(img);
        name.setText(infoWindowData.getDescription().toString());

        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

}
