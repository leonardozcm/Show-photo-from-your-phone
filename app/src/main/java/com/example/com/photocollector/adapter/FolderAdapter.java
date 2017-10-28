package com.example.com.photocollector.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.com.photocollector.R;
import com.example.com.photocollector.Utils.MyImageLoader;
import com.example.com.photocollector.presenter.FolderListPresenter;
import com.example.com.photocollector.view.activity.ShowActivity;

import java.util.List;

/**
 * Created by 59771 on 2017/10/26.
 */

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {

    private List<String> mFolderList;
    private Point mPoint=new Point(0,0);

    static class ViewHolder extends RecyclerView.ViewHolder{
        View folder_view;
        TextView folder_name;
        ImageView foler_cover;
        public ViewHolder(View view){
            super(view);
            folder_view=view;
            folder_name=(TextView)view.findViewById(R.id.folder_name);
            foler_cover=(ImageView)view.findViewById(R.id.folder_cover);
        }
    }

    public FolderAdapter(List<String> folders){
        mFolderList=folders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.file_view,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        holder.foler_cover.measure(w,h);
        mPoint.set(holder.foler_cover.getMeasuredWidth()/3,holder.foler_cover.getMeasuredHeight()/5);

        holder.folder_view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO: 2017/10/26 点击进入文件夹
                Intent intent=new Intent(v.getContext(), ShowActivity.class);
                intent.putExtra("parent",mFolderList.get(holder.getAdapterPosition()));
                v.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
       holder.folder_name.setText(mFolderList.get(position));
        // TODO: 2017/10/27 获取封面
        Bitmap bitmap= MyImageLoader.getInstance().loadNativeImage(FolderListPresenter.getCoverPath(mFolderList.get(position)), mPoint, new MyImageLoader.MyImageCallBack() {
            @Override
            public void onImageLoader(Bitmap bitmap, String path) {

                if(bitmap!=null){
                    holder.foler_cover.setImageBitmap(bitmap);
                }

            }
        });
        holder.foler_cover.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return mFolderList.size();
    }
}