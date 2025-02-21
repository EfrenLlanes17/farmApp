package com.example.dinehero;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        generateRecurringEvents(eventList);
        sortEvents();
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
            eventList.set(position, event);
            Collections.sort(eventList, Comparator.comparing(Event::getDate));
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
        for (Event event : originalEvents) {
            eventList.add(event);

            if (event.getRecurrence() != null && !event.getRecurrence().equals("None")) {
                Date eventDate = parseDate(event.getDate());
                for (int i = 0; i < 10; i++) {
                    switch (event.getRecurrence()) {
                        case "Daily":
                            eventDate.setTime(eventDate.getTime() + 24 * 60 * 60 * 1000); // Add one day
                            break;
                        case "Weekly":
                            eventDate.setTime(eventDate.getTime() + 7 * 24 * 60 * 60 * 1000); // Add one week
                            break;
                        case "Monthly":
                            // Add one month (not fully accurate, you can improve this logic)
                            eventDate.setMonth(eventDate.getMonth() + 1);
                            break;
                        case "Yearly":
                            eventDate.setYear(eventDate.getYear() + 1);
                            break;
                    }

                    Event recurringEvent = new Event(event.getTitle(), sdf.format(eventDate), event.getType(), event.getRecurrence());
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
