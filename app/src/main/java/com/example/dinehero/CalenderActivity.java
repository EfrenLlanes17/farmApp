package com.example.dinehero;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.text.ParseException;
import java.util.ArrayList;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.datepicker.MaterialDatePicker;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import android.app.AlertDialog;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class CalenderActivity extends AppCompatActivity {


    private BottomNavigationView TNV;

    public static List<Event> eventList;
    public static EventAdapter adapter;

    private TextView noResults;
    private RecyclerView recyclerView;

    private FloatingActionButton fabAddEvent;

    private FloatingActionButton fabFilterEvent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        if (!ForYouActivity.alreadyPlants) {
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Tomato", "red", "green", "yellow", "brown", "bees", 12, "bushy", "flowers attract bees"));
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Sunflower", "yellow", "yellow", "yellow", "brown", "bees, butterflies", 24, "tall stalk", "large, bright flowers attract pollinators"));
            ForYouActivity.plantList.add(new ForYouActivity.Plant("Blueberry", "green", "green", "white", "red", "bees, birds", 36, "shrub", "small flowers attract bees"));
            ForYouActivity.alreadyPlants = true;
        }




        TNV = findViewById(R.id.topNavView2);
        TNV.setItemActiveIndicatorEnabled(false);

        recyclerView = findViewById(R.id.recyclerViewEvents);
        fabAddEvent = findViewById(R.id.fabAddEvent);
        fabFilterEvent = findViewById(R.id.fabFilterEvent);
        noResults = findViewById(R.id.noResults);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (eventList == null){
            eventList = new ArrayList<>();
            adapter = new EventAdapter(this, eventList);
        }
        else{
            adapter = new EventAdapter(this, eventList);
        }
        adapter = new EventAdapter(this, eventList);
        recyclerView.setAdapter(adapter);

        checkList();

        fabAddEvent.setOnClickListener(v -> openDatePicker());

        scrollToClosestEvent(EventAdapter.eventList);

        fabFilterEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterDialog();
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

    public void openCalenderActivity(){

        Intent intent = new Intent(this, CalenderActivity.class);
        this.startActivity(intent);
    }

    public void openAIChatActivity(){

        Intent intent = new Intent(this, AiChatActivity.class);
        this.startActivity(intent);
    }



    public void openForYouActivity(){

        Intent intent = new Intent(this, ForYouActivity.class);
        this.startActivity(intent);
    }


    private void generateRecurringEvents(Event event) {
        Log.d("EventAdapter", "Generating recurrence for: " + event.getTitle());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        List<Event> newEvents = new ArrayList<>();
//        newEvents.add(event); // Add the original event

        if (event.getRecurrence() != null && !event.getRecurrence().equals("None")) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parseDate(event.getDate()));

            int recurrenceCount;
            switch (event.getRecurrence()) {
                case "Daily":
                    recurrenceCount = 365;
                    break;
                case "Weekly":
                    recurrenceCount = 53;
                    break;
                case "Monthly":
                    recurrenceCount = 13;
                    break;
                case "Yearly":
                    recurrenceCount = 1;
                    break;
                default:
                    recurrenceCount = 0; // No recurrence
            }
            newEvents.add(new Event(sdf.format(calendar.getTime()) , event.getTitle(), event.getType(), event.getRecurrence()));
            for (int i = 1; i <= recurrenceCount; i++) {
                switch (event.getRecurrence()) {
                    case "Daily":
                        calendar.add(Calendar.DAY_OF_YEAR, 1);
                        break;
                    case "Weekly":
                        calendar.add(Calendar.WEEK_OF_YEAR, 1);
                        break;
                    case "Monthly":
                        calendar.add(Calendar.MONTH, 1);
                        break;
                    case "Yearly":
                        calendar.add(Calendar.YEAR, 1);
                        break;
                }

                Event recurringEvent = new Event(sdf.format(calendar.getTime()) , event.getTitle(), event.getType(), event.getRecurrence());
                newEvents.add(recurringEvent);
            }
        }
        else{
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parseDate(event.getDate()));
            newEvents.add(new Event(sdf.format(calendar.getTime()) , event.getTitle(), event.getType(), event.getRecurrence()));
        }

        eventList.addAll(newEvents);
        adapter.notifyDataSetChanged();
        adapter.updateEvents(eventList);
        //Collections.sort(eventList, Comparator.comparing(Event::getDate));
        checkList();
        adapter.notifyDataSetChanged();
        sortEventsByDate(eventList);
        adapter.notifyDataSetChanged();
        checkList();

    }




    private Date parseDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        try {
            return sdf.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
            return new Date();
        }
    }


    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();

        // Create a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                android.R.style.Theme_DeviceDefault_Light_Dialog,
                (view, year, month, dayOfMonth) -> {
                    // This will be triggered when the user selects a date
                    String selectedDate = year + "/" + (month + 1) + "/" + dayOfMonth;
                    showEventDialog(selectedDate); // Pass the selected date to event dialog
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Set the positive (OK) button with a custom listener
        datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", (dialog, which) -> {
            DatePicker datePicker = datePickerDialog.getDatePicker();
            int selectedYear = datePicker.getYear();
            int selectedMonth = datePicker.getMonth();
            int selectedDay = datePicker.getDayOfMonth();
            String confirmedDate = selectedYear + "/" + (selectedMonth + 1) + "/" + selectedDay;
            showEventDialog(confirmedDate); // Show event dialog with confirmed date
        });

        // Set the negative (Cancel) button
        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", (dialog, which) -> {
            dialog.dismiss(); // Simply dismiss the dialog
        });

        // Show the date picker dialog
        datePickerDialog.show();
    }


    private void scrollToClosestEvent(List<Event> x) {
        if (x == null || x.isEmpty()) return;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        Date today = new Date();
        int closestIndex = -1;
        long minDiff = Long.MAX_VALUE;
        int header = 0;
        int notHeader = 0;


        for (int i = 0; i < x.size(); i++) {
            try {
                if (x.get(i).isHeader()) {
                    header++;
                } else {
                    notHeader++;
                }

                Date eventDate = sdf.parse(x.get(i).getDate());
                long diff = Math.abs(eventDate.getTime() - today.getTime());

                if (diff < minDiff) {
                    minDiff = diff;
                    closestIndex = i;
                }

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
            Toast.makeText(this, "Headers: " + header + " Not: " + notHeader + " Scrolling to: " + closestIndex + " Today " + today.toString(), Toast.LENGTH_SHORT).show();

        recyclerView.scrollToPosition(closestIndex);

    }





    private void showEventDialog(String date) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CalenderActivity.this);
        builder.setTitle("Add Event");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_event, null);
        builder.setView(dialogView);

        AutoCompleteTextView eventTitleInput = dialogView.findViewById(R.id.eventTitle);
        Spinner eventTypeSpinner = dialogView.findViewById(R.id.eventType);
        Spinner recurrenceSpinner = dialogView.findViewById(R.id.recurrence);
        Button okButton = dialogView.findViewById(R.id.btnOk);
        Button cancelButton = dialogView.findViewById(R.id.btnCancel);

        // Set up spinners
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(
                this, R.array.event_types, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventTypeSpinner.setAdapter(typeAdapter);

        ArrayAdapter<CharSequence> recurrenceAdapter = ArrayAdapter.createFromResource(
                this, R.array.recurrence_options, android.R.layout.simple_spinner_item);
        recurrenceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recurrenceSpinner.setAdapter(recurrenceAdapter);

        AlertDialog dialog = builder.create();

        okButton.setOnClickListener(v -> {
            String title = eventTitleInput.getText().toString();
            String type = eventTypeSpinner.getSelectedItem().toString();
            String recurrence = recurrenceSpinner.getSelectedItem().toString();

            //eventList.add(new Event(date, title, type, recurrence));

            generateRecurringEvents(new Event(date, title, type, recurrence));

            // Sort events by date
            Collections.sort(eventList, (e1, e2) -> e1.getDate().compareTo(e2.getDate()));

            Toast.makeText(this, "Updated event list size: " + eventList.size(), Toast.LENGTH_SHORT).show();

            adapter.notifyDataSetChanged();
            dialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }


    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CalenderActivity.this);
        builder.setTitle("Filter Dates");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_filter_event, null);
        builder.setView(dialogView);

        AutoCompleteTextView eventTitleInput = dialogView.findViewById(R.id.eventTitle);
        Spinner eventTypeSpinner = dialogView.findViewById(R.id.eventType);
        Spinner recurrenceSpinner = dialogView.findViewById(R.id.recurrence);
        EditText etStartDate = dialogView.findViewById(R.id.etStartDate);
        EditText etEndDate = dialogView.findViewById(R.id.etEndDate);
        Button okButton = dialogView.findViewById(R.id.btnOk);


        // Set up spinners
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(
                this, R.array.event_types, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventTypeSpinner.setAdapter(typeAdapter);




        ArrayAdapter<CharSequence> recurrenceAdapter = ArrayAdapter.createFromResource(
                this, R.array.recurrence_options, android.R.layout.simple_spinner_item);
        recurrenceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recurrenceSpinner.setAdapter(recurrenceAdapter);

        AlertDialog dialog = builder.create();

        okButton.setOnClickListener(v -> {
            String title = eventTitleInput.getText().toString();
            String type = eventTypeSpinner.getSelectedItem().toString();
            String recurrence = recurrenceSpinner.getSelectedItem().toString();


            // Sort events by date
//            Collections.sort(eventList, (e1, e2) -> e1.getDate().compareTo(e2.getDate()));
//
//            Toast.makeText(this, "Updated event list size: " + eventList.size(), Toast.LENGTH_SHORT).show();
//
//            adapter.notifyDataSetChanged();
            dialog.dismiss();
        });



        dialog.show();
    }



    public void sortEventsByDate(List<Event> eventList) {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        Collections.sort(eventList, new Comparator<Event>() {
            @Override
            public int compare(Event e1, Event e2) {
                try {
                    Date d1 = sdf.parse(e1.getDate());
                    Date d2 = sdf.parse(e2.getDate());
                    return d1.compareTo(d2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }




 public void checkList(){
     if(eventList.size() ==0){
         noResults.setVisibility(View.VISIBLE);
     }
     else{
         noResults.setVisibility(View.INVISIBLE);

     }
 }


    public void openMainActivity(){

        Intent intent = new Intent(this, MainActivity2.class);
        this.startActivity(intent);
    }
    public void openInboxActivity(){

        Intent intent = new Intent(this, CalenderActivity.class);
        this.startActivity(intent);
    }
    public void openProfileActivity(){

        Intent intent = new Intent(this, ProfileActivity.class);
        this.startActivity(intent);
    }

}