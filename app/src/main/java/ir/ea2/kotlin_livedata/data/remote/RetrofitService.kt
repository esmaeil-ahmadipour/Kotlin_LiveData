package ir.ea2.kotlin_livedata.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "http://192.168.43.88:9000/api/"

object RetrofitService {
private var client:OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(BasicAuthInterceptor("a@a.ua","123"))
    .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    //We Use An Object Of ApiService Interface To Create Retrofit Service And Then ,
    //apiService Access To Method's Of ApiService Interface (Like: getNote)
    val  apiService : ApiService = retrofit.create(ApiService::class.java)
}