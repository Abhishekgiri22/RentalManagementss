package com.dee.rentalmanagement;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.loader.content.CursorLoader;

import com.dee.rentalmanagement.api.UsersAPI;
import com.dee.rentalmanagement.model.User;
import com.dee.rentalmanagement.notification.CreateChannel;
import com.dee.rentalmanagement.serverresponse.ImageResponse;
import com.dee.rentalmanagement.serverresponse.RegisterResponse;
import com.dee.rentalmanagement.strictmode.StrictModeClass;
import com.dee.rentalmanagement.url.Url;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signup_Form extends AppCompatActivity {

    EditText etName, etAddress,etPhone,etEmail, etPassword, etConPassword ;
    Button btnRegister;
    TextView tvLoginLink;
    CircleImageView imgProfile;
     String imagePath;
    private String imageName = "";
    NotificationManagerCompat notificationManagerCompat;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup__form);

        notificationManagerCompat = notificationManagerCompat.from(this);
        CreateChannel channel = new CreateChannel(this);
        channel.createChannel();



        etName = findViewById(R.id.etName);
        etAddress = findViewById(R.id.etAddress);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConPassword = findViewById(R.id.etConpassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLoginLink = findViewById(R.id.tvLoginLink);
        imgProfile = findViewById(R.id.imgProfile);
        sharedPreferences = getSharedPreferences("Signup", MODE_PRIVATE);
        editor = sharedPreferences.edit();


        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrowseImage();
            }
        });
tvLoginLink.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Signup_Form.this,Login_Form.class);
startActivity(intent);
    }
});

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPassword.getText().toString().equals(etConPassword.getText().toString())) {
                    if (validate()) {
                        saveImageOnly();
                        register();
                    }
                } else {
                    Toast.makeText(Signup_Form.this, "Password does not match", Toast.LENGTH_SHORT).show();
                    etPassword.requestFocus();
                    return;
                }

            }
        });
    }

    private boolean validate() {
        boolean status = true;
        if (etEmail.getText().toString().length() < 6) {
//            etEmail.setError("Minimum 6 character");
            status = false;
        }
        return status;
    }

    private void BrowseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (data == null) {
                Toast.makeText(this, "Please select an image ", Toast.LENGTH_SHORT).show();
            }
        }
        Uri uri = data.getData();
        imgProfile.setImageURI(uri);
        imagePath = getRealPathFromUri(uri);
    }

    private String getRealPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(),
                uri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int colIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(colIndex);
        cursor.close();
        return result;
    }

    private void saveImageOnly() {
        File file = new File(imagePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("imageFile",
                file.getName(), requestBody);

        UsersAPI usersAPI = Url.getInstance().create(UsersAPI.class);
        Call<ImageResponse> responseBodyCall = usersAPI.uploadImage(body);

        StrictModeClass.StrictMode();
        //Synchronous method
        try {
            Response<ImageResponse> imageResponseResponse = responseBodyCall.execute();
            imageName = imageResponseResponse.body().getFilename();
            Toast.makeText(this, "Image inserted" + imageName, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    private void register() {
        String email = etEmail.getText().toString();
        String fullname = etName.getText().toString();
        String password = etPassword.getText().toString();
        String conpassword = etConPassword.getText().toString();
        String phone = etPhone.getText().toString();
        String address = etAddress.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(Signup_Form.this, "Please enter Email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(Signup_Form.this, "Please enter Password", Toast.LENGTH_SHORT).show();
            return;
        }


        if (TextUtils.isEmpty(conpassword)) {
            Toast.makeText(Signup_Form.this, "Please enter Confirm Password", Toast.LENGTH_SHORT).show();
            return;
        }


        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(Signup_Form.this, "Please enter Phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(address)) {
            Toast.makeText(Signup_Form.this, "Please enter Address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(Signup_Form.this, "Password too Short ", Toast.LENGTH_SHORT).show();
        } else {


            User users = new User(fullname, address, phone, email, password, conpassword, imageName);

            UsersAPI usersAPI = Url.getInstance().create(UsersAPI.class);
            Call<RegisterResponse> registerCall = usersAPI.registerUser(users);

            registerCall.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(Signup_Form.this, "Code " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(Signup_Form.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                    SignupNotification();
                    finish();
                }


                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    Toast.makeText(Signup_Form.this, "Errors" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void SignupNotification() {

        Notification notification = new NotificationCompat.Builder(this, CreateChannel.CHANNEL_1)
                .setSmallIcon(R.drawable.ic_message)
                .setContentTitle("Gharbeti Application")
                .setContentText("User Sucessfully Registered")
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManagerCompat.notify(1, notification);

    }
}

