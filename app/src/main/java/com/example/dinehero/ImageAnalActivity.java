package com.example.dinehero;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dinehero.databinding.ActivityMainBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import android.Manifest;
import android.location.Location;

import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ImageAnalActivity extends AppCompatActivity {

    //Initialize Variables

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

    private ProgressBar pbCircle;
    private ProgressBar pbLine;

    private ImageView beeImage;
    private ImageView OGImage;
    private ImageView HighImage;
    private ImageView InferImage;

    private TextView oglabel;
    private TextView beelabel;
    private TextView inferlebel;
    private TextView highlabel;



    ActivityMainBinding binding;
    private static final int LOCATION_REQUEST_CODE = 100;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_analysis);
        //Attach XML page to this java file



//        if (!alreadyPlants) {
//            plantList.add(new Plant("Tomato", "red", "green", "yellow", "brown", "bees", 12, "bushy", "flowers attract bees"));
//            plantList.add(new Plant("Sunflower", "yellow", "yellow", "yellow", "brown", "bees, butterflies", 24, "tall stalk", "large, bright flowers attract pollinators"));
//            plantList.add(new Plant("Blueberry", "green", "green", "white", "red", "bees, birds", 36, "shrub", "small flowers attract bees"));
//            alreadyPlants = true;
//        }

        eventLoc = findViewById(R.id.edtTextLoc);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getUserLocation();

//        eventLoc.setOnClickListener(v -> getUserLocation());


        //Attaching Variables to xml elements



        oglabel = findViewById(R.id.OGLabel);
        beelabel = findViewById(R.id.BeeLabel);
        inferlebel = findViewById(R.id.InferLabel);
        highlabel = findViewById(R.id.HighLabel);


        TNV = findViewById(R.id.topNavView2);
        //TNV.setSelectedItemId(findViewById(R.id.btnForYou).getId());
        TNV.setItemActiveIndicatorEnabled(false);

        pbCircle = findViewById(R.id.progressBar);
        pbLine = findViewById(R.id.progressBar2);

        ImageTings = findViewById(R.id.ImageTing);
        creatBtn = findViewById(R.id.createPostBtn);
        goToSignIN = findViewById(R.id.foryouSignIn);
        createPostTxt = findViewById(R.id.createPostText);
        eventName = findViewById(R.id.edtTextTitle);
        eventLoc = findViewById(R.id.edtTextLoc);
        eventDate = findViewById(R.id.edtTextDate);
        imageOutputText = findViewById(R.id.imageOutputText);
        beeImage = findViewById(R.id.beeImage);
        OGImage = findViewById(R.id.OGImage);
        HighImage = findViewById(R.id.HighImage);
        InferImage = findViewById(R.id.InferImage);





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

        //Setting Elements to be visible or invisible when page gets loaded


        ImageTings.setVisibility(View.VISIBLE);
        eventDate.setVisibility(View.VISIBLE);
        creatBtn.setVisibility(View.VISIBLE);
        goToSignIN.setVisibility(View.INVISIBLE);
        createPostTxt.setVisibility(View.VISIBLE);
        eventLoc.setVisibility(View.VISIBLE);
        eventName.setVisibility(View.VISIBLE);
        imageOutputText.setVisibility(View.INVISIBLE);
        pbLine.setVisibility(View.INVISIBLE);
        pbCircle.setVisibility(View.INVISIBLE);
        beeImage.setVisibility(View.INVISIBLE);
        OGImage.setVisibility(View.INVISIBLE);
        HighImage.setVisibility(View.INVISIBLE);
        InferImage.setVisibility(View.INVISIBLE);



        goToSignIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfileActivity();
            }
        });

        //Button that allows the user to press on the event date textview and open the date picker


        eventDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    ImageAnalActivity.this, android.R.style.Theme_Material_Dialog,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String selectedDate = (selectedMonth + 1)  + "/" + selectedDay + "/" + selectedYear;
                        eventDate.setText(selectedDate);
                    }, year, month, day);
            this.month2 = month;
            datePickerDialog.show();
        });

        //Analyse Button checks to make sure the input fields are not empty and then begins to process Image


        creatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uriiiii == null) {
                    Toast.makeText(ImageAnalActivity.this, "Please select an image first", Toast.LENGTH_SHORT).show();
                    return;
                }

                String plantName = eventName.getText().toString().trim();
                String location = eventLoc.getText().toString().trim();
                String date = eventDate.getText().toString().trim();

                if (plantName.isEmpty() || date.isEmpty()) {
                    Toast.makeText(ImageAnalActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                int dominantColor = analyzeImageColor(uriiiii);
                String colorName = getColorName(dominantColor);
                String suggestion = getSuggestionBasedOnColor(colorName);
                String plantInfo = getPlantInfo(plantName);
                String expectedColor = expectedColorForSeason(date);

                String outputText = String.format(
                        "Based on the %s color of your %s in %s, you should %s. " +
                                "\n\n%s",
                        colorName, plantName, location, suggestion, plantInfo
                );

                // Hide input fields, show progress elements
                ImageTings.setVisibility(View.INVISIBLE);
                eventDate.setVisibility(View.INVISIBLE);
                creatBtn.setVisibility(View.INVISIBLE);
                goToSignIN.setVisibility(View.INVISIBLE);
                createPostTxt.setVisibility(View.INVISIBLE);
                eventLoc.setVisibility(View.INVISIBLE);
                eventName.setVisibility(View.INVISIBLE);
                imageOutputText.setVisibility(View.VISIBLE);
                pbCircle.setVisibility(View.VISIBLE);
                pbLine.setVisibility(View.VISIBLE);
                pbLine.setProgress(0);
                imageOutputText.setText("Scanning image...");
                Handler handler = new Handler();
                int delay = 1000;

                handler.postDelayed(() -> {
                    imageOutputText.setText("Extracting dominant color data...");
                    pbLine.setProgress(7);
                }, delay);

                delay += 1000;

                handler.postDelayed(() -> {
                    imageOutputText.setText("Comparing color with plant health database...");
                    pbLine.setProgress(32);
                }, delay);

                delay += 1000;

                handler.postDelayed(() -> {
                    imageOutputText.setText("Checking for stress, deficiency, or optimal growth...");
                    pbLine.setProgress(47);
                }, delay);

                delay += 1000;

                handler.postDelayed(() -> {
                    imageOutputText.setText("Matching with expected seasonal variations...");
                    pbLine.setProgress(56);
                }, delay);

                delay += 1000;

                handler.postDelayed(() -> {
                    imageOutputText.setText("Generating recommendations...");
                    pbLine.setProgress(69);
                }, delay);

                delay += 1000;

                handler.postDelayed(() -> {
                    imageOutputText.setText("Finalizing results...");
                    pbLine.setProgress(87);
                }, delay);

                delay += 1000;

                handler.postDelayed(() -> {
                    pbLine.setProgress(100);
                }, delay);

                delay += 500;

                handler.postDelayed(() -> {

                    // showing results after image is processed

                    pbCircle.setVisibility(View.INVISIBLE);
                    pbLine.setVisibility(View.INVISIBLE);
                    beeImage.setVisibility(View.VISIBLE);
                    OGImage.setVisibility(View.VISIBLE);
                    HighImage.setVisibility(View.VISIBLE);
                    InferImage.setVisibility(View.VISIBLE);
                    oglabel.setVisibility(View.VISIBLE);
                    beelabel.setVisibility(View.VISIBLE);
                    inferlebel.setVisibility(View.VISIBLE);
                    highlabel.setVisibility(View.VISIBLE);
                    beeImage.setImageURI(uriiiii);
                    OGImage.setImageURI(uriiiii);
                    HighImage.setImageURI(uriiiii);
                    InferImage.setImageURI(uriiiii);
                    applyInfraredFilter(InferImage);
                    applyBeeVisionFilter(beeImage);
                    applyHighContrastFilter(HighImage);
                }, delay);



                handler.postDelayed(() -> simulateTypingEffect(outputText), delay);
            }
        });




