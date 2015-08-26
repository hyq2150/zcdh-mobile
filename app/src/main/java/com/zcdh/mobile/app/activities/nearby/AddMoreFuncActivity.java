package com.zcdh.mobile.app.activities.nearby;

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
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zcdh.comm.entity.Page;
import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcNearByService;
import com.zcdh.mobile.api.model.MoreToolsDTO;
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
public class AddMoreFuncActivity extends BaseActivity implements
		RequestListener, OnItemClickListener, DataLoadInterface {

	public final static int kREQUEST_ADD_FUNCS = 2021;
	public final static String kDATA_ADD_FUNCS = "kDATA_ADD_FUNCS";

	String kREQ_ID_findAllTools;
	String kREQ_ID_updateTools;

	IRpcNearByService nearByService;

	@ViewById(R.id.listView)
	PullToRefreshListView listView;

	FuncAdapter funcAdapter;

	List<MoreToolsDTO> moreFuncs = new ArrayList<MoreToolsDTO>();

	@Extra
	HashMap<Long, MoreToolsDTO> selectedFuncs = new HashMap<Long, MoreToolsDTO>();

	EmptyTipView emptyView;

	@AfterViews
	void bindViews() {
		SystemServicesUtils.setActionBarCustomTitle(this,
				getSupportActionBar(), "更多");
		funcAdapter = new FuncAdapter();

		emptyView = new EmptyTipView(this);

		listView.setAdapter(funcAdapter);
		listView.setOnItemClickListener(this);
		listView.setEmptyView(emptyView);
		nearByService = RemoteServiceManager
				.getRemoteService(IRpcNearByService.class);
		loadData();
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			save();
		}
		return false;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			save();
			return true;
		}
		return false;
	}

	/**
	 * 加载所有
	 */
	@Background
	public void loadData() {
		nearByService.findAllTools(1, 10000).identify(
				kREQ_ID_findAllTools = RequestChannel.getChannelUniqueID(),
				this);
	}

	/**
	 * 保存
	 */
	@Background
	void save() {
		// if(selectedFuncs!=null && selectedFuncs.size()>0){
		// }
		List<String> ids = new ArrayList<String>();

		for (Long id : selectedFuncs.keySet()) {
			ids.add(id + "");
		}
		nearByService
				.updateTools(ids, getUserId())
				.identify(
						kREQ_ID_updateTools = RequestChannel
								.getChannelUniqueID(),
						this);
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
			MoreToolsDTO dto = moreFuncs.get(position);
			if (dto.getIosImg() != null
					&& !StringUtils.isBlank(dto.getIosImg().getMedium())) {
				ImageLoader.getInstance().displayImage(
						dto.getIosImg().getMedium(), h.icon, options);
			} else {
				h.icon.setImageResource(R.drawable.more_active);
			}
			h.nameText.setText(dto.getToolName());
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
		if (reqId.equals(kREQ_ID_findAllTools)) {
			if (result != null) {
				Page<MoreToolsDTO> page = (Page<MoreToolsDTO>) result;
				moreFuncs = page.getElements();
				if (moreFuncs != null)
					funcAdapter.notifyDataSetChanged();
			} else {
				/*
				 * for (int i = 0; i < 5; i++) { MoreToolsDTO dto = new
				 * MoreToolsDTO(); dto.setId((long) i);
				 * dto.setToolName("test test"); moreFuncs.add(dto); }
				 */
				funcAdapter.notifyDataSetChanged();
			}

			emptyView.isEmpty(!(funcAdapter.getCount() > 0));
		}

		if (reqId.equals(kREQ_ID_updateTools)) {
			if (result != null) {
				int success = (Integer) result;
				if (success == 0) {
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

	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		emptyView.showException((ZcdhException) error, this);
		finish();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		MoreToolsDTO dto = moreFuncs.get(position - 1);
		if (selectedFuncs != null && !selectedFuncs.containsKey(dto.getId())) {
			selectedFuncs.put(dto.getId(), dto);
		} else {
			selectedFuncs.remove(dto.getId());
		}
		// Toast.makeText(getApplicationContext(), selectedFuncs.size() + "",
		// Toast.LENGTH_SHORT).show();
		funcAdapter.notifyDataSetChanged();
	}

}
