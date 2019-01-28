package com.joy.permissioncheck.aspect;

import android.content.Context;
import android.util.Log;

import com.joy.permission.PermissionRequestActivity;
import com.joy.permission.interf.IPermission;
import com.joy.permission.interf.PermissionRequest;
import com.joy.permissioncheck.annotation.CheckPermission;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by joybar on 14/04/2018.
 */

@Aspect
public class CheckPermissionAspect {
	private static final String TAG = "CheckPermissionAspect";
	private static final String BEFORE_TAG = "[BEFORE]: ";
	private static final String END_TAG = "[END]: ";
	private static final String PERMISSION_DENIED_METHOD = "permissionDenied";
	private static final String PERMISSION_DENIED_AND_NEVER_ASK_METHOD = "permissionDeniedAndNeverAsk";
	private static final String PERMISSION_SHOW_RATIONALE = "showRationale";


	private static final String POINTCUT_METHOD = "execution(@*com.joy.permissioncheck.annotation.CheckPermission  * " + "*(..))";


	@Pointcut(POINTCUT_METHOD)
	public void executionCheckPermission() {
	}

	@Around("executionCheckPermission()")
	public Object checkPermission(final ProceedingJoinPoint joinPoint) throws Throwable {
		Log.d(TAG, "start  checkPermission");
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		CheckPermission checkPermission = signature.getMethod().getAnnotation(CheckPermission.class);
		if (checkPermission != null) {
			final String[] permissions = checkPermission.permissions();
			final int requestCode = checkPermission.requestCode();
			if (joinPoint.getThis() instanceof Context) {
				final Context context = (Context) joinPoint.getThis();
				final Class<?> targetClass = context.getClass();
				PermissionRequestActivity.permissionRequest(context, permissions, requestCode, new IPermission() {

					@Override
					public void permissionGranted() {
						Log.d(TAG, Arrays.toString(permissions) + " had granted ，requestCode=" + requestCode);
						try {
							joinPoint.proceed();
						} catch (Throwable throwable) {
							throwable.printStackTrace();
						}
					}

					@Override
					public void permissionDenied(int requestCode, String[] permissions) {
						Log.d(TAG, Arrays.toString(permissions) + " had denied ，requestCode=" + requestCode);
						invokeMethod(context, targetClass, requestCode, permissions, PERMISSION_DENIED_METHOD, null);
					}

					@Override
					public void permissionDeniedAndNeverAsk(int requestCode, String[] permissions) {
						Log.d(TAG, Arrays.toString(permissions) + " had permissionDenied And NeverAsk ，requestCode=" + requestCode);
						invokeMethod(context, targetClass, requestCode, permissions, PERMISSION_DENIED_AND_NEVER_ASK_METHOD, null);

					}

					@Override
					public void showRationale(int requestCode, String[] permissions, PermissionRequest permissionRequest) {
						Log.d(TAG, Arrays.toString(permissions) + " showRationale ，requestCode=" + requestCode);
						invokeMethod(context, targetClass, requestCode, permissions, PERMISSION_SHOW_RATIONALE, permissionRequest);
					}
				});
			} else {
				Log.d(TAG, "The context type must be Context");
			}

		}
		// Object  result = joinPoint.proceed();
		return null;
	}


	public void invokeMethod(Context context, Class<?> targetClass, int requestCode, String[] permissions, String methodName, PermissionRequest
            permissionRequest) {
		try {
			if (null == permissionRequest) {
				Method permissionDenied = targetClass.getMethod(methodName, int.class, String[].class);//得到方法对象
				permissionDenied.invoke(context, requestCode, permissions);
			} else {
				Method permissionDenied = targetClass.getMethod(methodName, int.class, String[].class,PermissionRequest.class);//得到方法对象
				permissionDenied.invoke(context, requestCode, permissions, permissionRequest);
			}

		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

}
