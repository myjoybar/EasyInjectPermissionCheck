package com.joy.permission;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.joy.permission.interf.IPermission;
import com.joy.permission.interf.PermissionRequest;


/**
 * Created by joybar on 15/04/2018.
 */

public class PermissionRequestActivity extends AppCompatActivity {
	private static final String TAG = "PermissionManager";
	private static IPermission permissionListener;
	private String[] permissions;
	private static final String PERMISSIONS = "permissions";
	private static final String REQUEST_CODE = "request_code";
	private int requestCode;

	public static void permissionRequest(Context context, String[] permissions, int requestCode, IPermission iPermission) {
		permissionListener = iPermission;
		Intent intent = new Intent(context, PermissionRequestActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Bundle bundle = new Bundle();
		bundle.putStringArray(PERMISSIONS, permissions);
		bundle.putInt(REQUEST_CODE, requestCode);
		intent.putExtras(bundle);
		context.startActivity(intent);

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			permissions = bundle.getStringArray(PERMISSIONS);
			requestCode = bundle.getInt(REQUEST_CODE, 0);
		}
		if (permissions == null || permissions.length <= 0) {
			finish();
			return;
		}
		checkShowRationale();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			permissions = bundle.getStringArray(PERMISSIONS);
			requestCode = bundle.getInt(REQUEST_CODE, 0);
		}
		if (permissions == null || permissions.length <= 0) {
			finish();
			return;
		}
		checkShowRationale();
	}



	private void checkShowRationale() {
		boolean hasRequestedPermission = PermissionManager.shouldShowRequestPermissionRationale(this, permissions);
		if (hasRequestedPermission && permissionListener != null) {
			permissionListener.showRationale(requestCode, permissions,  new PermissionRequest() {
				@Override
				public void proceed() {
					requestPermission();
				}

				@Override
				public void cancel() {
					finishPermissionRequestActivity();
				}
			});
		} else {
			requestPermission();
		}
	}

	/**
	 * 申请权限
	 */
	private void requestPermission() {

		if (PermissionManager.getInstance().checkPermissionAllGranted(this, permissions)) {
			permissionListener.permissionGranted();
			finish();
		} else {
			PermissionManager.getInstance().requestPermissions(this, permissions, requestCode);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (this.requestCode == requestCode) {
			if (PermissionManager.getInstance().verifyPermissions(grantResults)) {
				//所有权限都同意
				if (permissionListener != null) {
					permissionListener.permissionGranted();
					finishPermissionRequestActivity();
				}
			} else {
				finishPermissionRequestActivity();
				if (permissionListener != null) {
					boolean isNeverAskAgain = !PermissionManager.shouldShowRequestPermissionRationale(PermissionRequestActivity.this, permissions);
					if (isNeverAskAgain) {
						permissionListener.permissionDeniedAndNeverAsk(requestCode, permissions);
					} else {
						permissionListener.permissionDenied(requestCode, permissions);
					}

				}
			}
		}

	}
	public void finishPermissionRequestActivity() {
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
	}
}
