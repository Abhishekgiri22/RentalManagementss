package com.dee.rentalmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dee.rentalmanagement.adapter.imgSliderAdapter;
import com.dee.rentalmanagement.api.UsersAPI;
import com.dee.rentalmanagement.model.User;
import com.dee.rentalmanagement.url.Url;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {
    private CardView cvSearchRoom, cvProfile, cvContactUs, cvUploadRoom, cvLogout, cvMap;
    ViewPager viewPager;
    LinearLayout sliderDotsPanel;
    private int dotscount;
    private ImageView[] dots;

    private CircleImageView imgLogin;

//sensors
    TextView ProximitySensor;

    SensorManager sensorManager;
    Sensor proximitySensor;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private float acelVal;
    private float acelLast;
    private float acelShake;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        cvSearchRoom = findViewById(R.id.cvSearchRoom);
        cvProfile = findViewById(R.id.cvProfile);
        cvContactUs = findViewById(R.id.cvContactUs);
        cvUploadRoom = findViewById(R.id.cvUploadRoom);
        cvLogout = findViewById(R.id.cvLogout);
        cvMap = findViewById(R.id.cvMap);
        cvSearchRoom.setOnClickListener(this);
        cvProfile.setOnClickListener(this);
        cvContactUs.setOnClickListener(this);
        cvUploadRoom.setOnClickListener(this);
        cvLogout.setOnClickListener(this);
        cvMap.setOnClickListener(this);
        viewPager = findViewById(R.id.imgSlider);
        imgLogin = findViewById(R.id.imgLogin);

        //for sensors
        sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        acelVal = SensorManager.GRAVITY_EARTH;
        acelLast = SensorManager.GRAVITY_EARTH;
        acelShake = 0.00f;
        ProximitySensor = findViewById(R.id.proximityValue);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if (proximitySensor == null) {
            ProximitySensor.setText("No Proximity Sensor");
        } else {
            sensorManager.registerListener(sensorEventListener, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }


//for Indicator
        sliderDotsPanel = findViewById(R.id.sliderDotsPanel);

        imgSliderAdapter adapter = new imgSliderAdapter(this);
        viewPager.setAdapter(adapter);

        //Indicator
        dotscount = adapter.getCount();
        dots = new ImageView[dotscount];
        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactivedots));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotsPanel.addView(dots[i], params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.activedots));


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactivedots));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.activedots));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private boolean pagerMoved = false;
    private static final long ANIM_VIEWPAGER_DELAY = 5000;

    private Handler h = new Handler();

    private Runnable animateViewPager = new Runnable() {
        @Override
        public void run() {
            if (!pagerMoved) {
                if (viewPager.getCurrentItem() == viewPager.getChildCount()) {
                    viewPager.setCurrentItem(0, true);
                } else {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                }
                pagerMoved = false;
                h.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);

            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();

        if (h != null) {
            h.removeCallbacks(animateViewPager);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        h.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
    }
// Indicators finished


    // for dashboard clicks
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.cvSearchRoom:
                startActivity(new Intent(this, SearchRoomActivity.class));
                break;

            case R.id.cvUploadRoom:
                startActivity(new Intent(this, UploadRoomActivity.class));
                break;

            case R.id.cvContactUs:
                startActivity(new Intent(this, ContactUsActivity.class));
                break;
            case R.id.cvProfile:
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case R.id.cvMap:
                startActivity(new Intent(this, MapsActivity.class));
                break;
            case R.id.cvLogout:
                startActivity(new Intent(this, Login_Form.class));
        }

    }

    // for dashboard clicks ended...........
    //image to view in circleview started
    private void loadCurrentUser() {
        UsersAPI usersAPI = Url.getInstance().create(UsersAPI.class);
        Call<User> userCall = usersAPI.getUsersDetails(Url.token);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    imgLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(DashboardActivity.this, Login_Form.class));
                        }
                    });
                } else {
                    String imgpath = Url.imagePath + response.body().getImage();
                    Picasso.get().load(imgpath).into(imgLogin);

                    imgLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(DashboardActivity.this, "Logged in USer: " + response.body().getFullname(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }


            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(DashboardActivity.this, "Error" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

SensorEventListener sensorEventListener =new SensorEventListener() {
    @Override
    public void onSensorChanged(SensorEvent event) {
        WindowManager.LayoutParams params =DashboardActivity.this.getWindow().getAttributes();
if (event.sensor.getType()== Sensor.TYPE_PROXIMITY){
    if (event.values[0] == 0) {
        params.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.screenBrightness = 0;
        getWindow().setAttributes(params);
    } else {
        params.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.screenBrightness = -1f;
        getWindow().setAttributes(params);
    }
}
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
};
private final SensorEventListener sensorListener = new SensorEventListener() {
    @Override
    public void onSensorChanged(SensorEvent event) {
float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        acelLast=acelVal;
        acelVal=(float) Math.sqrt((double) (x*x + y*y + z*z));
        float delta = acelVal - acelLast;
        acelShake = acelShake * 0.9f + delta;

        if(acelShake > 12) {
            logoutUser();            }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Toast.makeText(DashboardActivity.this, "Error", Toast.LENGTH_SHORT).show();
    }
};
    private void logoutUser() {
        editor.remove("token");
        editor.commit();
        startActivity(new Intent(DashboardActivity.this, MainActivity.class));
    }
}