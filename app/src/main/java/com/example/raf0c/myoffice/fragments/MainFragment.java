package com.example.raf0c.myoffice.fragments;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.raf0c.myoffice.R;
import com.example.raf0c.myoffice.constants.Constants;
import com.example.raf0c.myoffice.controller.ApplicationController;
import com.example.raf0c.myoffice.interfaces.MyLocationListener;
import com.example.raf0c.myoffice.model.Visits;
import com.example.raf0c.myoffice.services.GeofenceTransitionsIntentService;
import com.example.raf0c.myoffice.sql.VisitsDataSource;
import com.example.raf0c.myoffice.utils.GeofenceErrorMessages;
import com.example.raf0c.myoffice.utils.PlaceJSONParser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by raf0c on 25/09/15.
 */
public class MainFragment extends Fragment
        implements ConnectionCallbacks, OnConnectionFailedListener, ResultCallback<Status>, OnMarkerDragListener{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LinearLayout myLayout;
    private MapView mapView;
    private static Context mContext;
    public  String mCityName;
    public  double mLatitude;
    public  double mLongitude;
    protected static final String TAG = "creating-and-monitoring-geofences";
    public AutoCompleteTextView autoPlaces;
    private Marker mMarker;

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * The list of geofences used in this sample.
     */
    protected ArrayList<Geofence> mGeofenceList;

    /**
     * Used to keep track of whether geofences were added.
     */
    private boolean mGeofencesAdded;

    /**
     * Used when requesting to add or remove geofences.
     */
    private PendingIntent mGeofencePendingIntent;

    /**
     * Used to persist application state about whether geofences were added.
     */
    private SharedPreferences mSharedPreferences;

    // Button for removing geofences.
    private Button mRemoveGeofencesButton;

    private Button mDragMarkerButton;

    private Button mFinishDraggingButton;

    public  static MyLocationListener myLocationListener;
    private boolean locationChanged = false;


    public MainFragment() {
    }

    public static MainFragment newInstance(Context context) {
        MainFragment fragment = new MainFragment();
        mContext = context;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myLayout = (LinearLayout) inflater.inflate(R.layout.fragment_map, container, false);
        mapView = (MapView) myLayout.findViewById(R.id.mapView);

        mapView.onCreate(savedInstanceState);
        mapView.onResume(); //without this, map showed but was empty
        autoPlaces = (AutoCompleteTextView) myLayout.findViewById(R.id.search_place);
        autoPlaces.setThreshold(1);



        autoPlaces.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fetchPlaces(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        // Setting an item click listener for the AutoCompleteTextView dropdown list
        autoPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int index, long id) {

                ListView lv = (ListView) arg0;
                SimpleAdapter adapter = (SimpleAdapter) arg0.getAdapter();

                HashMap<String, String> hm = (HashMap<String, String>) adapter.getItem(index);

                // Getting url to the Google Places details api
                String url = hm.get("reference");

                getPlaceInfo(url);

            }
        });

        // Gets to GoogleMap from the MapView and does initialization stuff
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.getUiSettings().setMyLocationButtonEnabled(false);

            }
        });
        //mMap.setMyLocationEnabled(false);

        mRemoveGeofencesButton = (Button) myLayout.findViewById(R.id.remove_geofences_button);
        mDragMarkerButton = (Button) myLayout.findViewById(R.id.drag_marker_button);
        mFinishDraggingButton = (Button) myLayout.findViewById(R.id.finish_dragging);

        mRemoveGeofencesButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                removeGeofence(v);
            }
        });
        mDragMarkerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                handleMarkerManually(v);
            }
        });
        mFinishDraggingButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(v);
            }
        });

        // Empty list for storing geofences.
        mGeofenceList = new ArrayList<>();

        // Initially set the PendingIntent used in addGeofences() and removeGeofences() to null.
        mGeofencePendingIntent = null;

        // Retrieve an instance of the SharedPreferences object.
        mSharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, getActivity().MODE_PRIVATE);

        // Get the value of mGeofencesAdded from SharedPreferences. Set to false as a default.
        mGeofencesAdded = mSharedPreferences.getBoolean(Constants.GEOFENCES_ADDED_KEY, false);
        setButtonsEnabledState();


        // Kick off the request to build GoogleApiClient.
        buildGoogleApiClient();


        return  myLayout;
    }

    private void showDialog(final View v){
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

        View promptView = layoutInflater.inflate(R.layout.layout_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        // set prompts.xml to be the layout file of the alertdialog builder
        alertDialogBuilder.setView(promptView);

        final EditText input = (EditText) promptView.findViewById(R.id.userInput);

        // setup a dialog window
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // get user input and set it to result
                        mCityName = input.getText().toString();
                        mDragMarkerButton.setVisibility(View.VISIBLE);
                        mFinishDraggingButton.setVisibility(View.GONE);
                        mLatitude = mMap.getMyLocation().getLatitude();
                        mLongitude = mMap.getMyLocation().getLongitude();
                        Log.e("FinalAddress", mCityName + " -- " + mLatitude + " -- " + mLongitude + " --");
                        addGeofence(v);

                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,	int id) {
                                mDragMarkerButton.setVisibility(View.VISIBLE);
                                mFinishDraggingButton.setVisibility(View.GONE);
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alertD = alertDialogBuilder.create();

        alertD.show();

    }



    private void handleMarkerManually(View v){
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        //mMap.setMyLocationEnabled(true);

        // Get the button view
        View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        locationButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMap!=null){
                    LatLng loc = new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude());
                    mMap.clear();
                    mMarker = mMap.addMarker(new MarkerOptions().position(loc).draggable(true));
                    if(mMap != null){
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                    }
                }else{
                    Toast.makeText(getActivity(),"The map is not rendered",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //mMap.setOnMyLocationChangeListener(myLocationChangeListener);
        mFinishDraggingButton.setVisibility(View.VISIBLE);
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.clear();
            mMarker = mMap.addMarker(new MarkerOptions().position(loc).draggable(true));
            if(mMap != null){
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
            }
        }
    };


