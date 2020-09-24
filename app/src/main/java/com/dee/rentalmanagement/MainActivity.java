package com.dee.rentalmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
Button btnIn, btnUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

btnIn = findViewById(R.id.btnIn);
btnUp = findViewById(R.id.btnUp);

btnIn.setOnClickListener(this);
btnUp.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
switch (v.getId()){
    case R.id.btnIn:
startActivity(new Intent(this,Login_Form.class));
break;
    case R.id.btnUp:
startActivity(new Intent(this,Signup_Form.class));
}
    }
}
