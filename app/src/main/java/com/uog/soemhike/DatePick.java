package com.uog.soemhike;

//import android.app.DatePickerDialog;
//import android.app.Dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.uog.soemhike.activity.HikeAdvanceSearchActivity;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Locale;

public class DatePick extends DialogFragment implements DatePickerDialog.OnDateSetListener{


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        LocalDate d = LocalDate.now();

        int year = d.getYear();
        int month = d.getMonthValue();
        int day = d.getDayOfMonth();

        return new DatePickerDialog(getActivity(),this,year,month,day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        if (getActivity() instanceof EntryActivity) {
            EntryActivity mainActivity = (EntryActivity) getActivity();
            mainActivity.setDate(LocalDate.of(year, month, day));
        }
        else if(getActivity()instanceof HikeAdvanceSearchActivity){
            HikeAdvanceSearchActivity searchActivity = (HikeAdvanceSearchActivity) getActivity();
            searchActivity.setDate(LocalDate.of(year, month, day));
        }
    }
}
