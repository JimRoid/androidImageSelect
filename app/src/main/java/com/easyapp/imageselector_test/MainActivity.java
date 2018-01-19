package com.easyapp.imageselector_test;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.easyapp.imageselector.MultiImageSelectorActivity;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends AppCompatActivity  {

    private static final int REQUEST_IMAGE = 2;

    private TextView mResultText;
    private RadioGroup mChoiceMode, mShowCamera;
    private EditText mRequestNum;

    private ArrayList<String> mSelectPath;
    private ImageView iv_resource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_resource = findViewById(R.id.iv_resource);
        mResultText = findViewById(R.id.result);
        mChoiceMode = findViewById(R.id.choice_mode);
        mShowCamera = findViewById(R.id.show_camera);
        mRequestNum = findViewById(R.id.request_num);

        mChoiceMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.multi) {
                    mRequestNum.setEnabled(true);
                } else {
                    mRequestNum.setEnabled(false);
                    mRequestNum.setText("");
                }
            }
        });

        View button = findViewById(R.id.button);
        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedMode;

                if (mChoiceMode.getCheckedRadioButtonId() == R.id.single) {
                    selectedMode = MultiImageSelectorActivity.MODE_SINGLE;
                } else {
                    selectedMode = MultiImageSelectorActivity.MODE_MULTI;
                }

                boolean showCamera = mShowCamera.getCheckedRadioButtonId() == R.id.show;

                int maxNum = 9;
                if (!TextUtils.isEmpty(mRequestNum.getText())) {
                    maxNum = Integer.valueOf(mRequestNum.getText().toString());
                }

                Intent intent = new Intent(MainActivity.this, MultiImageSelectorActivity.class);
                // 是否显示拍摄图片
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, showCamera);
                // 最大可选择图片数量
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxNum);
                // 选择模式
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, selectedMode);
                // 默认选择
                if (mSelectPath != null && mSelectPath.size() > 0) {
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
                }
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                Log.d("size", "mSelectPath:" + mSelectPath.size());
                beginCrop(mSelectPath.get(0));
                StringBuilder sb = new StringBuilder();
                for (String p : mSelectPath) {
                    sb.append(p);
                    sb.append("\n");
                }
                mResultText.setText(sb.toString());
            }
        }
    }

    private void beginCrop(String path) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        intent.putExtras(bundle);
        intent.setClass(this, CropActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
