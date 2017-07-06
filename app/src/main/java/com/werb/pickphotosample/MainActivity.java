package com.werb.pickphotosample;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.werb.permissionschecker.PermissionChecker;
import com.werb.pickphotoview.PickPhotoView;
import com.werb.pickphotoview.adapter.SpaceItemDecoration;
import com.werb.pickphotoview.util.PickConfig;
import com.werb.pickphotoview.util.PickUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SampleAdapter adapter;
    private PermissionChecker permissionChecker;
    private String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionChecker = new PermissionChecker(this);
        if(permissionChecker.isLackPermissions(PERMISSIONS)){
            permissionChecker.requestPermissions();
        }

        //Select Single Image - When image is selected, gallery immediately closes and returns image.
        CustomButton btn1 = (CustomButton) findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PickPhotoView.Builder(MainActivity.this)
                        .setPickPhotoSize(1)
                        .setShowCamera(true)
                        .setSpanCount(3)
                        .setLightStatusBar(true)
                        .setStatusBarColor("#ffffff")
                        .setToolbarColor("#ffffff")
                        .setToolbarIconColor("#000000")
                        .setClickSelectable(true)
                        .start();
            }
        });

        //Select Multiple Images - User can select multiple images and click Select to confirm.
        CustomButton btn2 = (CustomButton) findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PickPhotoView.Builder(MainActivity.this)
                        .setPickPhotoSize(3)
                        .setShowCamera(true)
                        .setSpanCount(4)
                        .setLightStatusBar(true)
                        .setStatusBarColor("#ffffff")
                        .setToolbarColor("#ffffff")
                        .setToolbarIconColor("#000000")
                        .setSelectIconColor("#00C07F")
                        .setClickSelectable(true)
                        .start();
            }
        });

        //Image Preview Select - Clicking on image opens Image Preview. Must click select icon to select image.
        CustomButton btn3 = (CustomButton) findViewById(R.id.btn3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PickPhotoView.Builder(MainActivity.this)
                        .setPickPhotoSize(6)
                        .setShowCamera(true)
                        .setSpanCount(4)
                        .setLightStatusBar(true)
                        .setStatusBarColor("#ffffff")
                        .setToolbarColor("#ffffff")
                        .setToolbarIconColor("#000000")
                        .setClickSelectable(false)
                        .start();
            }
        });

        RecyclerView photoList = (RecyclerView) findViewById(R.id.photo_list);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        photoList.setLayoutManager(layoutManager);
        photoList.addItemDecoration(new SpaceItemDecoration(PickUtils.getInstance(MainActivity.this).dp2px(PickConfig.ITEM_SPACE), 3));
        adapter = new SampleAdapter(this, null);
        photoList.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 0){
            return;
        }
        if(data == null){
            return;
        }
        if (requestCode == PickConfig.PICK_PHOTO_DATA) {
            ArrayList<String> selectPaths = (ArrayList<String>) data.getSerializableExtra(PickConfig.INTENT_IMG_LIST_SELECT);
            adapter.updateData(selectPaths);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionChecker.PERMISSION_REQUEST_CODE:
                if (!permissionChecker.hasAllPermissionsGranted(grantResults)) {
                    permissionChecker.showDialog();
                }
                break;
        }
    }
}
