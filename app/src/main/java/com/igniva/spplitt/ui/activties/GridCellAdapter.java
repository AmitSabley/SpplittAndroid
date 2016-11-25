//package com.igniva.spplitt.ui.activties;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//
//import com.igniva.spplitt.R;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.GregorianCalendar;
//import java.util.HashMap;
//import java.util.List;
//
///**
// * Created by ip-d on 17/2/16.
// */
//public class GridCellAdapter extends BaseAdapter {
//    private static final String tag = "GridCellAdapter";
//    private final Context _context;
//
//    private final List<String> list;
//    List<String> arrayselected;
//    private static final int DAY_OFFSET = 1;
//    private final String[] weekdays = new String[]{"Sun", "Mon", "Tue",
//            "Wed", "Thu", "Fri", "Sat"};
//    private final String[] months = {"January", "February", "March",
//            "April", "May", "June", "July", "August", "September",
//            "October", "November", "December"};
//    private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30,
//            31, 30, 31};
//    private int daysInMonth;
//      private int currentDayOfMonth;
//    private int currentWeekDay;
//    RelativeLayout relative;
//    ImageView imageViewBackground_gridcell;
//    View ViewCenter;
//    private final HashMap<String, Integer> eventsPerMonthMap;
//    private final SimpleDateFormat dateFormatter = new SimpleDateFormat(
//            "dd-MMMM-yyyy");
//
//    // Days in Current Month
//    public GridCellAdapter(Context context, int textViewResourceId,
//                           int month, int year, List<String> array1) {
//        super();
//
//        this._context = context;
//        this.list = new ArrayList<String>();
//        this.arrayselected = array1;
//        Calendar calendar = Calendar.getInstance();
//        setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
//        setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
//        printMonth(month, year);
//        eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
//    }
//
//    private String getMonthAsString(int i) {
//        return months[i];
//    }
//
//    private String getWeekDayAsString(int i) {
//        return weekdays[i];
//    }
//
//    private int getNumberOfDaysOfMonth(int i) {
//        return daysOfMonth[i];
//    }
//
//    public String getItem(int position) {
//        return list.get(position);
//    }
//
//    @Override
//    public int getCount() {
//        return list.size();
//    }
//
//    /**
//     * Prints Month
//     *
//     * @param mm
//     * @param yy
//     */
//    private void printMonth(int mm, int yy) {
//        int trailingSpaces = 0;
//        int daysInPrevMonth = 0;
//        int prevMonth = 0;
//        int prevYear = 0;
//        int nextMonth = 0;
//        int nextYear = 0;
//
//        int currentMonth = mm - 1;
//
//        String currentMonthName = getMonthAsString(currentMonth);
//        daysInMonth = getNumberOfDaysOfMonth(currentMonth);
//        GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
//        if (currentMonth == 11) {
//            prevMonth = currentMonth - 1;
//            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
//            nextMonth = 0;
//            prevYear = yy;
//            nextYear = yy + 1;
//        } else if (currentMonth == 0) {
//            prevMonth = 11;
//            prevYear = yy - 1;
//            nextYear = yy;
//            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
//            nextMonth = 1;
//        } else {
//            prevMonth = currentMonth - 1;
//            nextMonth = currentMonth + 1;
//            nextYear = yy;
//            prevYear = yy;
//            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
//        }
//
//        int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
//        trailingSpaces = currentWeekDay;
//        if (cal.isLeapYear(cal.get(Calendar.YEAR)))
//            if (mm == 2)
//                ++daysInMonth;
//            else if (mm == 3)
//                ++daysInPrevMonth;
//
//        // Trailing Month days
//        for (int i = 0; i < trailingSpaces; i++) {
//            list.add(String
//                    .valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET)
//                            + i)
//                    + "-GREY"
//                    + "-"
//                    + getMonthAsString(prevMonth)
//                    + "-"
//                    + prevYear);
//        }
//
//        // Current Month Days
//        for (int i = 1; i <= daysInMonth; i++) {
//            if (getMonthAsString(currentMonth).equals((String) android.text.format.DateFormat.format("MMMM", new Date()))) {
//                if (i > getCurrentDayOfMonth()) {
//                    list.add(String.valueOf(i) + "-BLUE" + "-"
//                            + getMonthAsString(currentMonth) + "-" + yy);
//
//                } else if (i <= getCurrentDayOfMonth()) {
//                    list.add(String.valueOf(i) + "-WHITE" + "-"
//                            + getMonthAsString(currentMonth) + "-" + yy);
//
//                }
//            } else {
//                        list.add(String.valueOf(i) + "-WHITE" + "-"
//                                + getMonthAsString(currentMonth) + "-" + yy);
//            }
//        }
//
//        // Leading Month days
//        for (int i = 0; i < list.size() % 7; i++) {
//            list.add(String.valueOf(i + 1) + "-GREY" + "-"
//                    + getMonthAsString(nextMonth) + "-" + nextYear);
//        }
//    }
//
//    /**
//     * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
//     * ALL entries from a SQLite database for that month. Iterate over the
//     * List of All entries, and get the dateCreated, which is converted into
//     * day.
//     *
//     * @param year
//     * @param month
//     * @return
//     */
//    private HashMap<String, Integer> findNumberOfEventsPerMonth(int year,
//                                                                int month) {
//        HashMap<String, Integer> map = new HashMap<String, Integer>();
//
//        return map;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View row = convertView;
//        if (row == null) {
//            LayoutInflater inflater = (LayoutInflater) _context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            row = inflater.inflate(R.layout.screen_gridcell, parent, false);
//        }
//
//        // Get a reference to the Day gridcell
//        relative = (RelativeLayout) row.findViewById(R.id.relative);
//        TextView gridcell = (TextView) row.findViewById(R.id.calendar_day_gridcell);
//        imageViewBackground_gridcell = (ImageView) row.findViewById(R.id.imageViewBackground_gridcell);
//        ViewCenter = (View) row.findViewById(R.id.ViewCenter);
//        // ACCOUNT FOR SPACING
//        String[] day_color = list.get(position).split("-");
//        String theday = day_color[0];
//        String themonth = day_color[2];
//        String theyear = day_color[3];
//
//        // Set the Day GridCell
//        gridcell.setText(theday);
//        if (Integer.parseInt(theday) < 10) {
//            theday="0"+theday;
//        }
//        relative.setTag(theday + "-" + themonth + "-" + theyear);
//        if (day_color[1].equals("WHITE")) {
//            gridcell.setTextColor(Color.WHITE);
//            ViewCenter.setVisibility(View.GONE);
//            imageViewBackground_gridcell.setBackgroundDrawable(_context.getResources().getDrawable(R.drawable.ic_cross));
//        }
//        if(arrayselected.contains(relative.getTag())) {
//                ViewCenter.setVisibility(View.VISIBLE);
//                imageViewBackground_gridcell.setVisibility(View.GONE);
//                gridcell.setTextColor(Color.BLACK);
//                gridcell.setBackgroundResource(R.drawable.square_drawable);
//        }
//        if (day_color[1].equals("GREY")) {
//            ViewCenter.setVisibility(View.GONE);
//            imageViewBackground_gridcell.setVisibility(View.GONE);
//            gridcell.setTextColor(Color.parseColor("#DCDCDC"));
//            gridcell.setBackgroundColor(Color.parseColor("#0000ffff"));
//        }
//        return row;
//    }
//
//
//    public int getCurrentDayOfMonth() {
//        return currentDayOfMonth;
//    }
//
//    private void setCurrentDayOfMonth(int currentDayOfMonth) {
//        this.currentDayOfMonth = currentDayOfMonth;
//    }
//
//    public void setCurrentWeekDay(int currentWeekDay) {
//        this.currentWeekDay = currentWeekDay;
//    }
//
//    public int getCurrentWeekDay() {
//        return currentWeekDay;
//    }
//}
//
