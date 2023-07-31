package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends Activity {

    private static Context contextOfThis;

    public static void gotoGameOverIntentFn() {
        /*Bundle bundle = new Bundle();
        Intent intent = new Intent(MainActivity.this, gameover.class);
        startActivity(intent);*/

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        LinearLayout container = findViewById(R.id.container);
        MovingObstaclesView movingObstaclesView = new MovingObstaclesView(this, null);
        container.addView(movingObstaclesView);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        contextOfThis = getApplicationContext();
        movingObstaclesView.startMoving();
    }

    private void getTips() {
    }


}

