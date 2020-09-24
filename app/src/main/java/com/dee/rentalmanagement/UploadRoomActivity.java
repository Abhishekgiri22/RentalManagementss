package com.dee.rentalmanagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.loader.content.CursorLoader;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dee.rentalmanagement.api.RoomAPI;
import com.dee.rentalmanagement.api.UsersAPI;
import com.dee.rentalmanagement.model.Room;
import com.dee.rentalmanagement.model.User;
import com.dee.rentalmanagement.notification.CreateChannel;
import com.dee.rentalmanagement.serverresponse.ImageResponse;
import com.dee.rentalmanagement.serverresponse.RegisterResponse;
import com.dee.rentalmanagement.serverresponse.RoomResponse;
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

import static com.dee.rentalmanagement.url.Url.imagePath;

public class UploadRoomActivity extends AppCompatActivity {

    EditText etTitle, etlocation, etPhone, etPrice, etdetails;
    Button btnuploadRoom;
    CircleImageView imgRoom;
    String imagePath;
    private String imageName = "";
    NotificationManagerCompat notificationManagerCompat;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_room);

        notificationManagerCompat = notificationManagerCompat.from(this);
        CreateChannel channel = new CreateChannel(this);
        channel.createChannel();



        etTitle = findViewById(R.id.etTitle);
        etlocation = findViewById(R.id.etlocation);
        etPhone = findViewById(R.id.etPhone);
        etPrice = findViewById(R.id.etPrice);
        etdetails = findViewById(R.id.etdetails);
        btnuploadRoom = findViewById(R.id.btnuploadRoom);
        imgRoom = findViewById(R.id.imgRoom);

        imgRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrowseImage();

            }
        });
        btnuploadRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    uploadRoom();
                    saveImageOnly();
                } else {
                    Toast.makeText(UploadRoomActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                    etTitle.requestFocus();

                }
                return;
            }
        });
    }


    private boolean validate() {
        boolean status = true;
        if (etTitle.getText().toString().length() < 6) {
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
        imgRoom.setImageURI(uri);
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

    private void uploadRoom() {

        String title = etTitle.getText().toString();
        String location = etlocation.getText().toString();
        String phone = etPhone.getText().toString();
        String price = etPrice.getText().toString();
        String details = etdetails.getText().toString();

        Room room = new Room(title, location, phone, price, details, imageName);

        RoomAPI roomAPI = Url.getInstance().create(RoomAPI.class);
        Call<RegisterResponse> registerCall = roomAPI.addRoom(room);

       registerCall.enqueue(new Callback<RegisterResponse>() {
           @Override
           public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
if (!response.isSuccessful()){
    Toast.makeText(UploadRoomActivity.this, "Code" + response.code(), Toast.LENGTH_SHORT).show();
return;
}
               Toast.makeText(UploadRoomActivity.this, "Uploaded Sucessfully", Toast.LENGTH_SHORT).show();
                RoomNotification();
                finish();
           }

           @Override
           public void onFailure(Call<RegisterResponse> call, Throwable t) {
               Toast.makeText(UploadRoomActivity.this, "Error" +t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

           }
       });
    }
    private void RoomNotification() {

        Notification notification = new NotificationCompat.Builder(this, CreateChannel.CHANNEL_1)
                .setSmallIcon(R.drawable.ic_message)
                .setContentTitle("Gharbeti Application")
                .setContentText("Room Successfully Uploaded")
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManagerCompat.notify(1, notification);

    }

}

