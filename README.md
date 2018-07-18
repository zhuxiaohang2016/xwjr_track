# xwjr_track

## How To Use ?

### 导包
    compile 'com.github.zhuxiaohang2016:xwjr_track:0.5'

### 配置
    1.TrackConfig.init(a,b,c)  a:context  b:请求url  c:apphubkey  备注：提前调用，否则可能获取不到经纬度
    
### 数据处理
    1.获取通用数据      TrackData.getCommonMap()  返回 Map<String, String>  
    2.上传数据    TrackOperate.upload(a)  a:需要上传的json字符串/ map数组 List<Map<String,String>>  
    3.上传短信内容    TrackOperate.upLoadSMS(a)  a:用户手机号  
    4.上传通讯录   TrackOperate.upLoadContract(a) a:用户手机号
    5.上传通话记录  TrackOperate.upLoadCall(a) a:用户手机号

## 注意事项
    所有权限需要自行处理，目前只会校验权限是否存在，不会主动请求权限，
    涉及到权限如下：
    //定位
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
    <uses-permission android:name="android.permission.LOCATION_HARDWARE"/>
    <uses-permission android:name="android.permission.ACCESS_LOCATION_EXTRA_COMMANDS"/>
    //网络
    <uses-permission android:name="android.permission.INTERNET" />
    //短信
    <uses-permission android:name="android.permission.READ_SMS"/>
    <uses-permission android:name="android.permission.WRITE_SMS"/>
    <uses-permission android:name="android.permission.RECEIVE_SMS"/>
    //联系人(前两个)以及通话记录（前四个）
    <uses-permission android:name="android.permission.READ_CONTACTS" />
    <uses-permission android:name="android.permission.WRITE_CONTACTS" />
    <uses-permission android:name="android.permission.READ_CALL_LOG" />
    <uses-permission android:name="android.permission.WRITE_CALL_LOG" />
