package com.crazysnail.runtimepermission;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AbstractPermissionActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String [] permissions={Manifest.permission.CALL_PHONE};
        if(needRequestPermission(permissions,this))
        {
            requestPermission(permissions,this);
        }
        else
        {
            call();
        }


    }

    /*实现预留的回调接口*/
    @Override
    public void requestPermissionResult(boolean requestResult)
    {
        
        if(true)
        {
            call();
        }
        else
        {
            Toast.makeText(this,"您未授权",Toast.LENGTH_LONG).show();
        }
    }

    private void call() {

        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:10086"));
            startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}
