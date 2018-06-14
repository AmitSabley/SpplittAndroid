package com.igniva.spplitt.ui.activties;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.igniva.spplitt.ui.fragments.WalkthroughFragment;
//import android.app.Activity;
import com.igniva.spplitt.R;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentActivity;
import android.content.Intent;
import android.widget.TextView;

public class WalkthroughActivity extends FragmentActivity {
    static final int NUM_PAGES = 3;

    ViewPager pager;
    PagerAdapter pagerAdapter;
    LinearLayout circles;
   TextView done;
    ImageView next;
    boolean isOpaque = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        setContentView(R.layout.activity_tutorial);
      
        next = ImageView.class.cast(findViewById(R.id.btn_next));
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(pager.getCurrentItem() + 1, true);
            }
        });

        done = TextView.class.cast(findViewById(R.id.btn_done));
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  startActivity(new Intent(getApplicationContext(), LoginOptionActivity.class));
            finish();
            }
        });

        pager = (ViewPager) findViewById(R.id.tour_pager);
        pager = (ViewPager) findViewById(R.id.tour_pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
//        pager.setPageTransformer(true, new CrossfadePageTransformer());
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == NUM_PAGES - 2 && positionOffset > 0) {
                    if (isOpaque) {
                        pager.setBackgroundColor(Color.TRANSPARENT);
                        isOpaque = false;
                    }
                } else {
                    if (!isOpaque) {
                        pager.setBackgroundColor(getResources().getColor(R.color.primary_material_light));
                        isOpaque = true;
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                setIndicator(position);
                
                switch(position){
                case 0:
                     next.setVisibility(View.VISIBLE);
                    done.setVisibility(View.GONE);
                break;
                case 1:
                      next.setVisibility(View.VISIBLE);
                    done.setVisibility(View.GONE);
                break;
                case 2:
                    next.setVisibility(View.GONE);
                    done.setVisibility(View.VISIBLE);
                break;
                default:
                     endTutorial();
                
                break;}
                
                
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        buildCircles();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(pager!=null){
            pager.clearOnPageChangeListeners();
        }
    }

    private void buildCircles(){
        circles = LinearLayout.class.cast(findViewById(R.id.circles));

        float scale = getResources().getDisplayMetrics().density;
        int padding = (int) (5 * scale + 0.5f);

        for(int i = 0 ; i < NUM_PAGES - 1 ; i++){
            ImageView circle = new ImageView(this);
            circle.setImageResource(R.drawable.ic_swipe_indicator_white_18dp);
            circle.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            circle.setAdjustViewBounds(true);
            circle.setPadding(padding, 0, padding, 0);
            circles.addView(circle);
        }

        setIndicator(0);
    }

    private void setIndicator(int index){
        if(index < NUM_PAGES){
            for(int i = 0 ; i < NUM_PAGES - 1 ; i++){
                ImageView circle = (ImageView) circles.getChildAt(i);
                if(i == index){
                    circle.setColorFilter(getResources().getColor(R.color.yellow));
                }else {
                    circle.setColorFilter(getResources().getColor(android.R.color.transparent));
                }
            }
        }
    }

    private void endTutorial(){
        finish();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            pager.setCurrentItem(pager.getCurrentItem() - 1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            WalkthroughFragment tp = null;
            switch(position){
                case 0:
                    tp = WalkthroughFragment.newInstance(R.layout.tour_fragment1);
                    break;
                case 1:
                    tp = WalkthroughFragment.newInstance(R.layout.tour_fragment2);
                    break;
                case 2:
                    tp = WalkthroughFragment.newInstance(R.layout.tour_fragment3);
                    break;
            }
            return tp;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

 
}