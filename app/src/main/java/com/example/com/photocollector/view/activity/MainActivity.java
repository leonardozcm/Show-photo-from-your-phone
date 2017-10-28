package com.example.com.photocollector.view.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.com.photocollector.R;
import com.example.com.photocollector.adapter.FolderAdapter;
import com.example.com.photocollector.presenter.FolderListPresenter;
import com.example.com.photocollector.view.viewinterface.ViewInterface;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity implements ViewInterface{
    private List<String> mFolderlist;
    private FolderListPresenter mPresenter;
    private FolderAdapter mFolderAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * 申请权限
         */
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new
            String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else {
            //// TODO: 2017/10/25 载入文件夹
            sender.subscribe(observer);
        }
    }
@Override
    public List<String> loadImage(){
        mPresenter=new FolderListPresenter(this);
        mPresenter.buildimagetree();
        mFolderlist=mPresenter.getData();
        return mFolderlist;
    }

    @Override
    public void showView(List<String> items) {
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.list);
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        mFolderAdapter=new FolderAdapter(mFolderlist);
        recyclerView.setAdapter(mFolderAdapter);
    }
    private Observable<List<String>> sender=Observable.create(new ObservableOnSubscribe<List<String>>() {
        @Override
        public void subscribe(ObservableEmitter<List<String>> e) throws Exception {
            e.onNext(loadImage());
        }
    });
    private Observer<List<String>> observer=new Observer<List<String>>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(List<String> value) {
            showView(value);
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    };

}
