package com.zcdh.mobile.app.activities.messages;

import com.squareup.picasso.Picasso;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.model.AdminAppConfigModelDTO;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.TimeUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fandy on 2015/9/1.
 */
public class MessagesAdapter extends BaseAdapter {

    private Context context;
    private List<AdminAppConfigModelDTO> mInfos;

    public MessagesAdapter(Context context) {
        this.context = context;
        mInfos = new ArrayList<AdminAppConfigModelDTO>();
    }

    public List<AdminAppConfigModelDTO> getItems() {
        return mInfos;
    }

    @Override
    public int getCount() {
        return mInfos.size();
    }

    @Override
    public AdminAppConfigModelDTO getItem(int position) {
        return mInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       /* MessageItem item = null;
        if (convertView != null && convertView instanceof MessageItem) {
            item = (MessageItem) convertView;
        } else {
            item = MessageItem_.build(context);
        }
        item.initWithAdminAppConfigModelDTO(getItem(position));
        return item;*/
        ViewHolder holder=null;
        if (convertView==null) {
            convertView= LayoutInflater.from(context).inflate(R.layout.message_item,parent,false);
            holder=new ViewHolder();
            holder.ivIcon= (ImageView) convertView.findViewById(R.id.iv_msg_icon);
            holder.ivRedPoint= (ImageView) convertView.findViewById(R.id.iv_red_point);
            holder.tvTitle= (TextView) convertView.findViewById(R.id.tv_title);
            holder.tvDate= (TextView) convertView.findViewById(R.id.tv_publish_time);
            holder.tvDes= (TextView) convertView.findViewById(R.id.tv_desc);
            convertView.setTag(holder);
        } else {
            holder= (ViewHolder) convertView.getTag();
        }
        initViews(getItem(position),holder);
        return convertView;
    }

    private void initViews(AdminAppConfigModelDTO info,ViewHolder holder) {
        String imgUrl = null;
        if (info.getImgUrl() != null) {
            imgUrl = info.getImgUrl();
        }
        Integer isRead = 1;
        if (StringUtils.getParams(info.getCustom_param()).get("isRead") != null) {
            isRead = Integer.valueOf(StringUtils.getParams(info.getCustom_param()).get("isRead"));
        }
        Date date = null;
        if (StringUtils.getParams(info.getCustom_param()).get("pushTime") != null) {
            date = new Date(
                    Long.valueOf(StringUtils.getParams(info.getCustom_param()).get("pushTime")));
        }
        init(holder,imgUrl, info.getModel_name(),
                StringUtils.getParams(info.getCustom_param()).get("desc"), date, isRead == 1);
    }

  /*  public void check(){
        for (int i = 0;i<mInfos.size();i++){
            Log.e("Adapter",mInfos.get(i).toString());
        }

    }*/

    /**
     */
    public void updateItems(List<AdminAppConfigModelDTO> elements) {
        mInfos.clear();
        mInfos.addAll(elements);
        notifyDataSetChanged();
    }

    /**
     *
     * @param elements
     */
    public void addToBottom(List<AdminAppConfigModelDTO> elements) {
        mInfos.addAll(elements);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        ImageView ivIcon;
        ImageView ivRedPoint;
        TextView tvTitle;
        TextView tvDate;
        TextView tvDes;
    }

    public void init(ViewHolder holder,String img_url, String title_str, String desc_str,
            Date date, boolean isRead) {
        if (img_url!=null) {
            Picasso.with(context).load(img_url).into(holder.ivIcon);
        }
        holder.tvTitle.setText(title_str);
        if (!StringUtils.isBlank(desc_str)) {
            holder.tvDes.setVisibility(View.VISIBLE);
            holder.tvDes.setText(desc_str);
        } else {
            holder.tvDes.setVisibility(View.GONE);
        }
        if (date != null) {
            holder.tvDate.setText(TimeUtil.converTime(date.getTime()));
        } else {
            holder.tvDate.setVisibility(View.GONE);
        }

        if (!isRead) {
            holder.ivRedPoint.setVisibility(View.VISIBLE);
        } else {
            holder.ivRedPoint.setVisibility(View.GONE);
        }
    }

}