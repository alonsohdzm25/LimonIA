package com.limonia.apiplagas

import com.limonia.apiplagas.models.*
import retrofit2.Response
import retrofit2.http.*

interface PlagaApiService {

    @GET("api/plagas/")
    suspend fun getPlagas() : Response<PlagasResponse>

    @GET
    suspend fun getPlagaByName(@Url url:String) : Response<PlagasResponse>

    @POST("api/auth/signup")
    suspend fun signUp(@Body signUpRequest: SignUpRequest) : Response<AuthResponse>

    @POST("api/auth/resendemail")
    suspend fun resendEmail(@Body email:String) : Response<ChangePasswordResponse>

    @POST("api/auth/signin")
    suspend fun signIn(@Body signInRequest: SignInRequest) : Response<AuthResponse>

    @POST("api/auth/recoverypassword")
    suspend fun recoveryPassword(@Body emailRequest: EmailRequest) : Response<ChangePasswordResponse>

    @PUT
    suspend fun updateUser(@Url url: String, @Body changePassword: ChangePasswordRequest ) : Response<ChangePasswordResponse>

    @GET("api/prohibitedproduct/")
    suspend fun getProducts() : Response<ProductsResponse>

    @GET
    suspend fun getProductByName(@Url url: String) : Response<ProductsResponse>

}