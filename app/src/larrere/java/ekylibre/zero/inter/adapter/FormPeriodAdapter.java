package ekylibre.zero.inter.adapter;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import ekylibre.util.DateTools;
import ekylibre.zero.R;
import ekylibre.zero.inter.model.Period;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.YEAR;


public class FormPeriodAdapter extends RecyclerView.Adapter<FormPeriodAdapter.ViewHolder> {

    private final List<Period> dataset;

    public FormPeriodAdapter(List<Period> dataset) {
        this.dataset = dataset;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        // Layout
        TextView beginDateField, beginTimeField;
        TextView endDateField, endTimeField;
        TextView warningMessage;
        ImageView deleteButton;

        // Item reference
        Period item;
        Context context;

        ViewHolder(View itemView) {
            super(itemView);

            context = itemView.getContext();

            // Get the layout references
            beginDateField = itemView.findViewById(R.id.period_begin_date_field);
            beginTimeField = itemView.findViewById(R.id.period_begin_time_field);
            endDateField = itemView.findViewById(R.id.period_end_date_field);
            endTimeField = itemView.findViewById(R.id.period_end_time_field);
            deleteButton = itemView.findViewById(R.id.period_delete);
            warningMessage = itemView.findViewById(R.id.period_warning_message);

            // Set click listeners
            deleteButton.setOnClickListener(v -> {
                if (getItemCount() > 1) {
                    int index = dataset.indexOf(item);
                    dataset.remove(index);
                    notifyItemRemoved(index);
                } else {
                    displayWarningDialog(context);
                }
            });

            beginDateField.setOnClickListener(v -> displayDatePicker(beginDateField, true));
            endDateField.setOnClickListener(v -> displayDatePicker(endDateField, false));
            beginTimeField.setOnClickListener(v -> displayTimePicker(beginTimeField, true));
            endTimeField.setOnClickListener(v -> displayTimePicker(endTimeField, false));
        }

        void validatePeriods() {
            Calendar startCal = Calendar.getInstance();
            startCal.setTime(item.startDateTime);
            Calendar stopCal = Calendar.getInstance();
            stopCal.setTime(item.stopDateTime);

            if (startCal.after(stopCal)) {
                // Huston, we have a problem ! start is after stop
                int msgId;
                // Check if the 2 dates are the same day
                if (startCal.get(YEAR) == stopCal.get(YEAR)
                        && startCal.get(MONTH) == startCal.get(MONTH)
                        && startCal.get(DAY_OF_MONTH) == stopCal.get(DAY_OF_MONTH))
                    // So the problem is in time
                    msgId = R.string.end_time_before_begin;
                else
                    msgId = R.string.end_date_before_begin;

                warningMessage.setText(context.getString(msgId));
                warningMessage.setVisibility(View.VISIBLE);

            } else
                warningMessage.setVisibility(View.GONE);
        }
        
        /**
         * The methode in charge of displaying an item
         * @param item The current Period item
         */
        void display(Period item) {

            // Save reference of the current item
            this.item = item;
            // Set fields according to current item
            beginDateField.setText(DateTools.displayDate(item.startDateTime));
            beginTimeField.setText(DateTools.displayTime(item.startDateTime));
            endDateField.setText(DateTools.displayDate(item.stopDateTime));
            endTimeField.setText(DateTools.displayTime(item.stopDateTime));
            validatePeriods();
        }

        void displayDatePicker(TextView textView, boolean isStart) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(isStart ? item.startDateTime : item.stopDateTime);
            DatePickerDialog datePickerDialog =
                    new DatePickerDialog(context, (dialogView, year, month, day) -> {
                        // Save picked date into item
                        cal.set(year, month, day);
                        if (isStart)
                            item.startDateTime = cal.getTime();
                        else
                            item.stopDateTime = cal.getTime();
                        // Display selected date
                        textView.setText(DateTools.displayDate(cal.getTime()));
                        validatePeriods();
                    }, cal.get(YEAR), cal.get(MONTH), cal.get(DAY_OF_MONTH));
            // Show the dialog
            datePickerDialog.show();
        }

        void displayTimePicker(TextView textView, boolean isStart) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(isStart ? item.startDateTime : item.stopDateTime);
            TimePickerDialog timePickerDialog =
                    new TimePickerDialog(context, (timePicker, hour, minute) -> {
                        // Save picked time into item
                        cal.set(HOUR_OF_DAY, hour);
                        cal.set(MINUTE, minute);
                        cal.set(SECOND, 0);
                        cal.set(MILLISECOND, 0);
                        if (isStart)
                            item.startDateTime = cal.getTime();
                        else
                            item.stopDateTime = cal.getTime();
                        // Display selected time
                        textView.setText(DateTools.displayTime(cal.getTime()));
                        validatePeriods();
                    }, cal.get(HOUR_OF_DAY), cal.get(MINUTE), true);
            // Show the dialog
            timePickerDialog.show();
        }
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_period, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.display(dataset.get(position));
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    private void displayWarningDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("L'intervention doit comporter au moins une période. Vous ne pouvez supprimer cette dernière.");
        builder.setPositiveButton("Ok", (dialog, i) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
