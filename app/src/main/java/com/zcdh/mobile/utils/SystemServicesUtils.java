package com.zcdh.mobile.utils;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.model.CheckUserRequisiteDTO;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.activities.BackupMainActivity;
import com.zcdh.mobile.app.activities.personal.BasicInfoActivity_;
import com.zcdh.mobile.framework.events.MyEvents;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.system.email.Email;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * @author jeason, 2014-4-2 下午5:45:55
 */
public class SystemServicesUtils {

    public static void hideActionbar(AppCompatActivity activity) {
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    public static void displayCustomedTitle(Context context, ActionBar actionbar, int title_res) {
        displayCustomedTitle(context, actionbar, context.getResources().getString(title_res));
    }

    /**
     * @version 1.0
     * @author jeason
     * @created 2014年4月3日
     */
    public static void displayCustomedTitle(Context context, ActionBar actionbar, String title) {
        actionbar.setBackgroundDrawable(context.getResources().getDrawable(R.color.menu_normal));

        // actionbar.setHomeButtonEnabled(true);
        // actionbar.setDisplayHomeAsUpEnabled(true);
        // actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.menu_back);
        actionbar.setDisplayShowCustomEnabled(true);
        actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
                | ActionBar.DISPLAY_HOME_AS_UP);

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        View view = LayoutInflater.from(context).inflate(R.layout.actionbar_title, null);
        actionbar.setCustomView(view, params);
        TextView tvTitle = (TextView) actionbar.getCustomView().findViewById(R.id.actionBarTitle);
        tvTitle.setText(title);
//		actionbar.setDisplayHomeAsUpEnabled(false);

    }

    /**
     * actiobar 标题居中
     */
    public static void setActionBarCustomTitle(Context context, ActionBar actionbar, String title) {

        // setActionBarCustomTitleAndIcon(context, actionbar, title, 0);
        displayCustomedTitle(context, actionbar, title);
    }

