package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIMEOUT = 4500; // Splash screen duration in milliseconds
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        TextView tipText = findViewById(R.id.tipText);
        // Make API call to retrieve game tip
        makeApiCallForGameTip();
    }

    private void makeApiCallForGameTip() {

        recyclerView = findViewById(R.id.recyclerView);
        TextView tipText = findViewById(R.id.tipText);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl("https://api-obstacle-dodge.vercel.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceholder jsonPlaceholder = retrofit.create(JsonPlaceholder.class);

        Call<Post> call = jsonPlaceholder.getTip();
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                Post randomTipData = response.body();
                tipText.setText(randomTipData.getTip());
            }

            @Override
            public void onFailure(Call<Post> call, Throwable throwable) {
                System.out.println(throwable);
            }
        });

        Request characterRequest = new Request();
        call = jsonPlaceholder.postData(characterRequest);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    Post Response = response.body();
                    System.out.println(Response.getCharacter().getName());
                    System.out.println(Response.getCharacter().getDescription());
                } else {
                    ;// Handle error response
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                ;// Handle network failures or other errors
            }
        });

        // Delay launching the main activity for a few seconds to display the splash screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);

                finish();
            }
        }, SPLASH_TIMEOUT);
    }
}
