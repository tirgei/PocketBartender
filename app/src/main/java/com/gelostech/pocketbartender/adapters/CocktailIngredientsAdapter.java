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
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.ionicons_typeface_library.Ionicons;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tirgei on 3/9/18.
 */

public class CocktailIngredientsAdapter extends RecyclerView.Adapter<CocktailIngredientsAdapter.Holder> {
    private List<String> ingredients = new ArrayList<>();
    private Context context;

    public class Holder extends RecyclerView.ViewHolder{
        private TextView icon;
        private TextView title;

        public Holder(View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.ingredients_icon);
            title = itemView.findViewById(R.id.ingredients_title);

            final Drawable i = new IconicsDrawable(context).icon(Ionicons.Icon.ion_ios_checkmark_outline).color(Color.parseColor("#b3b3b3")).sizeDp(18);
            icon.setCompoundDrawablesWithIntrinsicBounds(i, null, null, null);
        }
    }

    public CocktailIngredientsAdapter(Context context){
        this.context = context;
    }

    public void addIngredient(String i){
        ingredients.add(i);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredients, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        String ingredient = ingredients.get(position);
        holder.title.setText(ingredient);

    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}

