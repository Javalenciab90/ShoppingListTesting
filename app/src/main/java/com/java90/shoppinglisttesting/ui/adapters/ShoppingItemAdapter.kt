package com.java90.shoppinglisttesting.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.java90.shoppinglisttesting.R
import com.java90.shoppinglisttesting.data.local.ShoppingItem
import com.java90.shoppinglisttesting.databinding.ItemImageBinding
import com.java90.shoppinglisttesting.databinding.ItemShoppingBinding

class ShoppingItemAdapter() : ListAdapter<ShoppingItem, ShoppingItemAdapter.ShoppingItemViewHolder>(ShoppingItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewBinding = ItemShoppingBinding.inflate(inflater, parent, false)
        return ShoppingItemViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ShoppingItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ShoppingItemViewHolder(private val binding: ItemShoppingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : ShoppingItem) {
            with(binding) {
                tvName.text = item.name

                val amountText = "${item.amount}x"
                tvShoppingItemAmount.text = amountText

                val priceText = "${item.price}€"
                tvShoppingItemPrice.text = priceText

                Glide.with(itemView.context)
                        .load(item)
                        .centerCrop()
                        .error(R.drawable.ic_image)
                        .placeholder(R.drawable.ic_image)
                        .into(ivShoppingImage)
            }
        }
    }
}

private class ShoppingItemDiffCallback : DiffUtil.ItemCallback<ShoppingItem>() {
    override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }
}