package com.reduse.lol_abyss;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.google.android.material.navigation.NavigationView;
import com.reduse.lol_abyss.adapter.MyAdapter;
import com.reduse.lol_abyss.entity.MatchEntity;
import com.reduse.lol_abyss.request.ApiRequest;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity  extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private String playerName;
    private String playerId;

    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private RecyclerView recyclerView;
    private MyAdapter mAdapter;
    private ApiRequest request;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

       queue = MySingleton.getInstance(this).getRequestQueue();
        request = new ApiRequest(queue, this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        /*toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        if (extras.getString("NAME") != null && extras.getString("ACCOUNTID").length() > 0) {

            playerName = extras.getString("NAME");
            playerId = extras.getString("ACCOUNTID");

            setTitle(playerName);
        } else {
            // may be SearchActivity exist
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
      /*  ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();*/
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.rv_match);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
      recyclerView.setHasFixedSize(true);





        request.getHistoryMatches(playerId, new ApiRequest.HistoryCallback() {
            @Override
            public void onSuccess(List<MatchEntity> matches) {
                Log.d("APP", "MATCH = " + matches.toString());
                mAdapter = new MyAdapter(getApplicationContext(), matches);
                //Toast.makeText(getApplicationContext(), "matches.get(1).getMatchId()", Toast.LENGTH_SHORT).show();
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void noMatch(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        if(menuItem.isChecked()){
            menuItem.setChecked(false);
        }else{
            menuItem.setChecked(true);
        }

        mDrawerLayout.closeDrawers();

        switch (menuItem.getItemId()){

           
            case R.id.rechercher :
                Intent i = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(i);
                finish();
                return true;
            case R.id.stats :
                Intent intent = new Intent(getApplicationContext(), ChampionsActivity.class);
                startActivity(intent);
                finish();

        }

        return true;

    }
}