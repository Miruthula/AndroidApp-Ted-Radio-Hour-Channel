package com.example.yzhuo.homework5;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by yzhuo on 10/18/2015.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    List<Item> items;
    Context mContext;
    Player player;

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView image;
        TextView title;
        TextView date;
        LinearLayout play;
        LinearLayout listAll;



        public ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.listCV);
            image = (ImageView) itemView.findViewById(R.id.listImageView);
            title = (TextView) itemView.findViewById(R.id.listTitle);
            date = (TextView) itemView.findViewById(R.id.listDate);
            play = (LinearLayout) itemView.findViewById(R.id.listPlay);
            listAll = (LinearLayout) itemView.findViewById(R.id.listAll);

        }


    }

    ListAdapter(Context context,Player player, List<Item> items) {
        this.items = items;
        this.mContext = context;
        this.player = player;
    }


    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ListAdapter.ViewHolder holder, final int position) {
        holder.title.setText(items.get(position).getTitles());
        holder.date.setText(items.get(position).getReleaseDate());
        if(items.get(position).getImageURL()!=null){
            Picasso.with(mContext)
                    .load(items.get(position).getImageURL())
                    .into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.ic_action_pause);
        }

        holder.listAll.setOnClickListener(new View.OnClickListener() {
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
