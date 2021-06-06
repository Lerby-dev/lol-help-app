package com.reduse.lol_abyss;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.app.ActivityManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.material.navigation.NavigationView;
import com.reduse.lol_abyss.request.ApiRequest;

public class SearchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private EditText name;
    private Button btnSearch;
    private ProgressBar pbLoader;
    private TextView tv_recent;
    private RequestQueue queue;
    private ApiRequest request;

    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;

    private Handler handler;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String recentSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        pref = this.getSharedPreferences("lolPrefs", 0);
        editor = pref.edit();
        recentSearch = pref.getString("RECENT", null);

        queue = MySingleton.getInstance(this).getRequestQueue();
        request = new ApiRequest(queue, this);
        handler = new Handler();

        name = (EditText) findViewById(R.id.et_player);
        btnSearch = (Button) findViewById(R.id.btn_send);
        //pbLoader = (ProgressBar) findViewById(R.id.pb_search);
        tv_recent = (TextView) findViewById(R.id.tv_recent);


       // mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        if(recentSearch != null) {
            tv_recent.setText(recentSearch);

            tv_recent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    request.checkPlayerName(recentSearch, new ApiRequest.CheckPlayerCallback() {
                        @Override
                        public void onSuccess(String name, String id) {
                            //pbLoader.setVisibility(View.INVISIBLE);
                            Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                            Bundle extras = new Bundle();
                            extras.putString("NAME", name);
                            extras.putString("ID", id);
                            intent.putExtras(extras);
                            startActivity(intent);

                        }

                        @Override
                        public void dontExist(String message) {
                            //pbLoader.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(String message) {
                            // pbLoader.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

    }




    public void OnClick(View v) {
        final String recherche = name.getText().toString().trim();

        if (recherche.length() > 0) {
            //pbLoader.setVisibility(View.VISIBLE);
            //Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
            //startActivity(intent);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    request.checkPlayerName(recherche, new ApiRequest.CheckPlayerCallback() {
                        @Override
                        public void onSuccess(String name, String id) {
                            // pbLoader.setVisibility(View.INVISIBLE);

                            Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                            Bundle extras = new Bundle();
                            extras.putString("NAME", name);
                            extras.putString("ACCOUNTID", id);

                            intent.putExtras(extras);
                            editor.putString("RECENT", recherche);
                            editor.commit();
                            startActivity(intent);


                        }

                        @Override
                        public void dontExist(String message) {
                            // pbLoader.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(String message) {
                            //pbLoader.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }, 500);


        } else {
            Toast.makeText(getApplicationContext(), "Enter name", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        if(menuItem.isChecked()){
            menuItem.setChecked(false);
        }else{
            menuItem.setChecked(true);
        }

       // mDrawerLayout.closeDrawers();

        switch (menuItem.getItemId()){


            case R.id.rechercher :
                /*Intent i = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(i);
                finish();*/
                return true;
            case R.id.stats :
              Intent intent = new Intent(getApplicationContext(), ChampionsActivity.class);
                startActivity(intent);
                finish();

        }

        return true;

    }

}