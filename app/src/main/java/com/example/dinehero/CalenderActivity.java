package com.example.dinehero;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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
import java.util.Arrays;
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

    private static final String CHANNEL_ID = "eventReminderChannel";


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


        createNotificationChannel();

        TNV = findViewById(R.id.topNavView2);
        TNV.setItemActiveIndicatorEnabled(false);

        recyclerView = findViewById(R.id.recyclerViewEvents);
        fabAddEvent = findViewById(R.id.fabAddEvent);
        fabFilterEvent = findViewById(R.id.fabFilterEvent);
        noResults = findViewById(R.id.noResults);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (eventList == null) {
            eventList = new ArrayList<>();
            adapter = new EventAdapter(this, eventList);
        } else {
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
                } else if (item.getItemId() == findViewById(R.id.btnSearch).getId()) {
                    openMainActivity();
                }

                return false;
            }
        });


    }

    public void openCalenderActivity() {

        Intent intent = new Intent(this, CalenderActivity.class);
        this.startActivity(intent);
    }

    public void openAIChatActivity() {

        Intent intent = new Intent(this, AiChatActivity.class);
        this.startActivity(intent);
    }


    public void openForYouActivity() {

        Intent intent = new Intent(this, ForYouActivity.class);
        this.startActivity(intent);
    }


    private void generateRecurringEvents(Event event) throws ParseException {
        Boolean doISend = false;
        String noteDate = "";

        Log.d("EventAdapter", "Generating recurrence for: " + event.getTitle());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        List<Event> newEvents = new ArrayList<>();
//        newEvents.add(event);

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/M/dd", Locale.getDefault());
        String todayDate = sdf.format(Calendar.getInstance().getTime());
        String todayDateEvent = sdf2.format(Calendar.getInstance().getTime());

        if (event.getDate().equals(todayDateEvent)) {
            doISend = true;
            noteDate = event.getDate();

        }


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
                    recurrenceCount = 0;
            }
            newEvents.add(new Event(sdf.format(calendar.getTime()), event.getTitle(), event.getType(), event.getRecurrence()));
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

                Event recurringEvent = new Event(sdf.format(calendar.getTime()), event.getTitle(), event.getType(), event.getRecurrence());
                newEvents.add(recurringEvent);





                if (recurringEvent.getDate().equals(todayDate)) {
                    doISend = true;
                    noteDate = recurringEvent.getDate();

                }
