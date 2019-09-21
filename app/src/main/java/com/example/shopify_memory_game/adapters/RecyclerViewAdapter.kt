package com.example.shopify_memory_game.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify_memory_game.R
import com.example.shopify_memory_game.data.network.request.Card
import com.example.shopify_memory_game.internal.inflate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_card.view.*


class RecyclerViewAdapter(
    private val onRecyclerOnClickListener: OnRecyclerOnClickListener,
    private val tracker: RecyclerViewSelectionImageTracker
) : ListAdapter<Card, RecyclerViewAdapter.ImageViewHolder>(
    diffCallback
) {

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Card>() {
            override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.list_item_card
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = parent.inflate(viewType)
        return ImageViewHolder(view, onRecyclerOnClickListener)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ImageViewHolder(
        itemView: View,
        private val onRecyclerOnClickListener: OnRecyclerOnClickListener
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private var imageData: ImageData? = null


        override fun onClick(p0: View?) {
            if (!tracker.isCardFound(imageData!!) && !tracker.isCardSelected(imageData!!)) onRecyclerOnClickListener.onRecyclerViewClickListener(
                imageData!!
            )
        }

        fun bind(item: Card) {
            imageData = ImageData(item.id, adapterPosition)
            val isSelected = tracker.isCardSelected(imageData!!)
            val isFound = tracker.isCardFound(imageData!!)

            Picasso.get()
                .load(item.image.src)
                .into(itemView.card_image)

            itemView.card_title.text = item.title

            val visibility = if (isSelected or isFound) View.VISIBLE else View.INVISIBLE
            itemView.card_title.visibility = visibility
            itemView.card_image.visibility = visibility


            itemView.isActivated = isSelected


            itemView.setOnClickListener(this)
        }
    }

    data class ImageData(val product_id: Long, val position: Int)


    interface OnRecyclerOnClickListener {
        fun onRecyclerViewClickListener(imageData: ImageData)
    }
}