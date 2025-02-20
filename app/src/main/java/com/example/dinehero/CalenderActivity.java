package com.example.dinehero;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Event");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_event, null);
        EditText eventTitleInput = dialogView.findViewById(R.id.eventTitle);
        Spinner eventTypeSpinner = dialogView.findViewById(R.id.eventType);
        Spinner recurrenceSpinner = dialogView.findViewById(R.id.recurrence);

        // Set up spinners
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
                R.array.event_types, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventTypeSpinner.setAdapter(typeAdapter);

        ArrayAdapter<CharSequence> recurrenceAdapter = ArrayAdapter.createFromResource(this,
                R.array.recurrence_options, android.R.layout.simple_spinner_item);
        recurrenceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recurrenceSpinner.setAdapter(recurrenceAdapter);

        builder.setView(dialogView);

        // Create the dialog
        AlertDialog dialog = builder.create();
        dialog.show(); // Show the dialog

        // Set the "OK" button behavior
        Button okButton = dialog.findViewById(R.id.btnOk);
        okButton.setOnClickListener(v -> {
            String title = eventTitleInput.getText().toString();
            String type = eventTypeSpinner.getSelectedItem().toString();
            String recurrence = recurrenceSpinner.getSelectedItem().toString();

            // Save the event
            eventList.add(new Event(date, title, type, recurrence));
            adapter.notifyDataSetChanged(); // Update the event list view

            // Dismiss the dialog
            dialog.dismiss();
        });

        // Set the "Cancel" button behavior
        builder.setNegativeButton("Cancel", (dialogInterface, which) -> dialogInterface.dismiss());
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