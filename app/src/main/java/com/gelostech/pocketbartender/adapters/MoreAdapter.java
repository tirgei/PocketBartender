package com.gelostech.pocketbartender.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gelostech.pocketbartender.R;
import com.gelostech.pocketbartender.activities.CocktailActivity;
import com.gelostech.pocketbartender.models.HomeModel;

import java.util.List;

/**
 * Created by tirgei on 3/3/18.
 */

public class MoreAdapter extends RecyclerView.Adapter<MoreAdapter.MoreViewHolder> {
    private Context context;
    private List<HomeModel> cocktails;
    private final OnClickListener listener;

    public class MoreViewHolder extends RecyclerView.ViewHolder{
        private ImageView moreImageView;
        private TextView moreTitle;

        public MoreViewHolder(View itemView) {
            super(itemView);

            moreImageView = itemView.findViewById(R.id.more_cocktail_image);
            moreTitle = itemView.findViewById(R.id.more_title);
        }

        public void bind(final HomeModel model, final OnClickListener listener){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(model);
                }
            });
        }
    }

    public interface OnClickListener{
        void onClick(HomeModel model);
    }

    public MoreAdapter(Context context, List<HomeModel> cocktails, OnClickListener listener) {
        this.context = context;
        this.cocktails = cocktails;
        this.listener = listener;
    }

    public void updateList(List<HomeModel> cocktails){
        this.cocktails.clear();
        this.cocktails.addAll(cocktails);
        notifyDataSetChanged();
    }

    @Override
    public MoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_more_suggestion, parent, false);

        return new MoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoreViewHolder holder, int position) {
        HomeModel cocktail = cocktails.get(position);

        Glide.with(context).setDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.loading)).load(cocktail.getImageUrl()).thumbnail(0.05f).into(holder.moreImageView);
        holder.moreTitle.setText(cocktail.getName());
        holder.bind(cocktail, listener);

    }

    @Override
    public int getItemCount() {
        return cocktails.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
