# xwjr_track

## How To Use ?

### 导包
    compile 'com.github.zhuxiaohang2016:xwjr_track:2.6'
    

### 配置
    1.TrackConfig.init(a,b,c,d)  a:context  b:请求url  c:apphubkey d:amapKey   备注：提前调用，否则可能获取不到经纬度
      TrackConfig.init(a,b,c,e,d)  a:context  b:请求url  c:apphubkey d:amapKey e:单次上传数据条数限制，默认20   备注：提前调用，否则可能获取不到经纬度
    2.TrackConfig.setLocationInterval(a)    a:定位间隔时间，默认60000（60s）
    3.TrackConfig.localDataAutoUpload(a)    a:是否自动上传本地数据(超过单次上传数据条数限制，则会在3s后自动上传)，默认true
    4.TrackConfig.setDebug(a)   a:debug模式，决定日志是否输出，默认为false
    
### 数据处理
    1.获取通用数据      TrackData.getCommonMap()  返回 Map<String, String>  
    2.上传数据    TrackOperate.upload(a)  a:需要上传的json字符串/ map数组 List<Map<String,String>>  
    3.上传短信内容    TrackOperate.upLoadSMS(a)  a:用户手机号  
    4.上传通讯录   TrackOperate.upLoadContract(a) a:用户手机号
    5.上传通话记录  TrackOperate.upLoadCall(a) a:用户手机号
    6.本地存储单个map数据  TrackLocalData.saveTrackData(a)  a: Map<String, String>
    7.本地存储mapList数据  TrackLocalData.saveTrackData(a)  a: List<Map<String,String>> 
    8.获取本地存储的mapLists数据  TrackLocalData.getTrackData()  返回 List<Map<String,String>> 
    9.清楚本地存储的数据 TrackLocalData.clearTrackData()  
    10.上传本地存储的数据 TrackOperate.upLoadLocalData(a)  a:上传后是否清楚本地数据   非必选项，默然true  

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