/**
 * Marker listeners methods
 */

    @Override
    public void onMarkerDragStart(Marker marker) {

        mLatitude = marker.getPosition().latitude;
        mLongitude = marker.getPosition().longitude;

    }

    @Override
    public void onMarkerDrag(Marker marker) {
        mLatitude = marker.getPosition().latitude;
        mLongitude = marker.getPosition().longitude;
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        mLatitude = marker.getPosition().latitude;
        mLongitude = marker.getPosition().longitude;

    }

    private void fetchPlaces(String place) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/autocomplete/json?");
        JsonObjectRequest request  = new JsonObjectRequest(googlePlacesUrl.toString(), null, null);
        try {
            googlePlacesUrl.append("input=" + URLEncoder.encode(place, "utf-8"));
            googlePlacesUrl.append("&types=geocode");
            googlePlacesUrl.append("&sensor=true");
            googlePlacesUrl.append("&key=" + GeofenceErrorMessages.getGoogleMapsBrowserKey(getActivity()));

             request = new JsonObjectRequest(googlePlacesUrl.toString(), null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            List<HashMap<String, String>> places = null;

                            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

                            places = placeJsonParser.parse(jsonObject);

                            String[] from = new String[] { "description"};
                            int[] to = new int[] { R.id.autoText };

                            // Creating a SimpleAdapter for the AutoCompleteTextView
                            final SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), places, R.layout.item_actv, from, to);
                            // Setting the adapter
                            autoPlaces.setAdapter(adapter);


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(getActivity(), "Unable to fetch data: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }


        ApplicationController.getInstance().getRequestQueue().add(request);
    }


    private void getPlaceInfo(String ref) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
        JsonObjectRequest request  = new JsonObjectRequest(googlePlacesUrl.toString(), null, null);
        try {
            googlePlacesUrl.append("reference=" + URLEncoder.encode(ref, "utf-8"));
            googlePlacesUrl.append("&sensor=true");
            googlePlacesUrl.append("&key=" + GeofenceErrorMessages.getGoogleMapsBrowserKey(getActivity()));
            request = new JsonObjectRequest(googlePlacesUrl.toString(), null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            mMap.clear();
                            List<HashMap<String, String>> places = null;

                            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

                            places = placeJsonParser.parsePlace(jsonObject);

                            HashMap<String, String> hm = places.get(0);

                            // Getting latitude from the parsed data
                            double latitude = Double.parseDouble(hm.get("lat"));
                            mLatitude = latitude;
                            // Getting longitude from the parsed data
                            double longitude = Double.parseDouble(hm.get("lng"));
                            mLongitude = longitude;

                            String address = hm.get("formatted_address");
                            mCityName = address;

                            LatLng point = new LatLng(latitude, longitude);

                            CameraUpdate cameraPosition = CameraUpdateFactory.newLatLng(point);
                            CameraUpdate cameraZoom = CameraUpdateFactory.zoomBy(5);

                            // Showing the user input location in the Google Map
                            mMap.moveCamera(cameraPosition);
                            mMap.animateCamera(cameraZoom);

                            MarkerOptions options = new MarkerOptions();
                            options.position(point);
                            options.title("Office");
                            options.snippet("Latitude:"+latitude+",Longitude:"+longitude);
                            Circle circle = mMap.addCircle(new CircleOptions().center(point).radius(250).strokeColor(Color.RED));

                            // Adding the marker in the Google Map
                            mMap.addMarker(options);

                            addGeofence(getView());


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(getActivity(), "Unable to fetch data: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }


        ApplicationController.getInstance().getRequestQueue().add(request);
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }


    @SuppressLint("LongLogTag")
    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Connected to GoogleApiClient");

    }

    @SuppressLint("LongLogTag")
    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());

    }

    @SuppressLint("LongLogTag")
    @Override
    public void onResult(Status status) {
        if (status.isSuccess()) {
            // Update state and save in shared preferences.
            mGeofencesAdded = !mGeofencesAdded;
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(Constants.GEOFENCES_ADDED_KEY, mGeofencesAdded);
            editor.commit();

            // Update the UI. Adding geofences enables the Remove Geofences button
            setButtonsEnabledState();

            Toast.makeText(getActivity(), getString(mGeofencesAdded ? R.string.geofences_added : R.string.geofences_removed), Toast.LENGTH_SHORT).show();
        } else {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = GeofenceErrorMessages.getErrorString(getActivity(), status.getStatusCode());
            Log.e(TAG, errorMessage);
        }
    }

    /**
     * Builds and returns a GeofencingRequest. Specifies the list of geofences to be monitored.
     * Also specifies how the geofence notifications are initially triggered.
     */
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
        // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
        // is already inside that geofence.
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

        // Add the geofences to be monitored by geofencing service.
        builder.addGeofence(getGeoFence());


        // Return a GeofencingRequest.
        return builder.build();
    }


    /**
     * Adds geofences, which sets alerts to be notified when the device enters or exits one of the
     * specified geofences. Handles the success or failure results returned by addGeofences().
     */
    public void addGeofence(View view) {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(getActivity(), getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    // The GeofenceRequest object.
                    getGeofencingRequest(),
                    // A pending intent that that is reused when calling removeGeofences(). This
                    // pending intent is used to generate an intent when a matched geofence
                    // transition is observed.
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            logSecurityException(securityException);
        }
    }

    /**
     * Removes geofences, which stops further notifications when the device enters or exits
     * previously registered geofences.
     */
    public void removeGeofence(View view) {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(getActivity(), getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            // Remove geofences.
            LocationServices.GeofencingApi.removeGeofences(
                    mGoogleApiClient,
                    // This is the same pending intent that was used in addGeofences().
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            logSecurityException(securityException);
        }
    }

    @SuppressLint("LongLogTag")
    private void logSecurityException(SecurityException securityException) {
        Log.e(TAG, "Invalid location permission. " + "You need to use ACCESS_FINE_LOCATION with geofences", securityException);
    }

    /**
     * Gets a PendingIntent to send with the request to add or remove Geofences. Location Services
     * issues the Intent inside this PendingIntent whenever a geofence transition occurs for the
     * current list of geofences.
     *
     * @return A PendingIntent for the IntentService that handles geofence transitions.
     */
    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(getActivity(), GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        intent.putExtra(Constants.OFFICE_TAG, mCityName);
        intent.putExtra(Constants.LAT_TAG, mLatitude);
        intent.putExtra(Constants.LONG_TAG, mLongitude);
        return PendingIntent.getService(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    public Geofence getGeoFence() {

        Log.e("HIHI", "Latitude:"+mLatitude+",Longitude:"+mLongitude);

        Geofence gf = new Geofence.Builder()
                    // Set the request ID of the geofence. This is a string to identify this
                    // geofence.
                    .setRequestId("Office")

                            // Set the circular region of this geofence.
                    .setCircularRegion(mLatitude, mLongitude, Constants.GEOFENCE_RADIUS_IN_METERS)

                            // Set the expiration duration of the geofence. This geofence gets automatically
                            // removed after this period of time.
                    .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)

                            // Set the transition types of interest. Alerts are only generated for these
                            // transition. We track entry and exit transitions in this sample.
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)

                            // Create the geofence.
                    .build();
        return gf;
        }


    /**
     * Ensures that only one button is enabled at any time. The Add Geofences button is enabled
     * if the user hasn't yet added geofences. The Remove Geofences button is enabled if the
     * user has added geofences.
     */
    private void setButtonsEnabledState() {
        if (mGeofencesAdded) {
            mRemoveGeofencesButton.setEnabled(true);
        } else {
            mRemoveGeofencesButton.setEnabled(false);
        }
    }

}