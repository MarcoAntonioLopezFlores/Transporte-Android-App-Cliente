package com.app.expresstaxi.utils.api

import com.app.expresstaxi.models.*
import retrofit2.Call
import retrofit2.http.*

interface APIService {

    @POST("auth/login")
    @Headers("Content-Type: application/json")
    fun login(@Body auth: Auth): Call<JwtResponse>

    @POST("auth/token")
    @Headers("Content-Type: application/json")
    fun registrarToken(@Body token: Token): Call<Token>

    @POST("cliente/registrar")
    @Headers("Content-Type: application/json")
    fun registrarCliente(@Body cliente: Cliente): Call<Cliente>

    @POST("servicio/registrar")
    @Headers("Content-Type: application/json")
    fun registrarServicio(@Header("Authorization") token: String, @Body servicio: Servicio): Call<Servicio>

    @POST("mensaje/registrar")
    @Headers("Content-Type: application/json")
    fun registrarMensaje(@Header("Authorization") token: String, @Body message: Message): Call<Message>

    @PUT("servicio/cambiarEstado")
    @Headers("Content-Type: application/json")
    fun cambiarEstado(@Header("Authorization") token: String, @Body servicio: Servicio): Call<Servicio>

    @PUT("servicio/calificarSevicio")
    @Headers("Content-Type: application/json")
    fun calificar(@Header("Authorization") token: String, @Body servicio: Servicio): Call<Servicio>

    @GET("auth/token/{tipo}")
    fun obtenerToken(@Header("Authorization") token: String, @Path("tipo") tipo: String): Call<Token>

    @GET("servicio/obtener/{id}")
    fun obtenerServicio(@Header("Authorization") token: String, @Path("id") id: Long): Call<Servicio>

    @GET("mensaje/listarByServicio/{id}")
    fun listarMensajes(@Header("Authorization") token: String, @Path("id") id: Long): Call<List<Message>>

    @GET("servicio/listarByCliente/{id}")
    fun listarServicios(@Header("Authorization") token: String, @Path("id") id: Long): Call<List<Servicio>>

    @GET("servicio/obtenerLocalizacionConductor/{id}")
    fun obtenerUbicacionConductor(@Header("Authorization") token: String, @Path("id") id: Long): Call<Localizacion>

}

object ApiUtils {
    private const val URL = "https://transporte-dasedatos.herokuapp.com/app/"

    val apiService: APIService
    get() =  RetrofitClient.getClient(URL)!!.create(APIService::class.java)
}