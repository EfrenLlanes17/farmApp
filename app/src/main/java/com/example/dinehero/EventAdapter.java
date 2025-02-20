package com.example.dinehero;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
        View view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.dateText.setText(event.date);
        holder.titleText.setText(event.title + " (" + event.type + ")");


        switch (event.type) {
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
            showEditDialog(event, position);
            return true;
        });
    }

    private void showEditDialog(Event event, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Event");

        final EditText input = new EditText(context);
        input.setText(event.title);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            event.title = input.getText().toString();
            sortEvents();
            notifyDataSetChanged();
        });

        builder.setNegativeButton("Delete", (dialog, which) -> {
            eventList.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context, "Event Deleted", Toast.LENGTH_SHORT).show();
        });

        builder.setNeutralButton("Cancel", null);
        builder.show();
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    private void sortEvents() {
        Collections.sort(eventList, (e1, e2) -> e1.date.compareTo(e2.date));
    }



    private void generateRecurringEvents(List<Event> originalEvents) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        for (Event event : originalEvents) {
            eventList.add(event); // Add original event

            if (event.recurrence != null && !event.recurrence.equals("None")) {
                calendar.setTime(parseDate(event.date));

                for (int i = 0; i < 10; i++) { // Generate 10 future occurrences
                    switch (event.recurrence) {
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
                    Event recurringEvent = new Event(event.title, sdf.format(calendar.getTime()), event.type, event.recurrence);
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

