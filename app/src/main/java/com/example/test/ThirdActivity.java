package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class ThirdActivity extends AppCompatActivity {

    private EditText url, phoneNumber;
    private ImageButton btnWeb, btnPhone, btnCamera;
    private final int phoneCallCode = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        url = findViewById(R.id.txtUrl);
        phoneNumber = findViewById(R.id.txtPhoneNumber);
        btnWeb = findViewById(R.id.imgBtnWeb);
        btnCamera = findViewById(R.id.imgBtnCamera);
        btnPhone = findViewById(R.id.imgBtnCall);

        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = phoneNumber.getText().toString();
                if (number != null && !number.isEmpty()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, phoneCallCode);
                    } else {
                        OlderVersions(number);
                    }
                } else {
                    Toast.makeText(ThirdActivity.this, "Enter a valid phone number", Toast.LENGTH_LONG).show();
                }

            }

            //@SuppressLint("MissingPermission")
            private void OlderVersions(String number) {
                Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));

                if (CheckPermission(Manifest.permission.CALL_PHONE)) {
                    if (ActivityCompat.checkSelfPermission(ThirdActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(intentCall);
                } else {
                    Toast.makeText(ThirdActivity.this, "User decliend the access", Toast.LENGTH_LONG).show();
                }
            }

        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Caso del telefono
        switch (requestCode) {
            case phoneCallCode:
                String permission = permissions[0];
                int result = grantResults[0];

                if (permission.equals(Manifest.permission.CALL_PHONE)) {
                    //comprobar si a sido aceptado o denegado el permiso
                    if (result == PackageManager.PERMISSION_GRANTED) {
                        //Concedio permiso
                        String phoneNum = phoneNumber.getText().toString();
                        Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {return; }
                        startActivity(intentCall);
                    } else {
                        //no concedio permiso
                        Toast.makeText(ThirdActivity.this, "User decliend the access", Toast.LENGTH_LONG).show();
                    }
                }

                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private boolean CheckPermission(String permission) {
        int result = this.checkCallingOrSelfPermission(permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }
}
