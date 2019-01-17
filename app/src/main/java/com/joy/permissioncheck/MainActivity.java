package com.joy.permissioncheck;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.joy.permissioncheck.annotation.CheckPermission;


public class MainActivity extends AppCompatActivity {
	private static final String TAG = "PermissionManager";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	private void  init(){
		findViewById(R.id.btn_read_contact).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				readContacts();
			}
		});
		findViewById(R.id.btn_camera).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				openCamera();
			}
		});
		findViewById(R.id.btn_sdcard).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				readStorage();
			}
		});
	}

	/**
	 * 存储卡权限
	 */
	@CheckPermission(permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode = 1)
	private void readStorage() {
		// to do
		Log.d(TAG,"readStorage");
	}


	/**
	 * 相机权限
	 */
	@CheckPermission(permissions = {Manifest.permission.CALL_PHONE, Manifest.permission
			.CAMERA}, requestCode = 2)
	private void openCamera() {
		// to do
		Log.d(TAG,"openCamera");
	}



	/**
	 * 读取联系人权限
	 */
	@CheckPermission(permissions = {Manifest.permission.READ_CONTACTS}, requestCode = 3)
	private void readContacts() {
		// to do
		Log.d(TAG,"readContacts");
	}


}




