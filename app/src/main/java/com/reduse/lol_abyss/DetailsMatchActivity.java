package com.reduse.lol_abyss;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.reduse.lol_abyss.entity.MatchEntity;
import com.reduse.lol_abyss.helper.Helper;
import com.reduse.lol_abyss.request.ApiRequest;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Iterator;

public class DetailsMatchActivity extends AppCompatActivity {
    private RelativeLayout rlInfosJoueur;
    private TextView typeMatch, level, gold, cs, kda, duration, creation;
    private TextView statistiques2;
    private ImageView portrait, sum1, sum2, item1, item2, item3, item4, item5, item6, item7, vainqueur1, vainqueur2,
            vainqueur3, vainqueur4, vainqueur5, perdant1, perdant2, perdant3, perdant4, perdant5;
    private TableLayout statistiques;
    private RequestQueue queue;
    private ApiRequest request;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_match);

        initialize();

        Intent i = getIntent();
        if (i.hasExtra("ONE_MATCH")) {
            MatchEntity oneMatch = (MatchEntity) i.getSerializableExtra("ONE_MATCH");

            queue = MySingleton.getInstance(this).getRequestQueue();
            request = new ApiRequest(queue, this);
            if (oneMatch.isWinner()) {
                rlInfosJoueur.setBackgroundColor(R.color.win_row_bg);
            } else {
                rlInfosJoueur.setBackgroundColor(R.color.lose_row_bg);
            }

            Picasso.with(this).load("http://ddragon.leagueoflegends.com/cdn/11.8.1/img/champion/" + oneMatch.getChampName()).into(portrait);
            if (oneMatch.getSum1().equals("Default")) {
                Picasso.with(this).load(R.drawable.empty).into(sum1);
            } else {
                Picasso.with(this).load("http://ddragon.leagueoflegends.com/cdn/11.8.1/img/spell/" + oneMatch.getSum1()).into(sum1);
            }
            if (oneMatch.getSum2().equals("Default")) {
                Picasso.with(this).load(R.drawable.empty).into(sum2);
            } else {
                Picasso.with(this).load("http://ddragon.leagueoflegends.com/cdn/11.8.1/img/spell/" + oneMatch.getSum2()).into(sum2);
            }
            if(oneMatch.getItems()[0] != null){
            Helper.setImageItems(this, oneMatch.getItems()[0], item1);}
            if(oneMatch.getItems()[1] != null){
            Helper.setImageItems(this, oneMatch.getItems()[1], item2);}
            if(oneMatch.getItems()[2] != null){
            Helper.setImageItems(this, oneMatch.getItems()[2], item3);}
            if(oneMatch.getItems()[3] != null){
            Helper.setImageItems(this, oneMatch.getItems()[3], item4);}
            if(oneMatch.getItems()[4] != null){
            Helper.setImageItems(this, oneMatch.getItems()[4], item5);}
            if(oneMatch.getItems()[5] != null){
            Helper.setImageItems(this, oneMatch.getItems()[5], item6);}
            if(oneMatch.getItems()[6] != null){
            Helper.setImageItems(this, oneMatch.getItems()[6], item7);}


            typeMatch.setText(oneMatch.getTypeMatch());
            level.setText("Level " + String.valueOf(oneMatch.getChampLevel()));
            gold.setText(String.valueOf(Math.round(oneMatch.getGold() / 1000.0)) + "K");
            cs.setText(String.valueOf(oneMatch.getCs()));
            kda.setText(oneMatch.getKills() + "/" + oneMatch.getDeaths() + "/" + oneMatch.getAssists());
            duration.setText(Helper.convertDuration(oneMatch.getMatchDuration()));
            creation.setText(Helper.convertDate(oneMatch.getMatchCreation()));
            if(oneMatch.getGold() < 10000){
            statistiques2.setText(statistiques2.getText() +  " Bad with money ");}
            if(oneMatch.getKills() +  oneMatch.getAssists() < 7){
                statistiques2.setText(statistiques2.getText() + " Little involved in murders ");}
            if(oneMatch.getCs() < 100){
                statistiques2.setText(statistiques2.getText() + " Farm poorly ");}
            if(oneMatch.getCs() >= 100 && oneMatch.getGold() >= 10000 && oneMatch.getKills() +  oneMatch.getAssists() >= 7){
                statistiques2.setText(" Good game ");}


            float density = getResources().getDisplayMetrics().density;
            int size = (int) (70 * density);

            Helper.setImagePortraits(this, oneMatch.getTeamWinner().get(0).toString(), vainqueur1, request);
            Helper.setImagePortraits(this, oneMatch.getTeamWinner().get(1).toString(), vainqueur2, request);
            Helper.setImagePortraits(this, oneMatch.getTeamWinner().get(2).toString(), vainqueur3, request);
            Helper.setImagePortraits(this, oneMatch.getTeamWinner().get(3).toString(), vainqueur4, request);

            /*if (oneMatch.getChampId() == oneMatch.getTeamWinner().get(4) && oneMatch.isWinner() == true) {
                vainqueur5.getLayoutParams().height = size;
                vainqueur5.getLayoutParams().width = size;
            }*/
            Helper.setImagePortraits(this, oneMatch.getTeamWinner().get(4).toString(), vainqueur5, request);



            if(oneMatch.getTeamLoser().size() > 0) {
                Helper.setImagePortraits(this, oneMatch.getTeamLoser().get(0).toString(), perdant1, request);
                Helper.setImagePortraits(this, oneMatch.getTeamLoser().get(1).toString(), perdant2, request);
                Helper.setImagePortraits(this, oneMatch.getTeamLoser().get(2).toString(), perdant3, request);
                Helper.setImagePortraits(this, oneMatch.getTeamLoser().get(3).toString(), perdant4, request);

               /*if (oneMatch.getChampId() == oneMatch.getTeamLoser().get(4) && oneMatch.isWinner() == false) {
                    perdant5.getLayoutParams().height = size;
                    perdant5.getLayoutParams().width = size;
                }*/
                Helper.setImagePortraits(this, oneMatch.getTeamLoser().get(4).toString(), perdant5, request);
            }
            Helper.setImagePortraits(this, oneMatch.getTeamLoser().get(4).toString(), perdant5, request);



            /*Iterator iterator = oneMatch.getStats().entrySet().iterator();
            while (iterator.hasNext()) {

                HashMap.Entry key = (HashMap.Entry) iterator.next();

                TableRow row = new TableRow(this);
                TextView tv_key = new TextView(this);
                tv_key.setTextSize(15);
                TextView tv_value = new TextView(this);
                tv_value.setTextSize(15);
                row.addView(tv_key);
                row.addView(tv_value);

                LinearLayout.LayoutParams key_params = (LinearLayout.LayoutParams) tv_key.getLayoutParams();
                key_params.width = 0;
                key_params.weight = 1;
                key_params.height = (int) (20 * density);
                tv_key.setLayoutParams(key_params);

                LinearLayout.LayoutParams value_params = (LinearLayout.LayoutParams) tv_value.getLayoutParams();
                value_params.gravity = Gravity.RIGHT;
                value_params.height = (int) (20 * density);
                tv_value.setLayoutParams(value_params);

                tv_key.setText(key.getKey().toString());
                tv_value.setText(key.getValue().toString());

                statistiques.addView(row, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


            }*/


        }

    }

    public void initialize() {

        rlInfosJoueur = (RelativeLayout) findViewById(R.id.rl_infos_joueur);
        portrait = (ImageView) findViewById(R.id.iv_details_portrait);
        sum1 = (ImageView) findViewById(R.id.iv_details_sum1);
        sum2 = (ImageView) findViewById(R.id.iv_details_sum2);
        typeMatch = (TextView) findViewById(R.id.tv_details_type_match);
        level = (TextView) findViewById(R.id.tv_details_level);
        gold = (TextView) findViewById(R.id.tv_details_gold);
        cs = (TextView) findViewById(R.id.tv_details_cs);
        kda = (TextView) findViewById(R.id.tv_details_kda);
        duration = (TextView) findViewById(R.id.tv_details_duration);
        creation = (TextView) findViewById(R.id.tv_details_creation);
        item1 = (ImageView) findViewById(R.id.iv_details_item1);
        item2 = (ImageView) findViewById(R.id.iv_details_item2);
        item3 = (ImageView) findViewById(R.id.iv_details_item3);
        item4 = (ImageView) findViewById(R.id.iv_details_item4);
        item5 = (ImageView) findViewById(R.id.iv_details_item5);
        item6 = (ImageView) findViewById(R.id.iv_details_item6);
        item7 = (ImageView) findViewById(R.id.iv_details_item7);
        vainqueur1 = (ImageView) findViewById(R.id.iv_vainqueur1);
        vainqueur2 = (ImageView) findViewById(R.id.iv_vainqueur2);
        vainqueur3 = (ImageView) findViewById(R.id.iv_vainqueur3);
        vainqueur4 = (ImageView) findViewById(R.id.iv_vainqueur4);
        vainqueur5 = (ImageView) findViewById(R.id.iv_vainqueur5);
        perdant1 = (ImageView) findViewById(R.id.iv_perdants1);
        perdant2 = (ImageView) findViewById(R.id.iv_perdants2);
        perdant3 = (ImageView) findViewById(R.id.iv_perdants3);
        perdant4 = (ImageView) findViewById(R.id.iv_perdants4);
        perdant5 = (ImageView) findViewById(R.id.iv_perdants5);
        statistiques = (TableLayout) findViewById(R.id.tl_details_stats);
        statistiques2 = (TextView) findViewById(R.id.tv_details_stats2);


    }
}