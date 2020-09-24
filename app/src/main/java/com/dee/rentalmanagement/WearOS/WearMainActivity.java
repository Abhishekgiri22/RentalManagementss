package com.dee.rentalmanagement.WearOS;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dee.rentalmanagement.R;

public class WearMainActivity extends AppCompatActivity {
    private TextView tv;
    private Button btn;
    private EditText et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wear_main);

        tv = findViewById(R.id.tv);
        btn = findViewById(R.id.btn);
        et = findViewById(R.id.et);

btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
tv.setText(et.getText().toString());
    }
});
setAmbinetEnabled();
    }

    private void setAmbinetEnabled() {
    }

}
