package com.company.dilnoza.chart.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.company.dilnoza.chart.R
import com.company.dilnoza.chart.datas.ProductData
import com.company.dilnoza.chart.datas.UserData
import com.company.dilnoza.chart.datas.ValueData
import com.company.dilnoza.chart.extention.bindItem
import com.company.dilnoza.chart.extention.inflate
import kotlinx.android.synthetic.main.item_pie.view.*

class PieAdapter:
    RecyclerView.Adapter<PieAdapter.ViewHolder>() {
    private val ls=ArrayList<UserData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder((parent.inflate(R.layout.item_pie)))

    override fun getItemCount() = ls.size
    fun submitList(data: List<UserData>) {
        ls.clear()
        ls.addAll(data)
        notifyItemRangeRemoved(0, data.size)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        fun bind() = bindItem {
            val d = ls[adapterPosition]
            age.text=d.age.toString()
            lastName.text=d.lastName
            firstName.text=d.firstName
        }
    }
}