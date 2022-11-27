package com.asedias.monopolyone.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.decode.SvgDecoder
import coil.load
import com.asedias.monopolyone.databinding.MarketListItemBinding
import com.asedias.monopolyone.domain.model.market.Thing

class MarketAdapter: RecyclerView.Adapter<BindingViewHolder<MarketListItemBinding>>() {

    inner class ThingViewHolder(_binding: MarketListItemBinding) : RecyclerView.ViewHolder(_binding.root) {
        val binding: MarketListItemBinding = _binding
    }

    private val differCallback = object : DiffUtil.ItemCallback<Thing>() {
        override fun areItemsTheSame(oldItem: Thing, newItem: Thing): Boolean {
            return oldItem.thing_prototype_id == newItem.thing_prototype_id
        }

        override fun areContentsTheSame(oldItem: Thing, newItem: Thing): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BindingViewHolder(
        MarketListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: BindingViewHolder<MarketListItemBinding>, position: Int) {
        val thing = differ.currentList[position]
        holder.binding
        holder.itemView.apply {
            //Picasso.get().load(thing.image).into(holder.binding.image)
            //Glide.with(this).load(thing.image).into(holder.binding.image)
            holder.binding.image.load(thing.image) {
                crossfade(true)
                //placeholder(R.drawable.banner_background)
                decoderFactory(SvgDecoder.Factory())
            }
            holder.binding.title.text = thing.title
            holder.binding.count.text = thing.count.toString()
            holder.binding.price.text = "От ${thing.price} р."
            holder.binding.remain.text = "${thing.count} шт."
        }
    }

    override fun getItemCount() = differ.currentList.size

}