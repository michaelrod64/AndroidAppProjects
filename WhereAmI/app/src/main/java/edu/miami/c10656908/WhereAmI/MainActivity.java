package edu.miami.c10656908.WhereAmI;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener, TextToSpeech.OnInitListener{

    private GoogleApiClient googleAPIClient;
    private Location currentLocation;
    private LocationRequest locationRequest;
    private TextToSpeech mySpeaker;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySpeaker = new TextToSpeech(this,this);
        googleAPIClient = new GoogleApiClient.Builder(this).
                addConnectionCallbacks(this).addOnConnectionFailedListener(this).
                addApi(LocationServices.API).build();
    }

    public void onStart() {
        Log.i("test", "about to start connecting");
        googleAPIClient.connect();
        super.onStart();
    }

    public void onStop() {
        Log.i("test", "is stopped");

        LocationServices.FusedLocationApi.removeLocationUpdates(
                googleAPIClient,this);
        googleAPIClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i("test", "is connected");
        String errorMessage = "";
        LocationSettingsRequest.Builder settingsBuilder;
        PendingResult<LocationSettingsResult> pendingResult;
        LocationSettingsResult settingsResult;

        locationRequest = new LocationRequest();
        locationRequest.setInterval(getResources().getInteger(
                R.integer.time_between_location_updates_ms));
        locationRequest.setFastestInterval(getResources().getInteger(
                R.integer.time_between_location_updates_ms) / 2);
        locationRequest.setPriority(
                LocationRequest.PRIORITY_HIGH_ACCURACY);
        settingsBuilder = new LocationSettingsRequest.Builder();
        settingsBuilder.addLocationRequest(locationRequest);
        pendingResult = LocationServices.SettingsApi.checkLocationSettings(
                googleAPIClient,settingsBuilder.build());

        startLocationUpdates();
    }
    //-----------------------------------------------------------------------------

    public void myClickHandler(View view) {




        //startLocationUpdates();

        //new DecodeLocation(getApplicationContext(),this).
          //      execute(currentLocation);






    }
    //-----------------------------------------------------------------------------
    private void startLocationUpdates() {
        Log.i("test", "starting location updates");
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    googleAPIClient,locationRequest,this);
        } catch (SecurityException e) {
            Toast.makeText(this,"Cannot get updates",Toast.LENGTH_LONG).show();
            finish();
        }

    }
    //-----------------------------------------------------------------------------
    public void onLocationChanged(Location newLocation) {

        Log.i("test", "location changed");
        if(currentLocation == null) {
            currentLocation = newLocation;

            TextView locationText;
            String currentText;
            Time timeOfChange;

            locationText = (TextView) findViewById(R.id.current_location);

            currentText = locationText.getText().toString();


            timeOfChange = new Time();
            timeOfChange.set(currentLocation.getTime());
            currentText += timeOfChange.format("%A %D %T") + "   ";
            currentText += "\nProvider " + currentLocation.getProvider() + " found location\n";

            currentText += String.format("%.2f %s", newLocation.getLatitude(),
                    newLocation.getLatitude() >= 0.0 ? "N" : "S") + "   ";
            currentText += String.format("%.2f %s", newLocation.getLongitude(),
                    newLocation.getLongitude() >= 0.0 ? "E" : "W") + "   ";
            if (newLocation.hasAccuracy()) {
                currentText += String.format("%.2fm", newLocation.getAccuracy());
            }
            currentText += "\n\n";
            locationText.setText(currentText);

            HashMap<String, String> speechParameters = new HashMap<String, String>();
            speechParameters.put(
                    TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "WHAT_I_SAID");
            mySpeaker.speak(currentText, TextToSpeech.QUEUE_ADD,
                    speechParameters);

        }
        else if (currentLocation.distanceTo(newLocation) > 10) {
            TextView locationText;
            String currentText;
            Time timeOfChange;

            locationText = (TextView) findViewById(R.id.current_location);
            currentText = locationText.getText().toString();

            currentLocation = newLocation;

            timeOfChange = new Time();
            timeOfChange.set(currentLocation.getTime());
            currentText += timeOfChange.format("%A %D %T") + "   ";
            currentText += "\nProvider " + currentLocation.getProvider() + " found location\n";

            currentText += String.format("%.2f %s", newLocation.getLatitude(),
                    newLocation.getLatitude() >= 0.0 ? "N" : "S") + "   ";
            currentText += String.format("%.2f %s", newLocation.getLongitude(),
                    newLocation.getLongitude() >= 0.0 ? "E" : "W") + "   ";
            if (newLocation.hasAccuracy()) {
                currentText += String.format("%.2fm", newLocation.getAccuracy());
            }
            currentText += "\n\n";
            locationText.setText(currentText);
            HashMap<String, String> speechParameters = new HashMap<String, String>();
            speechParameters.put(
                    TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "WHAT_I_SAID");
            mySpeaker.speak(currentText, TextToSpeech.QUEUE_ADD,
                    speechParameters);
        }
    }
    //-----------------------------------------------------------------------------
    public void onConnectionFailed(ConnectionResult result) {
        Log.i("test", "connection failed");
    }
    //-----------------------------------------------------------------------------
    public void onConnectionSuspended(int cause) {
        Log.i("test", "connection suspended");

    }

    @Override
    public void onInit(int status) {

    }
