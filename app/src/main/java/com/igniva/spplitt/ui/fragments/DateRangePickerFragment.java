package com.igniva.spplitt.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TabHost;

import com.igniva.spplitt.R;
import com.igniva.spplitt.utils.Log;
import com.igniva.spplitt.utils.Utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateRangePickerFragment extends DialogFragment implements View.OnClickListener{

    private OnDateRangeSelectedListener onDateRangeSelectedListener;

    private TabHost tabHost;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private Button butSetDateRange;
    boolean is24HourMode;

    public DateRangePickerFragment() {
        // Required empty public constructor
    }

    public static DateRangePickerFragment newInstance(OnDateRangeSelectedListener callback, boolean is24HourMode) {
        DateRangePickerFragment dateRangePickerFragment = new DateRangePickerFragment();
        dateRangePickerFragment.initialize(callback, is24HourMode);
        return dateRangePickerFragment;
    }

    public void initialize(OnDateRangeSelectedListener callback,
                           boolean is24HourMode) {
        onDateRangeSelectedListener = callback;
        this.is24HourMode = is24HourMode;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_date_range_picker, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        final Calendar c = Calendar.getInstance();
        tabHost = (TabHost) root.findViewById(R.id.tabHost);
        butSetDateRange = (Button) root.findViewById(R.id.but_set_time_range);
        startDatePicker = (DatePicker) root.findViewById(R.id.start_date_picker);
        startDatePicker.setMinDate(c.getTimeInMillis());
        endDatePicker = (DatePicker) root.findViewById(R.id.end_date_picker);
        endDatePicker.setMinDate(c.getTimeInMillis());
        butSetDateRange.setOnClickListener(this);
        tabHost.findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec startDatePage = tabHost.newTabSpec(getResources().getString(R.string.from_date));
        startDatePage.setContent(R.id.start_date_group);
        startDatePage.setIndicator(getResources().getString(R.string.from_date));

        TabHost.TabSpec endDatePage = tabHost.newTabSpec(getResources().getString(R.string.to_date));
        endDatePage.setContent(R.id.end_date_group);
        endDatePage.setIndicator(getResources().getString(R.string.to_date));

        tabHost.addTab(startDatePage);
        tabHost.addTab(endDatePage);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null)
            return;
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }


    public void setOnDateRangeSelectedListener(OnDateRangeSelectedListener callback) {
        this.onDateRangeSelectedListener = callback;
    }


    @Override
    public void onClick(View v) {
        dismiss();
        onDateRangeSelectedListener.onDateRangeSelected(startDatePicker.getDayOfMonth(),startDatePicker.getMonth(),startDatePicker.getYear(),
                endDatePicker.getDayOfMonth(),endDatePicker.getMonth(),endDatePicker.getYear());
    }

    public interface OnDateRangeSelectedListener {
        void onDateRangeSelected(int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear);
    }


//    public static List<Date> dateInterval(Date initial, Date finall) {
//        List<Date> dates = new ArrayList<Date>();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(initial);
//
//        while (calendar.getTime().before(finall)) {
//            Date result = calendar.getTime();
//            dates.add(result);
//            calendar.add(Calendar.DATE, 1);
//        }
//
//        return dates;
//    }
}
