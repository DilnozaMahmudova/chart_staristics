package com.company.dilnoza.chart.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.company.dilnoza.chart.R
import com.company.dilnoza.chart.datas.ValueData
import com.company.dilnoza.chart.extention.bindItem
import com.company.dilnoza.chart.extention.inflate
import kotlinx.android.synthetic.main.item_value.view.*


class ValueAdapter :
    RecyclerView.Adapter<ValueAdapter.ViewHolder>() {
    private val list=ArrayList<ValueData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder((parent.inflate(R.layout.item_value)))

    override fun getItemCount() = list.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()
    fun submitList(data: List<ValueData>) {
        list.clear()
        list.addAll(data)
        notifyItemRangeRemoved(0, data.size)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        fun bind() = bindItem {
            val d = list[adapterPosition]
            value.text=d.value.toString()
        }
    }
}
