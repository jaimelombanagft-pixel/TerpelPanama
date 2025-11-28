package com.terpel.app.ui.welcome

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.terpel.app.R

class WelcomePagerAdapter(private val pages: List<WelcomePage>) : RecyclerView.Adapter<WelcomePagerAdapter.PageVH>() {
    class PageVH(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val description: TextView = view.findViewById(R.id.description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_welcome_page, parent, false)
        return PageVH(view)
    }

    override fun onBindViewHolder(holder: PageVH, position: Int) {
        val item = pages[position]
        holder.title.text = item.title
        holder.description.text = item.description
    }

    override fun getItemCount(): Int = pages.size
}

