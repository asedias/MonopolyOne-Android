package com.asedias.monopolyone.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

open class BindingViewHolder<T>(_binding: T) : RecyclerView.ViewHolder((_binding as ViewBinding).root) {
    val binding: T = _binding
}