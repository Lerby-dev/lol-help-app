package com.reduse.lol_abyss.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.reduse.lol_abyss.R;
import com.reduse.lol_abyss.entity.Champion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GridAdapter extends BaseAdapter {

    private Context context;
    private List<Champion> championList = new ArrayList<>();
    private LayoutInflater inflater;

    public GridAdapter(Context context, List<Champion> championList) {
        this.context = context;
        this.championList = championList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return championList.size();
    }

    @Override
    public Object getItem(int position) {
        return championList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MyViewHolder myviewHolder;

        if(convertView == null){

            convertView = inflater.inflate(R.layout.gallery_row, parent, false);
            myviewHolder = new MyViewHolder();
            myviewHolder.image = (ImageView) convertView.findViewById(R.id.iv_champ);
            myviewHolder.name = (TextView) convertView.findViewById(R.id.tv_champ_name);
            convertView.setTag(myviewHolder);

        }else{
            myviewHolder = (MyViewHolder) convertView.getTag();
        }

        final Champion champion = championList.get(position);

        Picasso.with(context).load("http://ddragon.leagueoflegends.com/cdn/11.8.1/img/champion/"+champion.getImageName()).into(myviewHolder.image);
        myviewHolder.name.setText(champion.getImageName().replace(".png", ""));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String champName = champion.getImageName().replace(".png", "");
               // int id = champion.getId();
                Toast.makeText(context, champName, Toast.LENGTH_SHORT).show();
            }
        });


        return convertView;
    }

    public class MyViewHolder{
        ImageView image;
        TextView name;
    }
}
