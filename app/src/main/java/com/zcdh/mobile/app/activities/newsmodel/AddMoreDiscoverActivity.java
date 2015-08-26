package com.zcdh.mobile.app.activities.newsmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zcdh.comm.entity.Page;
import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobAppService;
import com.zcdh.mobile.api.model.InformationTitleDTO;
import com.zcdh.mobile.app.DataLoadInterface;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

/**
 * 添加更多功能
 * 
 * @author yangjiannan
 *
 */
@EActivity(R.layout.activity_add_more)
public class AddMoreDiscoverActivity extends BaseActivity implements
		RequestListener, OnItemClickListener, OnRefreshListener2<ListView>,
		DataLoadInterface {

	public final static int kREQUEST_ADD_DISCOVERY = 2020;
	public final static String kDATA_FUNCS = "kData_funcs";

	private String kREQ_ID_findAllInformationTitleList;
	private String kREQ_ID_addInformationTitle;

	private IRpcJobAppService appService;

	@ViewById(R.id.listView)
	PullToRefreshListView listView;

	private EmptyTipView emptyTipView;

	private FuncAdapter funcAdapter;

	private List<InformationTitleDTO> moreFuncs = new ArrayList<InformationTitleDTO>();

	@Extra
	HashMap<Long, InformationTitleDTO> selectedFuncs = new HashMap<Long, InformationTitleDTO>();

	@AfterViews
	void bindViews() {
		SystemServicesUtils.setActionBarCustomTitle(this,
				getSupportActionBar(), "更多");

		funcAdapter = new FuncAdapter();
		listView.setAdapter(funcAdapter);
		emptyTipView = new EmptyTipView(this);
		listView.setEmptyView(emptyTipView);
		listView.setOnItemClickListener(this);
		listView.setOnRefreshListener(this);
		appService = RemoteServiceManager
				.getRemoteService(IRpcJobAppService.class);
		loadData();
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			if (selectedFuncs.size() <= 0) {
				Toast.makeText(getApplicationContext(), "请至少选择一项",
						Toast.LENGTH_SHORT).show();
			} else {
				save();
			}
		}
		return false;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (selectedFuncs.size() <= 0) {
				Toast.makeText(getApplicationContext(), "请至少选择一项",
						Toast.LENGTH_SHORT).show();
			} else {
				save();
			}
			return true;
		}
		return false;
	}

	/**
	 * 加载所有
	 */
	@Background
	public void loadData() {
		appService
				.findAllInformationTitleList(1, 10000)
				.identify(
						kREQ_ID_findAllInformationTitleList = RequestChannel
								.getChannelUniqueID(),
						this);
	}

	/**
	 * 保存
	 */
	@Background
	void save() {
		if (selectedFuncs != null && selectedFuncs.size() > 0) {
			List<String> ids = new ArrayList<String>();

			for (Long id : selectedFuncs.keySet()) {
				ids.add(id + "");
			}
			appService
					.updateInformationTitle(ids, getUserId())
					.identify(
							kREQ_ID_addInformationTitle = RequestChannel
									.getChannelUniqueID(),
							this);
		}
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		InformationTitleDTO dto = moreFuncs.get(position - 1);
		if (selectedFuncs != null && !selectedFuncs.containsKey(dto.getId())) {
			selectedFuncs.put(dto.getId(), dto);
		} else {
			selectedFuncs.remove(dto.getId());
		}
		funcAdapter.notifyDataSetChanged();
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
			Holder h = null;
			if (convertView == null) {
				h = new Holder();
				convertView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.func_item, null);
				h.icon = (ImageView) convertView.findViewById(R.id.icon);
				h.nameText = (TextView) convertView.findViewById(R.id.nameText);
				h.checkImg = (ImageView) convertView
						.findViewById(R.id.checkImg);
				convertView.setTag(h);
			} else {
				h = (Holder) convertView.getTag();
			}
			InformationTitleDTO dto = moreFuncs.get(position);
			if (dto.getImg() != null
					&& !StringUtils.isBlank(dto.getImg().getMedium())) {
				ImageLoader.getInstance().displayImage(
						dto.getImg().getMedium(), h.icon, options);
			} else {
				h.icon.setImageResource(R.drawable.more_active);
			}
			h.nameText.setText(dto.getTitle());
			if (selectedFuncs.get(dto.getId()) != null) {
				h.checkImg.setImageResource(R.drawable.btnyes_37x37);
			} else {
				h.checkImg.setImageResource(R.drawable.btnempty_37x37);
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
	public void onRequestSuccess(String reqId, Object result) {
		if (reqId.equals(kREQ_ID_findAllInformationTitleList)) {
			if (result != null) {
				Page<InformationTitleDTO> page = (Page<InformationTitleDTO>) result;
				moreFuncs = page.getElements();
				if (moreFuncs != null)
					funcAdapter.notifyDataSetChanged();
			}
			emptyTipView.isEmpty(!(moreFuncs != null && moreFuncs.size() > 0));
		}

		if (reqId.equals(kREQ_ID_addInformationTitle)) {
			if (result != null) {
				int success = (Integer) result;
				if (success == 0) {
					Intent data = new Intent();
					data.putExtra(kDATA_FUNCS, selectedFuncs);
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
		emptyTipView.showException((ZcdhException) error, this);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		loadData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

	}

}
