package com.easyapp.imageselector_test;

import android.os.Bundle;

import com.easyapp.imageselector.CropImageActivity;
import com.easyapp.imageselector.crop.CropDemoPreset;

/**
 * Created by easyapp_jim on 2016/7/15.
 */
public class CropActivity extends CropImageActivity {
    @Override
    protected CropDemoPreset getCropDemoPreset() {
        return CropDemoPreset.CIRCULAR;
    }

    @Override
    protected String getPath() {
        Bundle bundle = getIntent().getExtras();
        String path = bundle.getString("path", "");
        return path;
    }
}
