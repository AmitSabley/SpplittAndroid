package com.igniva.spplitt.ui.activties;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.bumptech.glide.Glide;
import com.igniva.spplitt.App;
import com.igniva.spplitt.R;
import com.igniva.spplitt.controller.ResponseHandlerListener;
import com.igniva.spplitt.controller.WebNotificationManager;
import com.igniva.spplitt.controller.WebServiceClient;
import com.igniva.spplitt.model.DataPojo;
import com.igniva.spplitt.model.ResponsePojo;
import com.igniva.spplitt.model.ReviewListPojo;
import com.igniva.spplitt.ui.adapters.ReviewsListdapter;
import com.igniva.spplitt.utils.PreferenceHandler;
import com.igniva.spplitt.utils.Utility;
import com.igniva.spplitt.utils.Validations;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class OtherProfileActivity extends BaseActivity implements View.OnClickListener {
    private static final String LOG_TAG = "OtherProfileActivity";
    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView mIvProfilePic;
    String otherUserId;

    DataPojo dataPojo;
    Bitmap myBitmap;

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("OtherProfileActivity", "OnResume Called");
        try {
            App.getInstance().trackScreenView(LOG_TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    RecyclerView mRvAds;
    FloatingActionButton mFab;
    FloatingActionButton mFabCross;
    Animation alphaAnimation;
    float pixelDensity;
    boolean flag = true;
    LinearLayout revealView, layoutButtons;
    int i = 0;
    RatingBar mRatingBar;
    EditText mEtReviews;
    List<ReviewListPojo> mReviewList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent in = getIntent();
        otherUserId = in.getStringExtra("other_user_id");

        // Webservice Call
        // Step 1, Register Callback Interface
        WebNotificationManager.registerResponseListener(responseHandlerListenerLogin);
        // Step 2, Call Webservice Method
        WebServiceClient.getOthersProfile(this, otherUserDetailsPayload(), true, 1, responseHandlerListenerLogin);
//
        setUpLayouts();
    }

    private void setPalette() {

        try {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    try {
                        myBitmap = Glide.
                                with(OtherProfileActivity.this).
                                load(WebServiceClient.BASE_URL + dataPojo.getOther_picture()).
                                asBitmap().
                                into(100, 100). // Width and height
                                get();
                        Palette.from(myBitmap).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                int primaryDark = getResources().getColor(R.color.colorPrimaryDark);
                                int primary = getResources().getColor(R.color.colorPrimary);
                                collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
                                collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkVibrantColor(primaryDark));
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            new Thread(runnable).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUpLayouts() {
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFabCross = (FloatingActionButton) findViewById(R.id.fab_cross);
      /*  if (PreferenceHandler.readString(OtherProfileActivity.this, PreferenceHandler.USER_ID, "").equals(otherUserId)) {
            mFab.setVisibility(View.GONE);
        } else {
            mFab.setVisibility(View.VISIBLE);
        }*/
        mRvAds = (RecyclerView) findViewById(R.id.rv_ads_applied);
        mRvAds.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int scrollDy = 0;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollDy += dy;
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (scrollDy == 0 && (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE)) {
                    AppBarLayout appBarLayout = ((AppBarLayout) findViewById(R.id.app_bar_layout));
                    appBarLayout.setExpanded(true);
                }
            }
        });
        mIvProfilePic = (ImageView) findViewById(R.id.iv_other_profile);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        //if dont want to show title on expanded toolbar
//        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));


        pixelDensity = getResources().getDisplayMetrics().density;
        alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_anim);
        revealView = (LinearLayout) findViewById(R.id.linearView);
        layoutButtons = (LinearLayout) findViewById(R.id.layoutButtons);
        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        mEtReviews = (EditText) findViewById(R.id.et_review);
    }

    @Override
    public void setDataInViewLayouts() {
        try {
            Glide.with(this)
                    .load(WebServiceClient.BASE_URL + dataPojo.getOther_picture()).asBitmap()
                    .into(mIvProfilePic);

            collapsingToolbarLayout.setTitle(dataPojo.getOther_username());
            dataPojo.getOther_review();
            setPalette();
        } catch (Exception e) {
        }
    }

    public void reviewsAndRating(View v) {
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
        /*
         MARGIN_RIGHT = 16;
         FAB_BUTTON_RADIUS = 28;
         */
            int x = mIvProfilePic.getRight();
            int y = mIvProfilePic.getBottom();
            x -= ((28 * pixelDensity) + (16 * pixelDensity));

            int hypotenuse = (int) Math.hypot(mIvProfilePic.getWidth(), mIvProfilePic.getHeight());

            if (flag) {
                mFabCross.setVisibility(View.VISIBLE);
                mFab.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
//            mFab.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mFab.setImageResource(R.mipmap.check);

                FrameLayout.LayoutParams parameters = (FrameLayout.LayoutParams)
                        revealView.getLayoutParams();
                parameters.height = mIvProfilePic.getHeight();
                revealView.setLayoutParams(parameters);

                Animator anim = ViewAnimationUtils.createCircularReveal(revealView, x, y, 0, hypotenuse);
                anim.setDuration(700);

                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        layoutButtons.setVisibility(View.VISIBLE);
                        layoutButtons.startAnimation(alphaAnimation);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });

                revealView.setVisibility(View.VISIBLE);
                anim.start();

                flag = false;
            } else {

                addReviews();
            }
        } else {
            if (flag) {
                mFabCross.setVisibility(View.VISIBLE);
                mFab.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
//            mFab.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mFab.setImageResource(R.mipmap.check);

                FrameLayout.LayoutParams parameters = (FrameLayout.LayoutParams) revealView.getLayoutParams();
                parameters.height = mIvProfilePic.getHeight();
                revealView.setLayoutParams(parameters);
                layoutButtons.setVisibility(View.VISIBLE);
                layoutButtons.startAnimation(alphaAnimation);
                revealView.setVisibility(View.VISIBLE);
                flag = false;
            } else {

                addReviews();
            }

        }
        Utility.hideKeyboard(this, v);
    }

    private void addReviews() {
        boolean val = new Validations().isValidateReviews(getApplicationContext(), mRatingBar, mEtReviews);
        if (val) {
            // Webservice Call
            // Step 1, Register Callback Interface
            WebNotificationManager.registerResponseListener(responseHandlerListenerLogin);
            // Step 2, Call Webservice Method
            WebServiceClient.addRating(this, createReviewsPayload(), true, 2, responseHandlerListenerLogin);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_btn_back:
                App.getInstance().trackEvent(LOG_TAG, "Back Press", "Ad Details Back Pressed");
                onBackPressed();
                break;
            case R.id.ll_ads_applied:
                App.getInstance().trackEvent(LOG_TAG, "Applied Ads Called", "Applied Ads Fragment");
                Intent intent = new Intent(this, AdsAppliedListActivity.class);
                intent.putExtra("other_user_id", otherUserId);
                intent.putExtra("ads_type", getResources().getString(R.string.applied_ads));
                startActivity(intent);
                break;
            case R.id.ll_ads_posted:
                App.getInstance().trackEvent(LOG_TAG, "Posted Ads Called", "Posted Ads Fragment");
                Intent intentPosted = new Intent(this, AdsAppliedListActivity.class);
                intentPosted.putExtra("other_user_id", otherUserId);
                intentPosted.putExtra("ads_type", getResources().getString(R.string.posted_ads));
                startActivity(intentPosted);
                break;
            case R.id.fab:
                App.getInstance().trackEvent(LOG_TAG, "Review and Rate User", "Review And Rate User Dialog");
                /*reviewsAndRating(v);*/
                break;
            case R.id.fab_cross:
                App.getInstance().trackEvent(LOG_TAG, "Close Rate User", "Close Rate User Dialog");
                closeRatingSection(v);
                break;
        }
    }

    private void closeRatingSection(View v) {
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
        /*
         MARGIN_RIGHT = 16;
         FAB_BUTTON_RADIUS = 28;
         */
            int x = mIvProfilePic.getRight();
            int y = mIvProfilePic.getBottom();
            x -= ((28 * pixelDensity) + (16 * pixelDensity));

            int hypotenuse = (int) Math.hypot(mIvProfilePic.getWidth(), mIvProfilePic.getHeight());

            mFab.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimaryDark));
            mFab.setImageResource(R.mipmap.star);

            Animator anim = ViewAnimationUtils.createCircularReveal(revealView, x, y, hypotenuse, 0);
            anim.setDuration(400);

            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    revealView.setVisibility(View.GONE);
                    layoutButtons.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            anim.start();


        } else {
            mFab.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimaryDark));
            mFab.setImageResource(R.mipmap.star);
            revealView.setVisibility(View.GONE);
            layoutButtons.setVisibility(View.GONE);
        }
        flag = true;
        mFabCross.setVisibility(View.GONE);
        Utility.hideKeyboard(this, v);
    }

    private String otherUserDetailsPayload() {
        String payload = null;
        try {
            JSONObject userData = new JSONObject();
            try {
                userData.put("user_id", PreferenceHandler.readString(OtherProfileActivity.this, PreferenceHandler.USER_ID, ""));
                userData.put("auth_token", PreferenceHandler.readString(OtherProfileActivity.this, PreferenceHandler.AUTH_TOKEN, ""));
                userData.put("other_user_id", otherUserId);
                if (PreferenceHandler.readString(OtherProfileActivity.this, PreferenceHandler.USER_ID, "").equals(otherUserId)) {
                    userData.put("profile_type", "my");
                } else {
                    userData.put("profile_type", "other");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            payload = userData.toString();
        } catch (Exception e) {
            payload = null;
        }
        return payload;
    }

    private String createReviewsPayload() {
        String payload = null;
        try {
            JSONObject userData = new JSONObject();
            try {
                userData.put("user_id", PreferenceHandler.readString(OtherProfileActivity.this, PreferenceHandler.USER_ID, ""));
                userData.put("auth_token", PreferenceHandler.readString(OtherProfileActivity.this, PreferenceHandler.AUTH_TOKEN, ""));
                userData.put("rating_value", mRatingBar.getRating());
                userData.put("other_user_id", otherUserId);
                userData.put("review", mEtReviews.getText().toString().trim());
            } catch (Exception e) {
                e.printStackTrace();
            }
            android.util.Log.e("rseponse", "" + userData);
            payload = userData.toString();
        } catch (Exception e) {
            payload = null;
        }
        return payload;
    }

    ResponseHandlerListener responseHandlerListenerLogin = new ResponseHandlerListener() {
        @Override
        public void onComplete(ResponsePojo result, WebServiceClient.WebError error, ProgressDialog mProgressDialog, int mUrlNo) {
            WebNotificationManager.unRegisterResponseListener(responseHandlerListenerLogin);
            if (error == null) {
                switch (mUrlNo) {
                    case 1:
                        getOthersProfileData(result);
                        break;
                    case 2:
                        getReviewsData(result);
                        break;
                }
            } else {
                // TODO display error dialog
                new Utility().showErrorDialogRequestFailed(OtherProfileActivity.this);
            }
            if (mProgressDialog != null && mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        }
    };

    private void getReviewsData(ResponsePojo result) {
        try {
            if (result.getStatus_code() == 400) {
//                Error
                new Utility().showErrorDialog(this, result);
            } else {
                //Success
                dataPojo = result.getData();
                Utility.showToastMessageShort(getApplicationContext(), dataPojo.getRating());
                ReviewListPojo mReviewListPojo = new ReviewListPojo();
                mReviewListPojo.setUser_id(PreferenceHandler.readString(OtherProfileActivity.this, PreferenceHandler.USER_ID, ""));
                mReviewListPojo.setUser_name(PreferenceHandler.readString(OtherProfileActivity.this, PreferenceHandler.USER_NAME, ""));
                mReviewListPojo.setUser_image(PreferenceHandler.readString(OtherProfileActivity.this, PreferenceHandler.PICTURE, ""));
                mReviewListPojo.setUser_review(mEtReviews.getText().toString().trim());
                mReviewListPojo.setUser_rating_value(Math.round(mRatingBar.getRating()) + "");
                mReviewList.add(mReviewListPojo);
                dataPojo.setOther_review(mReviewList);


                mRatingBar.setRating(0);
                mEtReviews.setText("");

                if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
        /*
         MARGIN_RIGHT = 16;
         FAB_BUTTON_RADIUS = 28;
         */
                    int x = mIvProfilePic.getRight();
                    int y = mIvProfilePic.getBottom();
                    x -= ((28 * pixelDensity) + (16 * pixelDensity));

                    int hypotenuse = (int) Math.hypot(mIvProfilePic.getWidth(), mIvProfilePic.getHeight());
                    mFabCross.setVisibility(View.GONE);
                    mFab.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimaryDark));
                    mFab.setImageResource(R.mipmap.star);

                    Animator anim = ViewAnimationUtils.createCircularReveal(revealView, x, y, hypotenuse, 0);
                    anim.setDuration(400);

                    anim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            revealView.setVisibility(View.GONE);
                            layoutButtons.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });

                    anim.start();
                    flag = true;
                } else {
                    mFabCross.setVisibility(View.GONE);
                    mFab.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimaryDark));
                    mFab.setImageResource(R.mipmap.star);
                    mFab.setImageResource(R.mipmap.star);
                    revealView.setVisibility(View.GONE);
                    layoutButtons.setVisibility(View.GONE);
                    flag = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getOthersProfileData(ResponsePojo result) {
        try {
            if (result.getStatus_code() == 400) {
//                Error
                new Utility().showErrorDialog(this, result);
            } else {
                //Success
                dataPojo = result.getData();

                setDataInViewLayouts();
                mReviewList = dataPojo.getOther_review();
//                if(mReviewList.size()>0) {
                ReviewsListdapter mAdsListAdapter = new ReviewsListdapter(OtherProfileActivity.this, mReviewList, dataPojo);
                mRvAds.setAdapter(mAdsListAdapter);
                mAdsListAdapter.notifyDataSetChanged();
                mRvAds.setHasFixedSize(true);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(OtherProfileActivity.this);
                mRvAds.setLayoutManager(mLayoutManager);
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

