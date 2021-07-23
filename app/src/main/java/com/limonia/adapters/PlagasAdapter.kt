package com.limonia.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.limonia.R
import com.limonia.apiplagas.models.PlagaResponse
import com.limonia.view_holders.PlagasViewHolder
import com.limonia.views.PhotoActivity

class PlagasAdapter(
    private val plagas: List<PlagaResponse>
    ) : RecyclerView.Adapter<PlagasViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlagasViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PlagasViewHolder(layoutInflater.inflate(R.layout.item_plaga,parent,false))
    }

    override fun onBindViewHolder(holder: PlagasViewHolder, position: Int) {
        val item = plagas[position]
        if (item != null) {
            holder.bind(item)
        }

        holder.itemView.setOnClickListener {
            var intent = Intent(holder.itemView.context, PhotoActivity::class.java)
            intent.putExtra("labelPlaga", item.name)
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int = plagas.size


}