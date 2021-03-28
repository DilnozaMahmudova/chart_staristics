package com.company.dilnoza.chart.ui.screen

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.company.dilnoza.chart.R
import com.company.dilnoza.chart.datas.ValueData
import com.company.dilnoza.chart.retrofit.AnalyticApi
import com.company.dilnoza.chart.retrofit.ApiClient
import com.company.dilnoza.chart.retrofit.ResponseData
import com.company.dilnoza.chart.ui.adapter.ValueAdapter
import kotlinx.android.synthetic.main.fragment_value.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FragmentValue : AppCompatActivity() {
    private var adapter = ValueAdapter()
    private lateinit var request: String
    private val api = ApiClient.retrofit.create(AnalyticApi::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_value)
        progressValue.indeterminateDrawable.setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY)
        request=intent.getStringExtra("REQUEST")!!
        load()
    }

    private fun load() {
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter
        when (request) {
            "task" -> {
                api.getAllTasks().enqueue(object : Callback<ResponseData<List<ValueData>>> {
                    override fun onResponse(
                        call: Call<ResponseData<List<ValueData>>>,
                        response: Response<ResponseData<List<ValueData>>>
                    ) {
                        val data = response.body() ?: return
                        if (data.status == "OK") {
                            prog_value.visibility= View.GONE
                            adapter.submitList(data.data!!)
                            maxValue.text= data.data.maxByOrNull { it.value }?.value.toString()
                            minValue.text=data.data.minByOrNull { it.value }?.value.toString()
                            averageValue.text=data.data.map { it.value }.average().toString()
                        } else {
                            Toast.makeText(this@FragmentValue, data.message, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(
                        call: Call<ResponseData<List<ValueData>>>,
                        t: Throwable
                    ) {
                        Toast.makeText(this@FragmentValue, "Network Error", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            "balance" -> {
                api.getAllBalance().enqueue(object : Callback<ResponseData<List<ValueData>>> {
                    override fun onResponse(
                        call: Call<ResponseData<List<ValueData>>>,
                        response: Response<ResponseData<List<ValueData>>>
                    ) {
                        val data = response.body() ?: return
                        if (data.status == "OK") {
                            prog_value.visibility=View.GONE
                            adapter.submitList(data.data!!)
                            maxValue.text= data.data.maxByOrNull { it.value }?.value.toString()
                            minValue.text=data.data.minByOrNull { it.value }?.value.toString()
                            averageValue.text=data.data.map { it.value }.average().toString()
                        } else {
                            Toast.makeText(this@FragmentValue, data.message, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(
                        call: Call<ResponseData<List<ValueData>>>,
                        t: Throwable
                    ) {
                        Toast.makeText(this@FragmentValue, "Network Error", Toast.LENGTH_SHORT).show()
                    }
                })

            }
            "worker" -> {
                api.getAllWorkers().enqueue(object : Callback<ResponseData<List<ValueData>>> {
                    override fun onResponse(
                        call: Call<ResponseData<List<ValueData>>>,
                        response: Response<ResponseData<List<ValueData>>>
                    ) {
                        val data = response.body() ?: return
                        if (data.status == "OK") {
                            prog_value.visibility=View.GONE
                            adapter.submitList(data.data!!)
                            maxValue.text= data.data.maxByOrNull { it.value }?.value.toString()
                            minValue.text= data.data.minByOrNull { it.value }?.value.toString()
                            averageValue.text=data.data.map { it.value }.average().toString()
                        } else {
                            Toast.makeText(this@FragmentValue, data.message, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(
                        call: Call<ResponseData<List<ValueData>>>,
                        t: Throwable
                    ) {
                        Toast.makeText(this@FragmentValue, "Network Error", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }
}