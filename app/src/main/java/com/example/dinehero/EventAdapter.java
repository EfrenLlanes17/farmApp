package com.example.dinehero;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

    public EventAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
//        sortEvents();
    }


    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your custom calendar_list_item.xml layout
        View view = LayoutInflater.from(context).inflate(R.layout.calendar_list_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);

        // Set the event date, title, event type, and recurrence status
        holder.eventDate.setText(event.getDate());
        holder.eventTitle.setText(event.getTitle());
        holder.eventType.setText(event.getType());
        holder.recurrenceStatus.setText(event.getRecurrence());

        // Set text color based on event title
        switch (event.getTitle()) {
            case "Task":
                holder.eventDate.setTextColor(Color.BLUE);
                break;
            case "Employee Schedule":
                holder.eventDate.setTextColor(Color.GREEN);
                break;
            case "Harvest":
                holder.eventDate.setTextColor(Color.RED);
                break;
            case "IoT":
                holder.eventDate.setTextColor(Color.MAGENTA);
                break;
        }

        // Long-press to edit or delete event
        holder.itemView.setOnLongClickListener(v -> {
            showEditEventDialog(event, position);
            return true;
        });
    }




    private void showEditEventDialog(Event event, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Event");

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_edit_event, null);

        // Setup the dialog for editing event details
        TextView eventTitleInput = dialogView.findViewById(R.id.eventTitle);
        Spinner eventTypeSpinner = dialogView.findViewById(R.id.eventType);
        Spinner recurrenceSpinner = dialogView.findViewById(R.id.recurrence);
        Button btnOk = dialogView.findViewById(R.id.btnOk);
        Button btnDelete = dialogView.findViewById(R.id.btnDelete);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        // Set existing values
        eventTitleInput.setText(event.getTitle());

        // Setup spinners
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
        AlertDialog dialog = builder.create();
        dialog.show();

        // Save Changes
        btnOk.setOnClickListener(v -> {
            event.setTitle(eventTitleInput.getText().toString());
            event.setType(eventTypeSpinner.getSelectedItem().toString());
            event.setRecurrence(recurrenceSpinner.getSelectedItem().toString());

            // Update the event list and UI
            eventList.remove(position);
            eventList.add(event);
            sortEvents();
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
        Log.d("EventAdapter", "generateRecurringEvents called");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        List<Event> newEvents = new ArrayList<>(); // Temporary list to store events

        for (Event event : originalEvents) {
            newEvents.add(event);  // Add the original event

            if (event.getRecurrence() != null && !event.getRecurrence().equals("None")) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(parseDate(event.getDate()));

                for (int i = 1; i <= 12; i++) {
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

                    Event recurringEvent = new Event(event.getTitle(), sdf.format(calendar.getTime()), event.getType(), event.getRecurrence());
                    newEvents.add(recurringEvent);
                }
            }
        }

        // Log before updating the event list
        Log.d("EventAdapter", "Generated events, total count: " + newEvents.size());

        // Update eventList and notify the adapter
        eventList.clear();
        eventList.addAll(newEvents);
        Collections.sort(eventList, Comparator.comparing(Event::getDate));

        // Log after updating the event list
        Log.d("EventAdapter", "Updated event list size: " + eventList.size());

        // Show toast if event list is updated
        if (context != null) {
            Toast.makeText(context, "Updated event list size: " + eventList.size(), Toast.LENGTH_SHORT).show();
        }

        notifyDataSetChanged();
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
        TextView eventDate, eventTitle, eventType, recurrenceStatus;

        EventViewHolder(View itemView) {
            super(itemView);
            eventDate = itemView.findViewById(R.id.eventDate);
            eventTitle = itemView.findViewById(R.id.eventTitle);
            eventType = itemView.findViewById(R.id.eventType);
            recurrenceStatus = itemView.findViewById(R.id.recurrenceStatus);
        }
    }
}





