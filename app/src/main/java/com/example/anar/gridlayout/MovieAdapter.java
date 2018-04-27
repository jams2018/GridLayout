package com.example.anar.gridlayout;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private Context context;
    private List<Movie> list;

    public MovieAdapter(Context context, List<Movie> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(v); // added
        viewHolder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, Detailed.class);
                i.putExtra("title", list.get(viewHolder.getAdapterPosition()).getTitle());
                i.putExtra("year", list.get(viewHolder.getAdapterPosition()).getYear());
                i.putExtra("director", list.get(viewHolder.getAdapterPosition()).getDirector());
                i.putExtra("description", list.get(viewHolder.getAdapterPosition()).getDescription());
                i.putExtra("image", list.get(viewHolder.getAdapterPosition()).getThumbnail());
                context.startActivity(i);
            }
        });

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = list.get(position);

        holder.textTitle.setText(movie.getTitle());
        holder.textYear.setText(String.valueOf(movie.getYear()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textTitle, textYear;
               LinearLayout view_container;


        public ViewHolder(View itemView) {
            super(itemView);

            view_container = itemView.findViewById(R.id.container);
            textTitle = itemView.findViewById(R.id.main_title);
            textYear = itemView.findViewById(R.id.main_year);
        }
    }

}
