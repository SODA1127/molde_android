package com.limefriends.molde.menu_map;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.limefriends.molde.R;
import com.limefriends.molde.menu_map.cameraManager.MoldeReportCameraActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MoldeReportActivity extends AppCompatActivity {
    @BindView(R.id.first_iamge)
    ImageButton first_iamge;
    @BindView(R.id.second_iamge)
    ImageButton second_iamge;
    @BindView(R.id.third_iamge)
    ImageButton third_iamge;
    @BindView(R.id.forth_iamge)
    ImageButton forth_iamge;
    @BindView(R.id.fifth_iamge)
    ImageButton fifth_iamge;

    @BindView(R.id.first_iamge_delete_button)
    ImageButton first_iamge_delete_button;
    @BindView(R.id.second_iamge_delete_button)
    ImageButton second_iamge_delete_button;
    @BindView(R.id.third_iamge_delete_button)
    ImageButton third_iamge_delete_button;
    @BindView(R.id.forth_iamge_delete_button)
    ImageButton forth_iamge_delete_button;
    @BindView(R.id.fifth_iamge_delete_button)
    ImageButton fifth_iamge_delete_button;

    private final int TAKE_PICTURE_FOR_ADD_IMAGE = 100;
    private static SparseArrayCompat<Uri> imageArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity_molde_report);
        ButterKnife.bind(this);

        if(imageArray == null){
            imageArray = new SparseArrayCompat<Uri>();
        }

        Intent data = getIntent();
        if (data != null) {
            Uri uri = data.getParcelableExtra("imagePath");
            int imageSeq = data.getIntExtra("imageSeq", 1);
            if(uri != null && imageSeq != 0){
                Log.e("d", uri + "," + imageSeq);
                if(imageArray.get(imageSeq) == null){
                    imageArray.append(imageSeq, uri);
                }else{
                    imageArray.setValueAt(imageSeq, uri);
                }
            }
        }


        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //UI 컨트롤
        first_iamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePictureForAdd(1);
            }
        });
        second_iamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePictureForAdd(2);
            }
        });
        third_iamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePictureForAdd(3);
            }
        });
        forth_iamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePictureForAdd(4);
            }
        });
        fifth_iamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePictureForAdd(5);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }

    public void takePictureForAdd(final int imageSeq) {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), MoldeReportCameraActivity.class);
                intent.putExtra("imageSeq", imageSeq);
                startActivityForResult(intent, TAKE_PICTURE_FOR_ADD_IMAGE);
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(getApplicationContext(), "권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.with(MoldeReportActivity.this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("권한얻는 데 실패했습니다.\n\n[설정] -> [어플리케이션] -> 해당앱에 들어가 권한을 켜주세요.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }

    public void applyImageArray(){
        for(int i = 1; i <= 5; i++){
            if(imageArray.get(i) != null){
                switch (i) {
                    case 1:
                        first_iamge.setImageURI(imageArray.get(i));
                        first_iamge_delete_button.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        second_iamge.setImageURI(imageArray.get(i));
                        second_iamge_delete_button.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        third_iamge.setImageURI(imageArray.get(i));
                        third_iamge_delete_button.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        forth_iamge.setImageURI(imageArray.get(i));
                        forth_iamge_delete_button.setVisibility(View.VISIBLE);
                        break;
                    case 5:
                        fifth_iamge.setImageURI(imageArray.get(i));
                        fifth_iamge_delete_button.setVisibility(View.VISIBLE);
                        break;
                    default:
                        finish();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        applyImageArray();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return true;
    }


    @Override
    public void onBackPressed() {
        MoldeSearchMapInfoActivity.checkBackPressed = true;
        imageArray.clear();
        imageArray = null;
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
