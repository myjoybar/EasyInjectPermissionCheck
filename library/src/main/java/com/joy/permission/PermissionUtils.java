package com.joy.permission;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by joybar on 15/04/2018.
 */

public class PermissionUtils {


    private static PermissionUtils permissionUtils;
    public static PermissionUtils getInstance(){
        if(permissionUtils == null){
            permissionUtils = new PermissionUtils();
        }
        return permissionUtils;
    }

    private HashMap<String,String> permissions;
    public HashMap<String,String> getPermissions(Context context){
        if(permissions == null){
            permissions = new HashMap<>();
            initPermissions(context);
        }
        return permissions;
    }

    private void initPermissions(Context context){
        //联系人/通讯录权限
        permissions.put("android.permission.WRITE_CONTACTS",context.getString(R.string.permission_write_contacts));
        permissions.put("android.permission.GET_ACCOUNTS",context.getString(R.string.permission_get_accounts));
        permissions.put("android.permission.READ_CONTACTS",context.getString(R.string.permission_read_contacts));
        //电话权限
        permissions.put("android.permission.READ_CALL_LOG",context.getString(R.string.permission_read_call_log));
        permissions.put("android.permission.READ_PHONE_STATE",context.getString(R.string.permission_read_phone_state));
        permissions.put("android.permission.CALL_PHONE",context.getString(R.string.permission_call_phone));
        permissions.put("android.permission.WRITE_CALL_LOG",context.getString(R.string.permission_write_call_log));
        permissions.put("android.permission.USE_SIP",context.getString(R.string.permission_use_sip));
        permissions.put("android.permission.PROCESS_OUTGOING_CALLS",context.getString(R.string.permission_process_outgoing_calls));
        permissions.put("com.android.voicemail.permission.ADD_VOICEMAIL",context.getString(R.string.permission_add_voicemail));
        //日历权限
        permissions.put("android.permission.READ_CALENDAR",context.getString(R.string.permission_read_calendar));
        permissions.put("android.permission.WRITE_CALENDAR",context.getString(R.string.permission_write_calendar));
        //相机拍照权限
        permissions.put("android.permission.CAMERA",context.getString(R.string.permission_camera));
        //传感器权限
        permissions.put("android.permission.BODY_SENSORS",context.getString(R.string.permission_body_sensors));
        //定位权限
        permissions.put("android.permission.ACCESS_FINE_LOCATION",context.getString(R.string.permission_access_fine_location));
        permissions.put("android.permission.ACCESS_COARSE_LOCATION",context.getString(R.string.permission_access_coarse_location));
        //文件存取
        permissions.put("android.permission.READ_EXTERNAL_STORAGE",context.getString(R.string.permission_read_external_storage));
        permissions.put("android.permission.WRITE_EXTERNAL_STORAGE",context.getString(R.string.permission_write_external_storage));
        //音视频、录音权限
        permissions.put("android.permission.RECORD_AUDIO",context.getString(R.string.permission_record_audio));
        //短信权限
        permissions.put("android.permission.READ_SMS",context.getString(R.string.permission_read_sms));
        permissions.put("android.permission.RECEIVE_WAP_PUSH",context.getString(R.string.permission_receive_wap_push));
        permissions.put("android.permission.RECEIVE_MMS",context.getString(R.string.permission_receive_mms));
        permissions.put("android.permission.RECEIVE_SMS",context.getString(R.string.permission_receive_sms));
        permissions.put("android.permission.SEND_SMS",context.getString(R.string.permission_send_sms));
        permissions.put("android.permission.READ_CELL_BROADCASTS",context.getString(R.string.permission_read_cell_broadcasts));
    }

    /**
     * 获得权限名称集合（去重）
     * @param permission 权限数组
     * @return 权限名称
     */
    public String getPermissionNames(List<String> permission,Context context){
        if(permission==null || permission.size()==0){
            return "\n";
        }
        StringBuilder sb = new StringBuilder();
        List<String> list = new ArrayList<>();
        HashMap<String,String> permissions = getPermissions(context);
        for(int i=0; i<permission.size(); i++){
            String name = permissions.get(permission.get(i));
            if(name!=null && !list.contains(name)){
                list.add(name);
                sb.append(name);
                sb.append("\n");
            }
        }
        return sb.toString();
    }

}
