package com.example.leonid.jetpack.restoraunt.UIClasses;



import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.Toast;




import java.util.Calendar;

public class MyDatePickerFragment extends DialogFragment {
    public String date = null;
    public Boolean is_first;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        Bundle mArgs = getArguments();
        is_first = (Boolean)mArgs.get("is_first");
        DatePickerDialog dpd = new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
        if(is_first)
        {
            Toast.makeText(getActivity(), "בחר תאריך התחלה", Toast.LENGTH_LONG).show();
            dpd.setTitle("בחר תאריך התחלה");

        }
        else
        {
            Toast.makeText(getActivity(), "בחר תאריך סיום", Toast.LENGTH_LONG).show();
            dpd.setTitle("בחר תאריך סיום");
        }

        return  dpd;
    }

    private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    Toast.makeText(getActivity(), "selected date is " + view.getYear() +
                            " / " + (view.getMonth()+1) +
                            " / " + view.getDayOfMonth(), Toast.LENGTH_SHORT).show();
//month +1  i
                    int month_out = month +1;
                    String month_out_st;
                    String day_out_st;
                    if (month_out < 10)
                    {
                        month_out_st ="0" + String.valueOf(month_out);
                    }
                    else
                    {
                        month_out_st = String.valueOf(month_out);
                    }
                    if (day < 10)
                    {
                        day_out_st = "0" + String.valueOf(day);
                    }
                    else
                    {
                        day_out_st = String.valueOf(day);
                    }
                    date =String.valueOf(year) + "-" + month_out_st + "-" + day_out_st;
                }
            };

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if(activity instanceof MyDialogCloseListener)
        {
            ((MyDialogCloseListener)activity).handleDialogClose(is_first,date,true);
        }

    }
}