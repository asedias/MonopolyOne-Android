package com.example.material3test.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class BindingViewHolder<T>(_binding: T) : RecyclerView.ViewHolder((_binding as ViewBinding).root) {
    val binding: T = _binding
}