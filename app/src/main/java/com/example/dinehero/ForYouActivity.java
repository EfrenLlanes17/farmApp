package com.example.dinehero;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

    private int month2;

    public static boolean alreadyPlants = false;

    private Uri uriiiii;

    public static ArrayList<Plant> plantList = new ArrayList<>();



    ActivityMainBinding binding;
    private static final int LOCATION_REQUEST_CODE = 100;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_for);
//        if (!alreadyPlants) {
//            plantList.add(new Plant("Tomato", "red", "green", "yellow", "brown", "bees", 12, "bushy", "flowers attract bees"));
//            plantList.add(new Plant("Sunflower", "yellow", "yellow", "yellow", "brown", "bees, butterflies", 24, "tall stalk", "large, bright flowers attract pollinators"));
//            plantList.add(new Plant("Blueberry", "green", "green", "white", "red", "bees, birds", 36, "shrub", "small flowers attract bees"));
//            alreadyPlants = true;
//        }
        Toast.makeText(ForYouActivity.this, "" + plantList.size(), Toast.LENGTH_SHORT).show();

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
        imageOutputText.setVisibility(View.INVISIBLE);



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
            this.month2 = month;
            datePickerDialog.show();
        });


        creatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uriiiii == null) {
                    Toast.makeText(ForYouActivity.this, "Please select an image first", Toast.LENGTH_SHORT).show();
                    return;
                }

                String plantName = eventName.getText().toString().trim();
                String location = eventLoc.getText().toString().trim();
                String date = eventDate.getText().toString().trim();

                if (plantName.isEmpty() || location.equals("Location") || date.isEmpty()) {
                    Toast.makeText(ForYouActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                int dominantColor = analyzeImageColor(uriiiii);
                String colorName = getColorName(dominantColor);
                String suggestion = getSuggestionBasedOnColor(colorName);

                // Get additional plant information
                String plantInfo = getPlantInfo(plantName);

                // Expected color in the current season
                String expectedColor = expectedColorForSeason(date);

                String outputText = String.format(
                        "Based on the %s color of your %s in %s, you should %s more. " +
                                "The plant should look more %s this time of year.\n\n%s",
                        colorName, plantName, location, suggestion, expectedColor, plantInfo
                );

                imageOutputText.setText(outputText);
                ImageTings.setVisibility(View.INVISIBLE);
                eventDate.setVisibility(View.INVISIBLE);
                creatBtn.setVisibility(View.INVISIBLE);
                goToSignIN.setVisibility(View.INVISIBLE);
                createPostTxt.setVisibility(View.INVISIBLE);
                eventLoc.setVisibility(View.INVISIBLE);
                eventName.setVisibility(View.INVISIBLE);
                imageOutputText.setVisibility(View.VISIBLE);

                Toast.makeText(ForYouActivity.this, "Analysis Complete", Toast.LENGTH_SHORT).show();
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
                    openAIChatActivity();//openProfileActivity();
                }
                else if (item.getItemId() == findViewById(R.id.btnSearch).getId()) {
                    openMainActivity();
                }

                return false;
            }
        });


    }




    private int analyzeImageColor(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int pixel = bitmap.getPixel(width / 2, height / 2); // Sample the center pixel
            return pixel;
        } catch (IOException e) {
            e.printStackTrace();
            return Color.BLACK; // Default to black in case of error
        }
    }


    private String getColorName(int color) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        if (green > red && green > blue) return "green";
        if (red > green && red > blue) return "red";
        if (blue > red && blue > green) return "blue";
        if (red > 100 && green > 100 && blue < 100) return "yellow";
        if (red > 139 && green < 69 && blue < 19) return "brown";

        return "unknown";
    }



    private String getSuggestionBasedOnColor(String color) {
        switch (color) {
            case "green":
                return "continue regular care";
            case "yellow":
                return "water";
            case "brown":
                return "prune and check for pests";
            case "red":
                return "monitor for diseases";
            default:
                return "observe and take action as needed";
        }
    }


    private String expectedColorForSeason(String date) {
        if (date.contains("12/") || date.contains("1/") || date.contains("2/")) {
            return "darker green";
        } else if (date.contains("3/") || date.contains("4/") || date.contains("5/")) {
            return "bright green";
        } else if (date.contains("6/") || date.contains("7/") || date.contains("8/")) {
            return "lush green";
        } else {
            return "yellowish-green";
        }
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


    private String getPlantInfo(String plantName) {
        for (Plant plant : plantList) {
            if (plant.getName().equalsIgnoreCase(plantName)) {
                return "The " + plant.getName() + " typically has " + plant.getColorBySeason(getCurrentSeason()) +
                        " color in this season. It is best planted " + plant.getPlantSpacing() + " inches apart. " +
                        "Common pollinators include " + plant.getPollinators() + ". It grows in a " + plant.getGrowthPattern() +
                        " pattern, and " + plant.getPollinatorAttraction() + ".";
            }
        }
        return "Information on " + plantName + " is limited. Ensure proper spacing and consider local pollinators.";
    }


    private String getCurrentSeason() {
        if (month2 >= 3 && month2 <= 5) return "spring";
        if (month2 >= 6 && month2 <= 8) return "summer";
        if (month2 >= 9 && month2 <= 11) return "fall";
        return "winter";
    }
    private Plant getPlantByName(String name) {
        for (Plant plant : plantList) {
            if (plant.getName().equalsIgnoreCase(name)) {
                return plant;
            }
        }
        return null;
    }


    public static class Plant {
        private String name;
        private String winterColor;
        private String summerColor;
        private String springColor;
        private String fallColor;
        private String pollinators;
        private int plantSpacing;
        private String growthPattern;
        private String pollinatorAttraction;

        public Plant(String name, String winterColor, String summerColor, String springColor, String fallColor,
                     String pollinators, int plantSpacing, String growthPattern, String pollinatorAttraction) {
            this.name = name;
            this.winterColor = winterColor;
            this.summerColor = summerColor;
            this.springColor = springColor;
            this.fallColor = fallColor;
            this.pollinators = pollinators;
            this.plantSpacing = plantSpacing;
            this.growthPattern = growthPattern;
            this.pollinatorAttraction = pollinatorAttraction;
        }

        public String getName() {
            return name;
        }

        public String getColorBySeason(String season) {
            switch (season.toLowerCase()) {
                case "winter": return winterColor;
                case "summer": return summerColor;
                case "spring": return springColor;
                case "fall": return fallColor;
                default: return "unknown";
            }
        }

        public String getPollinators() {
            return pollinators;
        }

        public int getPlantSpacing() {
            return plantSpacing;
        }

        public String getGrowthPattern() {
            return growthPattern;
        }

        public String getPollinatorAttraction() {
            return pollinatorAttraction;
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

    public void openAIChatActivity(){

        Intent intent = new Intent(this, AiChatActivity.class);
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