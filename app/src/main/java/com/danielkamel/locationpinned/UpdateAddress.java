package com.danielkamel.locationpinned;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateAddress extends AppCompatActivity {

    EditText addressEditField;
    Button UpdateButton;

    String id, addresss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_address);
        addressEditField = findViewById(R.id.UpdateAddressTextField);
        UpdateButton = findViewById(R.id.UpdateButton);

getIntentData();
        UpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationDatabaseHelper db = new LocationDatabaseHelper(v.getContext());



                String cords = "";
                GeoCodingHelper geo = new GeoCodingHelper();
                cords = geo.getCodes(v.getContext(), addressEditField.getText().toString().trim());

                if (cords == null || cords.isEmpty()) {
                    return;
                }
                String[] str = cords.split(",");
//                db.addAddress(addressEditField.getText().toString().trim(), str[0], str[1]);
                db.update(v.getContext(),id,addressEditField.getText().toString().trim(),str[0], str[1]);
                finish();
            }
        });
    }

    void getIntentData() {
        if (getIntent().hasExtra("ID") && getIntent().hasExtra("Address")) {

            id = getIntent().getStringExtra("ID");
            addresss = getIntent().getStringExtra("Address");

        }
         else {
        Toast.makeText(this, "Error: can't find address to edit", Toast.LENGTH_LONG).show();
    }
    }
}