package com.gelostech.pocketbartender.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gelostech.pocketbartender.R;
import com.gelostech.pocketbartender.models.HomeModel;
import com.github.captain_miao.optroundcardview.OptRoundCardView;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.ionicons_typeface_library.Ionicons;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by tirgei on 3/4/18.
 */

public class FavesAdapter extends RecyclerView.Adapter<FavesAdapter.FavesViewHolder> {
    private Context context;
    private List<HomeModel> faves;
    private final OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(HomeModel model, int viewID, int position);
    }

    public class FavesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView image;
        private ImageButton faveItem;
        private TextView itemName;
        private WeakReference<OnItemClickListener> listenerRef;
        private OptRoundCardView card;
        private HomeModel model;

        public FavesViewHolder(View itemView, FavesAdapter.OnItemClickListener listener) {
            super(itemView);

            image = itemView.findViewById(R.id.faves_image);
            faveItem = itemView.findViewById(R.id.faves_set_status);
            itemName = itemView.findViewById(R.id.faves_item_name);
            card = itemView.findViewById(R.id.faves_card);
            listenerRef = new WeakReference<>(listener);

            faveItem.setOnClickListener(this);
            card.setOnClickListener(this);
        }

        private void bind(final HomeModel model){
            this.model = model;
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == faveItem.getId())
                listenerRef.get().onItemClick(model, 0, getAdapterPosition());
            else if(v.getId() == card.getId())
                listenerRef.get().onItemClick(model, 1, getAdapterPosition());

        }
    }

    public void removeFave(int pos){
        faves.remove(pos);
        notifyDataSetChanged();
    }

    public FavesAdapter(Context context, List<HomeModel> faves, OnItemClickListener listener){
        this.context = context;
        this.faves = faves;
        this.listener = listener;
    }


    @NonNull
    @Override
    public FavesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_faves, parent, false);

        return new FavesViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull FavesViewHolder holder, int position) {
        HomeModel model = faves.get(position);

        holder.itemName.setText(model.getName());
        Glide.with(context).load(getImage(model.getCocktailThumb())).thumbnail(0.1f).into(holder.image);
        holder.faveItem.setImageDrawable(new IconicsDrawable(context).icon(Ionicons.Icon.ion_ios_heart).color(ContextCompat.getColor(context, R.color.faveItem)).sizeDp(24));
        holder.bind(model);
    }

    @Override
    public int getItemCount() {
        return faves.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
