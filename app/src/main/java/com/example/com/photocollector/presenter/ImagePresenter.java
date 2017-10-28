package com.example.com.photocollector.presenter;

import com.example.com.photocollector.Data.ImageMap;
import com.example.com.photocollector.presenter.PresenterInterface.Presenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 59771 on 2017/10/27.
 */

public class ImagePresenter implements Presenter{
    List<String> images=new ArrayList<>();
    private static ImageMap mImageMap=ImageMap.getInstance();
    String parent;

    public ImagePresenter(String parent) {
        this.parent = parent;
    }

    @Override
    public List<String> getData() {
        images=mImageMap.getImages(parent);
        return images;
    }
}
