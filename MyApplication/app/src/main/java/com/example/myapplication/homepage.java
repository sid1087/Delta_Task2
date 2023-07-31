package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class homepage extends AppCompatActivity {

    boolean defaultValue = false;
    Button btn = (Button)findViewById(R.id.startbutton);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);


        //btn.setOnClickListener(new View.OnClickListener() {
          //  @Override
        //   public void onClick(View v) {
        //        Intent intent = new Intent(homepage.this, MainActivity.class);
         //       startActivity(intent);
           // }
       // });
    }


}