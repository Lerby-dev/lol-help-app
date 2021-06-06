package com.reduse.lol_abyss.request;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.NetworkError;
import com.android.volley.ServerError;
import com.reduse.lol_abyss.entity.Champion;
import com.reduse.lol_abyss.entity.MatchEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Queue;

public class ApiRequest {

    private final RequestQueue queue;
    private final Context context;
        private static final String API_KEY = "RGAPI-f1545837-efd4-495b-99c0-d2d43ceb0298";
    private final String region = "ru";
    private List<MatchEntity> historyMatches = new ArrayList<>();

    public ApiRequest(RequestQueue queue, Context context) {
        this.queue = queue;
        this.context = context;
    }

    public void checkPlayerName(final String name, final CheckPlayerCallback callback) {

        String url = "https://" + region.toLowerCase() + ".api.riotgames.com/lol/summoner/v4/summoners/by-name/" + name + "?api_key=" + API_KEY;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("APP", response.toString());

                try {

                    String named = response.getString("name").toLowerCase();

                    String id = response.getString("accountId");

                    callback.onSuccess(named, id);

                } catch (JSONException e) {
                    Log.d("APP", "EXCEPTION =" + e);
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError) {
                    callback.onError("No Connection");
                } else if (error instanceof ServerError) {
                    callback.dontExist("Not exist name");
                }
                Log.d("APP", "ERROR = " + error);

            }
        });

        queue.add(request);


    }

    public interface CheckPlayerCallback {
        void onSuccess(String name, String id);

        void dontExist(String message);

        void onError(String message);
    }

    public String getJsonFile(Context context, String filename) {

        String json = null;

        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);


        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return json;
    }

    public String getChampionName(String champId) throws JSONException {

        String json = getJsonFile(context, "champion.json");

        JSONObject champ = new JSONObject(json);
        JSONObject data = champ.getJSONObject("data");
        JSONObject champInfo = null;

       /* Iterator<String> keys = data.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            JSONObject inner = data.getJSONObject(key);
            String champkey = inner.getString("id");
            if(champkey == champId){
                champInfo = inner;
            }
        }*/


        if (data.has(String.valueOf(champId))) {
            champInfo = data.getJSONObject(String.valueOf(champId));
        }
        String champName = "Default";
        if (champInfo != null) {
            JSONObject image = champInfo.getJSONObject("image");
            champName = image.getString("full");
        }
        return champName;

    }

    public String getSummonerName(int spellId) throws JSONException {

        String json = getJsonFile(context, "summoner-spell.json");
        JSONObject summoner = new JSONObject(json);
        JSONObject data = summoner.getJSONObject("data");
        JSONObject summonerInfo = null;
        if (data.has(String.valueOf(spellId))) {
            summonerInfo = data.getJSONObject(String.valueOf(spellId));
        }
        String summonerName = "Default";
        if (summonerInfo != null) {
            JSONObject image = summonerInfo.getJSONObject("image");
            summonerName = image.getString("full");
        }
        return summonerName;
    }

    public void getHistoryMatches(String id, final HistoryCallback callback) {

        String url = "https://" + region + ".api.riotgames.com/lol/match/v4/matchlists/by-account/" + id + "?api_key=" + API_KEY;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.length() > 0) {
                    try {
                        JSONArray games = response.getJSONArray("matches");

                        for (int i = 0; i < 15; i++) {

                            JSONObject oneMatch = games.getJSONObject(i);
                            String matchId = oneMatch.getString("gameId");
                            String champId = oneMatch.getString("champion");
                            String url1 = "https://" + region + ".api.riotgames.com/lol/match/v4/matches/" + matchId + "?api_key=" + API_KEY;


                            getEntityGame(matchId, oneMatch, url1, new HistoryCallback2() {
                                @Override
                                public void onSuccess(MatchEntity matches) {
                                    historyMatches.add(matches);

                                    //Toast.makeText(context,  String.valueOf(historyMatches.size()), Toast.LENGTH_SHORT).show();
                                    callback.onSuccess(historyMatches);
                                }

                                @Override
                                public void noMatch(String message) {

                                }

                                @Override
                                public void onError(String message) {

                                }
                            });

                        }


                        //Toast.makeText(context,  String.valueOf(historyMatches.size()), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Log.d("APP", "EXEPTION HISTORY = " + e);
                        e.printStackTrace();
                    }


                } else {
                    callback.noMatch("");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                if (error instanceof NetworkError) {
                    callback.onError("");
                }

            }
        });

        queue.add(request);

    }

    @NonNull
    public void getEntityGame(String matchId, JSONObject res1, String url1, final HistoryCallback2 callback) {
        final long[] matchCreation = new long[1];
        final long[] matchDuration = new long[1];
        Integer[] items = new Integer[7];
        List<Integer> teamWinners = new ArrayList<>();
        List<Integer> teamLosers = new ArrayList<>();
        final MatchEntity[] s = {new MatchEntity()};

        JsonObjectRequest request = new JsonObjectRequest(url1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response1) {
                try {

                    matchCreation[0] = Long.parseLong(response1.getString("gameCreation"));
                    matchDuration[0] = Long.parseLong(response1.getString("gameDuration"));
                    String typeMatch = response1.getString("gameType");
                    String champId = res1.getString("champion");
                    boolean win = false;
                    typeMatch = typeMatch.replace("_", " ");
                    JSONArray teams = response1.getJSONArray("teams");
                    //JSONObject team1 = teams.getJSONObject(0);
                    // JSONObject team2 = teams.getJSONObject(1);
                    // String winner1 = team1.getString("win");

                    int kills = 0;
                    int deaths = 0;
                    int assists = 0;
                    int gold = 0;
                    int cs = 0;
                    int spelll = 0;
                    int spell2 = 0;
                    int champLevel = 0;
                    //JSONArray player = team1.getJSONArray("championId");
                    //JSONArray player2 = team2.getJSONArray("championId");
            /*if(winner1 == "Win"){
                for(int i = 0; i < player.length(); i++){
                    teamWinners.add(Integer.parseInt(player.getJSONObject(i).toString()));
                    teamLosers.add(Integer.parseInt(player2.getJSONObject(i).toString()));
                }
            }else{
                for(int i = 0; i < player.length(); i++){

                    teamWinners.add(Integer.parseInt(player2.getJSONObject(i).toString()));
                    teamLosers.add(Integer.parseInt(player.getJSONObject(i).toString()));
                }
            }*/
                    String number = null;
                    String number2 = null;
                    JSONArray peoples = response1.getJSONArray("participants");
                    for (int i = 0; i < 10; i++) {

                        JSONObject onePeople = peoples.getJSONObject(i);
                        if (onePeople.getString("championId") == champId) {
                            JSONObject stats = onePeople.getJSONObject("stats");
                            kills = Integer.parseInt(stats.getString("kills"));


                            deaths = Integer.parseInt(stats.getString("deaths"));
                            assists = Integer.parseInt(stats.getString("assists"));
                            gold = Integer.parseInt(stats.getString("goldEarned"));
                            cs = Integer.parseInt(stats.getString("totalMinionsKilled"));
                            champLevel = Integer.parseInt(stats.getString("champLevel"));
                            spelll = Integer.parseInt(onePeople.getString("spell1Id"));
                            spell2 = Integer.parseInt(onePeople.getString("spell2Id"));
                            items[0] = Integer.parseInt(stats.getString("item0"));
                            items[1] = Integer.parseInt(stats.getString("item1"));
                            items[2] = Integer.parseInt(stats.getString("item2"));
                            items[3] = Integer.parseInt(stats.getString("item3"));
                            items[4] = Integer.parseInt(stats.getString("item4"));
                            items[5] = Integer.parseInt(stats.getString("item5"));
                            items[6] = Integer.parseInt(stats.getString("item6"));

                            win = Boolean.parseBoolean(stats.getString("win"));
                            if (win == true) {
                                number = onePeople.getString("teamId");
                            } else {
                                number2 = onePeople.getString("teamId");
                            }

                        }
                        JSONObject st = onePeople.getJSONObject("stats");
                        if(Boolean.parseBoolean(st.getString("win")) == false){
                            teamLosers.add(Integer.parseInt(onePeople.getString("championId")));
                        }else{
                            teamWinners.add(Integer.parseInt(onePeople.getString("championId")));
                        }

                    }



                    s[0] = new MatchEntity(win, matchId, matchCreation[0], matchDuration[0], champId, kills, deaths, assists, gold, cs, champLevel,
                            null, items, getSummonerName(spelll), getSummonerName(spell2), getChampionName(champId), typeMatch, teamWinners, teamLosers);

                    //historyMatches.add(s[0]);
                    callback.onSuccess(s[0]);
                    //Toast.makeText(context,  "Was Added", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
        //MatchEntity singleMatch = new MatchEntity(win, matchId, matchCreation, matchDuration, champId, kills, deaths,
        // assists, gold, cs, champLevel, statistiques, items, sum1Name, sum2Name, champName, typeMatch, teamWinners, teamLosers);
    }

    public interface HistoryCallback {
        void onSuccess(List<MatchEntity> matches);

        void noMatch(String message);

        void onError(String message);
    }

    public interface HistoryCallback2 {
        void onSuccess(MatchEntity matches);

        void noMatch(String message);

        void onError(String message);
    }

    public void getAllChampions(AllChampionsCallback callback) {
        List<Champion> allChampions = new ArrayList<>();
        String json = getJsonFile(context, "championNew.json");
        String champName = null;
        JSONObject champ = null;

        try {
            champ = new JSONObject(json);
            JSONObject data = champ.getJSONObject("data");

            Iterator<String> keys = data.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                JSONObject inner = data.getJSONObject(key);
                JSONObject image = inner.getJSONObject("image");
                String champImage = image.getString("full");
                Champion champion = new Champion();
                //champion.setId(String.valueOf(key));
                champion.setImageName(champImage);
                allChampions.add(champion);
            }

            callback.onSuccess(allChampions);

        } catch (JSONException e) {
            callback.onError("");
            e.printStackTrace();
        }

    }

    public interface AllChampionsCallback {
        void onSuccess(List<Champion> listChampions);

        void onError(String message);
    }
}