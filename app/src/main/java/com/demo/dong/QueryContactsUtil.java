package com.demo.dong;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/1/18.
 */

public class QueryContactsUtil {

    /** 电话号码 **/
    private static final int PHONES_NUMBER_INDEX = 1;

    /** 联系人显示名称 **/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;

    /** 获取库Phon表字段 **/
    private static final String[] PHONES_PROJECTION = new String[] {
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.Contacts.Photo.PHOTO_ID,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID };

    public static ArrayList<Contacts> getContactsLists(Context context){
        ArrayList<Contacts> list = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                PHONES_PROJECTION, null, null, null);
        if(null != cursor){
            while (cursor.moveToNext()){
                String phoneNum = cursor.getString(PHONES_NUMBER_INDEX);
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNum))
                    continue;
                String name = cursor.getString(PHONES_DISPLAY_NAME_INDEX);
                Contacts contacts = new Contacts(name, phoneNum);
                list.add(contacts);
            }
        }
        return list;
    }
}
