package com.danielkamel.locationpinned;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeoCodingHelper {
    public GeoCodingHelper() {
    }

    public String getGeoCodes(Context context ,  double latitude,double longitude) {


        String name = "";
        System.out.println(latitude+", "+longitude);
        if (Geocoder.isPresent()) {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            try {
                List<Address> ls = geocoder.getFromLocation(latitude, longitude, 4);
                for (Address addr : ls) {
                    if(addr.getFeatureName() != null || addr.getThoroughfare() != null || addr.getLocality() != null){
//                        name = addr.getFeatureName().toString();
                        name = addr.getFeatureName() +" " + addr.getThoroughfare() +", "+ addr.getLocality();
                        break;
                    }



                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return  name;


    }

    public String getCodes(Context context , String name) {


        String cords = "";

//        System.out.println(latitude+", "+longitude);
        if (Geocoder.isPresent()) {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            try {
                List<Address> ls = geocoder.getFromLocationName(name,5);//getFromLocation(latitude, longitude, 4);
                for (Address addr : ls) {
                    if(addr.getFeatureName() != null || addr.getThoroughfare() != null || addr.getLocality() != null){
//                        name = addr.getFeatureName().toString();
//                        name = addr.getFeatureName() +" " + addr.getThoroughfare() +", "+ addr.getLocality();
//                        break;

                        cords = addr.getLatitude() +","+addr.getLongitude();
                        break;
                    }



                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return  cords;


    }
}
