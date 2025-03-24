package com.example.dinehero;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dinehero.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

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


                    if (responseMessage == null) {
                        String extractedDate = extractDate(message);
                        if (extractedDate != null) {
                            List<Event> eventsOnDate = getEventsForDate(extractedDate);
                            if (!eventsOnDate.isEmpty()) {
                                responseMessage = extractedDate + " contains these events:\n";
                                for (Event event : eventsOnDate) {
                                    responseMessage += "- " + event.getTitle() + "\n";
                                }
                            } else {
                                responseMessage = "No events found on " + extractedDate;
                            }
                        } else {
                            responseMessage = "I couldn't find relevant information in your message.";
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