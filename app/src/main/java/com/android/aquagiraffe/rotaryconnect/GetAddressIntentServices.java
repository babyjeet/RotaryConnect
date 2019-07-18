package com.android.aquagiraffe.rotaryconnect;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GetAddressIntentServices extends IntentService {

    private static final String IDENTIFIER = "GetAddressIntentService";
    private ResultReceiver addressResultReceiver;

    public GetAddressIntentServices() {
        super(IDENTIFIER);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String msg = "";

        addressResultReceiver = intent.getParcelableExtra("add_receiver");

        if (addressResultReceiver == null)
        {
            return;
        }
        Location location = intent.getParcelableExtra("add_location");

        if (location == null)
        {
            msg = "No location, can't go further without location";
            sendResultsToReceiver(0, msg);
            return;
        }

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
        } catch (IOException e) {
            Log.e("", "Error in getting address for the location");
        }

        if (addresses == null || addresses.size() ==0)
        {
            msg = "No address found for the location";
            sendResultsToReceiver(1,msg);
        }else {
            Address address = addresses.get(0);
            StringBuffer addressDetails = new StringBuffer();
            addressDetails.append(address.getThoroughfare());
            addressDetails.append("\n");

            addressDetails.append("");
            addressDetails.append(address.getSubLocality());
            addressDetails.append("\n");

            addressDetails.append(",");
            addressDetails.append(address.getLocality());
            addressDetails.append("\n");

            addressDetails.append(",");
            addressDetails.append(address.getAdminArea());
            addressDetails.append("\n");

            addressDetails.append(",");
            addressDetails.append(address.getCountryName());
            addressDetails.append("\n");

            addressDetails.append(",");
            addressDetails.append(address.getPostalCode());
            addressDetails.append("\n");

            sendResultsToReceiver(2,addressDetails.toString());
        }
    }


    private void sendResultsToReceiver(int resultCode,String message)
    {
        Bundle bundle = new Bundle();
        bundle.putString("address_result", message);
        addressResultReceiver.send(resultCode, bundle);
    }
}
