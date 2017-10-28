package com.example.com.photocollector.presenter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;

import com.example.com.photocollector.Data.ImageMap;
import com.example.com.photocollector.presenter.PresenterInterface.Presenter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;


/**
 * Created by 59771 on 2017/10/27.
 */

public class FolderListPresenter implements Presenter {


    private Context mContext;
    private static ImageMap mImageMap=ImageMap.getInstance();
    private static HashMap<String,List<String>> mImagetree=new HashMap<String, List<String>>();
   private final static int BUILD_OK=1;

    public FolderListPresenter(Context context) {
        mContext=context;
    }

    private static Handler mHandler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case BUILD_OK:
                    mImageMap.loadImageMap(mImagetree);
            }
        }
    };

    public void buildimagetree() {
        mImagetree.clear();
                Uri mImageUri= MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver=mContext.getContentResolver();

                Cursor mCursor=mContentResolver.query(mImageUri,null,MediaStore.Images.Media.MIME_TYPE
                        +"=? or "+MediaStore.Images.Media.MIME_TYPE+"=?",new String[]{"image/jpeg", "image/png"}
                        ,MediaStore.Images.Media.DATE_MODIFIED);

                if(mCursor==null){
                    Log.d(TAG, "run: mCursor is null!!!!");
                    return;
                }
                Log.d(TAG, "run: mCursor is OK!!!!");

                while (mCursor.moveToNext()){
                    Log.d(TAG, "run: Loading");
                    String path=mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    String parentname=new File(path).getParentFile().getName();

                    if(!mImagetree.containsKey(parentname)){
                        List<String> images=new ArrayList<>();
                        images.add(path);
                        mImagetree.put(parentname,images);
                    }else {
                        mImagetree.get(parentname).add(path);
                    }
                }
                Log.d(TAG, "run: "+Boolean.toString(mImagetree.isEmpty()));
                mImageMap.loadImageMap(mImagetree);
               // mHandler.sendEmptyMessage(BUILD_OK);
                mCursor.close();
    }

    @Override
    public List<String> getData() {
        return ImageMap.getInstance().getParentFolder();
    }

    static public String getCoverPath(String folder) {

        return ImageMap.getInstance().getImages(folder).get(0);
    }
}
