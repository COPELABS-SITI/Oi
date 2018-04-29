package pt.ulusofona.copelabs.oi.helpers;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**
 * Created by copelabs on 11/04/2018.
 */

public class LocationListener implements android.location.LocationListener {

    private static final String TAG = LocationListener.class.getSimpleName();

    private Context mContext;

    private LocationManager mlocationMnager;

    private android.location.Location mLocation;

    private static LocationListener mInstance = null;

    private LocationListener(Context context) {
        mContext = context;
        mlocationMnager = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);

    }

    public static LocationListener getInstance(Context context){
        if(mInstance == null){
            mInstance=new LocationListener(context);
        }
        return mInstance;
    }

    public void start() {

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mlocationMnager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        Log.d(TAG, "LocationListener found: " + location.getLatitude());
        mLocation=location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void stop(){
        mlocationMnager.removeUpdates(this);
    }

    public Location getLocation(){
        if(mLocation==null){
            Location location = new Location("default");
            location.setLatitude(0.0);
            location.setLongitude(0.0);
            mLocation=location;
        }
            return mLocation;
    }
}
