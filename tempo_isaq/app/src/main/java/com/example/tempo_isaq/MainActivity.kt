package com.example.tempo_isaq

import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tempo_isaq.consts.Const
import com.example.tempo_isaq.model.Main
import com.example.tempo_isaq.services.Api
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        val retrofit: Api = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build()
            .create(Api::class.java)
        retrofit.weatherMap("São Paulo",Const.api_key).enqueue(object:Callback<Main>{
            override fun onResponse(call: Call<Main>, response: Response<Main>){
                if (response.isSuccessful){
                    respostaServidor(response)
                }else{
                    Toast.makeText(applicationContext,"erro api",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Main>, p1: Throwable) {
                Toast.makeText(applicationContext,"erro api",Toast.LENGTH_SHORT).show()
            }
    private fun respostaServidor(response: Response<Main>){
        val main = response.body()!!.main
        //val tempo = main.get("temp")
        val sys = response.body()!!.sys
        val cidade = response.body()!!.name
        println(cidade)
        //val temp = main.get("temp").toString()
        val weather = response.body()!!.weather
        val nuvem = weather[0].main
        val descricao = weather[0].description
        val K_C = 273.15
        println(descricao)
        //val temp_c = (temp.toDouble() - K_C)
    }

        })
    }
}


