package com.reduse.lol_abyss.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.reduse.lol_abyss.DetailsMatchActivity;
import com.reduse.lol_abyss.R;
import com.reduse.lol_abyss.entity.MatchEntity;
import com.reduse.lol_abyss.helper.Helper;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<MatchEntity> matches;

    public MyAdapter(Context context, List<MatchEntity> matches){
        this.context = context;
        this.matches = matches;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.match_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
         //final MatchEntity oneMatch = matches.get(position);
        MatchEntity oneMatch = new MatchEntity();
        oneMatch = matches.get(position);
        if(oneMatch.isWinner()){
            ((MyViewHolder) holder).winOrLose.setBackgroundColor(Color.GREEN);
            ((MyViewHolder) holder).cardView.setCardBackgroundColor(R.color.win_row_bg);
        }else{
            ((MyViewHolder) holder).winOrLose.setBackgroundColor(Color.RED);
            ((MyViewHolder) holder).cardView.setCardBackgroundColor(R.color.lose_row_bg);
        }

        Picasso.with(context).load("http://ddragon.leagueoflegends.com/cdn/11.8.1/img/champion/"+oneMatch.getChampName()).into(((MyViewHolder) holder).portrait);
        ((MyViewHolder) holder).typeMatch.setText(oneMatch.getTypeMatch());
        ((MyViewHolder) holder).kda.setText(oneMatch.getKills() + "/" + oneMatch.getDeaths() + "/" + oneMatch.getAssists());
        ((MyViewHolder) holder).gold.setText(String.valueOf(Math.round(oneMatch.getGold() / 1000.0)) + "K");
        ((MyViewHolder) holder).cs.setText(String.valueOf(oneMatch.getCs()));
            if(oneMatch.getItems()[0] != null){
        Helper.setImageItems(context, oneMatch.getItems()[0], ((MyViewHolder) holder).item1);}
        if(oneMatch.getItems()[1] != null){
        Helper.setImageItems(context, oneMatch.getItems()[1], ((MyViewHolder) holder).item2);}
        if(oneMatch.getItems()[2] != null){
        Helper.setImageItems(context, oneMatch.getItems()[2], ((MyViewHolder) holder).item3);}
        if(oneMatch.getItems()[3] != null){
        Helper.setImageItems(context, oneMatch.getItems()[3], ((MyViewHolder) holder).item4);}
        if(oneMatch.getItems()[4] != null){
        Helper.setImageItems(context, oneMatch.getItems()[4], ((MyViewHolder) holder).item5);}
        if(oneMatch.getItems()[5] != null){
        Helper.setImageItems(context, oneMatch.getItems()[5], ((MyViewHolder) holder).item6);}
        if(oneMatch.getItems()[6] != null){
        Helper.setImageItems(context, oneMatch.getItems()[6], ((MyViewHolder) holder).item7);}

        ((MyViewHolder) holder).matchDuration.setText(Helper.convertDuration(oneMatch.getMatchDuration()));
        ((MyViewHolder) holder).matchCreation.setText(Helper.convertDate(oneMatch.getMatchCreation()));

        MatchEntity finalOneMatch = oneMatch;
        ((MyViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsMatchActivity.class);
                intent.putExtra("ONE_MATCH", finalOneMatch);
                v.getContext().startActivity(intent);

            }
        });
        //setAnimation(((MyViewHolder) holder).cardView);

    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    private void setAnimation(View toAnimate){

        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        animation.setDuration(1000);
        toAnimate.startAnimation(animation);

    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        View winOrLose;
        ImageView portrait, item1, item2, item3, item4, item5, item6, item7;
        TextView typeMatch, kda, gold, cs, matchDuration, matchCreation;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            winOrLose = itemView.findViewById(R.id.win_or_lose);
            portrait = (ImageView) itemView.findViewById(R.id.iv_portrait);
            item1 = (ImageView) itemView.findViewById(R.id.iv_item1);
            item2 = (ImageView) itemView.findViewById(R.id.iv_item2);
            item3 = (ImageView) itemView.findViewById(R.id.iv_item3);
            item4 = (ImageView) itemView.findViewById(R.id.iv_item4);
            item5 = (ImageView) itemView.findViewById(R.id.iv_item5);
            item6 = (ImageView) itemView.findViewById(R.id.iv_item6);
            item7 = (ImageView) itemView.findViewById(R.id.iv_item7);
            typeMatch = (TextView) itemView.findViewById(R.id.tv_type_match);
            kda = (TextView) itemView.findViewById(R.id.tv_kda);
            gold = (TextView) itemView.findViewById(R.id.tv_gold);
            cs = (TextView) itemView.findViewById(R.id.tv_cs);
            matchDuration = (TextView) itemView.findViewById(R.id.tv_duration);
            matchCreation = (TextView) itemView.findViewById(R.id.tv_date);

            cardView = (CardView) itemView.findViewById(R.id.cardview);

        }
    }
}
