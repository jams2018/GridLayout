package com.example.anar.gridlayout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class CameraAdapter extends RecyclerView.Adapter<CameraAdapter.ViewHolder> {

    private Context context;
    private List<Camera> cList;
    private RequestOptions option;

    public CameraAdapter(Context context, List<Camera> cList) {
        this.context = context;
        this.cList = cList;
        option = new RequestOptions().centerCrop().placeholder(R.drawable.loading).error(R.drawable.loading);
    }

    @NonNull
    @Override
    public CameraAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.single_camera, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CameraAdapter.ViewHolder holder, int position) {
        final Camera camera = cList.get(position);
        holder.camera_name.setText(camera.getDescription());

        //load camera image
        Glide.with(context).load(cList.get(position).getImageUrl()).apply(option).into(holder.camera_thumbnail);
    }

    @Override
    public int getItemCount() {
        return cList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout view_container;
        TextView camera_name;
        ImageView camera_thumbnail;


        ViewHolder(View itemView) {
            super(itemView);
            view_container = itemView.findViewById(R.id.camera_list_container);
            camera_name = itemView.findViewById(R.id.camera_id);
            camera_thumbnail = itemView.findViewById(R.id.camera_image);

        }
    }
}
