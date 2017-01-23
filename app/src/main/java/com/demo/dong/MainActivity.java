package com.demo.dong;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private boolean flag_contacts = true;
    private boolean flag_sendMsg = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkContactsPermission();
                checkSendMsgPermission();

                if(flag_contacts && flag_sendMsg){
                    startActivity(new Intent(MainActivity.this,SendMsgActivity.class));
                }
            }
        });
    }

    private void checkContactsPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)){
                Toast.makeText(MainActivity.this,"需要重新开启读取联系人列表权限", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},0x101);
        }else{
            flag_contacts = true;
        }
    }

    private void checkSendMsgPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)){
                Toast.makeText(MainActivity.this,"需要重新开启发送短信权限", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},0x102);
        }else{
            flag_sendMsg = false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 0x101:
                if(grantResults.length >0 &&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //用户同意授权
                   flag_contacts = true;
                }else{
                    //用户拒绝授权
                    flag_contacts = false;
                    Toast.makeText(MainActivity.this,"联系人列表权限被拒绝", Toast.LENGTH_SHORT).show();
                }
                break;

            case 0x102:
                if(grantResults.length >0 &&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //用户同意授权
                    flag_sendMsg = true;
                }else{
                    //用户拒绝授权
                    flag_sendMsg = false;
                    Toast.makeText(MainActivity.this,"发送短信权限被拒绝", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
