package com.igniva.spplitt.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.igniva.spplitt.R;
import com.igniva.spplitt.controller.WebServiceClient;
import com.igniva.spplitt.model.CountriesListPojo;
import com.igniva.spplitt.ui.activties.CityActivity;
import com.igniva.spplitt.ui.activties.RecyclerViewFastScroller;
import com.igniva.spplitt.ui.activties.StateActivity;
import com.igniva.spplitt.utils.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by igniva-php-08 on 4/7/16.
 */
public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> implements Filterable,RecyclerViewFastScroller.BubbleTextGetter {
    List<CountriesListPojo> mListCountries;
    Context mContext;
    private ItemFilter mFilter = new ItemFilter();
    LayoutInflater layoutInflater;
    private List<CountriesListPojo>filteredData;
    String userName;
    String userPassword;
    String userEmail;
    int userGender;
    String from;
    public CountryAdapter(Context context, List<CountriesListPojo> listCountries, String userName, String userPassword, String userEmail, int userGender, String from) {
        this.mListCountries = listCountries;
        this.filteredData = listCountries ;
        this.mContext = context;
        this.userName=userName;
        this.userPassword=userPassword;
        this.userEmail=userEmail;
        this.userGender=userGender;
        this.from=from;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_countries, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.mTvCountryName.setText(filteredData.get(position).getCountry_name());
        Glide.with(mContext)
                .load(WebServiceClient.BASE_URL + filteredData.get(position).getCountry_flag())
                .into(holder.mIvCountryImage);
        holder.mCvCountries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(mContext,StateActivity.class);
                in.putExtra("userName",userName);
                in.putExtra("userPassword",userPassword);
                in.putExtra("userEmail",userEmail);
                in.putExtra("userGender",userGender);
                in.putExtra("country_id",filteredData.get(position).getCountry_id());
                in.putExtra("country_name",filteredData.get(position).getCountry_name());
                in.putExtra("from", from);
//                in.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                mContext.startActivity(in);
//                ((Activity)mContext).finish();
            }
        });
    }

    @Override
    public String getTextToShowInBubble(final int pos) {
        return Character.toString(filteredData.get(pos).getCountry_name().charAt(0));
    }

    @Override
    public int getItemCount() {
        return filteredData.size();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvCountryName;
        ImageView mIvCountryImage;
        CardView mCvCountries;
        private ViewHolder(View itemView) {
            super(itemView);
            mTvCountryName = (TextView) itemView.findViewById(R.id.tv_country_name);
            mIvCountryImage = (ImageView) itemView.findViewById(R.id.iv_countries);
            mCvCountries=(CardView)itemView.findViewById(R.id.cv_category_main);
        }

    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<CountriesListPojo> list = mListCountries;

            int count = list.size();
            final List<CountriesListPojo> nlist = new ArrayList<CountriesListPojo>(count);

            String filterableString ;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getCountry_name();
                if (filterableString.toLowerCase().startsWith(filterString)) {
                    nlist.add(list.get(i));
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<CountriesListPojo>) results.values;
            notifyDataSetChanged();
        }

    }


}

