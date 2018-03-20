package com.gelostech.pocketbartender.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gelostech.pocketbartender.R;
import com.gelostech.pocketbartender.models.HomeModel;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.ionicons_typeface_library.Ionicons;

import java.util.List;

/**
 * Created by tirgei on 3/4/18.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private Context context;
    private List<HomeModel> historyItems;

    public class SearchViewHolder extends RecyclerView.ViewHolder{
        private TextView historyIcon;
        private TextView historyTitle;

        public SearchViewHolder(View itemView) {
            super(itemView);

            historyIcon = itemView.findViewById(R.id.history_icon);
            historyTitle = itemView.findViewById(R.id.history_title);

            final Drawable icon = new IconicsDrawable(context).icon(Ionicons.Icon.ion_ios_refresh_empty).color(Color.parseColor("#b3b3b3")).sizeDp(20);
            historyIcon.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
        }
    }

    public SearchAdapter(Context context, List<HomeModel> items){
        this.context = context;
        this.historyItems = items;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_history, parent, false);

        return new SearchViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        HomeModel model = historyItems.get(position);

        holder.historyTitle.setText(model.getName());

    }

    @Override
    public int getItemCount() {
        return historyItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
