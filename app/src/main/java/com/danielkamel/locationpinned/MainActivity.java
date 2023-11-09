package com.danielkamel.locationpinned;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    RecyclerView recycleView;
    SearchView searchView;



    FloatingActionButton newAddressButton;
    LocationDatabaseHelper databaseHelper;
    ArrayList<String> ids, addresses, latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recycleView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.search_address);

        newAddressButton =  findViewById(R.id.AddNewAddress);

        newAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewAddress.class ));
            }
        });






        databaseHelper = new LocationDatabaseHelper(MainActivity.this);
        ids = new ArrayList<String>();
        addresses = new ArrayList<String>();
        latitude = new ArrayList<String>();
        longitude = new ArrayList<String>();

        getAddresses();

        CustomAdapter customAdapter = new CustomAdapter(MainActivity.this , this, ids, addresses, latitude, longitude);
        recycleView.setAdapter(customAdapter);
        recycleView.setLayoutManager(new LinearLayoutManager( MainActivity.this));


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                customAdapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                customAdapter.filter(newText);
                return true;
            }
        });

    }

    void getAddresses(){
        Cursor pointer = databaseHelper.readAllData();
        if (pointer.getCount()  == 0 ){

            Toast.makeText(this,"No Notes Found",Toast.LENGTH_SHORT).show();
        }else{
            while(pointer.moveToNext()){

                ids.add(pointer.getString(0));
                addresses.add(pointer.getString(1));
                latitude.add(pointer.getString(2));
                longitude.add(pointer.getString(3));

            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1){
            recreate();
        }
    }
}