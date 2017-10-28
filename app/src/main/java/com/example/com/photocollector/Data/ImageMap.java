package com.example.com.photocollector.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 59771 on 2017/10/27.
 * 使用单例模式来构建一个数据库，储存文件Map
 */

public class ImageMap implements ImageMapInterface{
    private HashMap<String,List<String>> imagetree=new HashMap<String, List<String>>();
    private ImageMap(){}
    private static final ImageMap imagemap=new ImageMap();

    public static ImageMap getInstance(){
        return imagemap;
    }

    @Override
    public void loadImageMap(HashMap<String, List<String>> imagetree) {
        this.imagetree=imagetree;
    }

    @Override
    public List<String> getParentFolder() {
        ArrayList<String> parents=new ArrayList<String>();
        parents.addAll(imagetree.keySet());
        return parents;
    }

    @Override
    public List<String> getImages(String parent) {
        return imagetree.get(parent);
    }
}
