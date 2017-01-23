package com.demo.dong;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/18.
 */

public class SendMsgActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
        ContactsAdapter.OnItemCheckedListener,View.OnClickListener{

    private ListView lv;
    private ContactsAdapter contactsAdapter;
    private ArrayList<Contacts> contactsLists;
    private ArrayList<Contacts> checked_Lists;
    private Button btn_send;
    private Button btn_edit_send;
    private PendingIntent pendingIntent;

    String message = "【测试！测试！】好好学习天天向上好好学习天天向上好好学习"
            + "天天向上好好学习天天向上好好学习天天向上好好学习天天向上好好学习天天向上好"
            + "好学习天天向上好好学习天天向上好好学习天天向上好好学习天天好好学习天天向上"
            + "好好学习天天向上好好学习天天向上好好学习天天向上好好学习天天向上好好学习天天向上好好学习天天向上"
            + "学习天天向上好好学习天天向上好好学习天天向上向上";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendmsg);

        btn_send = (Button)findViewById(R.id.btn_send);
        btn_edit_send = (Button)findViewById(R.id.btn_edit_send);
        lv = (ListView)findViewById(R.id.lv);
        contactsAdapter = new ContactsAdapter(this);
        contactsLists = QueryContactsUtil.getContactsLists(this);
        contactsAdapter.setList(contactsLists);
        lv.setAdapter(contactsAdapter);
        checked_Lists = new ArrayList<>();
        initMsgInfo();
        lv.setOnItemClickListener(this);
        btn_edit_send.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        contactsAdapter.setItemCheckedListener(this);
    }

    private void initMsgInfo() {
        String SENT_SMS_ACTION = "SENT_SMS_ACTION";
        Intent sentIntent = new Intent(SENT_SMS_ACTION);
        pendingIntent = PendingIntent.getBroadcast(this, 0, sentIntent, 0);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        Toast.makeText(SendMsgActivity.this,"短信发送成功",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(SendMsgActivity.this,"短信发送失败",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        },new IntentFilter(SENT_SMS_ACTION));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(SendMsgActivity.this,contactsLists.get(position).phoneNum,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemChecked(int position) {
        Contacts contacts = contactsLists.get(position);
        if(!checked_Lists.contains(contacts)){
            checked_Lists.add(contacts);
        }
    }

    @Override
    public void onItemUnChecked(int position) {
        Contacts contacts = contactsLists.get(position);
        if(checked_Lists.contains(contacts)){
            checked_Lists.remove(contacts);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_send:
                if(checked_Lists.size() > 0){
                    for(Contacts contacts : checked_Lists){
                        Log.i("====",contacts.phoneNum+" -- "+contacts.name);
                        sendMsg(contacts.phoneNum);
                    }
                }
                break;

            case R.id.btn_edit_send:
                if(checked_Lists.size() > 0){
                    for(Contacts contacts : checked_Lists){
                        Log.i("====",contacts.phoneNum+" -- "+contacts.name);
                        sendEditMsg(contacts.phoneNum);
                    }
                }
                break;
        }
    }

    private void sendMsg(String phoneNum) {
        //初始化发短信SmsManager类
        SmsManager smsManager = SmsManager.getDefault();
        List<String> divideContents = smsManager.divideMessage(message);
        for (String text : divideContents) {
            smsManager.sendTextMessage(phoneNum, null, text, pendingIntent, null);
        }
    }

    private void sendEditMsg(String phoneNum){
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+phoneNum));
        intent.putExtra("sms_body", message);
        startActivity(intent);
    }
}
