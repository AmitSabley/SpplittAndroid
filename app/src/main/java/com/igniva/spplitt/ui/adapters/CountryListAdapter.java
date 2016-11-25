package com.igniva.spplitt.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.igniva.spplitt.R;
import com.igniva.spplitt.controller.WebServiceClient;
import com.igniva.spplitt.model.CategoriesListPojo;
import com.igniva.spplitt.model.CountriesListPojo;
import com.igniva.spplitt.ui.activties.ViewAdsActivity;
import com.igniva.spplitt.utils.PreferenceHandler;

import java.util.List;

/**
 * Created by igniva-php-08 on 26/5/16.
 */
public class CountryListAdapter extends BaseAdapter {

    List<CountriesListPojo> mListCountries;
    Context mContext;
    TextView mTvCountryName;
    ImageView mIvCountryImage;
    LinearLayout mLlMain;
    LayoutInflater layoutInflater;

    public CountryListAdapter(Context context, List<CountriesListPojo> listCountries) {
        this.mListCountries = listCountries;
        this.mContext = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mListCountries.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View itemView, ViewGroup viewGroup) {
        if(itemView==null) {
            itemView = layoutInflater.inflate(R.layout.adapter_countries, null);
        }
        mTvCountryName = (TextView) itemView.findViewById(R.id.tv_country_name);
        mIvCountryImage = (ImageView) itemView.findViewById(R.id.iv_countries);
        mLlMain = (LinearLayout) itemView.findViewById(R.id.ll_main);
        mTvCountryName.setText(mListCountries.get(i).getCountry_name());
        Glide.with(mContext)
                .load(WebServiceClient.BASE_URL + mListCountries.get(i).getCountry_flag())
                .into(mIvCountryImage);
        return itemView;
    }


}

