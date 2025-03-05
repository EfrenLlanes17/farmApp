package com.example.dinehero;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dinehero.databinding.ActivityMainBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.w3c.dom.Text;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventListener;
import java.util.List;
import java.util.Locale;

public class ForYouActivity extends AppCompatActivity {

    private BottomNavigationView BNV;
    private BottomNavigationView TNV;
    private RecyclerView FRV;
    private static ArrayList<Product> forYouList = new ArrayList<>();
    private static  Boolean first = true;

    private Button goToSignIN;
    private TextView createPostTxt;
    private TextView imageOutputText;
    private EditText eventName;
    private TextView eventLoc;
    private EditText eventPrice;
    private EditText eventDisc;
    private Button creatBtn;

    private ImageButton ImageTings;

    private EditText eventAttending;
    private TextView eventDate;

    private Uri uriiiii;


    ActivityMainBinding binding;
    private static final int LOCATION_REQUEST_CODE = 100;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_for);


        eventLoc = findViewById(R.id.edtTextLoc);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Request location on click
        eventLoc.setOnClickListener(v -> getUserLocation());


        TNV = findViewById(R.id.topNavView2);
        //TNV.setSelectedItemId(findViewById(R.id.btnForYou).getId());
        TNV.setItemActiveIndicatorEnabled(false);

        ImageTings = findViewById(R.id.ImageTing);
        creatBtn = findViewById(R.id.createPostBtn);
        goToSignIN = findViewById(R.id.foryouSignIn);
        createPostTxt = findViewById(R.id.createPostText);
        eventName = findViewById(R.id.edtTextTitle);
        eventLoc = findViewById(R.id.edtTextLoc);
        eventDate = findViewById(R.id.edtTextDate);
        imageOutputText = findViewById(R.id.imageOutputText);



//        if(!(ProfileActivity.getCurrentUserUsername().equals(""))){
//            goToSignIN.setVisibility(View.INVISIBLE);
//            createPostTxt.setVisibility(View.VISIBLE);
//            ImageTings.setVisibility(View.VISIBLE);
//            eventDisc.setVisibility(View.VISIBLE);
//            creatBtn.setVisibility(View.VISIBLE);
//            eventPrice.setVisibility(View.VISIBLE);
//            eventLoc.setVisibility(View.VISIBLE);
//            eventName.setVisibility(View.VISIBLE);
//            eventAttending.setVisibility(View.VISIBLE);
//            eventDate.setVisibility(View.VISIBLE);
//        }

        ImageTings.setVisibility(View.VISIBLE);
        eventDate.setVisibility(View.VISIBLE);
        creatBtn.setVisibility(View.VISIBLE);
        goToSignIN.setVisibility(View.INVISIBLE);
        createPostTxt.setVisibility(View.VISIBLE);
        eventLoc.setVisibility(View.VISIBLE);
        eventName.setVisibility(View.VISIBLE);



        goToSignIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfileActivity();
            }
        });


        eventDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    ForYouActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String selectedDate = (selectedMonth + 1)  + "/" + selectedDay + "/" + selectedYear;
                        eventDate.setText(selectedDate);
                    }, year, month, day);

            datePickerDialog.show();
        });


        creatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//
//                ProfileActivity.addToFollowing(new Product(eventName.getText().toString(),eventDisc.getText().toString(),eventLoc.getText().toString(),null,uriiiii,false,eventDate.getText().toString(),1,Integer.parseInt(eventAttending.getText().toString()), Integer.parseInt(eventPrice.getText().toString())));
//                MainActivity2.addToProdList(new Product(eventName.getText().toString(),eventDisc.getText().toString(),eventLoc.getText().toString(),null,uriiiii,false,eventDate.getText().toString(),1,Integer.parseInt(eventAttending.getText().toString()), Integer.parseInt(eventPrice.getText().toString())));
//
//                eventName.setText("");
//                eventDisc.setText("");
//                eventLoc.setText("");
//                eventAttending.setText("");
//                eventDate.setText("");
//                ImageTings.setImageDrawable(ContextCompat.getDrawable(ForYouActivity.this,R.drawable.baseline_image_search_24222222222));
//                //ImageTings.setBackground(ContextCompat.getDrawable(ForYouActivity.this,R.drawable.baseline_image_search_24));
                imageOutputText.setText("Test Test");
                Toast.makeText(ForYouActivity.this, "Event Created Successfully", Toast.LENGTH_SHORT).show();
            }
        });

        ImageTings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(ForYouActivity.this)
                                .crop()
                                .compress(1024)
                                .maxResultSize(1580,1080)
                                .start();

            }
        });

        TNV.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == findViewById(R.id.btnDiscover).getId()) {
                    openCalenderActivity();

                } else if (item.getItemId() == findViewById(R.id.btnForYou).getId()) {
                    openForYouActivity();
                } else if (item.getItemId() == findViewById(R.id.btnFollowing).getId()) {
                    openProfileActivity();
                }
                else if (item.getItemId() == findViewById(R.id.btnSearch).getId()) {
                    openMainActivity();
                }

                return false;
            }
        });


    }

    private void getUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        getCityStateCountry(location);
                    } else {
                        Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getCityStateCountry(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String city = address.getLocality();   // City
                String state = address.getAdminArea(); // State
                String country = address.getCountryName(); // Country

                eventLoc.setText(city + ", " + state + ", " + country);
            } else {
                eventLoc.setText("Location not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
            eventLoc.setText("Geocoder failed");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getUserLocation();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, @NonNull Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        Uri uri = data.getData();

        uriiiii = uri;
        ImageTings.setImageURI(uri);
    }
    public void openMainActivity(){

        Intent intent = new Intent(this, MainActivity2.class);
        this.startActivity(intent);
    }
    public void openInboxActivity(){

        Intent intent = new Intent(this, ForYouActivity.class);
        this.startActivity(intent);
    }
    public void openProfileActivity(){

        Intent intent = new Intent(this, ProfileActivity.class);
        this.startActivity(intent);
    }
    public void openForYouActivity(){

        Intent intent = new Intent(this, ForYouActivity.class);
        this.startActivity(intent);
    }

    public void openFollowingActivity(){
        Intent intent = new Intent(this, FollowingActivity.class);
        this.startActivity(intent);
    }

    public void openCalenderActivity(){

        Intent intent = new Intent(this, CalenderActivity.class);
        this.startActivity(intent);
    }
    public void openSearchActivity(){

        Intent intent = new Intent(this, SearchActivity.class);
        this.startActivity(intent);
    }
    public static void setFirst(boolean boo){

        first = boo;
    }
    public static boolean getFirst(){
        return first;
    }
    public static ArrayList<Product> getForYouList(){
        return forYouList;
    }

    public static void setForYouList(ArrayList<Product> array){

        forYouList = array;
    }
}