//                else{
//                    Toast.makeText(this,  recurringEvent.getDate() + " not equal  " + todayDate, Toast.LENGTH_SHORT).show();
//
//                }


            }
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parseDate(event.getDate()));
            newEvents.add(new Event(sdf.format(calendar.getTime()), event.getTitle(), event.getType(), event.getRecurrence()));
        }

        eventList.addAll(newEvents);
        adapter.notifyDataSetChanged();
        adapter.updateEvents(eventList);

        if(doISend && !noteDate.isEmpty()){
            sendInstantNotification(noteDate );
        }



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

    private List<Event> getEventsForDate(String date) throws ParseException {




        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy/M/dd", Locale.getDefault());
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        Date tempdate = oldFormat.parse(date);
        String formattedDate = newFormat.format(tempdate);
        date = formattedDate;

        List<Event> eventsOnDate = new ArrayList<>();
        for (Event event : eventList) {
            if (event.getDate().equals(date)) {
                eventsOnDate.add(event);
            }
        }
        return eventsOnDate;
    }


    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();

        // Create a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                android.R.style.Theme_DeviceDefault_Light_Dialog,
                (view, year, month, dayOfMonth) -> {
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
            showEventDialog(confirmedDate);
        });

        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", (dialog, which) -> {
            dialog.dismiss();
        });

        datePickerDialog.show();
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "EventReminderChannel";
            String description = "Channel for event reminders";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.enableVibration(true);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendInstantNotification( String date) throws ParseException {

        String responseMessage;

        List<Event> eventsOnDate = getEventsForDate(date);
        if (!eventsOnDate.isEmpty()) {
            responseMessage = "Today contains these events:\n";
            for (Event event : eventsOnDate) {
                responseMessage += "\n- " + event.getTitle() + "\n";
            }
        }
        else{
            responseMessage = "Empty";
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_person_24)
                .setContentTitle("Today's Events")
                .setContentText(responseMessage)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setFullScreenIntent(null, true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(100, builder.build()); // 100 is notification ID
    }


    private void scheduleEventNotification(String eventName, String eventDate) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            calendar.setTime(sdf.parse(eventDate));
            calendar.set(Calendar.HOUR_OF_DAY, 9);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, EventReminderReceiver.class);
        intent.putExtra("eventName", eventName);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
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
        Spinner moreData = dialogView.findViewById(R.id.moreData);
        Button okButton = dialogView.findViewById(R.id.btnOk);
        Button cancelButton = dialogView.findViewById(R.id.btnCancel);
        EditText newTxt = dialogView.findViewById(R.id.newTxt);
        Button newBTn = dialogView.findViewById(R.id.newBTn);

        newTxt.setVisibility(View.INVISIBLE);
        newBTn.setVisibility(View.INVISIBLE);

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(
                this, R.array.event_types, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventTypeSpinner.setAdapter(typeAdapter);

        ArrayAdapter<CharSequence> recurrenceAdapter = ArrayAdapter.createFromResource(
                this, R.array.recurrence_options, android.R.layout.simple_spinner_item);
        recurrenceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recurrenceSpinner.setAdapter(recurrenceAdapter);

        Boolean moreInfo = false;

        eventTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (eventTypeSpinner.getSelectedItem().equals("Employee Schedule")) {
                    moreData.setVisibility(View.VISIBLE);


                    ArrayAdapter<CharSequence> employeeAdapter = ArrayAdapter.createFromResource(
                            CalenderActivity.this, R.array.employList, android.R.layout.simple_spinner_item);
                    employeeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    moreData.setAdapter(employeeAdapter);
                } else if (eventTypeSpinner.getSelectedItem().equals("Harvest")) {
                    moreData.setVisibility(View.VISIBLE);


                    ArrayAdapter<CharSequence> harvestAdapter = ArrayAdapter.createFromResource(
                            CalenderActivity.this, R.array.HarvestList, android.R.layout.simple_spinner_item);
                    harvestAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    moreData.setAdapter(harvestAdapter);
                }
             else if (eventTypeSpinner.getSelectedItem().equals("IoT")) {
                moreData.setVisibility(View.VISIBLE);


                ArrayAdapter<CharSequence> iotAdapter = ArrayAdapter.createFromResource(
                        CalenderActivity.this, R.array.IoTList, android.R.layout.simple_spinner_item);
                    iotAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                moreData.setAdapter(iotAdapter);
            }
            else {
                    moreData.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });




        moreData.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (moreData.getSelectedItem().toString().equals("New Employee")){
                    newTxt.setVisibility(View.VISIBLE);
                    newBTn.setVisibility(View.VISIBLE);



                }
                else{
                    eventTitleInput.setText(eventTitleInput.getText() + " " + moreData.getSelectedItem().toString()) ;
                    newTxt.setVisibility(View.INVISIBLE);
                    newBTn.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });


        newBTn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] array = getResources().getStringArray(R.array.employList);

                List<String> stringList = new ArrayList<>(Arrays.asList(array));

                stringList.add(stringList.size(),newTxt.getText().toString());

                ArrayAdapter<String> adapter = new ArrayAdapter<>(CalenderActivity.this, android.R.layout.simple_spinner_item, stringList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                moreData.setAdapter(adapter);

                eventTitleInput.setText(eventTitleInput.getText() + " " + newTxt.getText().toString()) ;

            }
        });



        AlertDialog dialog = builder.create();

        okButton.setOnClickListener(v -> {
            String title = eventTitleInput.getText().toString();
            String type = eventTypeSpinner.getSelectedItem().toString();
            String recurrence = recurrenceSpinner.getSelectedItem().toString();

            //eventList.add(new Event(date, title, type, recurrence));

            try {
                generateRecurringEvents(new Event(date, title, type, recurrence));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }


            Collections.sort(eventList, (e1, e2) -> e1.getDate().compareTo(e2.getDate()));



            adapter.notifyDataSetChanged();
            dialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }


    private void showFilterDialog() {
        // Inflate the dialog layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_filter_event, null);

        AutoCompleteTextView eventTitle = dialogView.findViewById(R.id.eventTitle);
        Spinner eventType = dialogView.findViewById(R.id.eventType);
        Spinner recurrence = dialogView.findViewById(R.id.recurrence);
        Spinner com = dialogView.findViewById(R.id.completed);
        EditText etStartDate = dialogView.findViewById(R.id.etStartDate);
        EditText etEndDate = dialogView.findViewById(R.id.etEndDate);
        Button btnOk = dialogView.findViewById(R.id.btnOk);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(
                this, R.array.event_typesfilter, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventType.setAdapter(typeAdapter);

        ArrayAdapter<CharSequence> recurrenceAdapter = ArrayAdapter.createFromResource(
                this, R.array.recurrence_optionsfilter, android.R.layout.simple_spinner_item);
        recurrenceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recurrence.setAdapter(recurrenceAdapter);

        ArrayAdapter<CharSequence> completedAdapter = ArrayAdapter.createFromResource(
                this, R.array.completedfilter, android.R.layout.simple_spinner_item);
        recurrenceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        com.setAdapter(completedAdapter);



        btnOk.setOnClickListener(v -> {
            String titleFilter = eventTitle.getText().toString().trim();
            String typeFilter = eventType.getSelectedItem().toString();
            String recurrenceFilter = recurrence.getSelectedItem().toString();
            String startDateFilter = etStartDate.getText().toString();
            String endDateFilter = etEndDate.getText().toString();
            String completed = com.getSelectedItem().toString();

            List<Event> filteredList = filterEvents(titleFilter, typeFilter, recurrenceFilter, startDateFilter, endDateFilter,completed);

            updateUI(filteredList);

            dialog.dismiss();
        });
    }


    private List<Event> filterEvents(String title, String type, String recurrence, String startDate, String endDate, String com) {
        List<Event> filteredList = new ArrayList<>();
        if(!title.isEmpty() || !type.equals("All Types") || !recurrence.equals("All Recurrences") ||!startDate.isEmpty()||!endDate.isEmpty() ||!com.equals("All Statuses")) {
            for (Event event : eventList) {
                boolean matchesTitle = title.isEmpty() || event.getTitle().toLowerCase().contains(title.toLowerCase());
                boolean matchesType = type.equals("All Types") || event.getType().equalsIgnoreCase(type);
                boolean matchesRecurrence = recurrence.equals("All Recurrences") || event.getRecurrence().equalsIgnoreCase(recurrence);
                boolean matchesDateRange = isWithinDateRange(event.getDate(), startDate, endDate);

                boolean status = true;
                if(com.equals("Completed")){
                    status = true;
                } else if (com.equals("Not Completed")) {
                    status = false;
                }
                boolean matchesCompleted = com.equals("All Statuses") || event.getFinished() == status;

                if (matchesTitle && matchesType && matchesRecurrence && matchesDateRange &&  matchesCompleted) {
                    filteredList.add(event);
                }
            }

            return filteredList;
        }
        else{


            return eventList;
        }

    }


    private boolean isWithinDateRange(String eventDate, String startDate, String endDate) {
        if (startDate.isEmpty() && endDate.isEmpty()) return true; // No date filter applied




        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());
        try {
            SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
            Date date = oldFormat.parse(eventDate);

            Date start = startDate.isEmpty() ? null : sdf.parse(startDate);
            Date end = endDate.isEmpty() ? null : sdf.parse(endDate);

//            Toast.makeText(this, "Start:  " + start.toString(), Toast.LENGTH_LONG).show();
//            Toast.makeText(this,  "End: " + end.toString(), Toast.LENGTH_LONG).show();

            if (start != null && date.before(start)) {
                //Toast.makeText(this, date.toString() + " excluded: before " + start.toString(), Toast.LENGTH_LONG).show();
                return false;
            }

            if (end != null && date.after(end)) {
                //Toast.makeText(this, date.toString() + " excluded: after " + end.toString(), Toast.LENGTH_LONG).show();
                return false;
            }

            //Toast.makeText(this, date.toString()+" included: within date range. Start: " + start.toString(), Toast.LENGTH_LONG).show();
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            //Toast.makeText(this, "Error " , Toast.LENGTH_SHORT).show();

            return false;
        }
    }

    /**
     * Updates the calendar UI with the filtered event list.
     */
    private void updateUI(List<Event> filteredList) {

        EventAdapter adapter2 = new EventAdapter(this, filteredList);
        recyclerView.setAdapter(adapter2);
        if(filteredList.size() ==0){
            noResults.setVisibility(View.VISIBLE);
        }
        else{
            noResults.setVisibility(View.INVISIBLE);

        }

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