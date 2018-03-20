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
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.ionicons_typeface_library.Ionicons;

import java.util.List;

/**
 * Created by tirgei on 3/3/18.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {
    private Context context;
    private List<HomeModel> cocktails;
    private final OnClickListener listener;

    public class HomeViewHolder extends RecyclerView.ViewHolder{
        private ImageView homeImageView;
        private TextView homeTitle;

        public HomeViewHolder(View itemView) {
            super(itemView);

            homeImageView = itemView.findViewById(R.id.home_cocktail_image);
            homeTitle = itemView.findViewById(R.id.home_title);
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

    public HomeAdapter(Context context, List<HomeModel> cocktails, OnClickListener listener) {
        this.context = context;
        this.cocktails = cocktails;
        this.listener = listener;
    }

    public void addCocktail(HomeModel model){
        this.cocktails.add(model);
        notifyDataSetChanged();
    }

    public void updateList(List<HomeModel> cocktails){
        this.cocktails.clear();
        this.cocktails.addAll(cocktails);
        notifyDataSetChanged();
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);

        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeViewHolder holder, int position) {
        HomeModel cocktail = cocktails.get(position);

        Glide.with(context).load(cocktail.getImageUrl()).apply(new RequestOptions().placeholder(R.drawable.loading)).thumbnail(0.05f).into(holder.homeImageView);
        holder.homeTitle.setText(cocktail.getName());
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
