package com.reportdroid.shibuyaxpress.reportdroid.Services;

import com.reportdroid.shibuyaxpress.reportdroid.Models.Report;
import com.reportdroid.shibuyaxpress.reportdroid.Models.ResponseMessage;
import com.reportdroid.shibuyaxpress.reportdroid.Models.ResponseUser;
import com.reportdroid.shibuyaxpress.reportdroid.Models.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by paulf on 13-Nov-17.
 */

public interface ApiService {

    String API_BASE_URL = "https://reportdroid-guiltydarkness1313.c9users.io";

    //login works
    @FormUrlEncoded
    @POST("/api/v1/login")
    Call<User> Login(@Field("username") String nombre,
                     @Field("password") String password);

    //works too
    @FormUrlEncoded
    @POST("/api/v1/usuarios")
    Call<ResponseMessage>createUser(
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password
    );
    //???
    @Multipart
    @POST("/api/v1/usuarios")
    Call<ResponseMessage> createUserWithImage(
            @Part("username")RequestBody username,
            @Part("email") RequestBody email,
            @Part("password")RequestBody password,
            @Part MultipartBody.Part imagen
            );

    //get data from reports
    @GET("api/v1/reportes")
    Call<List<Report>> getReports();

    //works too
    @FormUrlEncoded
    @POST("/api/v1/reportes")
    Call<ResponseMessage>createReport(
            @Field("titulo") String title,
            @Field("direccion") String direccion,
           // @Field("latitud") Double latitud,
            //@Field("longitud")Double longitud,
            @Field("descripcion")String descripcion,
            @Field("usuario_id")String id
            );

    //???
    @Multipart
    @POST("/api/v1/reportes")
    Call<ResponseMessage> createReportWithImage(
            @Part("titulo")RequestBody title,
            @Part("direccion") RequestBody direccion,
            //@Part("latitud")RequestBody latitud,
            //@Part("longitud")RequestBody longitud,
            @Part("descripcion")RequestBody descripcion,
            @Part("usuario_id")RequestBody id,
            @Part MultipartBody.Part imagen
    );

    @GET("/api/v1/usuarios/{id}")
    Call<ResponseUser> showUser(@Path("id") Integer id);
    //definir una clase que contenga dos clases status y usuario-

}
