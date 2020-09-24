package com.dee.rentalmanagement.bbl;

import com.dee.rentalmanagement.api.UsersAPI;
import com.dee.rentalmanagement.serverresponse.RegisterResponse;
import com.dee.rentalmanagement.url.Url;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;


public class LoginBBL {
    boolean isSuccess = false;

    public boolean checkUser(String email, String password) {

        UsersAPI usersAPI = Url.getInstance().create(UsersAPI.class);
        Call<RegisterResponse> usersCall = usersAPI.checkUser(email, password);

        try {
            Response<RegisterResponse> loginResponse = usersCall.execute();
            if (loginResponse.isSuccessful() &&
                    loginResponse.body().getStatus().equals("Login success!")) {

                Url.token += loginResponse.body().getToken();
                // Url.Cookie = imageResponseResponse.headers().get("Set-Cookie");
                isSuccess = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }
}

