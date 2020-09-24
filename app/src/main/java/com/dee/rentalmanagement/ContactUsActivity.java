package com.dee.rentalmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dee.rentalmanagement.api.UsersAPI;
import com.dee.rentalmanagement.model.Contact;
import com.dee.rentalmanagement.model.User;
import com.dee.rentalmanagement.notification.CreateChannel;
import com.dee.rentalmanagement.url.Url;

import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsActivity extends AppCompatActivity {
    EditText etName, etEmail, etMessage;
    Button btnCSubmit;
    NotificationManagerCompat notificationManagerCompat;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private TextView tvresult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        notificationManagerCompat = notificationManagerCompat.from(this);
        CreateChannel channel = new CreateChannel(this);
        channel.createChannel();


        etMessage = findViewById(R.id.etMessage);
        btnCSubmit = findViewById(R.id.btnCSubmit);
        tvresult=findViewById(R.id.tvresult);
        sharedPreferences = getSharedPreferences("Message", MODE_PRIVATE);
        editor = sharedPreferences.edit();







btnCSubmit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
contact();
    }
});
    }

    private void contact() {
        UsersAPI usersAPI = Url.getInstance().create(UsersAPI.class);
        Call<User> userCall=usersAPI.getUsersDetails(Url.token);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                String fullname = response.body().getFullname();
                String email = response.body().getEmail();
                String message = etMessage.getText().toString();

                Contact contacts = new Contact(fullname, email, message);

                UsersAPI usersAPI = Url.getInstance().create(UsersAPI.class);
                Call<Void> registerCall = usersAPI.contactUs(Url.token, fullname, email, message);

                registerCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (!response.isSuccessful())
                        {

                        Toast.makeText(ContactUsActivity.this, "Message cannot be added", Toast.LENGTH_SHORT).show();
                }
                        else{
                            Toast.makeText(ContactUsActivity.this, "Message added sucessfully", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(ContactUsActivity.this, "Message cannot be added", Toast.LENGTH_SHORT).show();
                    }
                    });
                }


            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ContactUsActivity.this, "Message cannot be added", Toast.LENGTH_SHORT).show();
            }
            });
        }

    private void MessageNotification() {

        Notification notification = new NotificationCompat.Builder(this, CreateChannel.CHANNEL_1)
                .setSmallIcon(R.drawable.ic_message)
                .setContentTitle("Gharbeti Application")
                .setContentText("Feedback Sucessfully sent to Company")
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManagerCompat.notify(1, notification);

    }
    }
