package com.company.dilnoza.chart.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.company.dilnoza.chart.R
import com.company.dilnoza.chart.datas.ProductData
import com.company.dilnoza.chart.extention.bindItem
import com.company.dilnoza.chart.extention.inflate
import kotlinx.android.synthetic.main.item_pie.view.*

class PieProductAdapter: RecyclerView.Adapter<PieProductAdapter.ViewHolder>() {
    private val ls=ArrayList<ProductData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder((parent.inflate(R.layout.item_pie)))

    override fun getItemCount() = ls.size
    fun submitList(data: List<ProductData>) {
        ls.clear()
        ls.addAll(data)
        notifyItemRangeRemoved(0, data.size)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        fun bind() = bindItem {
            val d = ls[adapterPosition]
            age.text=d.manufacturer
            lastName.text=d.modelName
            firstName.text=d.serial
        }
    }
}