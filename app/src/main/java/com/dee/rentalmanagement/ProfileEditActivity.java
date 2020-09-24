package com.dee.rentalmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dee.rentalmanagement.api.UsersAPI;
import com.dee.rentalmanagement.model.User;
import com.dee.rentalmanagement.notification.CreateChannel;
import com.dee.rentalmanagement.serverresponse.RegisterResponse;
import com.dee.rentalmanagement.url.Url;

import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileEditActivity extends AppCompatActivity {

    EditText etUName, etUAddress, etUPhone, etUEmail;
    Button btnUpdate;
    CircleImageView imgUUser;
    String id;
    NotificationManagerCompat notificationManagerCompat;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        notificationManagerCompat = notificationManagerCompat.from(this);
        CreateChannel channel = new CreateChannel(this);
        channel.createChannel();

        etUName = findViewById(R.id.etUName);
        etUAddress = findViewById(R.id.etUAddress);
        etUPhone = findViewById(R.id.etUPhone);
        etUEmail = findViewById(R.id.etUEmail);
        imgUUser = findViewById(R.id.imgUser);
        btnUpdate = findViewById(R.id.btnUpdate);
        sharedPreferences = getSharedPreferences("Updates", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateUser();
            }
        });

        UsersAPI usersAPI = Url.getInstance().create(UsersAPI.class);
        Call<User> user = usersAPI.getUsersDetails(Url.token);

        user.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(ProfileEditActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
                etUName.setText(response.body().getFullname());
                etUAddress.setText(response.body().getAddress());
                etUPhone.setText(response.body().getPhone());
                etUEmail.setText(response.body().getEmail());
//
                id = response.body().get_id();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("Message", "OnFailure" + t.getLocalizedMessage());
                Toast.makeText(ProfileEditActivity.this, "Error" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void UpdateUser() {
        UsersAPI usersAPI = Url.getInstance().create(UsersAPI.class);
        String fullname = etUName.getText().toString();
        String address = etUAddress.getText().toString();
        String phone = etUPhone.getText().toString();
        String email = etUEmail.getText().toString();



        Call<RegisterResponse> updatecall = usersAPI.userupdate(Url.token, id, fullname, address, phone, email);

        updatecall.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                Toast.makeText(ProfileEditActivity.this, "Profile updated Sucessfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ProfileEditActivity.this, ProfileActivity.class));
                UpdateNotification();
                finish();
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(ProfileEditActivity.this, "Error", Toast.LENGTH_SHORT).show();

                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(1000);
            }
        });
//
    }
    private void UpdateNotification() {

        Notification notification = new NotificationCompat.Builder(this, CreateChannel.CHANNEL_1)
                .setSmallIcon(R.drawable.imgss)
                .setContentTitle("Gharbeti Application")
                .setContentText("Profile Sucessfully Updated")
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManagerCompat.notify(1, notification);
}
}