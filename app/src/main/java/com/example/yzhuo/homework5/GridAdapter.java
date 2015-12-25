package com.example.yzhuo.homework5;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by yzhuo on 10/18/2015.
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder>{
    List<Item> items;
    Context mContext;
    Player player;

    public class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout gridAll;
        ImageView image;
        TextView title;
        ImageView play;

        public ViewHolder(View itemView) {
            super(itemView);
            //cv = (CardView) itemView.findViewById(R.id.myGridView);
            image = (ImageView) itemView.findViewById(R.id.gridImage);
            play = (ImageView) itemView.findViewById(R.id.gridPlay);
            title = (TextView) itemView.findViewById(R.id.gridTitle);
            gridAll = (RelativeLayout) itemView.findViewById(R.id.gridAll);
        }
    }

    GridAdapter(Context context, Player player, List<Item> items){
        this.items = items;
        this.mContext = context;
        this.player=player;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_view,parent,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.title.setText(items.get(position).getTitles());
        if(items.get(position).getImageURL()!=null){
            Picasso.with(mContext)
                    .load(items.get(position).getImageURL())
                    .into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.ic_stat_list);
        }

        holder.gridAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,PlayActivity.class);
                intent.putExtra(MainActivity.TITLE,items.get(position).getTitles());
                intent.putExtra(MainActivity.IMAGE, items.get(position).getImageURL());
                intent.putExtra(MainActivity.DESC, items.get(position).getDescription());
                intent.putExtra(MainActivity.PUBDATE, items.get(position).getReleaseDate());
                intent.putExtra(MainActivity.DURATION, items.get(position).getDuration());
                intent.putExtra(MainActivity.MP3FILE, items.get(position).getMediaURL());
                player.stop();
                mContext.startActivity(intent);
            }
        });

        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                player.setMP3URL(items.get(position).getMediaURL());
                player.streaming();
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }






}
