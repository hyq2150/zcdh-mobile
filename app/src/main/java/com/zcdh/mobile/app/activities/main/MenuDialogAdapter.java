package com.zcdh.mobile.app.activities.main;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.model.AdminAppConfigModelDTO;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.activities.settings.SettingsHomeActivity;
import com.zcdh.mobile.app.extension.ExtensionDialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by fandy on 2015/8/29.
 */
public class MenuDialogAdapter extends BaseAdapter {

    private ArrayList<AdminAppConfigModelDTO> datas;
    private Context ctx;
    private DisplayImageOptions options;


    public MenuDialogAdapter(Context context, ArrayList<AdminAppConfigModelDTO> datas) {
        this.ctx = context;
        this.datas = datas;
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
                .considerExifParams(true).build();
    }

    public void setDatas(ArrayList<AdminAppConfigModelDTO> datas) {
        this.datas = datas;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(ctx).inflate(R.layout.main_menu_listview_item, parent, false);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.menu_item_icon);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.menu_item_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (ZcdhApplication.getInstance().getZcdh_uid() < 0 && datas.get(position).getModelUrl() != null
                && !datas.get(position).getModelUrl().contains(SettingsHomeActivity.class.getSimpleName())
                && !datas.get(position).getModelUrl().contains(ExtensionDialog.class.getSimpleName())) {
            viewHolder.textView.setTextColor(ctx.getResources().getColor(R.color.grey1));
        } else {
            viewHolder.textView.setTextColor(ctx.getResources().getColor(R.color.font_color));
        }
        viewHolder.textView.setText(datas.get(position).getModel_name());
        if (datas.get(position).getImgUrl()!=null) {
            try {
                ImageLoader.getInstance()
                        .displayImage(datas.get(position).getImgUrl(),
                                viewHolder.icon, options);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView icon;
        TextView textView;
    }
}
