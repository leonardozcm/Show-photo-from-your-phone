package com.example.com.photocollector.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.example.com.photocollector.R;
import com.example.com.photocollector.adapter.ImageAdapter;
import com.example.com.photocollector.presenter.ImagePresenter;
import com.example.com.photocollector.view.viewinterface.ViewInterface;

import java.util.List;

public class ShowActivity extends AppCompatActivity implements ViewInterface{
    private ImagePresenter mImagePresenter;
    private List<String> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        Intent intent=getIntent();
        String parent=intent.getStringExtra("parent");
        mImagePresenter=new ImagePresenter(parent);
        showView(loadImage());
    }

    @Override
    public List<String> loadImage() {
       return mImagePresenter.getData();
    }

    @Override
    public void showView(List<String> items) {
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.show_list);
        StaggeredGridLayoutManager layoutManager=new
                StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        ImageAdapter adapter=new ImageAdapter(items);
        recyclerView.setAdapter(adapter);

    }
}
