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
import android.widget.TextView;

import com.igniva.spplitt.R;
import com.igniva.spplitt.model.CityListPojo;
import com.igniva.spplitt.model.StateListPojo;
import com.igniva.spplitt.ui.activties.CityActivity;
import com.igniva.spplitt.ui.activties.CreateAccountActivity;
import com.igniva.spplitt.ui.activties.RecyclerViewFastScroller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by igniva-php-08 on 5/7/16.
 */
public class StateAdapter extends RecyclerView.Adapter<StateAdapter.ViewHolder> implements Filterable,RecyclerViewFastScroller.BubbleTextGetter {
    List<StateListPojo> mListCountries;
    Context mContext;
    private ItemFilter mFilter = new ItemFilter();
    LayoutInflater layoutInflater;
    private List<StateListPojo>filteredData;
    String mCountryId;
    String mCountryName;

    String userName;
    String userPassword;
    String userEmail;
    int userGender;
    String from;
    public StateAdapter(Context context, List<StateListPojo> listCountries, String countryId, String countryName, String userName, String userPassword, String userEmail, int userGender, String from) {
        this.mListCountries = listCountries;
        this.filteredData = listCountries ;
        this.mContext = context;
        this.mCountryId=countryId;
        this.mCountryName=countryName;
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

        holder.mTvCountryName.setText(filteredData.get(position).getState_name());
        holder.mCvCountries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, CityActivity.class);
                intent.putExtra("userName",userName);
                intent.putExtra("userPassword",userPassword);
                intent.putExtra("userEmail",userEmail);
                intent.putExtra("userGender",userGender);
                intent.putExtra("countryId",mCountryId);
                intent.putExtra("countryName",mCountryName);
                intent.putExtra("stateId",filteredData.get(position).getState_id());
                intent.putExtra("stateName",filteredData.get(position).getState_name());
                intent.putExtra("from", from);
//                intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                mContext.startActivity(intent);
//                ((Activity)mContext).finish();
            }
        });
    }

    @Override
    public String getTextToShowInBubble(final int pos) {
        return Character.toString(filteredData.get(pos).getState_name().charAt(0));
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

            final List<StateListPojo> list = mListCountries;

            int count = list.size();
            final List<StateListPojo> nlist = new ArrayList<StateListPojo>(count);

            String filterableString ;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getState_name();
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
            filteredData = (ArrayList<StateListPojo>) results.values;
            notifyDataSetChanged();
        }

    }


}


