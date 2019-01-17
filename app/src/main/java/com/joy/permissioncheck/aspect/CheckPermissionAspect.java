package com.joy.permissioncheck.aspect;

import android.app.Activity;
import android.util.Log;

import com.joy.permission.PermissionGuide;
import com.joy.permission.PermissionRequestActivity;
import com.joy.permission.interf.IPermission;
import com.joy.permissioncheck.annotation.CheckPermission;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Arrays;

/**
 * Created by joybar on 14/04/2018.
 */

@Aspect
public class CheckPermissionAspect {
    private static final String TAG = "CheckPermissionAspect";
    private static final String BEFORE_TAG = "[BEFORE]: ";
    private static final String END_TAG = "[END]: ";


    private static final String POINTCUT_METHOD = "execution(@com.joy.permissioncheck.annotation.CheckPermission  * "
         + "*(..))";


    @Pointcut(POINTCUT_METHOD)
    public void executionCheckPermission() {
    }

    @Around("executionCheckPermission()")
    public Object checkPermission(final ProceedingJoinPoint joinPoint) throws Throwable {
        Log.d(TAG,"start  checkPermission");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        CheckPermission checkPermission = signature.getMethod().getAnnotation(CheckPermission.class);

        if (checkPermission != null) {
            final String[] permissions = checkPermission.permissions();
            final int requestCode = checkPermission.requestCode();
            if (joinPoint.getThis() instanceof Activity) {
                final Activity activity = (Activity) joinPoint.getThis();
                PermissionRequestActivity.permissionRequest(activity, permissions, requestCode, new IPermission() {

                    @Override
                    public void permissionGranted() {
                        Log.d(TAG, Arrays.toString(permissions)+" had granted ，requestCode="+requestCode);
                        try {
                            joinPoint.proceed();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }

                    @Override
                    public void permissionDenied(int requestCode, String[] permissions) {
                        Log.d(TAG, Arrays.toString(permissions)+" had denied ，requestCode="+requestCode);
                        PermissionGuide.showTipsDialog(activity,permissions);
                    }

                    @Override
                    public void permissionCanceled(int requestCode) {
                        Log.d(TAG, Arrays.toString(permissions)+" had canceled ，requestCode="+requestCode);

                    }
                });
            }else{
                Log.d(TAG,"The context type must be Activity");
            }

        }
       // Object  result = joinPoint.proceed();
        return null ;
    }


}
