package com.example.dinehero;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private Context context;
    private List<Event> eventList;

    private EventAdapter adapter; // Declare adapter


    public EventAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
        generateRecurringEvents(eventList);
        sortEvents();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.dateText.setText(event.getDate());
        holder.titleText.setText(event.getTitle() + " (" + event.getType() + ")");


        switch (event.getTitle()) {
            case "Task":
                holder.dateText.setTextColor(Color.BLUE);
                break;
            case "Employee Schedule":
                holder.dateText.setTextColor(Color.GREEN);
                break;
            case "Harvest":
                holder.dateText.setTextColor(Color.RED);
                break;
            case "IoT":
                holder.dateText.setTextColor(Color.MAGENTA);
                break;
        }

        // Long-press to edit or delete event
        holder.itemView.setOnLongClickListener(v -> {
            showEditEventDialog(event, position);
            return true;
        });
    }

    private void showEditEventDialog(Event event, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context); // Fix: Use context, not application context
        builder.setTitle("Edit Event");

        LayoutInflater inflater = LayoutInflater.from(context); // Fix: Use context, not application context
        View dialogView = inflater.inflate(R.layout.dialog_edit_event, null);

        AutoCompleteTextView eventTitleInput = dialogView.findViewById(R.id.eventTitle);
        Spinner eventTypeSpinner = dialogView.findViewById(R.id.eventType);
        Spinner recurrenceSpinner = dialogView.findViewById(R.id.recurrence);
        Button btnOk = dialogView.findViewById(R.id.btnOk);
        Button btnDelete = dialogView.findViewById(R.id.btnDelete);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        // Set existing values
        eventTitleInput.setText(event.getTitle());

        // Set up spinners
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(context,
                R.array.event_types, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventTypeSpinner.setAdapter(typeAdapter);
        eventTypeSpinner.setSelection(typeAdapter.getPosition(event.getType()));

        ArrayAdapter<CharSequence> recurrenceAdapter = ArrayAdapter.createFromResource(context,
                R.array.recurrence_options, android.R.layout.simple_spinner_item);
        recurrenceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recurrenceSpinner.setAdapter(recurrenceAdapter);
        recurrenceSpinner.setSelection(recurrenceAdapter.getPosition(event.getRecurrence()));

        builder.setView(dialogView);
        AlertDialog dialog = builder.create(); // Fix: Create the dialog here
        dialog.show(); // Fix: Show the dialog here

        // Save Changes
        btnOk.setOnClickListener(v -> {
            event.setTitle(eventTitleInput.getText().toString());
            event.setType(eventTypeSpinner.getSelectedItem().toString());
            event.setRecurrence(recurrenceSpinner.getSelectedItem().toString());

            // Update the event list and UI
            eventList.set(position, event);
            Collections.sort(eventList, Comparator.comparing(Event::getDate)); // Sort by date
            notifyDataSetChanged();

            dialog.dismiss();
        });

        // Delete Event
        btnDelete.setOnClickListener(v -> {
            eventList.remove(position);
            notifyDataSetChanged();
            dialog.dismiss();
        });

        // Cancel
        btnCancel.setOnClickListener(v -> dialog.dismiss());
    }



    @Override
    public int getItemCount() {
        return eventList.size();
    }

    private void sortEvents() {
        Collections.sort(eventList, (e1, e2) -> e1.getDate().compareTo(e2.getDate()));
    }



    private void generateRecurringEvents(List<Event> originalEvents) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        for (Event event : originalEvents) {
            eventList.add(event); // Add original event

            if (event.getRecurrence() != null && !event.getRecurrence().equals("None")) {
                calendar.setTime(parseDate(event.getDate()));

                for (int i = 0; i < 10; i++) { // Generate 10 future occurrences
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

                    // Create a new event for each recurrence
                    Event recurringEvent = new Event(event.getTitle(), sdf.format(calendar.getTime()), event.getType(), event.getRecurrence());
                    eventList.add(recurringEvent);
                }
            }
        }
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


    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView dateText, titleText;

        EventViewHolder(View itemView) {
            super(itemView);
            dateText = itemView.findViewById(android.R.id.text1);
            titleText = itemView.findViewById(android.R.id.text2);
        }
    }
}

