package com.java90.shoppinglisttesting.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.java90.shoppinglisttesting.R
import com.java90.shoppinglisttesting.databinding.ItemImageBinding

class ImageAdapter(private val onItemClickListener: ((String) -> Unit)) : ListAdapter<String, ImageAdapter.ImageViewHolder>(ImageItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewBinding = ItemImageBinding.inflate(inflater, parent, false)
        return ImageViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener { onItemClickListener.invoke(item) }
        holder.bind(item)
    }

    class ImageViewHolder(private val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : String) {
            with(binding) {
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

private class ImageItemDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}