# FodSDK 接入流程

## 资源接入

* 复制 aar 和 assets 资源到对应目录。
* 将 fod_game_config.json 的配置修改为我方平台提供的参数。如不清楚，请咨询我方运营工作人员，切勿填默认值。

## Application

### 如果游戏有自定义的 `Application`

需要继承我方 `FodApplication`，如果 CP 重写 `onCreate()` 方法，必须调用 `super.onCreate()` 方法，并将自己命名的 `Application` 配置到 AndroidManifest.xml 的 `<application>` 中的 `android:name`  属性中。

```xml
<application ...
    android:name="游戏自定义的 application">
```

### 如果游戏没有自定义的 `Application`

将我方 `FodApplication` 配置到 AndroidManifest.xml 中即可。

```xml
<application ...
    android:name="com.fodsdk.FodApplication">
```

## 功能接入

### 初始化

必须先调用初始化接口，设置全局监听，需在主线程中调用，成功后才可调用其他接口。

```java
FodSDK.get().init(Activity activity, IPlatformCallback callback);
```

回调：

```java
@Override
public void onInit(int code, Bundle bundle) {
    if (code == FodConstants.Code.SUCCESS) {
        Log.d(TAG, "Init Success");
    }
}
```

### 登录

登录接口务必须等初始化接口回调成功才能调用。

```java
FodSDK.get().login(Activity activity);
```

回调：

```java
@Override
public void onLogin(int code, Bundle bundle) {
    if (code == FodConstants.Code.SUCCESS) {
        String uid = bundle.getString("uid");
        String token = bundle.getString("token");
        Log.d(TAG, "Login Success, uid: " + uid + ", token: " + token);
    }
}
```

### 登出

登出接口可以不调用，但是登出回调中需要进行处理，因为悬浮框中的切换账号功能会调用，登出成功回调后需进行重启游戏操作。

```java
FodSDK.get().logout(Activity activity);
```

回调：

```java
@Override
public void onLogout(int code, Bundle bundle) {
    if (code == FodConstants.Code.SUCCESS) {
        Log.d(TAG, "Logout Success");
    }
}
```

### 退出游戏

SDK 内部不会执行退出逻辑，CP 需根据返回值进行逻辑处理。

```java
FodSDK.get().exit(Activity activity);
```

回调：

```java
@Override
public void onExit(int code, Bundle bundle) {
    if (code == FodConstants.Code.SUCCESS) {
        Log.d(TAG, "Exit Success");
        finish();
    }
}
```

### 统计

需要上报事件及角色信息，角色构建如下：

```java
private FodRole getRole() {
    FodRole role = new FodRole();
    role.setRoleId("AA1234");       // 角色 ID
    role.setRoleLevel(1);           // 角色等级
    role.setRoleName("角色名称");    // 角色名称
    role.setServerId("Server101");  // 服务器 ID
    role.setServerName("服务器名称"); // 服务器名称
    return role;
}
```

#### 选服

```java
FodSDK.get().logEvent(FodConstants.Event.SCENE_BEFORE_ENTRY, null); // 无角色信息可传 null
```

#### 进服

```java
FodSDK.get().logEvent(FodConstants.Event.SCENE_ENTRY, role);
```

#### 创建角色

```java
FodSDK.get().logEvent(FodConstants.Event.SCENE_CREATE_ROLE, getRole());
```

#### 角色等级

```java
FodSDK.get().logEvent(FodConstants.Event.SCENE_LEVEL, getRole());
```

### 支付

支付构建如下：

```java
FodPayEntity payEntity = new FodPayEntity();
payEntity.setOrderId("");               // 订单 ID
payEntity.setGoodsId("");               // 商品 ID
payEntity.setGoodsCount(1);             // 商品数量
payEntity.setGoodsName("商品名称");      // 商品名称
payEntity.setGoodsDesc("商品描述");      // 商品描述
payEntity.setPrice(100);                // 商品价格，单位：分
payEntity.setExt("");                   // 扩展字段，无则传空，有则以 Json 字符串格式传入
payEntity.setRole(getRole());           // 角色信息
FodSDK.get().pay(activity, payEntity);
```

回调：

```java
@Override
public void onPay(int code, Bundle bundle) {
    if (code == FodConstants.Code.SUCCESS) {
        Log.d(TAG, "Pay Success");
    }
}
```

### 生命周期等

```java
@Override
protected void onStart() {
    super.onStart();
    FodSDK.get().onStart();
}

@Override
protected void onResume() {
    super.onResume();
    FodSDK.get().onResume();
}

@Override
protected void onPause() {
    super.onPause();
    FodSDK.get().onPause();
}

@Override
protected void onStop() {
    super.onStop();
    FodSDK.get().onStop();
}

@Override
protected void onDestroy() {
    super.onDestroy();
    FodSDK.get().onDestroy();
}

@Override
protected void onRestart() {
    super.onRestart();
    FodSDK.get().onRestart();
}

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    FodSDK.get().onActivityResult(requestCode, resultCode, data);
}

@Override
protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    FodSDK.get().onNewIntent(intent);
}

@Override
public void onConfigurationChanged(@NonNull Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    FodSDK.get().onConfigurationChanged(newConfig);
}

@Override
public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    FodSDK.get().onWindowFocusChanged(hasFocus);
}
```

## 注意事项

- 接入 SDK 后的游戏母包提供到我方时，我方有可能会对包进行修改。
- 游戏 APK 需支持反编译和回编译。
- SDK 的 `minSdk` 为 24，`targetSdk` 为 34。
- 登出接口 CP 可以不调用，但是登出回调中 CP 需要进行处理，比如在登出成功的回调里最好重启游戏或退出游戏。
- 调用退出接口后我方会有回调，如果是成功的回调，CP 需处理退出逻辑，如 `finish()` 等。
- 游戏所有接口应在同一游戏 `Activity` 中调用，勿在 `SplashActivity` 中调用。
