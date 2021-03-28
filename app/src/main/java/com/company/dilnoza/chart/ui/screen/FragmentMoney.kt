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
import com.company.dilnoza.chart.ui.adapter.MoneyAdapter
import kotlinx.android.synthetic.main.fragment_money.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentMoney : AppCompatActivity() {
    private var adapter1 = MoneyAdapter()
    private var adapter2 = MoneyAdapter()
    private val api = ApiClient.retrofit.create(AnalyticApi::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_money)
        progress.indeterminateDrawable.setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY)
        load()
    }

    private fun load() {
        listTask.layoutManager = LinearLayoutManager(this)
        listWorker.layoutManager=LinearLayoutManager(this)
        listTask.adapter=adapter1
        listWorker.adapter=adapter2
        api.getAllBalance().enqueue(object : Callback<ResponseData<List<ValueData>>> {


            override fun onResponse(
                call: Call<ResponseData<List<ValueData>>>,
                response: Response<ResponseData<List<ValueData>>>
            ) {
                val data = response.body() ?: return
                if (data.status == "OK") {
                    loadTask(data.data!!)
                    prog_money.visibility=View.GONE
                } else {
                    Toast.makeText(this@FragmentMoney, data.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseData<List<ValueData>>>, t: Throwable) {
                Toast.makeText(this@FragmentMoney, "Network Error", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun loadTask(yValue: List<ValueData>) {
        api.getAllTasks().enqueue(object : Callback<ResponseData<List<ValueData>>> {
            override fun onResponse(
                call: Call<ResponseData<List<ValueData>>>,
                response: Response<ResponseData<List<ValueData>>>
            ) {
                val data = response.body() ?: return
                if (data.status == "OK") {
                    loadWorker(yValue, data.data!!)
                } else {
                    Toast.makeText(this@FragmentMoney, data.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseData<List<ValueData>>>, t: Throwable) {
                Toast.makeText(this@FragmentMoney, "Network Error", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun loadWorker(xValue: List<ValueData>, yValue: List<ValueData>) {
        api.getAllWorkers().enqueue(object : Callback<ResponseData<List<ValueData>>> {
            override fun onResponse(
                call: Call<ResponseData<List<ValueData>>>,
                response: Response<ResponseData<List<ValueData>>>
            ) {
                val data = response.body() ?: return
                if (data.status == "OK") {
                    prog_money.visibility=View.GONE
                    drawContent(xValue, yValue, data.data!!)
                } else {
                    Toast.makeText(this@FragmentMoney, data.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseData<List<ValueData>>>, t: Throwable) {
                Toast.makeText(this@FragmentMoney, "Network Error", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun drawContent(
        xValue: List<ValueData>,
        yValue: List<ValueData>,
        zValue: List<ValueData>
    ) {
        val entry1 = ArrayList<Int>()
        val entry2 = ArrayList<Int>()
        for (i in zValue.indices) {
            entry2.add(xValue[i].value / zValue[i].value)
            entry1.add(xValue[i].value/ yValue[i].value)
        }
        adapter1.submitList(entry1)
        adapter2.submitList(entry2)
        val max1=entry1.maxByOrNull { it }.toString()+"/"+entry2.maxByOrNull { it }.toString()
        val min1=entry1.minByOrNull { it }.toString()+"/"+entry2.minByOrNull { it }.toString()
        val average1=entry1.average().toInt().toString()+"/"+ entry2.average().toInt().toString()
        maxValue.text= max1
        minValue.text=min1
        averageValue.text=average1

    }
}