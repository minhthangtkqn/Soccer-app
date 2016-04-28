package com.example.tlds.testwebservice;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by PC on 4/27/2016.
 */
public class CustomListAdapter extends BaseAdapter {
    private List<Match> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public CustomListAdapter(Context aContext,  List<Match> listData) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_view_item, null);
            holder = new ViewHolder();
            holder.field_name = (TextView) convertView.findViewById(R.id.txt_Field);
            holder.maximum_players = (TextView) convertView.findViewById(R.id.txt_Max);
            holder.price = (TextView) convertView.findViewById(R.id.txt_Price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Match country = this.listData.get(position);
        holder.field_name.setText("San: " + country.getField_name());
        holder.maximum_players.setText("Maximum players: " + country.getMaximum_players());
        holder.price.setText("Price: " + country.getPrice());

        return convertView;
    }

    // Tìm ID của Image ứng với tên của ảnh (Trong thư mục mipmap).
    public int getMipmapResIdByName(String resName)  {
        String pkgName = context.getPackageName();

        // Trả về 0 nếu không tìm thấy.
        int resID = context.getResources().getIdentifier(resName , "mipmap", pkgName);
        Log.i("CustomListView", "Res Name: " + resName + "==> Res ID = " + resID);
        return resID;
    }

    static class ViewHolder {
        TextView field_name;
        TextView maximum_players;
        TextView price;
    }
}
