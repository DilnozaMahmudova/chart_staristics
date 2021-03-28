package com.company.dilnoza.chart.ui.screen

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.company.dilnoza.chart.R
import com.company.dilnoza.chart.datas.ProductData
import com.company.dilnoza.chart.datas.UserData
import com.company.dilnoza.chart.retrofit.AnalyticApi
import com.company.dilnoza.chart.retrofit.ApiClient
import com.company.dilnoza.chart.retrofit.ResponseData
import com.company.dilnoza.chart.ui.adapter.PieAdapter
import com.company.dilnoza.chart.ui.adapter.PieProductAdapter
import kotlinx.android.synthetic.main.fragment_pie.*
import kotlinx.android.synthetic.main.fragment_pie.averageValue
import kotlinx.android.synthetic.main.fragment_pie.maxValue
import kotlinx.android.synthetic.main.fragment_pie.minValue
import kotlinx.android.synthetic.main.fragment_value.list
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentPie : AppCompatActivity() {
    private var adapter1 = PieAdapter()
    private var adapter2 = PieProductAdapter()
    private lateinit var request:String
    private val api = ApiClient.retrofit.create(AnalyticApi::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_pie)
        progressPie.indeterminateDrawable.setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY)
        request=intent.getStringExtra("REQUEST")!!
        load()
    }

    private fun load() {
        list.layoutManager = LinearLayoutManager(this)
        when (request) {
            "user" -> {
                list.adapter = adapter1
                api.getAllUsers().enqueue(object : Callback<ResponseData<List<UserData>>> {
                    override fun onResponse(
                        call: Call<ResponseData<List<UserData>>>,
                        response: Response<ResponseData<List<UserData>>>
                    ) {
                        val data = response.body() ?: return
                        if (data.status == "OK") {
                            prog_pie.visibility=View.GONE
                            adapter1.submitList(data.data!!)
                            maxValue.text = data.data.maxByOrNull { it.age }?.age.toString()
                            minValue.text = data.data.minByOrNull { it.age }?.age.toString()
                            averageValue.text = data.data.map { it.age }.average().toString()
                        } else {
                            Toast.makeText(this@FragmentPie, data.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onFailure(
                        call: Call<ResponseData<List<UserData>>>,
                        t: Throwable
                    ) {
                        Toast.makeText(this@FragmentPie, "Network Error", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            "manufacturer" -> {
                content.visibility = View.GONE
                title_1.text = "Manufacturer"
                title_2.text = "Model"
                title_3.text = "Serial"
                list.adapter = adapter2
                api.getAllProducts().enqueue(object : Callback<ResponseData<List<ProductData>>> {
                    override fun onResponse(
                        call: Call<ResponseData<List<ProductData>>>,
                        response: Response<ResponseData<List<ProductData>>>
                    ) {
                        val data = response.body() ?: return
                        if (data.status == "OK") {
                            adapter2.submitList(data.data!!)
                            prog_pie.visibility=View.GONE
                        } else {
                            Toast.makeText(this@FragmentPie, data.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onFailure(
                        call: Call<ResponseData<List<ProductData>>>,
                        t: Throwable
                    ) {
                        Toast.makeText(this@FragmentPie, "Network Error", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }
}