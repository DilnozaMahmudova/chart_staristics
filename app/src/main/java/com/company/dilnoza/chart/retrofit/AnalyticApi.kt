package com.company.dilnoza.chart.retrofit

import com.company.dilnoza.chart.datas.*
import retrofit2.Call
import retrofit2.http.*



interface AnalyticApi {
    /**
     * 1. Get balance
     * */
    @GET("analytic/balance")
    fun getBalance(): Call<ResponseData<List<ValueData>>>
    /**
     * 2. Get product
     * */
    @GET("analytic/products")
    fun getProducts(): Call<ResponseData<List<ProductData>>>
    /**
     * 3. Get tasks
     * */
    @GET("analytic/tasks")
    fun getTasks(): Call<ResponseData<List<ValueData>>>
    /**
     * 4. Get users
     * */
    @GET("analytic/users")
    fun getUsers(): Call<ResponseData<List<UserData>>>
    /**
     * 5. Get workers
     * */
    @GET("analytic/workers")
    fun getWorkers(): Call<ResponseData<List<ValueData>>>
    /**
     * 6. Get all balance
     * */
    @GET("analytic/balance/all")
    fun getAllBalance(): Call<ResponseData<List<ValueData>>>
    /**
     * 7. Get all product
     * */
    @GET("analytic/products/all")
    fun getAllProducts(): Call<ResponseData<List<ProductData>>>
    /**
     * 8. Get all tasks
     * */
    @GET("analytic/tasks/all")
    fun getAllTasks(): Call<ResponseData<List<ValueData>>>
    /**
     * 9. Get all users
     * */
    @GET("analytic/users/all")
    fun getAllUsers(): Call<ResponseData<List<UserData>>>
    /**
     * 10. Get all workers
     * */
    @GET("analytic/workers/all")
    fun getAllWorkers(): Call<ResponseData<List<ValueData>>>
}