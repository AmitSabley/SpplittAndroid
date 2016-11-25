package com.igniva.spplitt.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.igniva.spplitt.R;
import com.igniva.spplitt.controller.WebServiceClient;
import com.igniva.spplitt.model.CategoriesListPojo;
import com.igniva.spplitt.ui.activties.ViewAdsActivity;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by igniva-php-08 on 20/5/16.
 */
public class CategoryListAdapter  extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {

    List<CategoriesListPojo> mListCategories;
    Context mContext;

    public CategoryListAdapter(Context context, List<CategoriesListPojo> listCategories) {
        this.mListCategories=listCategories;
        this.mContext = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvCategoryName;
        ImageView mIvCategoryImage;
        ImageView mIvCategoryImage1;
        CardView mCvMain;
        TextView mTvCategoryName1;
        public ViewHolder(View itemView) {
            super(itemView);
            mTvCategoryName=(TextView)itemView.findViewById(R.id.tv_category_name);
            mIvCategoryImage=(ImageView)itemView.findViewById(R.id.iv_category_img);
            mCvMain=(CardView)itemView.findViewById(R.id.cv_category_main);
            mTvCategoryName1=(TextView)itemView.findViewById(R.id.tv_category_name1);
            mIvCategoryImage1=(ImageView)itemView.findViewById(R.id.iv_category_img1);
        }
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_category, parent, false);
        ViewHolder vhItem = new ViewHolder(v);
        return vhItem;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        try {
            holder.mTvCategoryName.setText(mListCategories.get(position).getCategory_title());
            holder.mTvCategoryName1.setText(mListCategories.get(position).getCategory_title());
            Glide.with(mContext)
                    .load(WebServiceClient.BASE_URL+mListCategories.get(position).getCategory_icon()).asBitmap()
                    .into(holder.mIvCategoryImage1);

            holder.mCvMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in=new Intent(mContext,ViewAdsActivity.class);
                    in.putExtra("cat_id",mListCategories.get(position).getCategory_id());
                    mContext.startActivity(in);
                }
            });
        }catch (Exception e){
            e.printStackTrace();

        }
    }

    @Override
    public int getItemCount() {//return array.size
        return mListCategories.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}

