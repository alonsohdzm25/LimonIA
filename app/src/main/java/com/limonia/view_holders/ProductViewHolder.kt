package com.limonia.view_holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.limonia.apiplagas.models.ProductResponse
import com.limonia.databinding.ItemProductoBinding

class ProductViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemProductoBinding.bind(view)

    fun bind(product: ProductResponse) {
        binding.nombreComercial.text = product.comercialName
        binding.ingActivo.text = product.ingredienteActivo
    }

}