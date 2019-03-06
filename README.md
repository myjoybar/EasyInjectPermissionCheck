# 一个使用方便，对业务代码无侵入性的动态权限检查库

# EasyPermissionCheck
基于AOP注解实现
## Features
 - 注解实现，对代码无侵入性
 - 任意地方(service,broadcast)都可以申请权限

   
## Installation
### Gradle Dependency

#####   Add the library to your project build.gradle

```gradle
compile 'com.joybar.easypermission:library:1.0.7'
compile 'org.aspectj:aspectjrt:1.8.9' // aspectJ

```

其它spectj Gradle 配置请参考demo

## Sample Usage

### 一. 使用


#### 1. 请参照APP demo的定义注解，示例如下（可以拷贝以下代码到您的项目中）
```java
 
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckPermission {
    String[] permissions();
    int requestCode();
}

```

#### 2. 请参照APP demo,实现切入点的代码逻辑，示例如下（可以直接拷贝以下代码到您的项目中）
```java

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




```

#### 3. 在具体需要动态权限检查的地方，如下使用

```java

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
	

```

#### 4. 继承IPermissionCallback，并在以下三个回调方法中实现逻辑

```java

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

```



### 二. 流程原理说明

1. AspectJ在.java文件编译成class字节码的时候，会在方法前面，后面，或者里面加上我们定义的业务逻辑,上面的例子中用的是@Around。
2. AspectJ会在有@CheckPermission注解的方法内，插入checkPermission(final ProceedingJoinPoint joinPoint)方法里面的实现逻辑。
3. 如上所示：
    1. 通过signature.getMethod().getAnnotation(CheckPermission.class)获取到声明的权限，然后通过PermissionRequestActivity.permissionRequest()去请求权限
    2. 如果声明的权限已经被Granted，则会回调permissionGranted，并执行joinPoint.proceed()，也就意味着，我们的方法会直接执行
    3. 如果声明的权限被拒绝，则会回调permissionDenied方法，并通过反射执行实现类中相应的方法
    4. 另外两个回调方法permissionDeniedAndNeverAsk和showRationale的处理逻辑和上面一致   
4. 当然，本demo只是提供一种思路参考
## License

    Copyright 2018 MyJoybar

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.   
