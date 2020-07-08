package com.example.sevenspices.Register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.sevenspices.R;

public class NameAndInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_and_info);
    }

    public void SignUpAndContinue(View view) {
        startActivity(new Intent(NameAndInfo.this,UsernameAndPassword.class));
    }
}