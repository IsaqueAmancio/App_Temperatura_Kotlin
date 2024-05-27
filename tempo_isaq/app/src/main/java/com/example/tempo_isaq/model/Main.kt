package com.example.tempo_isaq.model

import org.json.JSONObject

data class Main (
    val main : JSONObject,
    val weather : List<Weather>,
    val name : String,
    val sys: JSONObject
)