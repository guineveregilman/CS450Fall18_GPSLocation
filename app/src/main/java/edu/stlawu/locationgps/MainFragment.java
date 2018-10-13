package edu.stlawu.locationgps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;


public class MainFragment extends Fragment implements Observer {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
////////////////////////////////////////////////////////////////////////////////////////////////////

    // Instance variables for GPS Application
    private TextView tv_lat;
    private TextView tv_lon;

    private Observable location;
    private LocationHandler handler = null;
    private final static int PERMISSION_REQUEST_CODE = 999;

    private boolean permissions_granted;
    private final static String LOGTAG =
            MainActivity.class.getSimpleName();

    private Button record;
    private LinearLayout locationValues;

    // Update these every time button is pressed...
    private Location initialLocation;
    private Location currentLocation;
    private Location previousLocation;



///////////////////////////////////////////////////////////////////////////////////////////////////

    private LinearLayout makeLocationInfoView(){

        LinearLayout locationInfoLayout = new LinearLayout(this.getContext());
        locationInfoLayout.setOrientation(LinearLayout.VERTICAL);
        locationInfoLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        locationInfoLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        locationInfoLayout.setPadding(0, 10, 0, 10);
        TextView locationText = new TextView(this.getContext());
        locationText.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        //TODO change the string value
        locationText.setText(String.format("Current Location: (%f, %f)", currentLocation.getLatitude(), currentLocation.getLongitude()));

        //TODO specify units of the text size
        locationText.setTextSize(24);

        locationInfoLayout.addView(locationText);

        return locationInfoLayout;

    }

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        this.tv_lat = view.findViewById(R.id.tv_lat);
        this.tv_lon = view.findViewById(R.id.tv_lon);
        this.record = view.findViewById(R.id.recordButton);
        this.locationValues = view.findViewById(R.id.locationValues);


        if (handler == null) {
            this.handler = new LocationHandler(getActivity());
            this.handler.addObserver(this);
        }

        record.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //TODO create a view and add it to the locationValues Linear Layout
                locationValues.addView(makeLocationInfoView());
            }
        });


        // check permissions
        if (this.getActivity().checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this.getActivity(),
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    PERMISSION_REQUEST_CODE
            );
        }

        // Inflate the layout for this fragment
        return view;
    }

    public boolean isPermissions_granted() {
        return permissions_granted;
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            // we have only asked for FINE LOCATION
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                this.permissions_granted = true;
                Log.i(LOGTAG, "Fine location permisssion granted.");
            }
            else {
                this.permissions_granted = false;
                Log.i(LOGTAG, "Fine location permisssion not granted.");
            }
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void update(Observable observable, Object o) {
        if (observable instanceof LocationHandler) {
            Location l = (Location) o;
            currentLocation = l;
            final double lat = l.getLatitude();
            final double lon = l.getLongitude();

            this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv_lat.setText(Double.toString(lat));
                    tv_lon.setText(Double.toString(lon));

                }
            });
        }
    }


}