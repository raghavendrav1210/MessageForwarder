package com.ragav.messageforwarder;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private EditText recipient = null;
    private EditText searchText = null;
    private Button save = null;

    private static final int MY_PERMISSIONS_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readPermission();
        //Fetch details from the Shared Prefs
        String data = readDataFromSharedPrefs();

        recipient = findViewById(R.id.editTextPhone);
        searchText = findViewById(R.id.search_src_text);
        save = findViewById(R.id.save);

        save.setOnClickListener(this);

        if (null == data) {

            Toast.makeText(this, "No recipient available. Please set a recipient", Toast.LENGTH_SHORT).show();
        } else {

            loadWithData(data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        readPermission();
    }

    private void loadWithData(String config) {

        if (null != config && config.contains("~")) {

            String[] recipientDetails = config.split("~");

            if (null != recipientDetails && recipientDetails.length > 0) {

                String phoneNo = recipientDetails[0];
                String filter = recipientDetails[1];

                if (null != filter) {

                    recipient.setText(phoneNo);
                    searchText.setText(filter);
                }
            }
        }
    }

    private void saveDataToSharedPrefs(String data) {

        Context context = this;
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.recipient_info), data);
        editor.commit();

        Toast.makeText(context, "Config Saved", Toast.LENGTH_SHORT).show();
    }

    private String readDataFromSharedPrefs() {

        Context context = this;
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        return sharedPref.getString(getString(R.string.recipient_info), null);
    }

    @Override
    public void onClick(View v) {
        String phoneNo = recipient.getText().toString().trim();
        String searchTextData = searchText.getText().toString().trim();

        saveDataToSharedPrefs(phoneNo + "~" + searchTextData);
    }

    public void readPermission() {
        try {

            ArrayList<String> arrPerm = new ArrayList<>();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                arrPerm.add(Manifest.permission.READ_SMS);
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                arrPerm.add(Manifest.permission.SEND_SMS);
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
                arrPerm.add(Manifest.permission.RECEIVE_SMS);
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_MMS) != PackageManager.PERMISSION_GRANTED) {
                arrPerm.add(Manifest.permission.RECEIVE_MMS);
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_WAP_PUSH) != PackageManager.PERMISSION_GRANTED) {
                arrPerm.add(Manifest.permission.RECEIVE_WAP_PUSH);
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {
                arrPerm.add(Manifest.permission.FOREGROUND_SERVICE);
            }

            if (!arrPerm.isEmpty()) {
                String[] permissions = new String[arrPerm.size()];
                permissions = arrPerm.toArray(permissions);
                ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        String permission = permissions[i];
                        if (Manifest.permission.READ_SMS.equals(permission)) {
                            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            }
                        }
                        if (Manifest.permission.SEND_SMS.equals(permission)) {
                            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            }
                        }
                        if (Manifest.permission.RECEIVE_SMS.equals(permission)) {
                            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            }
                        }
                        if (Manifest.permission.RECEIVE_MMS.equals(permission)) {
                            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            }
                        }
                        if (Manifest.permission.FOREGROUND_SERVICE.equals(permission)) {
                            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            }
                        }
                    }
                } else {
                }
                break;
            }
        }
    }
}