//-----------------------------------------------------------------------------
}
//=============================================================================
class DecodeLocation extends AsyncTask<Location,Void,String> {
    //-----------------------------------------------------------------------------
    private Context theContext;
    private Activity theActivity;
    //-----------------------------------------------------------------------------
    public DecodeLocation(Context context,Activity activity) {

        theContext = context;
        theActivity = activity;
    }
    //-----------------------------------------------------------------------------
    protected String doInBackground(Location... location) {

        return(geodecode(location[0]));
    }
    //-----------------------------------------------------------------------------
    protected void onPostExecute(String result) {

        ((TextView)theActivity.findViewById(R.id.current_location)).setText(result);

    }
    //-----------------------------------------------------------------------------
    private String geodecode(Location thisLocation) {

        String locationName;

        locationName = "Android says\n" + androidGeodecode(thisLocation) + "\n\n";
        locationName += "Google says\n" + googleGeodecode(thisLocation,
                "https://maps.googleapis.com/maps/api/geocode/xml?sensor=true&latlng=%f,%f");
//----Aaaargh, can't get this to work
//theActivity.getString(R.string.google_location_url));

        return(locationName);
    }
    //-----------------------------------------------------------------------------
    private String androidGeodecode(Location thisLocation) {

        Geocoder androidGeocoder;
        List<Address> addresses;
        Address firstAddress;
        String addressLine;
        String locationName;
        int index;

        if (Geocoder.isPresent()) {
            androidGeocoder = new Geocoder(theContext);
            try {
                addresses = androidGeocoder.getFromLocation(
                        thisLocation.getLatitude(),thisLocation.getLongitude(),1);
                if (addresses.isEmpty()) {
                    return("ERROR: Unkown location");
                } else {
                    firstAddress = addresses.get(0);
                    locationName = "";
                    index = 0;
                    while ((addressLine = firstAddress.getAddressLine(index)) != null) {
                        locationName += addressLine + ", ";
                        index++;
                    }
                    return (locationName);
                }
            } catch (Exception e) {
                return("ERROR: " + e.getMessage());
            }
        } else {
            return("ERROR: No Geocoder available");
        }
    }
    //-----------------------------------------------------------------------------
    private String googleGeodecode(Location thisLocation,
                                   String urlFormat) {

        String urlString;
        URL url;
        URLConnection urlConnection;
        HttpURLConnection httpConnection;
        InputStream xmlStream;
        DocumentBuilder xmlBuilder;
        Document xmlDocument;
        Element documentElement;
        NodeList nameNodes;
        Node nameNode;
        String theLocationName;

        urlString = String.format(urlFormat,thisLocation.getLatitude(),
                thisLocation.getLongitude());
        try {
            url = new URL(urlString);
            urlConnection = url.openConnection();
            if (!(urlConnection instanceof HttpURLConnection)) {
                return("ERROR: That's not an HTTP connection");
            }
            httpConnection = (HttpURLConnection) urlConnection;
            httpConnection.setRequestMethod("GET");
            httpConnection.setDoOutput(true);
            httpConnection.setReadTimeout(10000);
            try {
                httpConnection.connect();
            } catch (IOException e) {
                return ("ERROR: Could not connect");
            }

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                xmlStream = httpConnection.getInputStream();
                xmlBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                xmlDocument = xmlBuilder.parse(xmlStream);
                xmlStream.close();
                httpConnection.disconnect();
                if ((documentElement = xmlDocument.getDocumentElement()) == null) {
                    return("ERROR: Missing element");
                }
                nameNodes = documentElement.getElementsByTagName("formatted_address");
                if (nameNodes.getLength() < 1) {
                    return("ERROR: No name for this location");
                }
                nameNode = nameNodes.item(0).getFirstChild();
                if (nameNode == null) {
                    return("ERROR: No known name");
                }
                theLocationName = nameNode.getNodeValue();
                return(theLocationName);
            } else {
                return("ERROR: Bad connection");
            }
        } catch (Exception e) {
            return ("ERROR: Error while geodecoding");
        }
    }


}