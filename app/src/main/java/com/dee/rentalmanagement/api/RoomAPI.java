package com.dee.rentalmanagement.api;

import com.dee.rentalmanagement.model.Room;
import com.dee.rentalmanagement.serverresponse.RegisterResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface RoomAPI {
@POST ("rooms/addroom")
    Call<RegisterResponse> addRoom(@Body Room rooms);

@GET("rooms/listrooms")
Call<List<Room>>getAllRooms();

    }



