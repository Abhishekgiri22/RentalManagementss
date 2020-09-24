package com.dee.rentalmanagement.api;


import android.provider.ContactsContract;

import com.dee.rentalmanagement.model.Contact;
import com.dee.rentalmanagement.model.Room;
import com.dee.rentalmanagement.model.User;
import com.dee.rentalmanagement.serverresponse.ImageResponse;
import com.dee.rentalmanagement.serverresponse.RegisterResponse;
import com.dee.rentalmanagement.serverresponse.RoomResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;


public interface UsersAPI{
    @POST("users/register")
    Call<RegisterResponse> registerUser(@Body User users);

@POST("/room")
Call<RoomResponse> registerRoom(@Body Room room);

@FormUrlEncoded
@POST("users/login")
Call<RegisterResponse> checkUser(@Field("email")String email,@Field("password")String Password);

@FormUrlEncoded
@POST("/feedback")
Call<Void> contactUs(@Header("Authorization")String token,@Field("fullname")String fullname, @Field("email")String email,@Field("message")String message);

@Multipart
    @POST("upload")
    Call<ImageResponse> uploadImage(@Part MultipartBody.Part img);

@GET("users/me")
    Call<User> getUsersDetails(@Header("Authorization")String token);

@GET("users/user")
Call<List<User>> getUser(@Header("Authorization")String token);

@GET("feedback/feedback")
Call<List<Contact>> getContact(@Header("Authorization")String token);

@FormUrlEncoded
    @PUT("users/userupdate")
Call<RegisterResponse> userupdate(@Header("Authorization")String token,@Field("_id")String id,@Field("fullname")String fullname,@Field("address") String address, @Field("phone")String phone,@Field("email")String email);


@FormUrlEncoded
@PUT ("room/roomupdate")
Call<RoomResponse> roomupdate(@Header("Authorization")String token,@Field("_id")String id,@Field("title")String title,@Field("location") String location, @Field("phone")String phone,@Field("price")String price,@Field("details")String details);


    @GET("/room")
    Call<Room> getRoomsDetails(@Header("Authorization")String token);


}
