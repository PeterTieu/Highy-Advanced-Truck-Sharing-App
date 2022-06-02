package com.tieutech.highlyadvancedtrucksharingapp.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.android.gms.maps.model.LatLng;

//ABOUT: Class for Parsing JSON objects for the MapsActivity
public class DirectionsJSONParser {

    // Receive a JSONObject and return a list of lists containing latitude and longitude
    public List<List<HashMap>> parse(JSONObject jObject){

        //Instantiate the list of HashMap lists for the route
        List<List<HashMap>> routes = new ArrayList<List<HashMap>>() ;
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;
        ArrayList<String> directions = new ArrayList<String>();

        try {
            jRoutes = jObject.getJSONArray("routes"); //Obtain the JSON Array for the route

            //Traverse all routes
            for(int i=0;i<jRoutes.length();i++){
                jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
                List path = new ArrayList<HashMap<String, String>>();

                //Traverse all legs
                for(int j=0;j<jLegs.length();j++){
                    jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");

                    //Traverse all steps
                    for(int k=0;k<jSteps.length();k++){
                        String polyline = "";
                        //directions.add();
                        polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                        List<LatLng> list = decodePoly(polyline);

                        //Traverse all points
                        for(int l=0;l<list.size();l++){
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("lat", Double.toString(((LatLng)list.get(l)).latitude) );
                            hm.put("lng", Double.toString(((LatLng)list.get(l)).longitude) );
                            path.add(hm);
                        }
                    }
                    routes.add(path);
                }
            }
        }
        //Catch any JSON Exceptions
        catch (JSONException e) {
            e.printStackTrace();
        }
        //Catch any Exceptions
        catch (Exception e){
        }

        return routes;
    }

    //Decode polyline points
    //@param encoded: The encoded polyline point
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>(); //Create to store LatLng of the polyline points
        int index = 0; //Index to traverse all polyline points
        int len = encoded.length(); //The length of a polyline point
        int lat = 0; //The latitude of a polyline point
        int lng = 0; //The longitude of a polyline point

        //Traverse all the encoded polyline points
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }

}