package com.reduse.lol_abyss;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.reduse.lol_abyss.adapter.GridAdapter;
import com.reduse.lol_abyss.entity.Champion;
import com.reduse.lol_abyss.request.ApiRequest;

import java.util.Collections;
import java.util.List;

public class ChampionsActivity extends AppCompatActivity {

    private ProgressBar pb_loader;
    private GridView gv_gallery;
    private RequestQueue queue;
    private ApiRequest request;
    private GridAdapter adapter;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champions);

        queue = MySingleton.getInstance(this).getRequestQueue();
        request = new ApiRequest(queue, this);

        pb_loader = (ProgressBar) findViewById(R.id.pb_champ_loader);
        gv_gallery = (GridView) findViewById(R.id.gv_galerry);
        pb_loader.setVisibility(View.VISIBLE);
        handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                request.getAllChampions(new ApiRequest.AllChampionsCallback() {
                    @Override
                    public void onSuccess(List<Champion> listChampions) {
                        adapter = new GridAdapter(getApplicationContext(), listChampions);
                        Collections.sort(listChampions);
                        gv_gallery.setAdapter(adapter);
                        pb_loader.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(String message) {
                        pb_loader.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 1000);


    }
}