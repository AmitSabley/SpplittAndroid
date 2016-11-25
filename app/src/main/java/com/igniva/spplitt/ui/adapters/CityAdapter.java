package com.igniva.spplitt.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.igniva.spplitt.R;
import com.igniva.spplitt.controller.WebServiceClient;
import com.igniva.spplitt.model.CityListPojo;
import com.igniva.spplitt.model.CountriesListPojo;
import com.igniva.spplitt.ui.activties.CityActivity;
import com.igniva.spplitt.ui.activties.CreateAccountActivity;
import com.igniva.spplitt.ui.activties.MainActivity;
import com.igniva.spplitt.ui.activties.RecyclerViewFastScroller;
import com.igniva.spplitt.utils.PreferenceHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by igniva-php-08 on 4/7/16.
 */
public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> implements Filterable,RecyclerViewFastScroller.BubbleTextGetter {
    List<CityListPojo> mListCountries;
    Context mContext;
    private ItemFilter mFilter = new ItemFilter();
    LayoutInflater layoutInflater;
    private List<CityListPojo>filteredData;
    String mCountryId;
    String mCountryName;
    String mStateId;
    String mStateName;

    String userName;
    String userPassword;
    String userEmail;
    int userGender;
    String from;
    public CityAdapter(Context context, List<CityListPojo> listCountries, String countryId, String countryName, String stateId, String stateName, String userName, String userPassword, String userEmail, int userGender, String from) {
        this.mListCountries = listCountries;
        this.filteredData = listCountries ;
        this.mContext = context;
        this.mCountryId=countryId;
        this.mCountryName=countryName;
        this.mStateId=stateId;
        this.mStateName=stateName;
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

        holder.mTvCountryName.setText(filteredData.get(position).getCity_name());
        holder.mCvCountries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(mContext,filteredData.get(position).getCity_id(),Toast.LENGTH_SHORT).show();
                Intent intent = null;
                if(from.equals("1")){//create account
                    PreferenceHandler.writeInteger(mContext, PreferenceHandler.SHOW_EDIT_PROFILE, 10);
                    intent=new Intent(mContext,CreateAccountActivity.class);
                }else if(from.equals("2")){//EditProfile
                    PreferenceHandler.writeInteger(mContext, PreferenceHandler.SHOW_EDIT_PROFILE, 0);
                    intent=new Intent(mContext,MainActivity.class);
                }else if(from.equals("3")){//SEt Preferences
                    PreferenceHandler.writeInteger(mContext, PreferenceHandler.SHOW_EDIT_PROFILE, 2);
                    intent=new Intent(mContext,MainActivity.class);
                }
                else if(from.equals("4")){//Post AD
                    PreferenceHandler.writeInteger(mContext, PreferenceHandler.SHOW_EDIT_PROFILE, 3);
                    intent=new Intent(mContext,MainActivity.class);
                }else if(from.equals("5")){//Dashboard
                    PreferenceHandler.writeInteger(mContext, PreferenceHandler.SHOW_EDIT_PROFILE, 6);
                    intent=new Intent(mContext,MainActivity.class);
                }

                intent.putExtra("userName",userName);
                intent.putExtra("userPassword",userPassword);
                intent.putExtra("userEmail",userEmail);
                intent.putExtra("userGender",userGender);
                intent.putExtra("countryId",mCountryId);
                intent.putExtra("countryName",mCountryName);
                intent.putExtra("stateId",mStateId);
                intent.putExtra("stateName",mStateName);
                intent.putExtra("cityId",filteredData.get(position).getCity_id());
                intent.putExtra("cityName",filteredData.get(position).getCity_name());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mContext.startActivity(intent);
                ((Activity)mContext).finish();
            }
        });
    }

    @Override
    public String getTextToShowInBubble(final int pos) {
        return Character.toString(filteredData.get(pos).getCity_name().charAt(0));
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

            final List<CityListPojo> list = mListCountries;

            int count = list.size();
            final List<CityListPojo> nlist = new ArrayList<CityListPojo>(count);

            String filterableString ;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getCity_name();
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
            filteredData = (ArrayList<CityListPojo>) results.values;
            notifyDataSetChanged();
        }

    }


}


