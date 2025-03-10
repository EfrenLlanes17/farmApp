package com.example.dinehero;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private Context context;
    private List<Event> eventList;

    public EventAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = new ArrayList<>();
        groupEventsByDate(eventList);
        Collections.sort(eventList, (e1, e2) -> e1.getDate().compareTo(e2.getDate()));
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(context).inflate(R.layout.item_event_header, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.calendar_list_item, parent, false);
        }
        return new EventViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);

        if (event.isHeader()) {

            try {
                SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat newFormat = new SimpleDateFormat("MM-dd-yy", Locale.getDefault());
                Date date = oldFormat.parse(event.getDate());
                String formattedDate = newFormat.format(date);
                holder.headerTitle.setText(formattedDate);
            } catch (Exception e) {
                holder.headerTitle.setText(event.getDate()); // Display date header
            }

        } else {
            try {
                SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat newFormat = new SimpleDateFormat("MM-dd-yy", Locale.getDefault());
                Date date = oldFormat.parse(event.getDate());
                String formattedDate = newFormat.format(date);
                holder.eventDate.setText(formattedDate);
            } catch (Exception e) {
                holder.eventDate.setText(event.getDate());
            }

            holder.eventTitle.setText(event.getTitle());
            if (event.getFinished()) {
                // Remove strikethrough
                holder.eventTitle.setPaintFlags(holder.eventTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            holder.eventTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ((holder.eventTitle.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0) {
                        // Remove strikethrough
                        holder.eventTitle.setPaintFlags(holder.eventTitle.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                        event.setFinished(false);
                    } else {
                        // Apply strikethrough
                        holder.eventTitle.setPaintFlags(holder.eventTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        event.setFinished(true);
                    }
                }
            });
            holder.eventType.setText(event.getType());
            holder.recurrenceStatus.setText(event.getRecurrence());

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
                case "Order":
                    holder.eventDate.setTextColor(Color.BLACK);
                    break;
            }

            holder.itemView.setOnLongClickListener(v -> {
                showEditEventDialog(event, position);
                return true;
            });
        }
    }

    public void updateEvents(List<Event> rawEvents) {
        // Clear the current list and rebuild with headers
        eventList.clear();
        Collections.sort(rawEvents, (e1, e2) -> e1.getDate().compareTo(e2.getDate()));
        String lastDate = "";
        for (Event event : rawEvents) {
            if (!event.getDate().equals(lastDate)) {
                eventList.add(new Event(event.getDate(), true)); // header
                lastDate = event.getDate();
            }
            eventList.add(event);
        }
        notifyDataSetChanged();
    }

    private void showEditEventDialog(Event event, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Event");

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_edit_event, null);

        TextView eventTitleInput = dialogView.findViewById(R.id.eventTitle);
        Spinner eventTypeSpinner = dialogView.findViewById(R.id.eventType);
        Spinner recurrenceSpinner = dialogView.findViewById(R.id.recurrence);
        Button btnOk = dialogView.findViewById(R.id.btnOk);
        Button btnDelete = dialogView.findViewById(R.id.btnDelete);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnDeleteRecurrence = dialogView.findViewById(R.id.btnDeleteRecurrence);

        eventTitleInput.setText(event.getTitle());

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

        btnOk.setOnClickListener(v -> {
            event.setTitle(eventTitleInput.getText().toString());
            event.setType(eventTypeSpinner.getSelectedItem().toString());
            event.setRecurrence(recurrenceSpinner.getSelectedItem().toString());

            eventList.remove(position);
            eventList.add(event);
            groupEventsByDate(eventList);
            notifyDataSetChanged();
            dialog.dismiss();
        });

        btnDelete.setOnClickListener(v -> {
            eventList.remove(position);
            notifyDataSetChanged();
            dialog.dismiss();
        });

        btnDeleteRecurrence.setOnClickListener(v -> {
            List<Event> toRemove = new ArrayList<>();
            String selectedDate = event.getDate();
            toRemove.add(event);
            for (Event e : eventList) {
                if (!e.isHeader() &&
                        e.getTitle().equals(event.getTitle()) &&
                        e.getType().equals(event.getType()) &&
                        e.getRecurrence().equals(event.getRecurrence()) &&
                        e.getDate().compareTo(selectedDate) > 0) {
                    toRemove.add(e);
                }
            }

            eventList.removeAll(toRemove);
            notifyDataSetChanged();
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
    }

    @Override
    public int getItemViewType(int position) {
        return eventList.get(position).isHeader() ? 0 : 1;
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    private void groupEventsByDate(List<Event> events) {
        eventList.clear();
        Collections.sort(events, (e1, e2) -> e1.getDate().compareTo(e2.getDate()));

        String lastDate = "";
        for (Event event : events) {
            if (!event.getDate().equals(lastDate)) {
                eventList.add(new Event(event.getDate(),true)); // Add header
                lastDate = event.getDate();
            }
            eventList.add(event); // Add event
        }
        notifyDataSetChanged();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView eventDate, eventTitle, eventType, recurrenceStatus, headerTitle;

        EventViewHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType == 0) {
                headerTitle = itemView.findViewById(R.id.headerTitle);
            } else {
                eventDate = itemView.findViewById(R.id.eventDate);
                eventTitle = itemView.findViewById(R.id.eventTitle);
                eventType = itemView.findViewById(R.id.eventType);
                recurrenceStatus = itemView.findViewById(R.id.recurrenceStatus);
            }
        }
    }
}
