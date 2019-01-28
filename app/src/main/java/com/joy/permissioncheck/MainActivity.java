package com.joy.permissioncheck;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.joy.permission.interf.IPermissionCallback;
import com.joy.permission.interf.PermissionRequest;
import com.joy.permissioncheck.annotation.CheckPermission;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity implements IPermissionCallback {
	private static final String TAG = "CheckPermissionAspect";
	MainActivity activity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		activity =this;
		init();


	}




	private void init(){
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
	@CheckPermission(permissions = {Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode = 1)
	private void readStorage() {
		// to do
		Log.d(TAG,"readStorage");
	}


	/**
	 * 相机权限
	 */
	@CheckPermission(permissions = {Manifest.permission.CAMERA}, requestCode = 2)
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

	@Override
	public void permissionDenied(int requestCode, String[] permissions) {
		//申请权限被拒接

	}

	@Override
	public void permissionDeniedAndNeverAsk(int requestCode, String[] permissions) {
		// 申请权限被拒接并且用户点击了"不再询问"
	}

	@Override
	public void showRationale(int requestCode, String[] permissions, final PermissionRequest permissionRequest) {

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(activity.getString(com.joy.permission.R.string.tip));
		builder.setMessage("软件部分功能需要请求您的手机权限，请允许以下权限："+ Arrays.toString(permissions));
		builder.setPositiveButton(activity.getString(com.joy.permission.R.string.confirm), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				permissionRequest.proceed();
			}
		});
		builder.setNegativeButton(activity.getString(com.joy.permission.R.string.cancel), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				permissionRequest.cancel();
			}
		});

		builder.show();
	}
}




