package com.igniva.spplitt.ui.activties;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.igniva.spplitt.App;
import com.igniva.spplitt.R;
import com.igniva.spplitt.ui.fragments.AboutSpplittFragment;
import com.igniva.spplitt.ui.fragments.AppliedAdsFragment;
import com.igniva.spplitt.ui.fragments.ChangePasswordFragment;
import com.igniva.spplitt.ui.fragments.ContactUsFragment;
import com.igniva.spplitt.ui.fragments.EditProfileFragment;
import com.igniva.spplitt.ui.fragments.ManageMyAdsFragment;
import com.igniva.spplitt.ui.fragments.NotificationsFragment;
import com.igniva.spplitt.ui.fragments.PostAdFragment;
import com.igniva.spplitt.ui.fragments.PrivacyPolicy;
import com.igniva.spplitt.ui.fragments.SetMyPreferencesFragment;
import com.igniva.spplitt.ui.fragments.ViewCategoriesFragment1;
import com.igniva.spplitt.ui.fragments.ViewAllAdsFragment;
import com.igniva.spplitt.utils.Constants;
import com.igniva.spplitt.utils.PreferenceHandler;
import com.igniva.spplitt.utils.Utility;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String LOG_TAG = "MainActivity";

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("MainActivity", "OnResume Called");
        try {
            App.getInstance().trackScreenView(LOG_TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    FragmentTransaction fragmentTransaction;
    Bundle mBundle;
    TextView mToolbarTvText;
    boolean doubleBackToExitPressedOnce = false;
    DrawerLayout drawer;
    public static MainActivity main;
//    boolean mToolbarOptions=false;

    public static int currentFragmentId = Constants.CURRENT_FRAG_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main = this;
        //initialize shared preferneces

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        toolbar.setTitle();
        mToolbarTvText = (TextView) findViewById(R.id.toolbar_tv_text);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                Utility.hideKeyboard(getApplicationContext(), drawer);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                Utility.hideKeyboard(getApplicationContext(), drawer);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Utility.hideKeyboard(getApplicationContext(), drawer);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                Utility.hideKeyboard(getApplicationContext(), drawer);
            }
        });
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        //set Navigation drawer text Color
        int positionOfMenuItem = navigationView.getMenu().size() - 1;
        MenuItem item = navigationView.getMenu().getItem(positionOfMenuItem);
        SpannableString s = new SpannableString(getResources().getString(R.string.nav_logout));
        s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark)), 0, s.length(), 0);
        item.setTitle(s);
        currentFragmentId = Constants.CURRENT_FRAG_ID;

        if (PreferenceHandler.readInteger(this, PreferenceHandler.SHOW_EDIT_PROFILE, 0) == 0) {
            onNavigationItemSelected(navigationView.getMenu().getItem(1));
        } else if (PreferenceHandler.readInteger(this, PreferenceHandler.SHOW_EDIT_PROFILE, 0) == 2) {
            onNavigationItemSelected(navigationView.getMenu().getItem(8));
        } else if (PreferenceHandler.readInteger(this, PreferenceHandler.SHOW_EDIT_PROFILE, 0) == 3) {
            onNavigationItemSelected(navigationView.getMenu().getItem(4));
        } else if (PreferenceHandler.readInteger(this, PreferenceHandler.SHOW_EDIT_PROFILE, 0) == 5) {
            onNavigationItemSelected(navigationView.getMenu().getItem(3));
        } else {
            onNavigationItemSelected(navigationView.getMenu().getItem(0));
        }
    }

    public void addValue(Bundle mBundle) {
        this.mBundle = mBundle;
    }

    @SuppressWarnings("StatementWithEmptyBody")

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            int id = item.getItemId();
//            mToolbarOptions=false;
            if (id == R.id.nav_view_ad) {
                if (currentFragmentId != Constants.FRAG_ID_CATEGORIES) {
                    App.getInstance().trackEvent(LOG_TAG, "Categories Fragment Called", "Categories Fragment");
                    mToolbarTvText.setText(getResources().getString(R.string.nav_view_ad));
//                    mToolbarOptions = true;
                    fragmentTransaction.replace(R.id.fl_main_content, ViewCategoriesFragment1.newInstance());
                }
            } else if (id == R.id.nav_profile) {
                if (currentFragmentId != Constants.FRAG_ID_MY_PROFILE) {
                    if (drawer == null)
                        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    //
                    fragmentTransaction.replace(R.id.fl_main_content, EditProfileFragment.newInstance());
                    fragmentTransaction.commitAllowingStateLoss();
                    mToolbarTvText.setText(getResources().getString(R.string.nav_profile));
                    App.getInstance().trackEvent(LOG_TAG, "Edit Profile Fragment Called", "Edit Profile Fragment");
                    return true;
                }
            } else if (id == R.id.nav_change_password) {
                if (currentFragmentId != Constants.FRAG_ID_CHANGE_PASSWORD) {
                    App.getInstance().trackEvent(LOG_TAG, "Change Password Fragment Called", "Change Password Fragment");

                    mToolbarTvText.setText(getResources().getString(R.string.nav_change_password));
                    fragmentTransaction.replace(R.id.fl_main_content, ChangePasswordFragment.newInstance());
                }
            } else if (id == R.id.nav_notification) {

                if (currentFragmentId != Constants.FRAG_ID_NOTIFICATIONS) {
                    App.getInstance().trackEvent(LOG_TAG, "Notifications Fragment Called", "Notifications Fragment");

                    mToolbarTvText.setText(getResources().getString(R.string.nav_notifications));
                    fragmentTransaction.replace(R.id.fl_main_content, NotificationsFragment.newInstance());
                }
            } else if (id == R.id.nav_post_ad) {
                if (currentFragmentId != Constants.FRAG_ID_POST_ADS) {
                    App.getInstance().trackEvent(LOG_TAG, "Post Ad Fragment Called", "Post Ads Fragment");

                    mToolbarTvText.setText(getResources().getString(R.string.nav_post_ad));
                    Fragment mFragment = PostAdFragment.newInstance();
                    if (mBundle != null) {
                        mFragment.setArguments(mBundle);
                    }
                    fragmentTransaction.replace(R.id.fl_main_content, mFragment);
                }
            } else if (id == R.id.nav_manage_my_ad) {
                if (currentFragmentId != Constants.FRAG_ID_MANAGE_MY_ADS) {
                    App.getInstance().trackEvent(LOG_TAG, "My Ads Fragment Called", "My Ads Fragment");

                    mToolbarTvText.setText(getResources().getString(R.string.nav_manage_my_ad));
                    fragmentTransaction.replace(R.id.fl_main_content, ManageMyAdsFragment.newInstance());
                }
            } else if (id == R.id.nav_event_ad) {
                if (currentFragmentId != Constants.FRAG_ID_VIEW_EVENT) {
                    App.getInstance().trackEvent(LOG_TAG, "View Event Ads Fragment Called", "View Event Ads Fragment");

                    mToolbarTvText.setText(getResources().getString(R.string.nav_event_ad));
                    fragmentTransaction.replace(R.id.fl_main_content, ViewAllAdsFragment.newInstance());
//                    fragmentTransaction.replace(R.id.fl_main_content, ViewEventAdsFragment.newInstance());
                }
            } else if (id == R.id.nav_requested_ad) {
                if (currentFragmentId != Constants.FRAG_ID_APPLIED_ADS) {
                    App.getInstance().trackEvent(LOG_TAG, "Applied Ads Fragment Called", "Applied Ads Fragment");

                    mToolbarTvText.setText(getResources().getString(R.string.nav_requested_ad));
                    fragmentTransaction.replace(R.id.fl_main_content, AppliedAdsFragment.newInstance());
                }
            } else if (id == R.id.nav_set_preferences) {
                if (currentFragmentId != Constants.FRAG_ID_SET_PREFERENCES) {
                    App.getInstance().trackEvent(LOG_TAG, "Set Preferences Fragment Called", "Set Preferences Fragment");

                    mToolbarTvText.setText(getResources().getString(R.string.nav_set_preferences));
                    fragmentTransaction.replace(R.id.fl_main_content, SetMyPreferencesFragment.newInstance());
                }
            } else if (id == R.id.nav_invite_friends) {
                if (currentFragmentId != Constants.FRAG_ID_INVITE_FRIENDS) {
                    App.getInstance().trackEvent(LOG_TAG, "Invite Friends Fragment Called", "Invite Friends Fragment");

//                    currentFragmentId=Constants.FRAG_ID_INVITE_FRIENDS;
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.invite_friends_header));
                    sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.invite_friends_main));
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }
            } else if (id == R.id.nav_about_split) {
                if (currentFragmentId != Constants.FRAG_ID_ABOUT_US) {
                    App.getInstance().trackEvent(LOG_TAG, "About Fragment Called", "About Fragment");
                    mToolbarTvText.setText(getResources().getString(R.string.nav_about_split));
                    fragmentTransaction.replace(R.id.fl_main_content, AboutSpplittFragment.newInstance());
                }
            } else if (id == R.id.nav_contact_us) {
                if (currentFragmentId != Constants.FRAG_ID_CONTACT_US) {
                    App.getInstance().trackEvent(LOG_TAG, "Contact Us Fragment Called", "Contact Us Fragment");

                    mToolbarTvText.setText(getResources().getString(R.string.nav_contact_us));
                    fragmentTransaction.replace(R.id.fl_main_content, ContactUsFragment.newInstance());
                }
            } else if (id == R.id.nav_privacy_policy) {
                if (currentFragmentId != Constants.FRAG_ID_PRIVACY_POLICY) {
                    App.getInstance().trackEvent(LOG_TAG, "Privacy Policy Fragment Called", "Privacy Policy Fragment");

                    mToolbarTvText.setText(getResources().getString(R.string.nav_privacy_policy));
                    fragmentTransaction.replace(R.id.fl_main_content, PrivacyPolicy.newInstance());
                }
            } else if (id == R.id.nav_logout) {
                App.getInstance().trackEvent(LOG_TAG, "LogOut Called", "LogOut");

                currentFragmentId = Constants.FRAG_ID_LOGOUT;
                showLogoutDialog();
            }
            fragmentTransaction.commitAllowingStateLoss();
//            invalidateOptionsMenu();

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            Utility.hideKeyboard(getApplicationContext(), drawer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private void showLogoutDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,
                    R.style.CustomPopUpTheme);
            builder.setTitle(getApplicationContext().getResources().getString(R.string.logout_title));
            builder.setMessage(getApplicationContext().getResources().getString(R.string.logout_message));
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Utility.clearSharedPreferneces(MainActivity.this);
                    startActivity(new Intent(getApplicationContext(), LoginOptionActivity.class));
                    dialog.dismiss();
                    finish();

                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
            Utility.showToastMessageLong(getApplicationContext(),
                    getApplicationContext().getResources().getString(R.string.logout));
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (doubleBackToExitPressedOnce) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            main.finish();
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tap again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    @Override
    public void setUpLayouts() {

    }

    @Override
    public void setDataInViewLayouts() {

    }

    @Override
    public void onClick(View v) {

    }

}
