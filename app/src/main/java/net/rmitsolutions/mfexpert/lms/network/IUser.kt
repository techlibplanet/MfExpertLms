package net.rmitsolutions.mfexpert.lms.network

import io.reactivex.Observable
import net.rmitsolutions.mfexpert.lms.models.AccessToken
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface IUser {

    @FormUrlEncoded
    @POST("connect/token")
    fun validateUser(
            @Field("grant_type") grantType: String,
            @Field("client_id") clientId: String,
            @Field("client_secret") clientSecret: String,
            @Field("username") userName: String,
            @Field("password") password: String): Observable<AccessToken>

    @FormUrlEncoded
    @POST("connect/token")
    fun refreshToken(
            @Field("grant_type") grantType: String,
            @Field("client_id") clientId: String,
            @Field("client_secret") clientSecret: String,
            @Field("refresh_token") refreshToken: String): Observable<AccessToken>
}