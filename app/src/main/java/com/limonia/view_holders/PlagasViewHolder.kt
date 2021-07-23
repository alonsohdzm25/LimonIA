package com.limonia.view_holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.limonia.apiplagas.models.PlagaResponse
import com.limonia.databinding.ItemPlagaBinding
import com.squareup.picasso.Picasso

class PlagasViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemPlagaBinding.bind(view)

    fun bind(plaga: PlagaResponse) {
        Picasso.get().load(plaga.imgURL).into(binding.imgViewPlaga)
        binding.textViewPlaga.text = plaga.name

    }
}