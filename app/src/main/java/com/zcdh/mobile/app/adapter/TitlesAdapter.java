package com.zcdh.mobile.app.adapter;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.model.InformationTitleDTO;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class TitlesAdapter extends BaseAdapter {

    private DisplayImageOptions options;

    private List<InformationTitleDTO> titles;

    private Context context;

    public void setTitles(List<InformationTitleDTO> titles) {
        this.titles = titles;
        notifyDataSetChanged();
    }

    static class ViewHolder {

        ImageView image_view;

        ImageView hotImg;

        TextView tv_title;

        RelativeLayout ll_app_item;
    }

    public TitlesAdapter(Context ctx, List<InformationTitleDTO> titles) {
        this.titles = titles;
        context = ctx;
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
                .considerExifParams(true).build();
    }

    @Override
    public int getCount() {
        return titles.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (titles.size() >= position) {
            return position;
        }
        return titles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView,
            ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.app_item_view, null);
            holder.image_view = (ImageView) convertView
                    .findViewById(R.id.iv_app_icons);
            holder.hotImg = (ImageView) convertView
                    .findViewById(R.id.hotImg);
            holder.tv_title = (TextView) convertView
                    .findViewById(R.id.tv_app_title);
            holder.ll_app_item = (RelativeLayout) convertView
                    .findViewById(R.id.ll_app_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == titles.size()) {
            holder.image_view.setImageResource(R.drawable.ic_plus);
            holder.hotImg.setVisibility(View.GONE);
            holder.tv_title.setText("添加更多");
        } else {
            final InformationTitleDTO title = titles.get(position);
            holder.ll_app_item
                    .setBackgroundResource(R.drawable.white_corner_rect);
            holder.hotImg.setVisibility(View.VISIBLE);
            holder.tv_title.setVisibility(View.VISIBLE);
            if (title != null && title.getImg() != null) {
                ImageLoader.getInstance()
                        .displayImage(title.getImg().getBig(),
                                holder.image_view, options);
            }
            holder.tv_title.setText(title.getTitle());

            // 0 普通，31 最新，32 最热门
            // 显示 热、新 标识
            if (title.getStatus() == 32) {
                holder.hotImg.setImageResource(R.drawable.hot_54x31);
                holder.hotImg.setVisibility(View.VISIBLE);
            }
            if (title.getStatus() == 31) {
                holder.hotImg.setImageResource(R.drawable.new_54x31);
                holder.hotImg.setVisibility(View.VISIBLE);
            }
            if (title.getStatus() == 0) {
                holder.hotImg.setVisibility(View.GONE);
            }
        }
        return convertView;
    }


}
