package com.zcdh.mobile.app.activities.nearby;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcAppConfigService;
import com.zcdh.mobile.api.model.AdminAppConfigModelDTO;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.DataLoadInterface;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.SharedPreferencesUtil;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;
import com.zcdh.mobile.utils.SystemUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 添加更多功能
 *
 * @author yangjiannan
 */
@EActivity(R.layout.activity_add_more)
public class AddMoreFuncActivity extends BaseActivity implements
        RequestListener, OnItemClickListener, DataLoadInterface, OnRefreshListener2<ListView> {

    public final static int kREQUEST_ADD_FUNCS = 2021;

    public final static String kDATA_ADD_FUNCS = "kDATA_ADD_FUNCS";

    private String kREQ_ID_findAppConfigModelExt;

    private String kREQ_ID_UpdateAppconfigModelUserext;

    private IRpcAppConfigService nearByService;

    @ViewById(R.id.listView)
    PullToRefreshListView listView;

    private FuncAdapter funcAdapter;

    private List<AdminAppConfigModelDTO> moreFuncs = new ArrayList<>();

    //@Extra
    private HashMap<Long, Integer> selectedFuncs = new HashMap<>();

    private EmptyTipView emptyView;

    @AfterViews
    void bindViews() {
        SystemServicesUtils.displayCustomTitle(this,
                getSupportActionBar(), "更多");

        funcAdapter = new FuncAdapter();
        listView.setAdapter(funcAdapter);
        emptyView = new EmptyTipView(this);
        listView.setEmptyView(emptyView);
        listView.setOnItemClickListener(this);
        listView.setOnRefreshListener(this);
        nearByService = RemoteServiceManager
                .getRemoteService(IRpcAppConfigService.class);
        loadData();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            save();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 加载所有
     */
    @Background
    public void loadData() {
        nearByService.findAppConfigModelExt(ZcdhApplication.getInstance().getZcdh_uid(),
                SharedPreferencesUtil.getValue(AddMoreFuncActivity.this, NearbyMapFragment.class.getSimpleName(), 0l),
                ZcdhApplication.getInstance().getMyLocation().latitude,
                ZcdhApplication.getInstance().getMyLocation().longitude,
                Constants.DEVICES_TYPE,
                SystemUtil.getVerCode(this)
        ).identify(kREQ_ID_findAppConfigModelExt = RequestChannel.getChannelUniqueID(), this);
    }

    /**
     * 保存
     */
    @Background
    void save() {
        if (selectedFuncs != null && selectedFuncs.size() > 0) {
            List<Long> ids = new ArrayList<>();
            List<Integer> isSelected = new ArrayList<>();
            for (Long id : selectedFuncs.keySet()) {
                ids.add(id);
                isSelected.add(selectedFuncs.get(id));
            }

            nearByService.UpdateAppconfigModelUserext(ids,
                    isSelected,
                    ZcdhApplication.getInstance().getZcdh_uid()
            ).identify(
                    kREQ_ID_UpdateAppconfigModelUserext = RequestChannel.getChannelUniqueID(),
                    this);
        }
    }

    class FuncAdapter extends BaseAdapter {

        private DisplayImageOptions options;

        public FuncAdapter() {
            options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true).bitmapConfig(Bitmap.Config.RGB_565)
                    .cacheOnDisk(true).considerExifParams(true).build();
        }

        @Override
        public int getCount() {
            return moreFuncs.size();
        }

        @Override
        public Object getItem(int position) {
            return moreFuncs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder h;
            if (convertView == null) {
                h = new Holder();
                convertView = LayoutInflater.from(getApplicationContext())
                        .inflate(R.layout.func_item, parent, false);
                h.icon = (ImageView) convertView.findViewById(R.id.icon);
                h.nameText = (TextView) convertView.findViewById(R.id.nameText);
                h.checkImg = (ImageView) convertView
                        .findViewById(R.id.checkImg);
                convertView.setTag(h);
            } else {
                h = (Holder) convertView.getTag();
            }
            AdminAppConfigModelDTO dto = moreFuncs.get(position);
            if (dto.getImgUrl() != null
                    && !StringUtils.isBlank(dto.getImgUrl())) {
                ImageLoader.getInstance().displayImage(
                        dto.getImgUrl(), h.icon, options);
            } else {
                h.icon.setImageResource(R.drawable.more_active);
            }
            h.nameText.setText(dto.getModel_name());

            if (dto.getIsSelected() == 0) {
                h.checkImg.setImageResource(R.drawable.btnempty_37x37);
            } else if (dto.getIsSelected() == 1) {
                h.checkImg.setImageResource(R.drawable.btnyes_37x37);
            }
            return convertView;
        }

        class Holder {

            ImageView icon;

            TextView nameText;

            ImageView checkImg;
        }

    }

    @Override
    public void onRequestStart(String reqId) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_more_discovery, menu);
        return true;
    }

    @Override
    public void onRequestSuccess(String reqId, Object result) {
        if (reqId.equals(kREQ_ID_findAppConfigModelExt)) {
            if (result != null) {
                moreFuncs = (List<AdminAppConfigModelDTO>) result;
            }
            funcAdapter.notifyDataSetChanged();
            emptyView.isEmpty(!(moreFuncs != null && moreFuncs.size() > 0));
        }

        if (reqId.equals(kREQ_ID_UpdateAppconfigModelUserext)) {
            if (result != null) {
                int success = (Integer) result;
                if (success == 1) {
                    Intent data = new Intent();
                    data.putExtra(kDATA_ADD_FUNCS, selectedFuncs);
                    setResult(RESULT_OK, data);
                    finish();
                } else {
                    Toast.makeText(this, "服务繁忙", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onRequestFinished(String reqId) {
        listView.postDelayed(new Runnable() {

            @Override
            public void run() {
                listView.onRefreshComplete();
            }
        }, 100);
    }

    @Override
    public void onRequestError(String reqID, Exception error) {
        emptyView.showException((ZcdhException) error, this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        AdminAppConfigModelDTO dto = moreFuncs.get(position - 1);
        if (dto.getIsSelected() == 0) {
            dto.setIsSelected(1);
            selectedFuncs.put(dto.getId(), 1);
        } else {
            dto.setIsSelected(0);
            selectedFuncs.put(dto.getId(), 0);
        }
        funcAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        selectedFuncs.clear();
        loadData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

    }
}
