//package com.igniva.spplitt.ui.activties;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.Color;
//import android.graphics.Rect;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.annotation.Nullable;
//import android.support.design.widget.TabLayout;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v4.view.PagerAdapter;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.format.DateFormat;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.GestureDetector;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.GridView;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.igniva.spplitt.R;
//
//import org.json.JSONObject;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.Collections;
//import java.util.Date;
//import java.util.GregorianCalendar;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//
//
///**
// * Created by ip-d on 17/2/16.
// */
//public class CalenderAdapter extends Activity implements View.OnClickListener {
//
//
//    NonSwipeableViewPager nonSwipeableViewPager;
//    TabLayout tabLayout;
//    TextView monthTextView;
//    public TextView  itemTotalPrice, totalRupeesEveryDay, totalRupeesLetMeChoose, itemNumber, productName;
//    int[] today;
//    private DisplayMetrics displayMetrics;
//    int i = 0;
//    Calendar calendar, _calendar;
//
//    ImageButton increaseValue, decreaseValue;
//
//    CustomPagerAdapter adapterPager;
//
//    ImageView backImageView;
//    String[] datesArray = new String[30];
//    HashMap<String, String> productDetailsHashMap;
//    String product_image, product_name;
//    String product_id, idUser;
//    GestureDetector gesture;
//    int month;
//    int monthCal, year;
//    ImageView prevMonth, nextMonth;
//    TextView headerText;
//    GridCellAdapter adapter;
//    GridCellAdapter1 adapter1;
//    //    public static int CurrentMonthAsInt = 0;
//    private static final String dateTemplate = "MMMM yyyy";
//    GridView everyDayGridView, letMeChooseGridView;
//    public static ArrayList<String> alistletMeChoose = new ArrayList<>();
//    TextView totalDaysletmeChhose;
//    TextView totalDaysEveryDay;
//    CheckBox disclaimerText;
//    ArrayList<String> alistEveryDayselected = new ArrayList<>();
//    public static List<String> array = new ArrayList<String>();
//    private final String[] weekdayscalender = new String[]{"S", "M", "T",
//            "W", "T", "F", "S"};
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.subscription_fragment_product_details);
//        initView();
//    }
//
//
//
//    private void initView() {
//        productDetailsHashMap = new HashMap<>();
//
//        adapterPager = new CustomPagerAdapter(this);
//        alistletMeChoose.clear();
//        alistEveryDayselected.clear();
//
//
//        monthTextView = (TextView) findViewById(R.id.monthTextView);
//
//
//        prevMonth = (ImageView) findViewById(R.id.prevMonth);
//        prevMonth.setOnClickListener(this);
//
//        nextMonth = (ImageView) findViewById(R.id.nextMonth);
//        nextMonth.setOnClickListener(this);
//
//
//
//
//
//        _calendar = Calendar.getInstance(Locale.getDefault());
//        calendar = Calendar.getInstance();
//        StringBuilder builder = new StringBuilder();
//        builder.append(new SimpleDateFormat("MMMM", Locale.ENGLISH).format(calendar.getTime())).append(" ").append(String.valueOf(calendar.get(Calendar.YEAR)));
//        monthTextView.setText(builder.toString());
//
//        nonSwipeableViewPager = (NonSwipeableViewPager) findViewById(R.id.viewpager);
//        tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setSelectedTabIndicatorHeight(0);
//        setUpViewPager(nonSwipeableViewPager);
//        nonSwipeableViewPager.setPagingEnabled(false);
//
//        today = new int[3];
//        today[0] = calendar.get(Calendar.DAY_OF_MONTH);
//        today[1] = calendar.get(Calendar.MONTH);
//        monthCal = calendar.get(Calendar.MONTH) + 1;
//        year = calendar.get(Calendar.YEAR);
//        today[2] = calendar.get(Calendar.YEAR);
//        displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        month = calendar.get(Calendar.MONTH) + 1;
//        printMonth(month, year);
//        adapter = new GridCellAdapter(this,
//                R.id.calendar_day_gridcell, monthCal, year, alistEveryDayselected);
//        adapter.notifyDataSetChanged();
//        adapter1 = new GridCellAdapter1(this,
//                R.id.calendar_day_gridcell, monthCal, year);
//        adapter1.notifyDataSetChanged();
////        P1Demo.everyDayGridView.setAdapter(adapter);
//
//        gesture = new GestureDetector(this,
//                new GestureDetector.SimpleOnGestureListener() {
//
//                    @Override
//                    public boolean onDown(MotionEvent e) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//                                           float velocityY) {
//                        final int SWIPE_MIN_DISTANCE = 120;
//                        final int SWIPE_MAX_OFF_PATH = 250;
//                        final int SWIPE_THRESHOLD_VELOCITY = 200;
//                        try {
//                            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
//                                return false;
//                            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
//                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                                if (monthCal > 11) {
//                                    monthCal = 1;
//                                    year++;
//                                } else {
//                                    monthCal++;
//
//                                }
//                                setGridCellAdapterToDate(monthCal, year);
//                            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
//                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//
//                                if (monthCal <= 1) {
//                                    monthCal = 12;
//                                    year--;
//                                } else {
//                                    monthCal--;
//                                }
//                                setGridCellAdapterToDate(monthCal, year);
//                            }
//                        } catch (Exception e) {
//                            // nothing
//                        }
//                        return super.onFling(e1, e2, velocityX, velocityY);
//                    }
//                });
//    }
//
//
//
//    private void setUpViewPager(NonSwipeableViewPager nonSwipeableViewPager) {
//        nonSwipeableViewPager.setAdapter(adapterPager);
//        tabLayout.setupWithViewPager(nonSwipeableViewPager);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//
//            case R.id.prevMonth:
//                if (monthCal <= 1) {
//                    monthCal = 12;
//                    year--;
//                } else {
//                    monthCal--;
//                }
//                setGridCellAdapterToDate(monthCal, year);
//                break;
//            case R.id.nextMonth:
//                if (monthCal > 11) {
//                    monthCal = 1;
//                    year++;
//                } else {
//                    monthCal++;
//
//                }
//                setGridCellAdapterToDate(monthCal, year);
//                break;
//        }
//    }
//
//    private void setGridCellAdapterToDate(int month, int year) {
//
//
//        adapter = new GridCellAdapter(this,
//                R.id.calendar_day_gridcell, month, year, alistEveryDayselected);
//        adapter1 = new GridCellAdapter1(this,
//                R.id.calendar_day_gridcell, month, year);
//
//        SimpleDateFormat originalFormat = new SimpleDateFormat("MM,yyyy", Locale.ENGLISH);
//        SimpleDateFormat targetFormat = new SimpleDateFormat("MMMM yyyy");
//        Date date = null;
//        try {
//            date = originalFormat.parse(month+","+year);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        String formattedDate = targetFormat.format(date);
//        monthTextView.setText(formattedDate);
//        everyDayGridView.setAdapter(adapter);
//        letMeChooseGridView.setAdapter(adapter1);
//    }
//
//
//
//    public enum CustomPagerEnum {
//        Everyday("Every Day (30 days)", R.layout.subscription_fragment_every_day_layout), LetMeChoose("Let me Choose", R.layout.subscription_fragment_choose_day_layout);
//
//        private String pageTitle;
//        private int pageResourceId;
//
//        CustomPagerEnum(String pageTitle, int pageResourceId) {
//            this.pageTitle = pageTitle;
//            this.pageResourceId = pageResourceId;
//        }
//
//        public String getPageTitle() {
//            return pageTitle;
//        }
//
//        public int getPageResourceId() {
//            return pageResourceId;
//        }
//    }
//
//    public class CustomPagerAdapter extends PagerAdapter {
//        private Context context;
//
//        public CustomPagerAdapter(Context context) {
//            this.context = context;
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            CustomPagerEnum customPagerEnum = CustomPagerEnum.values()[position];
//            final LayoutInflater inflater = LayoutInflater.from(context);
//            ViewGroup layout = (ViewGroup) inflater.inflate(customPagerEnum.getPageResourceId(), container, false);
//
//            if (position == 0) {
//                everyDayGridView = (GridView) layout.findViewById(R.id.gridview);
//                everyDayGridView.setAdapter(adapter);
//                totalDaysEveryDay = (TextView) layout.findViewById(R.id.totalDaysEveryDay);
//                totalRupeesEveryDay = (TextView) layout.findViewById(R.id.totalRupeesEveryDay);
//                Button bookNowTextViewEveryDay = (Button) layout.findViewById(R.id.bookNowTextViewEveryDay);
//
//
//
//                GridView recylarViewDays=(GridView)layout.findViewById(R.id.recylarViewDays);
//                WeekDaysCalenAdapter weekdaysadapter=new WeekDaysCalenAdapter(getApplicationContext(),weekdayscalender);
//                recylarViewDays.setAdapter(weekdaysadapter);
//
////                new Handler().post(new Runnable() {
////                    @Override
////                    public void run() {
////                        totalDaysEveryDay.setText(alistEveryDayselected.size() + " Days");
////                        int rs = alistEveryDayselected.size() * Integer.parseInt(itemTotalPrice.getText().toString());
////                        totalRupeesEveryDay.setText(rs + "");
////                    }
////                });
//
//
//                bookNowTextViewEveryDay.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        addDataToHashMapEveryDay();
//
//                    }
//                });
//                container.addView(layout);
//                everyDayGridView.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        return gesture.onTouchEvent(event);
//                    }
//                });
//
//            } else {
//                letMeChooseGridView = (GridView) layout.findViewById(R.id.gridview);
//                totalDaysletmeChhose = (TextView) layout.findViewById(R.id.totalDays);
//                totalRupeesLetMeChoose = (TextView) layout.findViewById(R.id.totalRupees);
//                Button bookNowTextView = (Button) layout.findViewById(R.id.bookNowTextView);
//
//
//                GridView recylarViewDays=(GridView)layout.findViewById(R.id.recylarViewDays);
//                WeekDaysCalenAdapter weekdaysadapter=new WeekDaysCalenAdapter(getApplicationContext(),weekdayscalender);
//                recylarViewDays.setAdapter(weekdaysadapter);
//                letMeChooseGridView.setAdapter(adapter1);
//
//                totalDaysletmeChhose.setText("0 Days");
////                int rs = alistletMeChoose.size() * Integer.parseInt(itemTotalPrice.getText().toString());
////                totalRupeesLetMeChoose.setText(rs + "");
////                totalRupeesLetMeChoose.setText(itemTotalPrice.getText().toString());
//                letMeChooseGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                                               @Override
//                                                               public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                                                   String formattedDate1="";
//                                                                   String date_month_year = (String) letMeChooseGridView.getChildAt(position).getTag();
//                                                                   SimpleDateFormat originalFormat = new SimpleDateFormat("dd-MMMM-yyyy", Locale.ENGLISH);
//                                                                   SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");
//                                                                   Date date = null;
//                                                                   try {
//                                                                       date = originalFormat.parse(date_month_year);
//                                                                       formattedDate1 = targetFormat.format(date);
//                                                                   } catch (ParseException e) {
//                                                                       e.printStackTrace();
//                                                                   }
//
//
//
//                                                                   Calendar c = Calendar.getInstance();
//                                                                   SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
//                                                                   String formattedDate = df.format(c.getTime());
//                                                                   if (Integer.parseInt(formattedDate.split("-")[2].toString()) <= Integer.parseInt(formattedDate1.split("-")[2].toString())) {
//                                                                       if (Integer.parseInt(formattedDate.split("-")[1].toString()) == Integer.parseInt(formattedDate1.split("-")[1].toString())) {
//                                                                           if (Integer.parseInt(formattedDate.split("-")[0].toString()) < Integer.parseInt(formattedDate1.split("-")[0].toString())) {
//
//                                                                               if (alistletMeChoose.contains(date_month_year)) {
//                                                                                   ((TextView) view.findViewById(R.id.calendar_day_gridcell)).setTextColor(Color.parseColor("#ffffff"));
//                                                                                   ((TextView) view.findViewById(R.id.calendar_day_gridcell)).setBackgroundColor(Color.TRANSPARENT);
//                                                                                   alistletMeChoose.remove(date_month_year);
//                                                                               } else {
//                                                                                   ((View) view.findViewById(R.id.ViewCenter)).setVisibility(View.VISIBLE);
//                                                                                   ((TextView) view.findViewById(R.id.calendar_day_gridcell)).setBackgroundResource(R.drawable.square_drawable);
//                                                                                   ((TextView) view.findViewById(R.id.calendar_day_gridcell)).setTextColor(Color.parseColor("#000000"));
//                                                                                   alistletMeChoose.add(date_month_year);
//                                                                               }
//                                                                           }
//                                                                       } else if (Integer.parseInt(formattedDate.split("-")[1].toString()) < Integer.parseInt(formattedDate1.split("-")[1].toString())) {
//                                                                           if (alistletMeChoose.contains(date_month_year)) {
//                                                                               ((TextView) view.findViewById(R.id.calendar_day_gridcell)).setBackgroundColor(Color.TRANSPARENT);
//                                                                               ((TextView) view.findViewById(R.id.calendar_day_gridcell)).setTextColor(Color.parseColor("#ffffff"));
//                                                                               alistletMeChoose.remove(date_month_year);
//                                                                           } else {
//                                                                               ((View) view.findViewById(R.id.ViewCenter)).setVisibility(View.VISIBLE);
//                                                                               ((TextView) view.findViewById(R.id.calendar_day_gridcell)).setTextColor(Color.parseColor("#000000"));
//                                                                               ((TextView) view.findViewById(R.id.calendar_day_gridcell)).setBackgroundResource(R.drawable.square_drawable);
//                                                                               alistletMeChoose.add(date_month_year);
//                                                                           }
//                                                                       }
//                                                                   }
//                                                                   totalDaysletmeChhose.setText(alistletMeChoose.size() + " Days");
////                                                                   int rs = alistletMeChoose.size() * Integer.parseInt(itemTotalPrice.getText().toString());
////                                                                   totalRupeesLetMeChoose.setText(rs + "");
//                                                               }
//                                                           }
//                );
//
//
//                bookNowTextView.setOnClickListener(new View.OnClickListener()
//
//                                                   {
//                                                       @Override
//                                                       public void onClick(View v) {
//                                                           addDataToHashMapLetMeChoose();
//                                                       }
//                                                   }
//
//                );
//                letMeChooseGridView.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        return gesture.onTouchEvent(event);
//                    }
//                });
//                container.addView(layout);
//            }
//
//            return layout;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
//        }
//
//        @Override
//        public int getCount() {
//            return CustomPagerEnum.values().length;
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return view == object;
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            CustomPagerEnum customPagerEnum = CustomPagerEnum.values()[position];
//            return customPagerEnum.getPageTitle();
//        }
//    }
//
//    public void addDataToHashMapLetMeChoose() {
//        List<Date> datesList=new ArrayList<>();
//        datesArray = new String[alistletMeChoose.size()];
//        SimpleDateFormat originalFormat = new SimpleDateFormat("dd-MMMM-yy");
//        SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");
//        for (int i = 0; i < alistletMeChoose.size(); i++) {
//            Date date = null;
//            try {
//                date = originalFormat.parse(alistletMeChoose.get(i));
//                datesList.add(date);
//                Collections.sort(datesList);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//        for(int i=0;i<datesList.size();i++){
//            String formattedDate = targetFormat.format(datesList.get(i));
//            datesArray[i] = formattedDate.toString();
//        }
//        List<JSONObject> myJSONObjects = new ArrayList<>(datesArray.length);
//        //deliveryTomorrowArrayList
//
//        for (int i = 0; i < datesArray.length; i++) {
//            Log.e("===datesArray=e==",datesArray[i]);
//            try {
//                JSONObject jsonObj = new JSONObject();
//                jsonObj.put("dateItem", datesArray[i]);
//                jsonObj.put("quantityItem", itemNumber.getText().toString());
//                jsonObj.put("priceItem", itemTotalPrice.getText().toString());
//                myJSONObjects.add(jsonObj);
//
//            } catch (Exception e) {
//                Log.e("Exception--", e.toString());
//            }
//        }
//        productDetailsHashMap.put("product_id", product_id);
//        productDetailsHashMap.put("product_name", productName.getText().toString());
//        productDetailsHashMap.put("price", totalRupeesLetMeChoose.getText().toString());
//        productDetailsHashMap.put("quantity", itemNumber.getText().toString());
//        productDetailsHashMap.put("uniquedate", myJSONObjects.toString());
//        productDetailsHashMap.put("time", "06 - 09 am");
//        productDetailsHashMap.put("date_type", "Multiple dates");
//        int i=0;
//        if(disclaimerText.isChecked()==true){
//            i=1;
//        }
//        productDetailsHashMap.put("alternative",i+"" );
//        productDetailsHashMap.put("user_id", idUser);
//        Log.e("====e==", productDetailsHashMap.toString());
//
//    }
//
//    public void addDataToHashMapEveryDay() {
//        List<Date> datesList=new ArrayList<>();
//        SimpleDateFormat originalFormat = new SimpleDateFormat("dd-MMMM-yy");
//        SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");
//        datesArray = new String[alistEveryDayselected.size()];
//        for (int i = 0; i < alistEveryDayselected.size(); i++) {
//
//            Date date = null;
//            try {
//                date = originalFormat.parse(alistEveryDayselected.get(i));
//                datesList.add(date);
//                Collections.sort(datesList);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
////            String formattedDate = targetFormat.format(datesList.get(i));
////            datesArray[i] = formattedDate.toString();
//
//        }
//
//        for(int i=0;i<datesList.size();i++){
//            String formattedDate = targetFormat.format(datesList.get(i));
//            datesArray[i] = formattedDate.toString();
//        }
//
//
//
//        List<JSONObject> myJSONObjects = new ArrayList<>(datesArray.length);
//        //deliveryTomorrowArrayList
//        for (int i = 0; i < datesArray.length; i++) {
//            try {
//                JSONObject jsonObj = new JSONObject();
//                jsonObj.put("dateItem", datesArray[i]);
//                jsonObj.put("quantityItem", itemNumber.getText().toString());
//                jsonObj.put("priceItem", itemTotalPrice.getText().toString());
//                myJSONObjects.add(jsonObj);
//
//            } catch (Exception e) {
//                Log.e("Exception--", e.toString());
//            }
//        }
//
//        productDetailsHashMap.put("product_id", product_id);
//        productDetailsHashMap.put("product_name", productName.getText().toString());
//        productDetailsHashMap.put("price", totalRupeesEveryDay.getText().toString());
//        productDetailsHashMap.put("quantity", itemNumber.getText().toString());
//        productDetailsHashMap.put("uniquedate", myJSONObjects.toString());
//        productDetailsHashMap.put("time", "06 - 09 am");
//        productDetailsHashMap.put("date_type", "Everyday");
//        int i=0;
//        if(disclaimerText.isChecked()==true){
//            i=1;
//        }
//        productDetailsHashMap.put("alternative",i+"" );
//        productDetailsHashMap.put("user_id", idUser);
//
//    }
//
//
//    private int daysInMonth;
//    private final String[] weekdays = new String[]{"Sun", "Mon", "Tue",
//            "Wed", "Thu", "Fri", "Sat"};
//    private final String[] months = {"January", "February", "March",
//            "April", "May", "June", "July", "August", "September",
//            "October", "November", "December"};
//    private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30,
//            31, 30, 31};
//
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
//
//        GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
//
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
//
//
//        if (cal.isLeapYear(cal.get(Calendar.YEAR)))
//            if (mm == 2)
//                ++daysInMonth;
//            else if (mm == 3)
//                ++daysInPrevMonth;
//
//
//        // Current Month Days
//        for (int i = 1; i <= daysInMonth; i++) {
//            Calendar calendar = Calendar.getInstance();
//            int currentday=calendar.get(Calendar.DAY_OF_MONTH);
//            if (i > currentday) {
//                array.add(String.valueOf(i) + "-BLUE" + "-"
//                        + getMonthAsString(currentMonth) + "-" + yy);
//            }
//        }
//        SimpleDateFormat formattedDate = new SimpleDateFormat("dd-MMMM-yyyy");
//        Calendar c=Calendar.getInstance();
//        c.add(Calendar.DATE, 1);
//        String fromDate = (String)(formattedDate.format(c.getTime()));
//        c.add(Calendar.DATE, 30);
//        String upToDate = (String)(formattedDate.format(c.getTime()));
//        System.out.println("Tomorrows date is " + upToDate+"=="+Calendar.DATE);
//        List<Date> dates = new ArrayList<Date>();
//        Calendar calendar = new GregorianCalendar();
//        calendar.setTime(new Date(fromDate));
//
//        while (calendar.getTime().before(new Date(upToDate)))
//        {
//            Date result = calendar.getTime();
//            dates.add(result);
//            calendar.add(Calendar.DATE, 1);
//
//        }
//        for(int i=0;i<dates.size();i++){
//            c.setTime(dates.get(i));
//            alistEveryDayselected.add((String) (formattedDate.format(c.getTime())));
//        }
//    }
//
//
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
//
//
//    private class WeekDaysCalenAdapter extends BaseAdapter {
//        Context context;
//        String[] finalweekDays;
//
//        public WeekDaysCalenAdapter(Context activity, String[] weekdayscalender) {
//            this.context=activity;
//            this.finalweekDays=weekdayscalender;
//        }
//
//        @Override
//        public int getCount() {
//            return finalweekDays.length;
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return position;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            View row = convertView;
//            if (row == null) {
//                LayoutInflater inflater = (LayoutInflater) context
//                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                row = inflater.inflate(R.layout.week_gridcell, parent, false);
//                TextView calendar_day_gridcell_day=(TextView)row.findViewById(R.id.calendar_day_gridcell_day);
//                calendar_day_gridcell_day.setText(finalweekDays[position]+"");
//            }
//            return row;
//        }
//    }
//}
//
