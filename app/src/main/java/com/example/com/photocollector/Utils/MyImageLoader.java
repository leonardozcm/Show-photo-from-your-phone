package com.example.com.photocollector.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.content.ContentValues.TAG;


/**
 * Created by 59771 on 2017/10/27.
 */

public class MyImageLoader {
    private LruCache<String,Bitmap> mMemoryCache;
    private static MyImageLoader myImageLoader=new MyImageLoader();
    private ExecutorService mImageThreadPool = Executors.newFixedThreadPool(1);

    private MyImageLoader(){
        final int maxMemory=(int)(Runtime.getRuntime().maxMemory()
        );

        final int cacheSize=maxMemory/4;//使用内存的1/4来储存图片
        Log.d(TAG, "MyImageLoader: "+cacheSize);
        mMemoryCache=new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                Log.d(TAG, "sizeOf: "+value.getRowBytes()*value.getHeight()/1024);
                return value.getRowBytes()*value.getHeight()/1024;//返回每张图片的大小
            }
        };
    }

    public interface MyImageCallBack{
        public void onImageLoader(Bitmap bitmap,String path);
    }
    public static MyImageLoader getInstance(){
        return myImageLoader;
    }

    /**
     * 剪裁
     * @param path
     * @return
     */
    public Bitmap loadNativeImage(final String path, final Point mPoint,final MyImageCallBack myImageCallBack){
        final Bitmap bitmap=getBitmapFromMemoryCache(path);
        final  Handler mHandler=new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Log.d(TAG, "loadNativeImage: handleMessage");
                super.handleMessage(msg);
                myImageCallBack.onImageLoader((Bitmap)msg.obj,path);
            }
        };

        if(bitmap==null){
            Log.d(TAG, "loadNativeImage: bitmap == null");
            mImageThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap1=decodeThumbBitmapForFile(path,mPoint==null?0:mPoint.x,mPoint==null?0:mPoint.y);
                    Message msg=mHandler.obtainMessage();
                    msg.obj=bitmap1;
                    mHandler.sendMessage(msg);

                    addBitmaoToMemoryCache(path,bitmap1);
                }
            });
        }
        return bitmap;
    }
    /**
     * 不裁剪
     */
    public Bitmap loadNativeImage(final String path, final MyImageCallBack mCallBack){
        return this.loadNativeImage(path, null, mCallBack);
    }

    private void addBitmaoToMemoryCache(String key,Bitmap bitmap){
        if(getBitmapFromMemoryCache(key)==null&&bitmap!=null){
            mMemoryCache.put(key,bitmap);
        }
    }
    private Bitmap getBitmapFromMemoryCache(String key){
        return mMemoryCache.get(key);
    }


    private Bitmap decodeThumbBitmapForFile(String path,int viewWidth,int viewHeight){
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(path,options);
        Log.d(TAG, "decodeThumbBitmapForFile: "+viewWidth+",: "+viewHeight);

        options.inSampleSize=computeScale(options,viewWidth,viewHeight);
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeFile(path,options);
    }

    private int computeScale(BitmapFactory.Options options, int viewWidth, int viewHeight){
        int inSampleSize = 1;
        if(viewWidth == 0 || viewHeight == 0){
            Log.d(TAG, "computeScale: "+inSampleSize);
            return inSampleSize;
        }
        int bitmapWidth = options.outWidth;
        int bitmapHeight = options.outHeight;

        //假如Bitmap的宽度或高度大于我们设定图片的View的宽高，则计算缩放比例
        if(bitmapWidth > viewWidth || bitmapHeight > viewWidth){
            int widthScale = Math.round((float) bitmapWidth / (float) viewWidth);
            int heightScale = Math.round((float) bitmapHeight / (float) viewHeight);

            //为了保证图片不缩放变形，我们取宽高比例最小的那个
            inSampleSize = widthScale < heightScale ? widthScale : heightScale;
        }
        Log.d(TAG, "Counted computeScale: "+inSampleSize);
        return inSampleSize;
    }


}
