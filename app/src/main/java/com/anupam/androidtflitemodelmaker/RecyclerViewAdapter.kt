package com.anupam.androidtflitemodelmaker

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_row.view.*


class RecyclerViewAdapter(val items: ArrayList<DataModel>, val context: Context) :
    RecyclerView.Adapter<ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = items.get(position)

        holder.tvPrediction?.text = data.prediction

        val image = ContextCompat.getDrawable(context, data.drawableID)
        holder.imageView.setImageDrawable(image)

        if (data.isNSFW) {
            holder.imageView.setColorFilter(Color.BLACK)
        } else {
            holder.imageView.setColorFilter(Color.TRANSPARENT)
        }
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvPrediction = view.tvPrediction
    val imageView = view.imageView
}