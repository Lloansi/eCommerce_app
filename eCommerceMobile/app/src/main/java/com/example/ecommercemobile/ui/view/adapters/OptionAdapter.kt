package com.example.ecommercemobile.ui.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommercemobile.R
import com.example.ecommercemobile.databinding.ItemOptionBinding
import com.example.ecommercemobile.ui.view.adapters.interfaces.OnClickListenerOption

class OptionAdapter(private val options: List<Pair<Int,String>>,
                    val listener: OnClickListenerOption
): RecyclerView.Adapter<OptionAdapter.ViewHolder>(){

    private lateinit var context: Context

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemOptionBinding.bind(view)
        fun setListener(option: String) {
            binding.root.setOnClickListener {
                listener.onClick(option)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionAdapter.ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_option, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: OptionAdapter.ViewHolder, position: Int) {
        val option = options[position]
        with(holder) {
            setListener(option.second)
            binding.optionTV.text = option.second
            binding.iconIV.setImageResource(option.first)
        }
    }

    override fun getItemCount(): Int {
        return options.size
    }
}
