package com.joy.permission;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import java.util.List;

/**
 * Created by joybar on 15/04/2018.
 */

public class PermissionGuide {

    /**
     * 显示提示对话框
     */
    public static void showTipsDialog(final Activity activity, String[] permissionNames) {

        StringBuilder sb = new StringBuilder();
        sb.append(activity.getString(R.string.tip_msg));
        sb.append("\\n");
        List<String> deniedPermissions = PermissionManager.getInstance().getDeniedPermissions
                (activity, permissionNames);
        sb.append("\\n");
        String name = PermissionUtils.getInstance().getPermissionNames(deniedPermissions,activity);
        sb.append(name);
        sb.append("\\n");
        sb.append(activity.getString(R.string.tip_guide));
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.tip));
        builder.setMessage(sb.toString().replace("\\n", "\n"));
        builder.setPositiveButton(activity.getString(R.string.confirm), new
                DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startAppSettings(activity);
            }
        });
        builder.setNegativeButton(activity.getString(R.string.cancel), null);
        builder.show();
    }

    /**
     * 启动当前应用设置页面
     */
    private static void startAppSettings(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        activity.startActivity(intent);
    }

}
