package com.dee.rentalmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dee.rentalmanagement.adapter.RoomsAdapter;
import com.dee.rentalmanagement.adapter.ViewPagerAdapter;
import com.dee.rentalmanagement.api.RoomAPI;
import com.dee.rentalmanagement.model.Room;
import com.dee.rentalmanagement.url.Url;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchRoomActivity extends AppCompatActivity {
    private RecyclerView rvRooms;
    SwipeRefreshLayout refreshLayout;

    Button btnMyRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_room);

        btnMyRoom = findViewById(R.id.btnMyroom);




rvRooms = findViewById(R.id.rvRooms);
refreshLayout = findViewById(R.id.refreshLayout);
showRooms();

refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
    @Override
    public void onRefresh() {

    }
});

     }
private void showRooms(){
refreshLayout.setRefreshing(false);
    Retrofit retrofit = new Retrofit.Builder().baseUrl(Url.base_url).addConverterFactory(GsonConverterFactory.create()).build();
    RoomAPI roomAPI = retrofit.create(RoomAPI.class);

    Call<List<Room>> proListCall =roomAPI.getAllRooms();

    proListCall.enqueue(new Callback<List<Room>>() {
        @Override
        public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
if (!response.isSuccessful()){
    refreshLayout.setRefreshing(true);
    Toast.makeText(SearchRoomActivity.this, "Error Code" + response.code(), Toast.LENGTH_SHORT).show();
return;
}
List<Room>RoomList = response.body();
            RoomsAdapter roomsAdapter = new RoomsAdapter(SearchRoomActivity.this,RoomList);
rvRooms.setAdapter(roomsAdapter);
rvRooms.setLayoutManager(new LinearLayoutManager(SearchRoomActivity.this, LinearLayoutManager.VERTICAL,false));

        }

        @Override
        public void onFailure(Call<List<Room>> call, Throwable t) {
            Log.d("MSG", "onFailure" + t.getLocalizedMessage());
            Toast.makeText(SearchRoomActivity.this, "Error" +t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    });
}

}
