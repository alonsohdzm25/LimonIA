package com.limonia.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.limonia.R
import com.limonia.apiplagas.models.ProductResponse
import com.limonia.view_holders.ProductViewHolder

class ProductsAdapter(private val product: List<ProductResponse>) : RecyclerView.Adapter<ProductViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ProductViewHolder(layoutInflater.inflate(R.layout.item_producto, parent, false))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = product[position]
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun getItemCount(): Int = product.size

}