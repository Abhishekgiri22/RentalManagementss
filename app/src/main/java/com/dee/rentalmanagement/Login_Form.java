package com.dee.rentalmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dee.rentalmanagement.api.UsersAPI;
import com.dee.rentalmanagement.bbl.LoginBBL;
import com.dee.rentalmanagement.model.User;
import com.dee.rentalmanagement.notification.CreateChannel;
import com.dee.rentalmanagement.strictmode.StrictModeClass;
import com.dee.rentalmanagement.url.Url;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login_Form extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
Button btnRegisterLink;
NotificationManagerCompat notificationManagerCompat;
SharedPreferences sharedPreferences;
SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__form);

notificationManagerCompat= notificationManagerCompat.from(this);
CreateChannel channel= new CreateChannel(this);
channel.createChannel();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegisterLink = findViewById(R.id.btnRegisterLink);
sharedPreferences = getSharedPreferences("User",MODE_PRIVATE);
editor = sharedPreferences.edit();

        // on click listener

        btnRegisterLink.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Form.this, Signup_Form.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                LoginBBL loginBLL = new LoginBBL();
                StrictModeClass.StrictMode();

                if (loginBLL.checkUser(email, password)) {

                    UsersAPI usersAPI = Url.getInstance().create(UsersAPI.class);
                    Call<User> userCall = usersAPI.getUsersDetails(Url.token);
                    userCall.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (!response.isSuccessful()) {
                                Toast.makeText(Login_Form.this, "error", Toast.LENGTH_SHORT).show();
                            } else {
                                String admin = response.body().getAdmin();

                                if (admin == "false") {
                                    Toast.makeText(Login_Form.this, "Login Sucessfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Login_Form.this, DashboardActivity.class);
                                    startActivity(intent);
LoginNotification();
                                    finish();

                                } else {
                                    Toast.makeText(Login_Form.this, "Login Sucessfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Login_Form.this, DashboardActivity.class);
                                    startActivity(intent);
                                    LoginNotification();
                                    finish();                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(Login_Form.this, "error" , Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(Login_Form.this, "Email or Password Incorrect, Recheck email or password", Toast.LENGTH_SHORT).show();
                    etEmail.setText("");
                    etPassword.setText("");
                    etEmail.requestFocus();
                    Vibrator vibrator =(Vibrator)getSystemService(VIBRATOR_SERVICE);
vibrator.vibrate(1000);

                }
            }
        });
    }
    private void LoginNotification() {

        Notification notification = new NotificationCompat.Builder(this, CreateChannel.CHANNEL_1)
                .setSmallIcon(R.drawable.imgss)
                .setContentTitle("Gharbeti Application")
                .setContentText("Login Successful")
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManagerCompat.notify(1, notification);

    }
}