    /**
     *
     * @param context
     * @param actionbar
     * @param title
     * @param icon
     */
    public static void setActionBarCustomTitleAndIcon(Context context, ActionBar actionbar,
            String title, int icon) {
        if (icon != 0) {
            actionbar.setIcon(icon);
        }

        actionbar.setBackgroundDrawable(context.getResources().getDrawable(R.color.menu_normal));

        actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
                | ActionBar.DISPLAY_HOME_AS_UP);

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        View view = LayoutInflater.from(context).inflate(R.layout.actionbar_title, null);
        actionbar.setCustomView(view, params);
        TextView tvTitle = (TextView) actionbar.getCustomView().findViewById(R.id.actionBarTitle);
        tvTitle.setText(title);
        actionbar.setDisplayHomeAsUpEnabled(true);

    }

    /**
     * @author jeason, 2014-4-7 上午9:14:51
     */
    public static void set_login(Activity activity, Long uid) {
        // 设置zcdh_uid的值
        // 用微博登录成功的状态
        // 缓存在application
        ZcdhApplication application = (ZcdhApplication) activity.getApplication();
        application.setZcdh_uid(uid);

        // 缓存在SharedPreferences
        SharedPreferences sharedPreferences = activity
                .getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("zcdh_uid", uid);
        editor.commit();

        // 向主页面发送finish
        MyEvents.post(BackupMainActivity.kMSG_RESTART_APP, 1);

        activity.finish();
    }

    public static long getZCDH_UID(Context activity) {
        SharedPreferences sharedPreferences = activity
                .getSharedPreferences("User", Context.MODE_PRIVATE);
        long zcdh_uid = sharedPreferences.getLong("zcdh_uid", 0l);
        return zcdh_uid;
    }

    /**
     * copy asset 下的.db文件到/data目录下
     */
    public static boolean copyDbFromAssetsToSystem(Context context, String dbName) {
        // String path = "/data/data/com.zcdh.mobile/databases";
        String path = "/storage/emulated/0/mobiless/databases";
        File dbPath = new File(path + "/" + dbName);
        if (dbPath.exists()) {
            return true;
        } else {
            try {
                dbPath.createNewFile();

				/* 从assets中读取 */
                InputStream is = context.getAssets().open(dbName);
                FileOutputStream fos = new FileOutputStream(dbPath);
                byte[] bf = new byte[1024];
                int count = 0;

                while ((count = is.read(bf)) > 0) {
                    fos.write(bf, 0, count);
                }

                fos.flush();
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;

    }

    /**
     * 开始加载数据
     */
    public static void startLoading(Activity c, View v, int animId) {

        ImageView loadingImg = (ImageView) v;
        Animation anim = AnimationUtils.loadAnimation(c, animId);
        loadingImg.setVisibility(View.VISIBLE);
        loadingImg.startAnimation(anim);
    }

    /**
     *
     * @param c
     * @param v
     */
    public static void stopLoading(Activity c, View v) {

        v.clearAnimation();
        v.setVisibility(View.GONE);

    }

    public static HashMap<String, Object> getPlatformDevInfo(String name) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();

        if (Wechat.NAME.equals(name)) {
            hashMap.put("Id", 1);
            hashMap.put("SortId", 1);
            hashMap.put("AppId", com.zcdh.mobile.app.activities.vacation.payment.Constants.APP_ID);
            hashMap.put("AppSecret", "4dab4efae4b91338e6ff241c6ea96c50");
            hashMap.put("BypassApproval", false);
            hashMap.put("Enable", true);
            return hashMap;
        }

        if (WechatMoments.NAME.equals(name)) {
            hashMap.put("Id", 2);
            hashMap.put("SortId", 2);
            hashMap.put("AppId", com.zcdh.mobile.app.activities.vacation.payment.Constants.APP_ID);
            hashMap.put("AppSecret", "4dab4efae4b91338e6ff241c6ea96c50");
            hashMap.put("BypassApproval", false);
            hashMap.put("Enable", true);
            return hashMap;
        }

        if (SinaWeibo.NAME.equals(name)) {
            hashMap.put("Id", 3);
            hashMap.put("SortId", 3);
            hashMap.put("AppKey", Constants.SINA_APP_KEY);
            hashMap.put("AppSecret", "448d1bfaaddf94dc8116ce5de4026602");
            hashMap.put("RedirectUrl", Constants.REDIRECT_URL);
            hashMap.put("ShareByAppClient", true);
            hashMap.put("Enable", true);

            return hashMap;
        }

        if (TencentWeibo.NAME.equals(name)) {
            hashMap.put("Id", 4);
            hashMap.put("SortId", 4);
            hashMap.put("AppKey", "801491081");
            hashMap.put("AppSecret", "8c07114ce939ed5f99285aafa21e8adc");
            hashMap.put("RedirectUrl", "http://www.sharesdk.cn");
            hashMap.put("ShareByAppClient", true);
            hashMap.put("Enable", true);
            return hashMap;
        }

        if (QQ.NAME.equals(name)) {
            hashMap.put("Id", 5);
            hashMap.put("SortId", 5);
            hashMap.put("AppId", "101043530");
            hashMap.put("AppKey", "f183ccb8d176b78faa02cb97bed0fa45");
            hashMap.put("Enable", "true");
            return hashMap;
        }

        if (QZone.NAME.equals(name)) {
            hashMap.put("Id", 6);
            hashMap.put("SortId", 6);
            hashMap.put("AppKey", "101043530");
            hashMap.put("AppSecret", "f183ccb8d176b78faa02cb97bed0fa45");
            hashMap.put("ShareByAppClient", true);
            hashMap.put("Enable", "true");
            return hashMap;
        }

        if (Email.NAME.equals(name)) {
            hashMap.put("Id", 7);
            hashMap.put("SortId", 7);
            hashMap.put("Enable", "true");
            return hashMap;
        }

        if (ShortMessage.NAME.equals(name)) {
            hashMap.put("Id", 8);
            hashMap.put("SortId", 8);
            hashMap.put("Enable", "true");
            return hashMap;
        }

        return null;
    }

    public static void initShareSDK(Activity activity) {
        ShareSDK.initSDK(activity, Constants.SHARESDK_APPKEY);
        ShareSDK.setPlatformDevInfo(WechatMoments.NAME, getPlatformDevInfo(WechatMoments.NAME));
        ShareSDK.setPlatformDevInfo(SinaWeibo.NAME, getPlatformDevInfo(SinaWeibo.NAME));
        ShareSDK.setPlatformDevInfo(TencentWeibo.NAME, getPlatformDevInfo(TencentWeibo.NAME));
        ShareSDK.setPlatformDevInfo(QQ.NAME, getPlatformDevInfo(QQ.NAME));
        ShareSDK.setPlatformDevInfo(Email.NAME, getPlatformDevInfo(Email.NAME));
        ShareSDK.setPlatformDevInfo(QZone.NAME, getPlatformDevInfo(QZone.NAME));
        ShareSDK.setPlatformDevInfo(ShortMessage.NAME, getPlatformDevInfo(ShortMessage.NAME));
        ShareSDK.setPlatformDevInfo(Wechat.NAME, getPlatformDevInfo(Wechat.NAME));
    }

    public static void stopShareSDK(Activity activity) {
        ShareSDK.stopSDK(activity);
    }

    public static String getImageUrl(String filepath, String fileCategory, String fileName,
                                     int size_edition) {
        String image_url = String
                .format("http://192.168.1.150:81/%s/%s/%d/%s", filepath, fileCategory, size_edition,
                        fileName);
        return image_url;
    }

    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = tm.getDeviceId();

            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);

            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }

            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }

            json.put("device_id", device_id);

            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return apiKey;
    }

    public static boolean hasBind(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String flag = sp.getString("bind_flag", "");
        if ("ok".equalsIgnoreCase(flag)) {
            return true;
        }
        return false;
    }

    public static void setBind(Context context, boolean flag) {
        String flagStr = "not";
        if (flag) {
            flagStr = "ok";
        }
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("bind_flag", flagStr);
        editor.commit();
    }

    /**
     * 是否完善基本信息
     */
    public static boolean isCompleteInfo(final Context context) {
        CheckUserRequisiteDTO chkDto = ZcdhApplication.getInstance().getRequisiteDTO();
        boolean ok = false;
        if (chkDto != null) {
            if (chkDto.getIsUserRequisiteFilled() == 1) { // 已完善资料
                ok = true;
            }
        }
        if (!ok) {
            AlertDialog.Builder ab = new AlertDialog.Builder(context);
            ab.setTitle("完善基本资料");
            ab.setIcon(android.R.drawable.ic_dialog_info);
            ab.setPositiveButton("去完善", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    BasicInfoActivity_.intent(context).start();
                }
            });
            ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            ab.create().show();
        }
        return ok;
    }

    public static Class<?> getClass(String className) {

        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return clazz;
    }

    /**
     * 调用图库读取系统的图片
     */
    public static void userGallery(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * @param activity 调用相机的actitiy
     * @return 照片的预定路径(不一定存在, 如果照相界面取消了照相, 或者, 没有sdCard)
     * @describe 相机调用
     */
    public static String useCamera(Activity activity, int requestCode) {
        Uri picUri = getPicUri(activity);
        if (null == picUri) {
            return null;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, picUri);
        activity.startActivityForResult(intent, requestCode);
        return picUri.getPath();
    }

    public static Uri getPicUri(Context context) {
        // 验证是否有SDcard
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            try {
                File dir = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File pic = new File(dir, System.currentTimeMillis() + ".jpg");
                return Uri.fromFile(pic);
            } catch (Exception e) {
                Log.w("com.zcdh.mobile.utils.SystemServicesUtils.getPicUri",
                        "not fund dir>>" + e.getMessage());
            }
        } else {
            // 没有SDcard
            Toast.makeText(context, "no sdcard", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public static String onGalleryBack(Activity activity, Intent data) {
        if (data != null) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = activity.getContentResolver()
                    .query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String new_photoString = cursor.getString(columnIndex);
            cursor.close();

            return new_photoString;
        }
        return null;
    }
}
