package com.example.shopify_memory_game

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify_memory_game.data.network.request.Image
import com.example.shopify_memory_game.internal.inflate

class RecyclerViewAdapter(
    private val onRecyclerOnClickListener: OnRecyclerOnClickListener
    // private val tracker: EntrySelectedTracker? = null
) : ListAdapter<Image, RecyclerViewAdapter.ImageViewHolder>(diffCallback) {

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Image>() {
            override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
                return oldItem.product_id == newItem.product_id
            }

            override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = parent.inflate(viewType)
        return ImageViewHolder(view, onRecyclerOnClickListener)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    inner class ImageViewHolder(
        itemView: View,
        private val onRecyclerOnClickListener: OnRecyclerOnClickListener
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(p0: View?) {
            onRecyclerOnClickListener.onRecyclerViewClickListener()
        }

        fun bind(item: Image) {
            itemView.setOnClickListener(this)
        }
    }


    interface OnRecyclerOnClickListener {
        fun onRecyclerViewClickListener()
    }
}