package com.danielkamel.locationpinned;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewAddress extends AppCompatActivity {

    Button saveButton;
    EditText addressEditField;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address2);

        context = this;

        saveButton = findViewById(R.id.UpdateButton);
        addressEditField = findViewById(R.id.AddressTextField);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (addressEditField.getText().toString().trim().isEmpty()) {
                    Toast.makeText(context, "Please enter an address", Toast.LENGTH_SHORT).show();
                    return;
                }

                LocationDatabaseHelper db = new LocationDatabaseHelper(NewAddress.this);

                String cords = "";
                GeoCodingHelper geo = new GeoCodingHelper();
                cords = geo.getCodes(context, addressEditField.getText().toString().trim());

                if (cords == null || cords.isEmpty()) {
                    return;
                }
                String[] str = cords.split(",");
                db.addAddress(addressEditField.getText().toString().trim(), str[0], str[1]);
                finish();
            }
        });

    }
}