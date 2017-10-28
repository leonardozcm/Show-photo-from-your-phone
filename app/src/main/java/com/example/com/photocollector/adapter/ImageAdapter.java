package com.example.com.photocollector.adapter;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.com.photocollector.R;
import com.example.com.photocollector.Utils.MyImageLoader;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by 59771 on 2017/10/26.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private List<String> imageList;
    private Point mPoint=new Point(0,0);

    static class ViewHolder extends RecyclerView.ViewHolder{
        View show_view;
        ImageView show_image;
        public ViewHolder(View view){
            super(view);
            show_view=view;
            show_image=(ImageView)view.findViewById(R.id.show_image);
        }
    }

    public ImageAdapter(List<String> images){
        imageList=images;
    }

    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.show_item,parent,false);
        final ImageAdapter.ViewHolder holder=new ImageAdapter.ViewHolder(view);

        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        holder.show_image.measure(w,h);

        mPoint.set(holder.show_image.getMeasuredWidth()/3,holder.show_image.getMeasuredHeight()/5);
        Log.d(TAG, "onCreateViewHolder: "+holder.show_image.getMeasuredWidth()+", "+holder.show_image.getMeasuredHeight());
        holder.show_view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO: 2017/10/26 点击打开图片

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.show_image.setTag(imageList.get(position));
        Bitmap bitmap= MyImageLoader.getInstance().loadNativeImage(imageList.get(position), mPoint, new MyImageLoader.MyImageCallBack() {
            @Override
            public void onImageLoader(Bitmap bitmap, String path) {

                if(bitmap!=null){
                    holder.show_image.setImageBitmap(bitmap);
                }

            }
        });
        Log.d(TAG, "onBindViewHolder: "+mPoint.toString());
        holder.show_image.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }
}
