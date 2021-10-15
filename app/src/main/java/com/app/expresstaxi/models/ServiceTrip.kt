package com.app.expresstaxi.models


import java.time.LocalDateTime

data class ServiceTrip(val id:Int, val date: LocalDateTime, val calification:Double, val nameDriver:String, val autoIdDriver:String)