package com.company.dilnoza.chart.ui.screen

import android.content.Intent
import android.graphics.Color.*
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.company.dilnoza.chart.R
import com.company.dilnoza.chart.datas.ProductData
import com.company.dilnoza.chart.datas.UserData
import com.company.dilnoza.chart.datas.ValueData
import com.company.dilnoza.chart.retrofit.AnalyticApi
import com.company.dilnoza.chart.retrofit.ApiClient
import com.company.dilnoza.chart.retrofit.ResponseData
import com.company.dilnoza.chart.ui.CustomMarker
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Utils
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private val api = ApiClient.retrofit.create(AnalyticApi::class.java)
    private lateinit var user: List<UserData>
    private lateinit var product: List<ProductData>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadBalance()
        line_graph.setOnClickListener {
            startActivity(Intent(this, FragmentMoney::class.java))
        }
        line_task.setOnClickListener {
            startActivity(Intent(this, FragmentValue::class.java).apply {
                putExtra(
                    "REQUEST",
                    "task"
                )
            })
        }
        line_balance.setOnClickListener {
            startActivity(Intent(this, FragmentValue::class.java).apply {
                putExtra(
                    "REQUEST",
                    "balance"
                )
            })
        }
        line_workers.setOnClickListener {
            startActivity(Intent(this, FragmentValue::class.java).apply {
                putExtra(
                    "REQUEST",
                    "worker"
                )
            })
        }
        pie_user.setTouchEnabled(false)
        pie_user.setOnClickListener {
            startActivity(Intent(this, FragmentPie::class.java).apply {
                putExtra(
                    "REQUEST",
                    "user"
                )
            })
        }
        pie_manufacturer.setTouchEnabled(false)
        pie_manufacturer.setOnClickListener {
            startActivity(Intent(this, FragmentPie::class.java).apply {
                putExtra(
                    "REQUEST",
                    "manufacturer"
                )
            })
        }
    }

    private fun loadBalance() {
        api.getBalance().enqueue(object : Callback<ResponseData<List<ValueData>>> {
            override fun onResponse(
                call: Call<ResponseData<List<ValueData>>>,
                response: Response<ResponseData<List<ValueData>>>
            ) {
                val data = response.body() ?: return
                if (data.status == "OK") {
                    drawBalance(data.data!!)
                    loadTask(data.data)
                    prog_balance.visibility = View.GONE
                } else {
                    Toast.makeText(this@MainActivity, data.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseData<List<ValueData>>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Network Error", Toast.LENGTH_SHORT).show()
            }
        })
        api.getProducts().enqueue(object : Callback<ResponseData<List<ProductData>>> {
            override fun onResponse(
                call: Call<ResponseData<List<ProductData>>>,
                response: Response<ResponseData<List<ProductData>>>
            ) {
                val data = response.body() ?: return
                if (data.status == "OK") {
                    product = data.data!!
                    drawProduct(product)
                    prog_pie.visibility = View.GONE
                } else {
                    Toast.makeText(this@MainActivity, data.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseData<List<ProductData>>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Network Error", Toast.LENGTH_SHORT).show()
            }
        })
        api.getUsers().enqueue(object : Callback<ResponseData<List<UserData>>> {
            override fun onResponse(
                call: Call<ResponseData<List<UserData>>>,
                response: Response<ResponseData<List<UserData>>>
            ) {
                val data = response.body() ?: return
                if (data.status == "OK") {
                    user = data.data!!
                    drawUser(user)
                    prog_user.visibility = View.GONE
                } else {
                    Toast.makeText(this@MainActivity, data.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseData<List<UserData>>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Network Error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadTask(yValue: List<ValueData>) {
        api.getTasks().enqueue(object : Callback<ResponseData<List<ValueData>>> {
            override fun onResponse(
                call: Call<ResponseData<List<ValueData>>>,
                response: Response<ResponseData<List<ValueData>>>
            ) {
                val data = response.body() ?: return
                if (data.status == "OK") {
                    prog_task.visibility = View.GONE
                    drawTask(data.data!!)
                    loadWorker(yValue, data.data)
                } else {
                    Toast.makeText(this@MainActivity, data.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseData<List<ValueData>>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Network Error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadWorker(xValue: List<ValueData>, yValue: List<ValueData>) {
        api.getWorkers().enqueue(object : Callback<ResponseData<List<ValueData>>> {
            override fun onResponse(
                call: Call<ResponseData<List<ValueData>>>,
                response: Response<ResponseData<List<ValueData>>>
            ) {
                val data = response.body() ?: return
                if (data.status == "OK") {
                    prog_worker.visibility = View.GONE
                    drawWorkers(data.data!!)
                    drawContent(xValue, yValue, data.data)
                } else {
                    Toast.makeText(this@MainActivity, data.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseData<List<ValueData>>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Network Error", Toast.LENGTH_SHORT).show()
            }
        })
    }


    fun drawBalance(yValue: List<ValueData>) {
        val entry = ArrayList<Entry>()
        var max = yValue[0].value
        for (i in yValue.indices) {
            entry.add(Entry(i.toFloat(), yValue[i].value.toFloat()))
            if (yValue[i].value.toFloat() > max) {
                max = yValue[i].value
            }
        }
        val vl = LineDataSet(entry, "Balance")
        vl.setDrawValues(false)
        vl.setDrawFilled(true)
        vl.color = parseColor("#A932E0")
        vl.lineWidth = 3f
        vl.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        vl.setDrawCircleHole(false)
        vl.setDrawValues(false)
        vl.setDrawCircles(false)
        vl.fillColor = GRAY
        vl.fillAlpha = RED
        line_balance.xAxis.labelRotationAngle = 0f
        line_balance.data = LineData(vl)
        line_balance.isAutoScaleMinMaxEnabled = true
        line_balance.legend.isEnabled = true
        line_balance.xAxis.axisMaximum = 10f
        line_balance.setTouchEnabled(true)
        line_balance.setPinchZoom(true)
        line_balance.description.text = "Days"
        line_balance.setNoDataText("No fares yet!")
        line_balance.animateX(1800, Easing.EaseInExpo)
        line_balance.axisRight.isEnabled = false
        line_balance.axisRight.setDrawLabels(false)
        line_balance.axisRight.setDrawAxisLine(false)
        line_balance.axisRight.setDrawGridLines(false)
        line_balance.axisLeft.isEnabled = false
        line_balance.axisLeft.setDrawLabels(false)
        line_balance.axisLeft.setDrawAxisLine(false)
        line_balance.axisLeft.setDrawGridLines(false)
        line_balance.xAxis.setDrawAxisLine(false)
        line_balance.xAxis.setDrawGridLines(false)
        line_balance.xAxis.isEnabled=false
        val markerView = CustomMarker(this, R.layout.marker_view)
        line_balance.marker = markerView
        line_balance.invalidate()
        line_balance.resetZoom()
        max_balance.text = "$max"

    }

    fun drawTask(yValue: List<ValueData>) {
        val entry = ArrayList<Entry>()
        var max = yValue[0].value
        for (i in yValue.indices) {
            entry.add(Entry(i.toFloat(), yValue[i].value.toFloat()))
            if (yValue[i].value.toFloat() > max) {
                max = yValue[i].value
            }
        }
        val vl = LineDataSet(entry, "Tasks")
        vl.setDrawValues(false)
        vl.setDrawFilled(true)
        vl.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        vl.setDrawCircleHole(false)
        vl.setDrawCircles(false)
        vl.color = parseColor("#FE6D9A")
        vl.setCircleColor(parseColor("#FE6D9A"))
        vl.lineWidth = 3f
        vl.fillColor = GRAY
        vl.fillAlpha = RED
        line_task.xAxis.labelRotationAngle = 0f
        line_task.data = LineData(vl)
        line_task.axisRight.isEnabled = false
        line_task.axisRight.setDrawLabels(false)
        line_task.axisRight.setDrawAxisLine(false)
        line_task.axisRight.setDrawGridLines(false)
        line_task.axisLeft.isEnabled = false
        line_task.axisLeft.setDrawLabels(false)
        line_task.axisLeft.setDrawAxisLine(false)
        line_task.axisLeft.setDrawGridLines(false)
        line_task.xAxis.setDrawAxisLine(false)
        line_task.xAxis.setDrawGridLines(false)
        line_task.xAxis.isEnabled=false
        line_task.xAxis.axisMaximum = 10f
        line_task.setTouchEnabled(true)
        line_task.setPinchZoom(true)
        line_task.description.text = "Days"
        line_task.setNoDataText("No fares yet!")
        line_task.animateX(1800, Easing.EaseInExpo)
        val markerView = CustomMarker(this, R.layout.marker_view)
        line_task.marker = markerView
        line_task.invalidate()
        max_task.text = "$max"

    }

    fun drawWorkers(yValue: List<ValueData>) {
        val entry = ArrayList<Entry>()
        var max = yValue[0].value
        for (i in yValue.indices) {
            entry.add(Entry(i.toFloat(), yValue[i].value.toFloat()))
            if (yValue[i].value.toFloat() > max) {
                max = yValue[i].value
            }
        }
        val vl = LineDataSet(entry, "Workers")
        vl.setDrawValues(false)
        vl.setDrawFilled(true)
        vl.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        vl.setDrawCircleHole(false)
        vl.setDrawCircles(false)
        vl.lineWidth = 3f
        vl.color = parseColor("#1F2A65")
        vl.setCircleColor(parseColor("#1F2A65"))
        vl.fillColor = GRAY
        vl.fillAlpha = RED
        line_workers.xAxis.labelRotationAngle = 0f
        line_workers.data = LineData(vl)
        line_workers.axisRight.isEnabled = false
        line_workers.axisRight.setDrawLabels(false)
        line_workers.axisRight.setDrawAxisLine(false)
        line_workers.axisRight.setDrawGridLines(false)
        line_workers.axisLeft.isEnabled = false
        line_workers.axisLeft.setDrawLabels(false)
        line_workers.axisLeft.setDrawAxisLine(false)
        line_workers.axisLeft.setDrawGridLines(false)
        line_workers.xAxis.setDrawAxisLine(false)
        line_workers.xAxis.setDrawGridLines(false)
        line_workers.xAxis.isEnabled=false
        line_workers.xAxis.axisMaximum = 10f
        line_workers.setTouchEnabled(true)
        line_workers.setPinchZoom(true)
        line_workers.description.text = "Days"
        line_workers.setNoDataText("No fares yet!")
        line_workers.animateX(1800, Easing.EaseInExpo)
        val markerView = CustomMarker(this, R.layout.marker_view)
        line_workers.marker = markerView
        max_workers.text = "$max"

    }

    private fun drawContent(
        xValue: List<ValueData>,
        yValue: List<ValueData>,
        zValue: List<ValueData>
    ) {
        prog_graph.visibility = View.GONE
        val entry1 = ArrayList<Entry>()
        val entry2 = ArrayList<Entry>()
        for (i in zValue.indices) {
            entry2.add(Entry(i.toFloat(), xValue[i].value.toFloat() / zValue[i].value))
            entry1.add(Entry(i.toFloat(), xValue[i].value.toFloat() / yValue[i].value))
        }
        val lines = ArrayList<LineDataSet>()
        val vl = LineDataSet(entry1, "Money a per  task")
        vl.setDrawFilled(true)
        vl.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        vl.lineWidth = 3f
        vl.color = parseColor("#FE6D9A")
        vl.setCircleColor(parseColor("#FE6D9A"))
        vl.fillColor = GRAY
        vl.fillAlpha = RED
        vl.setDrawFilled(true)
        if (Utils.getSDKInt() >= 18) {
            val drawable = ContextCompat.getDrawable(this, R.drawable.back1)
            vl.fillDrawable = drawable
        } else {
            vl.fillColor = WHITE
        }
        val vl2 = LineDataSet(entry2, "Money a per worker")
        vl2.setDrawFilled(true)
        vl2.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        vl2.lineWidth = 3f
        vl2.color = parseColor("#A932E0")
        vl2.setCircleColor(parseColor("#A932E0"))
        vl2.fillColor = GRAY
        vl2.fillAlpha = RED
        vl2.setDrawFilled(true)
        if (Utils.getSDKInt() >= 18) {
            val drawable = ContextCompat.getDrawable(this, R.drawable.back2)
            vl2.fillDrawable = drawable
        } else {
            vl2.fillColor = WHITE
        }
        lines.add(vl)
        lines.add(vl2)
        line_graph.xAxis.labelRotationAngle = 0f
        line_graph.data = LineData(lines as List<ILineDataSet>?)
        line_graph.axisRight.isEnabled = false
        line_graph.xAxis.axisMaximum = 10f
        line_graph.setTouchEnabled(true)
        line_graph.setPinchZoom(true)
        line_graph.xAxis.position = XAxis.XAxisPosition.BOTTOM
        line_graph.description.text = "Days"
        line_graph.setNoDataText("No fares yet!")
        line_graph.animateX(1800, Easing.EaseInExpo)
        val markerView = CustomMarker(this, R.layout.marker_view)
        line_graph.marker = markerView
    }

    fun drawUser(yValue: List<UserData>) {
        val entry = ArrayList<PieEntry>()
        val product = yValue.groupingBy { it.age }.eachCount()
        for (i in 0 until product.size) {
            entry.add(
                PieEntry(
                    product.values.toList()[i].toFloat(),
                    product.keys.toTypedArray()[i].toString()
                )
            )
        }
        val dataSet = PieDataSet(entry, "User age")
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f
        dataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
        val data = PieData(dataSet)
        data.setValueTextSize(0f)
        data.setValueTextColor(TRANSPARENT)
        pie_user.data = data
        pie_user.holeRadius = 35f
        pie_user.centerTextRadiusPercent = 70f
        pie_user.setEntryLabelColor(WHITE)
        pie_user.setEntryLabelTextSize(13f)
        pie_user.highlightValue(null)
        pie_user.invalidate()
        pie_user.animateXY(5000, 5000)
    }

    fun drawProduct(yValue: List<ProductData>) {
        val entry = ArrayList<PieEntry>()
        val product = yValue.groupingBy { it.manufacturer }.eachCount()
        for (i in 0 until product.size) {
            entry.add(
                PieEntry(
                    product.values.toList()[i].toFloat(),
                    product.keys.toTypedArray()[i]
                )
            )
        }
        val dataSet = PieDataSet(entry, "Product manufacturer")
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f
        dataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
        val data = PieData(dataSet)
        data.setValueTextSize(0f)
        data.setValueTextColor(WHITE)
        pie_manufacturer.data = data
        pie_manufacturer.rotationAngle = 30f
        pie_manufacturer.holeRadius = 35f
        pie_manufacturer.centerTextRadiusPercent = 45f
        pie_manufacturer.setEntryLabelColor(WHITE)
        pie_manufacturer.setEntryLabelTextSize(13f)
        pie_manufacturer.highlightValue(null)
        pie_manufacturer.invalidate()
        pie_manufacturer.animateXY(5000, 5000)
    }
}