/**
 * @author jeason, 2014-6-12 上午10:35:11
 */
package com.zcdh.mobile.app.activities.messages;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;
import com.zcdh.comm.entity.Page;
import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcNearByService;
import com.zcdh.mobile.api.model.InformationDTO;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.DataLoadInterface;
import com.zcdh.mobile.app.activities.base.BaseFragment;
import com.zcdh.mobile.app.activities.main.MainPageFragment;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.app.views.MessageItem;
import com.zcdh.mobile.app.views.MessageItem_;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.RegisterUtil;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息Fragment
 *
 * @author jeason, 2014-6-12 上午10:35:11
 */
@EFragment(R.layout.messages)
public class MessagesFragment extends BaseFragment implements
        OnItemClickListener, OnRefreshListener2<ListView>, RequestListener,
        DataLoadInterface {

    private static final String TAG = MessagesFragment.class.getSimpleName();

    /**
     * 标识是否加载数据
     */
    public boolean flagLoaded;

    @ViewById(R.id.listview)
    PullToRefreshListView listview;

    private EmptyTipView emptyView;

    private MessagesAdapter adapter;

    private IRpcNearByService service;

    private int currentPage = 1;

    private final int PageSize = 10;

    private String kREQ_ID_findInformationByUserId;

    private String kREQ_ID_readInformation;

    private Page<InformationDTO> infos;

    private int isReadCount = 0;

    private MainPageFragment mCallbacker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = RemoteServiceManager
                .getRemoteService(IRpcNearByService.class);
    }

    public void setCallbacker(MainPageFragment callbacker) {
        mCallbacker = callbacker;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onPageStart(TAG);
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }

    @AfterViews
    void bindViews() {
        emptyView = new EmptyTipView(getActivity());
        listview.setMode(Mode.PULL_FROM_START);
        listview.setOnItemClickListener(this);
        listview.setOnRefreshListener(this);
        listview.setEmptyView(emptyView);
        adapter = new MessagesAdapter();
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

    /*
     * 联网获取消息
     */
    @Background
    public void findMsgs() {
        Log.i(TAG, "findMsgs");
        if (service != null) {
            service.findInformationByUserId(getUserId(), currentPage, PageSize)
                    .identify(
                            kREQ_ID_findInformationByUserId = RequestChannel
                                    .getChannelUniqueID(),
                            this);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
            long arg3) {
        InformationDTO informationDTO = infos.getElements().get(position - 1);
        String androidUrl = informationDTO.getAnroidURL();

        if (!RegisterUtil.isRegisterUser(getActivity())
                && !"com.zcdh.mobile.app.activities.messages.SystemNotificationActivity_"
                .equals(androidUrl)) {
            ToastUtil.show(R.string.login_first);
            return;
        }
        try {
            if (adapter.getItem(position - 1).getIsRead() == 0
                    && isReadCount > 0) {
                isReadCount--;
                if (isReadCount == 0) {
                    // at.invokeMethod(mCallbacker, "notifyReaded");
                    mCallbacker.notifyReaded();
                }
            }
            adapter.getItem(position - 1).setIsRead(1);
            adapter.notifyDataSetChanged();
            InformationDTO dto = adapter.getItem(position - 1);
            setReaded(dto.getId());
            ActivityDispatcher.toActivity(getActivity(),
                    adapter.getItem(position - 1).getAnroidURL(), StringUtils
                            .getParams(adapter.getItem(position - 1)
                                    .getCustomParam()),
                    adapter.getItem(position - 1).getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 设置消息已读
    @Background
    void setReaded(long id) {
        service.readInformation(id, getUserId()).identify(
                kREQ_ID_readInformation = RequestChannel.getChannelUniqueID(),
                this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        getLatest();
        onComplete();
    }

    /**
     * @author jeason, 2014-6-6 下午4:47:07
     */
    private void getLatest() {
        currentPage = 1;
        findMsgs();
        onComplete();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        if (RegisterUtil.isRegisterUser(getActivity())) {
            getMore();
        } else {
            Toast.makeText(getActivity(),
                    getResources().getString(R.string.login_first),
                    Toast.LENGTH_SHORT).show();
            onComplete();
        }
    }

    /**
     * @author jeason, 2014-6-6 下午4:47:05 加載更多
     */
    private void getMore() {
        if (infos == null) {
            currentPage = 1;
        } else {
            if (infos.hasNextPage()) {
                currentPage = infos.getNextPage();
            } else {
                onComplete();
                ToastUtil.show(R.string.no_more_data);
                return;
            }
        }
        findMsgs();
        onComplete();
    }

    /**
     * onRefreshComplete();必须放在一个异步调用方法执行 PulltorefreshListview的一个bug
     *
     * @author jeason, 2014-7-25 上午10:09:44
     */
    @UiThread
    void onComplete() {
        listview.onRefreshComplete();
    }

    @Override
    public void onRequestStart(String reqId) {
        // TODO Auto-generated method stub

    }

    @SuppressWarnings("unchecked")
    @Override
    public void onRequestSuccess(String reqId, Object result) {
        if (reqId.equals(kREQ_ID_findInformationByUserId)) {
            if (result != null) {
                infos = (Page<InformationDTO>) result;
                if (infos.getCurrentPage() == 1) {
                    adapter.updateItems(infos.getElements());
                } else {
                    adapter.addToBottom(infos.getElements());
                }
                checkUnRead();
                flagLoaded = true;
            }
        }

        if (reqId.equals(kREQ_ID_readInformation)) {
            adapter.notifyDataSetChanged();
        }

    }

    /**
     * @author jeason, 2014-6-13 下午3:25:37
     */
    private void checkUnRead() {
        isReadCount = 0;
        for (InformationDTO item : adapter.getItems()) {
            if (item.getIsRead() != null && item.getIsRead() == 0) {
                if (mCallbacker != null) {
                    isReadCount++;
                }
                break;
            }
        }
        if (mCallbacker != null) {

            if (isReadCount > 0) {
                mCallbacker.notifyUnReaded();//
            } else {
                mCallbacker.notifyReaded(); // 设置为没有消息的样式
            }
        }

    }

    @Override
    public void onRequestFinished(String reqId) {
        onComplete();
    }

    @Override
    public void onRequestError(String reqID, Exception error) {
        if (infos != null && infos.getElements() != null
                && infos.getElements().size() > 0) {
            ToastUtil.show(((ZcdhException) error).getErrMessage());
        }
        emptyView.showException((ZcdhException) error, this);
    }

    /**
     * 测试推送
     */
    private class MessagesAdapter extends BaseAdapter {

        private List<InformationDTO> mInfos;

        public MessagesAdapter() {
            mInfos = new ArrayList<InformationDTO>();
        }

        public List<InformationDTO> getItems() {
            return mInfos;
        }

        @Override
        public int getCount() {
            return mInfos.size();
        }

        @Override
        public InformationDTO getItem(int position) {
            return mInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MessageItem item = null;
            if (convertView != null && convertView instanceof MessageItem) {
                item = (MessageItem) convertView;
            } else {
                item = MessageItem_.build(getActivity());
            }
            item.initWithInformationDTO(getItem(position));
            return item;
        }

        /**
         * @author jeason, 2014-6-12 下午2:45:04
         */
        public void updateItems(List<InformationDTO> elements) {
            mInfos.clear();
            mInfos.addAll(elements);
            notifyDataSetChanged();
        }

        /**
         * @author jeason, 2014-6-12 下午2:45:06
         */
        public void addToBottom(List<InformationDTO> elements) {
            mInfos.addAll(elements);
            notifyDataSetChanged();
        }

    }
}
