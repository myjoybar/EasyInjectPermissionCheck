package com.joy.permission.interf;

/**
 * Created by joybar on 2019/1/24.
 */

public interface IPermissionCallback {
	//拒绝权限
	void permissionDenied(int requestCode, String[] permissions);
	//拒绝权限并且选中不再提示
	void permissionDeniedAndNeverAsk(int requestCode, String[] permissions);
	void showRationale(int requestCode, String[] permissions,PermissionRequest permissionRequest);
}
