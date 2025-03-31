package com.example.dinehero;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dinehero.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AiChatActivity extends AppCompatActivity {

    private BottomNavigationView BNV;
    private BottomNavigationView TNV;
    private RecyclerView FollowRV;
    private static ArrayList<Product> followingProductList = new ArrayList();

    ActivityMainBinding binding;

    private Button goToSignINfollowing;

    private MessageAdapter messageAdapter;
    private List<Message> messages = new ArrayList<>();

    private Handler handler = new Handler();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aichat);





        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        EditText messageInput = findViewById(R.id.messageInput);
        Button sendButton = findViewById(R.id.sendButton);

        messageAdapter = new MessageAdapter(messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        simulateTypingEffect("Welcome back "+ ProfileActivity.userName + "! Want to check today’s tasks, learn about crops, or get planting recommendations?",recyclerView);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageInput.getText().toString().trim();
                if (!message.isEmpty()) {
                    messages.add(new Message(message, true)); // User message
                    messageAdapter.notifyItemInserted(messages.size() - 1);
                    recyclerView.scrollToPosition(messages.size() - 1);
                    messageInput.setText("");

                    String responseMessage = null;

                    if (message.toLowerCase().contains("what should i plant")) {
                        responseMessage = "Please enter your soil type (e.g., sandy, clay, loamy) and season (spring, summer, fall, winter).";
                    } else if (message.toLowerCase().matches(".*(sandy|clay|loamy).*(spring|summer|fall|winter).*")) {
                        responseMessage = getCropRecommendation(message);
                    }
                    else {
                        for (ForYouActivity.Plant plant : ForYouActivity.plantList) {
                            if (message.toLowerCase().contains(plant.getName().toLowerCase())) {
                                responseMessage = "Here’s some information about " + plant.getName() + ":\n";
                                responseMessage += "- Color in Spring: " + plant.getColorBySeason("spring") + "\n";
                                responseMessage += "- Color in Summer: " + plant.getColorBySeason("summer") + "\n";
                                responseMessage += "- Color in Winter: " + plant.getColorBySeason("winter") + "\n";
                                responseMessage += "- Color in Fall: " + plant.getColorBySeason("fall") + "\n";
                                responseMessage += "- Pollinators: " + plant.getPollinators() + "\n";
                                responseMessage += "- Plant Spacing: " + plant.getPlantSpacing() + "\n";
                                responseMessage += "- Growth Pattern: " + plant.getGrowthPattern() + "\n";
                                responseMessage += "- Pollinator Attraction: " + plant.getPollinatorAttraction() + "\n";
                                break;
                            }
                        }
                    }

                    if (responseMessage == null) {
                        if (message.toLowerCase().contains("today")) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

                            String extractedDate = dateFormat.format(Calendar.getInstance().getTime());

                            SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                            SimpleDateFormat newFormat = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());
                            Date date = null;
                            try {
                                date = oldFormat.parse(extractedDate);
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                            String formattedDate = newFormat.format(date);

                            List<Event> eventsOnDate = getEventsForDate(extractedDate);
                            if (!eventsOnDate.isEmpty()) {
                                responseMessage = formattedDate + " contains these events:\n";
                                for (Event event : eventsOnDate) {
                                    responseMessage += "- " + event.getTitle() + "\n";
                                }
                            } else {
                                responseMessage = "No events found on " + formattedDate;
                            }
                        }
                    }

                    if (responseMessage == null) {
                        if (message.toLowerCase().contains("tomorrow")) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                            Calendar calendar = Calendar.getInstance();
                            calendar.add(Calendar.DAY_OF_YEAR, 1);
                            String extractedDate = dateFormat.format(calendar.getTime());
                            List<Event> eventsOnDate = getEventsForDate(extractedDate);


                            SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                            SimpleDateFormat newFormat = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());
                            Date date = null;
                            try {
                                date = oldFormat.parse(extractedDate);
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                            String formattedDate = newFormat.format(date);

                            if (!eventsOnDate.isEmpty()) {
                                responseMessage = formattedDate + " contains these events:\n";
                                for (Event event : eventsOnDate) {
                                    responseMessage += "- " + event.getTitle() + "\n";
                                }
                            } else {
                                responseMessage = "No events found on " + formattedDate;
                            }
                        }
                    }


                    if (responseMessage == null) {
                        String extractedDate = extractDate(message);
                        //Toast.makeText(AiChatActivity.this, extractedDate, Toast.LENGTH_SHORT).show();

                        if (extractedDate != null) {

                            SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                            SimpleDateFormat newFormat = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());
                            Date date = null;
                            try {
                                date = oldFormat.parse(extractedDate);
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                            String formattedDate = newFormat.format(date);

                            List<Event> eventsOnDate = getEventsForDate(extractedDate);
                            if (!eventsOnDate.isEmpty()) {
                                responseMessage = formattedDate + " contains these events:\n";
                                for (Event event : eventsOnDate) {
                                    responseMessage += "- " + event.getTitle() + "\n";
                                }
                            } else {
                                responseMessage = "No events found on " + formattedDate;
                            }
                        } else {
                            responseMessage = "Im sorry, I don't understand";
                        }
                    }


                    String finalMessage = responseMessage;
                    handler.postDelayed(() -> simulateTypingEffect(finalMessage, recyclerView), 500);
                }
            }
        });






        TNV = findViewById(R.id.topNavView2);
        //TNV.setSelectedItemId(findViewById(R.id.btnFollowing).getId());
        TNV.setItemActiveIndicatorEnabled(false);

        TNV.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == findViewById(R.id.btnDiscover).getId()) {
                    openCalendarActivity();

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

    public class Message {
        private String text;
        private boolean isUser;

        public Message(String text, boolean isUser) {
            this.text = text;
            this.isUser = isUser;
        }

        public String getText() {
            return text;
        }

        public void setText(String t) {
            text =  t;
        }

        public boolean isUser() {
            return isUser;
        }
    }

    private String getCropRecommendation(String message) {
        if (message.contains("sandy")) {
            return "For sandy soil, consider planting carrots, peanuts, or watermelon during spring and summer.";
        } else if (message.contains("clay")) {
            return "For clay soil, try corn, cabbage, or beans. These grow well in spring and summer.";
        } else if (message.contains("loamy")) {
            return "Loamy soil is ideal for most crops. You can plant tomatoes, potatoes, or lettuce based on the season.";
        }
        return "Please specify both soil type and season for the best recommendation.";
    }



    private String extractDate(String message) {
        Pattern datePattern = Pattern.compile(
                "(\\d{1,2}/\\d{1,2}/\\d{2,4})|" +
                        "(\\b(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)[a-z]*\\s*\\d{1,2},?\\s*(?:\\d{2,4})?)",
                Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = datePattern.matcher(message);

        if (matcher.find()) {
            return formatDate(matcher.group());
        }
        return null;
    }

    private List<Event> getEventsForDate(String date) {
        List<Event> eventsOnDate = new ArrayList<>();
        for (Event event : CalenderActivity.eventList) {
            if (event.getDate().equals(date)) {
                eventsOnDate.add(event);
            }
        }
        return eventsOnDate;
    }



    private String formatDate(String dateStr) {
        SimpleDateFormat inputFormat1 = new SimpleDateFormat("M/d", Locale.getDefault());
        SimpleDateFormat inputFormat2 = new SimpleDateFormat("M/d/yy", Locale.getDefault());
        SimpleDateFormat inputFormat3 = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
        SimpleDateFormat inputFormat4 = new SimpleDateFormat("MMMM d", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

        try {
            return outputFormat.format(inputFormat2.parse(dateStr));
        } catch (Exception ignored) {}

        try {
            return outputFormat.format(inputFormat3.parse(dateStr));
        } catch (Exception ignored) {}

        try {
            Calendar cal = Calendar.getInstance();
            Date parsedDate = inputFormat4.parse(dateStr);
            cal.setTime(parsedDate);
            cal.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
            return outputFormat.format(cal.getTime());
        } catch (Exception ignored) {}

        try {

            Calendar cal = Calendar.getInstance();
            Date parsedDate = inputFormat1.parse(dateStr);
            cal.setTime(parsedDate);
            cal.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
            return outputFormat.format(cal.getTime());
        } catch (Exception ignored) {}

        return null;
    }



    private void simulateTypingEffect(String fullMessage, RecyclerView recyclerView) {
        Message typingMessage = new Message("", false);
        messages.add(typingMessage);
        messageAdapter.notifyItemInserted(messages.size() - 1);
        recyclerView.scrollToPosition(messages.size() - 1);

        int messageLength = fullMessage.length();
        int typingDelay = Math.max(30, 200 / messageLength);

        new Thread(() -> {
            StringBuilder currentText = new StringBuilder();
            for (char c : fullMessage.toCharArray()) {
                currentText.append(c);
                typingMessage.setText(currentText.toString());

                runOnUiThread(() -> {
                    messageAdapter.notifyItemChanged(messages.size() - 1);
                    recyclerView.scrollToPosition(messages.size() - 1);
                });

                try {
                    Thread.sleep(typingDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void openMainActivity(){

        Intent intent = new Intent(this, MainActivity2.class);
        this.startActivity(intent);
    }
    public void openInboxActivity(){

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
    public void openAIChatActivity(){

        Intent intent = new Intent(this, AiChatActivity.class);
        this.startActivity(intent);
    }
    public void openFollowingActivity(){

        Intent intent = new Intent(this, AiChatActivity.class);
        this.startActivity(intent);
    }
    public void openSearchActivity(){

        Intent intent = new Intent(this, SearchActivity.class);
        this.startActivity(intent);
    }

    public void openCalendarActivity(){

        Intent intent = new Intent(this, CalenderActivity.class);
        this.startActivity(intent);
    }
    public static ArrayList<Product> getFollowingProductList(){
        return followingProductList;
    }
    public static void setFollowingProductList(ArrayList<Product> array){
        followingProductList = array;
    }
}