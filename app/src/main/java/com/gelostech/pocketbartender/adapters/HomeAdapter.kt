package com.gelostech.pocketbartender.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.gelostech.pocketbartender.R
import com.gelostech.pocketbartender.models.HomeModel
import com.gelostech.pocketbartender.utils.inflate
import com.gelostech.pocketbartender.utils.loadUrl
import kotlinx.android.synthetic.main.item_home.view.*

class HomeAdapter(private val cocktails: MutableList<HomeModel>, private val onClick: (HomeModel) -> Unit) :
        RecyclerView.Adapter<HomeAdapter.HomeViewHolder>(){

    class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(cocktail: HomeModel, onClick: (HomeModel) -> Unit) {
            with(cocktail) {
                itemView.homeCocktailImage.loadUrl(imageUrl)
                itemView.homeCocktailTitle.text = name
                itemView.setOnClickListener { onClick(this) }
            }
        }

    }

    fun replaceWith(newCocktails: MutableList<HomeModel>) {
        if (!cocktails.isEmpty()) cocktails.clear()

        cocktails.addAll(newCocktails)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(parent.inflate(R.layout.item_home))
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(cocktails[position], onClick)
    }

    override fun getItemCount(): Int = cocktails.size

}