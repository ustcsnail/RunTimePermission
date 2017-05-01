package com.crazysnail.runtimepermission;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by crazysnail on 2017/4/26.
 * 运行时权限申请抽象Activity类
 */

public abstract class AbstractPermissionActivity extends AppCompatActivity
{
    protected final int  REQUESTCODE=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    /*根据权限申请结果调用业务函数，该函数延迟到子类实现*/
    public abstract void requestPermissionResult(boolean requestResult);


    /**
     * 检查是否需要申请权限
     * 第一个参数是欲授权的权限组，第二个参数是申请授权的activity
     * 需要授权则返回true，不需要授权则返回false
     */
    public boolean needRequestPermission(String[] permissions, AppCompatActivity activity)
    {
        /*如果是安卓6.0之前的版本，不需要授权*/
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }
        for (String perm:permissions) {
            //若存在没授权的权限，则需要授权，返回true
            if(ContextCompat.checkSelfPermission(activity, perm) != PackageManager.PERMISSION_GRANTED)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 提取出未被授权的权限
     * 第一个参数是欲授权的权限组，第二个参数是申请授权的activity
     */
    private List<String> findDeniedPermissions(String[] permissions, AppCompatActivity activity) {
        List<String> permissonList = new ArrayList<String>();

        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(activity, perm) != PackageManager.PERMISSION_GRANTED) //检查权限授予情况
            {
                permissonList.add(perm);
            }
        }
        return permissonList;
    }

    /**
     * 申请权限
     * 第一个参数是欲授权的权限组，第二个参数是申请授权的activity
     */
   public boolean requestPermission(String[] permissions, AppCompatActivity activity) {

        List<String> permissonList = findDeniedPermissions(permissions,activity);

        if (null != permissonList && permissonList.size() > 0)
        {
            String[] needRequestPer = permissonList.toArray(new String[permissonList.size()]);
            ActivityCompat.requestPermissions(activity, needRequestPer, REQUESTCODE);//申请权限
            return true;
        }
       else
        {
            return false;
        }
    }

    /**
     * 权限申请结果回调函数,在该函数中调用预留虚函数
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if (requestCode ==REQUESTCODE) {
            if (verify(grantResults)) {
                requestPermissionResult(true);
            }
            else {
                requestPermissionResult(false);
            }
        }
    }

    /*确认所有权限都被授予*/
    private boolean verify(int[] grantResults) {
        if (grantResults==null||grantResults.length<=0)
            return false;

        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

}
