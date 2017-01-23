package com.demo.dong;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/1/18.
 */

public class ContactsAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private ArrayList<Contacts> list;
    private Context mContext;
    private HashMap<Integer, Boolean> cb_state = new HashMap<Integer,Boolean>();

    private OnItemCheckedListener itemCheckedListener;

    public void setItemCheckedListener(OnItemCheckedListener listener){
        this.itemCheckedListener = listener;
    }

    public interface OnItemCheckedListener{
        void onItemChecked(int position);
        void onItemUnChecked(int position);
    }

    public ContactsAdapter(Context context){
        this.mContext = context;
       inflater = LayoutInflater.from(context);
    }

    public void setList(ArrayList<Contacts> list){
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return list == null ? null:list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item,null);
            viewHolder.tv_info = (TextView) convertView.findViewById(R.id.tv_info);
            viewHolder.cb = (CheckBox) convertView.findViewById(R.id.checkBox);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cb_state.put(position,isChecked);
                    itemCheckedListener.onItemChecked(position);
                }else {
                    cb_state.remove(position);
                    itemCheckedListener.onItemUnChecked(position);
                }
            }
        });

        Contacts contacts = list.get(position);
        viewHolder.tv_info.setText(contacts.name+" : "+contacts.phoneNum);
        viewHolder.cb.setChecked(cb_state.get(position)==null ? false:true);

        return convertView;
    }

    class ViewHolder{
        TextView tv_info;
        CheckBox cb;
    }
}
