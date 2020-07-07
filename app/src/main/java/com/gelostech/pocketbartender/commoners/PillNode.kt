package com.gelostech.pocketbartender.commoners;

import android.content.Context
import android.graphics.Color
import android.os.Handler
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import com.gelostech.pocketbartender.R

class PillNode(context: Context,attrs:AttributeSet?) : RecyclerView(context,attrs) {

    private var listener:PillListener? = null
    private val adp = PillAdapter()
    private var animate = false

    constructor(context: Context):this(context,null)


    init {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        setHasFixedSize(true)
        adapter = adp
    }

    fun animatePills(){
        animate = true
        adp.notifyDataSetChanged()
        Handler().postDelayed({animate = false}, (200*adp.items.size).toLong())
    }

    fun setPillListener(listener:PillListener){
        this.listener = listener
    }

    fun setCurrentPill(pill: String){
        adp.currentPill = pill
        adp.notifyDataSetChanged()
    }

    fun setCurrentPill(pos: Int){
        adp.currentPill = adp.items[pos]
        adp.notifyDataSetChanged()
    }

    fun addPills(vararg pills: String){
        adp.items.addAll(pills)
        adp.currentPill = pills[0]
        adp.notifyDataSetChanged()
    }

    fun addPill(pill: String){
        adp.addPill(pill)
    }

    inner class PillAdapter(): RecyclerView.Adapter<PillAdapter.Holder>() {

        val items = mutableListOf<String>()
        val inflater = LayoutInflater.from(context)
        var currentPill:String? = null

        fun addPill(pill:String){
            items.add(pill)
            notifyItemInserted(items.size-1)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            return Holder(inflater.inflate(R.layout.pill_view,parent,false))
        }

        override fun getItemCount(): Int = items.size

        override fun onBindViewHolder(holder: Holder, position: Int) {
            val pill = items[position]
            holder.text.text = pill
            holder.itemView.setOnClickListener {
                if(pill == currentPill)return@setOnClickListener
                currentPill = pill
                notifyDataSetChanged()
                listener?.onPillClicked(pill,position)
            }

            if(currentPill == pill){
                holder.text.setBackgroundResource(R.drawable.pill_accent)
                holder.text.setTextColor(Color.WHITE)
                holder.itemView.animate().scaleX(0.9f).scaleY(0.9f).setInterpolator(ReverseInterpolator()).duration = 200
            }else{
                holder.text.setBackgroundResource(android.R.color.transparent)
                holder.text.setTextColor(Color.parseColor("#808080"))
            }

            if(animate)animate(holder.itemView,position)

        }

        fun animate(v:View,i:Int){
            v.scaleX = 0f
            v.scaleY = 0f
            v.animate().scaleX(1f).scaleY(1f).setDuration(500).setInterpolator(AccelerateDecelerateInterpolator())
                    .setStartDelay((i+1)*200L-((i*100))).start()
        }

        inner class Holder(view:View):ViewHolder(view){
            val text:TextView = view as TextView
        }


    }

    interface PillListener{
        fun onPillClicked(pill:String,pos: Int)
    }

}