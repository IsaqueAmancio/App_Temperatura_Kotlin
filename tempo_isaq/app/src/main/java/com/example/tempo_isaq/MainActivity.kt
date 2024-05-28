package com.example.tempo_isaq

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tempo_isaq.consts.Const
import com.example.tempo_isaq.databinding.ActivityMainBinding
import com.example.tempo_isaq.model.Main
import com.example.tempo_isaq.services.Api
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DecimalFormat


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.trocarTema.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                binding.conteinerPrincipal.setBackgroundColor(Color.parseColor("#0B2B40"))
            }else{
                binding.conteinerPrincipal.setBackgroundColor(Color.parseColor("#0FC2C0"))
            }
        }
        binding.btBuscar.setOnClickListener {
            val cid = binding.editBuscarCidade.text.toString()
            val retrofit: Api = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .build()
                .create(Api::class.java)
            retrofit.weatherMap(cid, Const.api_key).enqueue(object : Callback<Main> {
                override fun onResponse(p0: Call<Main>, response: Response<Main>) {
                    if (response.isSuccessful) {
                        respostaServidor(response)
                    } else {
                        Toast.makeText(applicationContext, "erro api", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Main>, p1: Throwable) {
                    Toast.makeText(applicationContext, "erro api", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
    }

    @SuppressLint("SetTextI18n")
    private fun respostaServidor(response: Response<Main>){
        val main = response.body()!!.main
        val temp = main.temp
        val temp_feels = main.feels_like
        val temp_min = main.temp_min
        val temp_max = main.temp_max
        val humi = main.humidity
        val K_C = 273.15
        val temp_c = (temp.toDouble() - K_C)
        val temp_c_min = (temp_min.toDouble() - K_C)
        val temp_c_max = (temp_max.toDouble() - K_C)
        val temp_c_feels = (temp_feels.toDouble() - K_C)
        val decimalFormat = DecimalFormat("00")
        val cidade = response.body()!!.name


        val sys = response.body()!!.sys
        var pais = sys.country


        val weather = response.body()!!.weather
        val nuvem = weather[0].main
        val descricao = weather[0].description


        //val temp = temp.main
        binding.txtTemperatura.setText("${decimalFormat.format(temp_c)} ºC")
        println(pais)
        if (pais == "BR"){
            pais = "Brasil"
            println("passei_aqui")
        }
        else if (pais == "US"){
            pais = "E.U.S"
        }else{
            pais = "???"
            println("passei_aqui")
        }
        println(nuvem)
        println(descricao)
        if(nuvem.equals("Clouds") && descricao.equals("few clouds")){
            binding.imgClima.setBackgroundResource(R.drawable.flewclouds)
        }else if(nuvem.equals("Clouds") && descricao.equals("scattered clouds")){
            binding.imgClima.setBackgroundResource(R.drawable.clouds)
        }else if(nuvem.equals("Clouds") && descricao.equals("sbroken clouds")){
            binding.imgClima.setBackgroundResource(R.drawable.clouds)
        }else if(nuvem.equals("Clouds") && descricao.equals("overcast clouds")){
            binding.imgClima.setBackgroundResource(R.drawable.brokenclouds)
        }else if(nuvem.equals("Clear") && descricao.equals("clear sky")){
            binding.imgClima.setBackgroundResource(R.drawable.clearsky)
        }else if(nuvem.equals("Snow")){
            binding.imgClima.setBackgroundResource(R.drawable.snow)
        }else if(nuvem.equals("Rain")){
            binding.imgClima.setBackgroundResource(R.drawable.rain)
        }else if(nuvem.equals("Drizzle")){
            binding.imgClima.setBackgroundResource(R.drawable.rain)
        }else if(nuvem.equals("Thunderstorm")){
            binding.imgClima.setBackgroundResource(R.drawable.trunderstorm)
        }

        val descricaoClima = when(descricao){
            "clear sky" -> {
                "Céu Limpo"
            }
            "few clouds" -> {
                "Poucas nuvens"
            }
            "scattered clouds" -> {
                "Nuvens dispersas"
            }
            else ->{
                "tratando"
            }

        }
        //binding.txtTemperatura.setText("${decimalFormat.format(temp_c)} ºC")

        binding.txtPaisCidade.setText("$pais - $cidade")

        binding.txtTituloInfo01.setText("Clima \n $descricaoClima \n\n Umidade \n $humi %")
        binding.txtTituloInfo02.setText("Temp. Min \n ${decimalFormat.format(temp_c_min)} ºC \n\n Temp. Max \n ${decimalFormat.format(temp_c_max)} ºC ")



    }



}