//Button that allows user to upload a picture when pressed

        ImageTings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(ImageAnalActivity.this)
                                .crop()
                                .compress(1024)
                                .maxResultSize(1580,1080)
                                .start();

            }
        });


        //Navigation Bar

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

    //Functions for Color filters to simulate Pollinator vision as well as other views

    private void applyPollinatorFilter(ImageView imageView) {
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                0.4f, 0.2f, 0.7f, 0, 0,
                0.3f, 0.5f, 0.6f, 0, 0,
                0.2f, 0.3f, 1.5f, 0, 0,
                0, 0, 0, 1, 0
        });

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
        imageView.setColorFilter(filter);
    }

    private void applyInfraredFilter(ImageView imageView) {
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                1.5f, 0.0f, 0.0f, 0, 0,
                0.0f, 1.0f, 0.0f, 0, 0,
                0.0f, 0.0f, 0.5f, 0, 0,
                0, 0, 0, 1, 0
        });

        imageView.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
    }


    private void applyBeeVisionFilter(ImageView imageView) {
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                0.3f, 0.3f, 0.0f, 0, 0,
                0.3f, 0.6f, 0.3f, 0, 0,
                0.2f, 0.3f, 1.2f, 0, 0,
                0, 0, 0, 1, 0
        });

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
        imageView.setColorFilter(filter);
    }

    private void applyHighContrastFilter(ImageView imageView) {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setScale(1.5f, 1.5f, 1.5f, 1f);  // Boosts color intensity

        imageView.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
    }





    private void simulateTypingEffect(String fullMessage) {
        int messageLength = fullMessage.length();
        int typingDelay = Math.max(30, 200 / messageLength);

        new Thread(() -> {
            StringBuilder currentText = new StringBuilder();
            for (char c : fullMessage.toCharArray()) {
                currentText.append(c);

                // Run UI updates on the main thread
                runOnUiThread(() -> imageOutputText.setText(currentText.toString()));

                try {
                    Thread.sleep(typingDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }





//Use Pixel Colors from around the image to eventually get dominate color


    private int analyzeImageColor(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();


            int[] pixels = new int[5];
            pixels[0] = bitmap.getPixel(width / 2, height / 2);
            pixels[1] = bitmap.getPixel(width / 4, height / 4);
            pixels[2] = bitmap.getPixel(3 * width / 4, height / 4);
            pixels[3] = bitmap.getPixel(width / 4, 3 * height / 4);
            pixels[4] = bitmap.getPixel(3 * width / 4, 3 * height / 4);

            return getDominantColor(pixels);
        } catch (IOException e) {
            e.printStackTrace();
            return Color.BLACK;
        }
    }


    private int getDominantColor(int[] colors) {
        HashMap<Integer, Integer> colorCount = new HashMap<>();

        for (int color : colors) {
            colorCount.put(color, colorCount.getOrDefault(color, 0) + 1);
        }

        int dominantColor = colors[0];
        int maxCount = 0;

        for (Map.Entry<Integer, Integer> entry : colorCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                dominantColor = entry.getKey();
                maxCount = entry.getValue();
            }
        }

        return dominantColor;
    }






//      associate the RGB Value with an understandable color for the user

    private String getColorName(int color) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);


        int high = 200;
        int medium = 140;
        int low = 80;

        // White & Black
        if (red > 220 && green > 220 && blue > 220) return "white";
        if (red < 50 && green < 50 && blue < 50) return "black";


        if (red > high && green < low && blue < low) return "red";
        if (green > high && red < low && blue < low) return "green";
        if (blue > high && red < low && green < low) return "blue";


        if (blue > 70 && blue > red + 20 && blue > green + 20) return "blue";
        if (blue > 70 && green > 50 && red < 80) return "dark blue";


        if (red > 200 && green > 150 && blue < 50) return "yellow";
        if (red > 200 && green > 100 && green <= 150 && blue < 80) return "orange";


        if (red < 100 && green > high && blue > high) return "cyan";
        if (red > medium && green < medium && blue > medium) return "magenta";
        if (red > 100 && green > 50 && blue > 50 && red > green && red > blue) return "brown";
        if (red > 120 && green > 120 && blue > 120 && red < 200 && green < 200 && blue < 200) return "gray";

        return "unknown";
    }







//Suggestions for plants not included in built in plant database

    private String getSuggestionBasedOnColor(String color) {
        switch (color) {
            case "green":
                return "continue regular care";
            case "yellow":
                return "water the plant more";
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

//Function gets user's location and defaults to Norman if unable
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
                        eventLoc.setText("Norman" + ", " + "Oklahoma" + ", " + "United States");
                        Toast.makeText(this, "Unable to get location, Defaulting To Norman, Oklahoma, United States", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //Turn Geo Mapping Location into understandable place for user

    private void getCityStateCountry(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String city = address.getLocality();
                String state = address.getAdminArea();
                String country = address.getCountryName();

                eventLoc.setText(city + ", " + state + ", " + country);
            } else {
                eventLoc.setText("Norman" + ", " + "Oklahoma" + ", " + "United States");
                Toast.makeText(this, "Unable to get location, Defaulting To Norman, Oklahoma, United States", Toast.LENGTH_SHORT).show();

            }
        } catch (IOException e) {
            e.printStackTrace();
            eventLoc.setText("Norman" + ", " + "Oklahoma" + ", " + "United States");
            Toast.makeText(this, "Unable to get location, Defaulting To Norman, Oklahoma, United States", Toast.LENGTH_SHORT).show();

        }
    }
    //Location permission prompt

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

    //Check if plant entered by the user is in built in plant database

    private String getPlantInfo(String plantName) {
        for (Plant plant : plantList) {
            if (plant.getName().equalsIgnoreCase(plantName)) {
                return "The " + plant.getName().toLowerCase() + " typically has " + plant.getColorBySeason(getCurrentSeason()) +
                        " color in this season. It is best planted " + plant.getPlantSpacing() + " inches apart. " +
                        "Common pollinators include " + plant.getPollinators() + ". It grows in a " + plant.getGrowthPattern() +
                        " pattern, and " + plant.getPollinatorAttraction() + ".";
            }
        }
        return "Information on " + plantName + " is limited. Ensure proper spacing and consider local pollinators.";
    }

//associate month with season
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

    //plant class

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

        Intent intent = new Intent(this, ImageAnalActivity.class);
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

        Intent intent = new Intent(this, ImageAnalActivity.class);
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