package com.example.com.photocollector.Data;

import java.util.HashMap;
import java.util.List;

/**
 * Created by 59771 on 2017/10/27.
 */

public interface ImageMapInterface {
    void loadImageMap(HashMap<String,List<String>> imagetree);
    List<String> getParentFolder();
    List<String> getImages(String parent);
}
