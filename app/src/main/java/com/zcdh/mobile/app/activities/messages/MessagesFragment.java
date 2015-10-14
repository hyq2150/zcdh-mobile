/**
 * @author jeason, 2014-6-12 上午10:35:11
 */
package com.zcdh.mobile.app.activities.messages;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huanxin.HXMsgHelper;
import com.umeng.analytics.MobclickAgent;
import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcAppConfigService;
import com.zcdh.mobile.api.IRpcNearByService;
import com.zcdh.mobile.api.model.AdminAppConfigModelDTO;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.DataLoadInterface;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.activities.base.BaseFragment;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.RegisterUtil;
import com.zcdh.mobile.utils.SharedPreferencesUtil;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;
import com.zcdh.mobile.utils.SystemUtil;
import com.zcdh.mobile.utils.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息Fragment
 *
 * @author jeason, 2014-6-12 上午10:35:11
 */
@EFragment(R.layout.messages)
public class MessagesFragment extends BaseFragment implements
        OnItemClickListener, PullToRefreshBase.OnRefreshListener<ListView>, RequestListener,
        DataLoadInterface {

    private static final String TAG = MessagesFragment.class.getSimpleName();

    public static final String SYSTEM_NOTIFICATION_ACTIVITY
            = "com.zcdh.mobile.app.activities.messages.SystemNotificationActivity_";

    private static final String HUANXIN_CHAT_ACTIVITY
            = "com.zcdh.mobile.app.activities.messages.ChatActivity_";

    /**
     * 标识是否加载数据
     */
    public boolean flagLoaded;

    @ViewById(R.id.listview)
    PullToRefreshListView listview;

    private EmptyTipView emptyView;

    private MessagesAdapter adapter;

    private IRpcAppConfigService service;

    private IRpcNearByService nearByService;

    private String kREQ_ID_findAppConfigModelByParentCode;

    private String KREQ_ID_FIND_APPCONFIG_MODULE_MAIN_PAGE;

    private String kREQ_ID_readInformation;

    private List<AdminAppConfigModelDTO> infos = new ArrayList<AdminAppConfigModelDTO>();

    private int isReadCount = 0;

    private OnMessageListener mMessageListener;

    public interface OnMessageListener {

        void onNotifyReaded();

        void onNotifyUnReaded();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = RemoteServiceManager
                .getRemoteService(IRpcAppConfigService.class);
        nearByService = RemoteServiceManager.getRemoteService(IRpcNearByService.class);
    }

    public void setOnMessageListener(OnMessageListener listener) {
        mMessageListener = listener;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
    }

    public void checkHuanxinMsg() {
        updateHuanxinMsgTips(HXMsgHelper.getUnreadMsgCountTotal() > 0);
        checkUnRead();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @AfterViews
    void bindViews() {
        emptyView = new EmptyTipView(getActivity());
        listview.setMode(Mode.PULL_FROM_START);
        listview.setOnItemClickListener(this);
        listview.setOnRefreshListener(this);
        listview.setEmptyView(emptyView);
        adapter = new MessagesAdapter(getActivity());
        listview.setAdapter(adapter);
        loadData();
    }

    /**
     * 加载数据
     */
    public void loadData() {
        if (!flagLoaded) {
            findMsgs();
        }
    }

    /**
     * 联网获取消息
     */
    @Background
    public void findMsgs() {
        if (service != null) {
            service.findAppConfigModelByParentId(getUserId(),
                    SharedPreferencesUtil.getValue(getActivity(),MessagesFragment.class.getSimpleName(),0l),
                    ZcdhApplication.getInstance().getMyLocation().latitude,
                    ZcdhApplication.getInstance().getMyLocation().longitude,
                    Constants.DEVICES_TYPE,
                    SystemUtil.getVerCode(getActivity()),
                    SystemServicesUtils.getAppID(getActivity())).identify(
                    kREQ_ID_findAppConfigModelByParentCode = RequestChannel.getChannelUniqueID(),
                    this);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AdminAppConfigModelDTO dto = adapter.getItem(position - 1);
        String androidUrl = dto.getModelUrl();
        if (!RegisterUtil.isRegisterUser(getActivity()) && !androidUrl.equals(
                SYSTEM_NOTIFICATION_ACTIVITY)) {
            ToastUtil.show(R.string.login_first);
            return;
        }
        try {
            //根据不同模块的类名跳转到不同的页面
            ActivityDispatcher.toActivity(getActivity(), androidUrl, StringUtils
                            .getParams(dto.getCustom_param()),
                    adapter.getItem(position - 1).getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!androidUrl.equals(
                HUANXIN_CHAT_ACTIVITY)) {
            setReaded(adapter.getItem(position - 1).getReference_id());
        }
    }

    // 设置消息已读
    @Background
    void setReaded(long id) {
        nearByService.readInformation(id, getUserId()).identify(
                kREQ_ID_readInformation = RequestChannel.getChannelUniqueID(),
                this);
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        findMsgs();
    }

    /**
     * onRefreshComplete();必须放在一个异步调用方法执行 PulltorefreshListview的一个bug
     *
     * @author jeason, 2014-7-25 上午10:09:44
     */
    void onComplete() {
        if (listview.isRefreshing()) {
            listview.onRefreshComplete();
        }
    }

    @Override
    public void onRequestStart(String reqId) {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onRequestSuccess(String reqId, Object result) {

        if(reqId.equals(KREQ_ID_FIND_APPCONFIG_MODULE_MAIN_PAGE)){
            ArrayList<AdminAppConfigModelDTO> appConfigList = (ArrayList<AdminAppConfigModelDTO>) result;
            if (appConfigList != null && appConfigList.size() > 0) {
                for (AdminAppConfigModelDTO dto : appConfigList) {
                    if(dto.getCustom_item_code() != null && dto.getCustom_item_code().equals(Constants.APPCONFIG_CUSTOM_ITEM_CODE_MAIN)){
                        SharedPreferencesUtil.putValue(ZcdhApplication.getInstance(), Constants.APPCONFIG_CUSTOM_ITEM_CODE_MAIN,dto.getId());
                    }else if(dto.getCustom_item_code() != null && dto.getCustom_item_code().equals(Constants.APPCONFIG_CUSTOM_ITEM_CODE_INFORMATION)){
                        SharedPreferencesUtil.putValue(ZcdhApplication.getInstance(),Constants.APPCONFIG_CUSTOM_ITEM_CODE_INFORMATION,dto.getId());
                    }
                }
                loadData();
            }
        }
        if (reqId.equals(kREQ_ID_findAppConfigModelByParentCode)) {
            List<AdminAppConfigModelDTO> tmp = (List<AdminAppConfigModelDTO>) result;
            if (tmp != null && !tmp.isEmpty()) {
                infos.clear();
                for (AdminAppConfigModelDTO dto : tmp) {
                    infos.add(dto);
                }
                adapter.updateItems(infos);
                flagLoaded = true;
//                checkUnRead();
                checkHuanxinMsg();
            }
        }

        if (reqId.equals(kREQ_ID_readInformation)) {
            findMsgs();
        }
        emptyView.isEmpty(!(adapter.getCount() > 0));
    }

    private void checkUnRead() {
        isReadCount = 0;
        for (AdminAppConfigModelDTO item : adapter.getItems()) {
            if (StringUtils.getParams(item.getCustom_param()).get("isRead") != null &&
                    StringUtils.getParams(item.getCustom_param()).get("isRead").equals("0")) {
                isReadCount++;
                break;
            }
        }
        if (mMessageListener != null) {
            if (isReadCount > 0) {
                mMessageListener.onNotifyUnReaded();//设置有新的消息
            } else {
                mMessageListener.onNotifyReaded(); // 设置为没有消息的样式
            }
        }

    }

    @Override
    public void onRequestFinished(String reqId) {
        onComplete();
    }

    @Override
    public void onRequestError(String reqID, Exception error) {
        if (infos != null && infos.size() > 0) {
            ToastUtil.show(((ZcdhException) error).getErrMessage());
        }
        emptyView.showException((ZcdhException) error, this);
    }

    /**
     * 更新客服消息项显示红点
     */
    public void updateHuanxinMsgTips(boolean hasNewMsg) {
        List<AdminAppConfigModelDTO> dtos=adapter.getItems();
        for (int i = 0; i < dtos.size(); i++) {
            if (dtos.get(i).getModelUrl() != null && dtos.get(i).getModelUrl().equals(HUANXIN_CHAT_ACTIVITY)) {
                String param = hasNewMsg ? "isRead=0" : "isRead=1";
                dtos.get(i).setCustom_param(param);
                break;
            }
        }
        adapter.notifyDataSetChanged();
    }
}