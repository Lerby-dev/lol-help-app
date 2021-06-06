package com.reduse.lol_abyss.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MatchEntity implements Serializable {

    private boolean winner;
    private String matchId;
    private long matchCreation;
    private long matchDuration;
    private String champId;
    private int kills;
    private int deaths;
    private int assists;
    private int gold;
    private int cs;
    private int champLevel;
    private LinkedHashMap<String, Integer> stats = new LinkedHashMap<>();
    private Integer[] items = new Integer[7];
    private String sum1, sum2, champName, typeMatch;
    private List<Integer> teamWinner = new ArrayList<>();
    private List<Integer> teamLoser = new ArrayList<>();

    public MatchEntity(boolean winner, String matchId, long matchCreation, long matchDuration, String champId, int kills, int deaths, int assists, int gold, int cs, int champLevel, LinkedHashMap<String, Integer> stats, Integer[] items, String sum1, String sum2, String champName, String typeMatch, List<Integer> teamWinner, List<Integer> teamLoser) {
        this.winner = winner;
        this.matchId = matchId;
        this.matchCreation = matchCreation;
        this.matchDuration = matchDuration;
        this.champId = champId;
        this.kills = kills;
        this.deaths = deaths;
        this.assists = assists;
        this.gold = gold;
        this.cs = cs;
        this.champLevel = champLevel;
        this.stats = stats;
        this.items = items;
        this.sum1 = sum1;
        this.sum2 = sum2;
        this.champName = champName;
        this.typeMatch = typeMatch;
        this.teamWinner = teamWinner;
        this.teamLoser = teamLoser;
    }

    public MatchEntity() {
    }

    public boolean isWinner() {
        return winner;
    }

    public String getMatchId() {
        return matchId;
    }

    public long getMatchCreation() {
        return matchCreation;
    }

    public long getMatchDuration() {
        return matchDuration;
    }

    public String getChampId() {
        return champId;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getAssists() {
        return assists;
    }

    public int getGold() {
        return gold;
    }

    public int getCs() {
        return cs;
    }

    public int getChampLevel() {
        return champLevel;
    }

    public LinkedHashMap<String, Integer> getStats() {
        return stats;
    }

    public Integer[] getItems() {
        return items;
    }

    public String getSum1() {
        return sum1;
    }

    public String getSum2() {
        return sum2;
    }

    public String getChampName() {
        return champName;
    }

    public String getTypeMatch() {
        return typeMatch;
    }

    public List<Integer> getTeamWinner() {
        return teamWinner;
    }

    public List<Integer> getTeamLoser() {
        return teamLoser;
    }
}