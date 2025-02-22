package com.example.dinehero;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import android.app.AlertDialog;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class CalenderActivity extends AppCompatActivity {


    private BottomNavigationView TNV;

    private List<Event> eventList = new ArrayList<>();
    private EventAdapter adapter;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAddEvent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);




        TNV = findViewById(R.id.topNavView2);
        TNV.setItemActiveIndicatorEnabled(false);

        recyclerView = findViewById(R.id.recyclerViewEvents);
        fabAddEvent = findViewById(R.id.fabAddEvent);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EventAdapter(this, eventList);
        recyclerView.setAdapter(adapter);

        fabAddEvent.setOnClickListener(v -> openDatePicker());




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

    public void openCalenderActivity(){

        Intent intent = new Intent(this, CalenderActivity.class);
        this.startActivity(intent);
    }



    public void openForYouActivity(){

        Intent intent = new Intent(this, ForYouActivity.class);
        this.startActivity(intent);
    }


    private void generateRecurringEvents(Event event) {
        Log.d("EventAdapter", "Generating recurrence for: " + event.getTitle());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        List<Event> newEvents = new ArrayList<>();
        newEvents.add(event); // Add the original event

        if (event.getRecurrence() != null && !event.getRecurrence().equals("None")) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parseDate(event.getDate()));

            int recurrenceCount;
            switch (event.getRecurrence()) {
                case "Daily":
                    recurrenceCount = 365;
                    break;
                case "Weekly":
                    recurrenceCount = 52;
                    break;
                case "Monthly":
                    recurrenceCount = 12;
                    break;
                case "Yearly":
                    recurrenceCount = 1;
                    break;
                default:
                    recurrenceCount = 0; // No recurrence
            }

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

        eventList.addAll(newEvents);
        Collections.sort(eventList, Comparator.comparing(Event::getDate));
        adapter.notifyDataSetChanged();
    }




    private Date parseDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
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
                    String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
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
            String confirmedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
            showEventDialog(confirmedDate); // Show event dialog with confirmed date
        });

        // Set the negative (Cancel) button
        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", (dialog, which) -> {
            dialog.dismiss(); // Simply dismiss the dialog
        });

        // Show the date picker dialog
        datePickerDialog.show();
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