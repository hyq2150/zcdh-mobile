package com.zcdh.mobile.app.activities.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.db.sqlite.DbModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView.OnStickyHeaderChangedListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcNearByService;
import com.zcdh.mobile.api.model.HotCityDTO;
import com.zcdh.mobile.api.model.JobObjectiveAreaDTO;
import com.zcdh.mobile.biz.entities.ZcdhArea;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.events.MyEvents;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.DbUtil;
import com.zcdh.mobile.utils.NetworkUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

/**
 * 地区选择
 * 
 * @author yangjiannan
 * 
 */
@EActivity(R.layout.activity_area)
public class AreaActivity extends BaseActivity implements OnItemClickListener ,OnStickyHeaderChangedListener,
RemoveItemListner, RequestListener{
	
	private static final String TAG = AreaActivity.class.getSimpleName();

	public final static String kMSG_AREA_SELECTED = "area_selected";
	public static final String kDATA_AREA_BUNLDE = "area_bundle";
	public static final String kDATA_AREA_CODE = "area_code";
	public static final String kDATA_AREA_NAME ="area_name";
	public static final int kREQUEST_AREA = 2004;
	public static final String kDATA_CODE = "area_code";
	public static final String kDATA_NAME = "area_name";
	public static final String kEVENT_AREA_SELECTED = "kEVENT_AREA_SELECTED";
	
	private IRpcNearByService neaByService;
	
	private String kREQ_ID_findHotCity;
	
	
	@ViewById(R.id.indexGridView)
	GridView indexGridView;

	@ViewById(R.id.areaListView)
	StickyListHeadersListView areaListView;

	private IndexAdapter indexAdapter;

	private StickyListHeadersAdapter areasAdapter;

	private FinalDb finalDb;

	// 所有地区
	//List<DbModel> areas;

	// 所有地区索引
	private List<String> indexs = new ArrayList<String>();

	private String[] sections;

	// 记录字母索引需要定位的ListView 的哪一行
	private int[] sectionIndics;
	
	private HashMap<String, Integer> sectionMap = new HashMap<String, Integer>();
	
	/**
	 * 标识是否单选
	 */
	@Extra
	boolean signle = true;
	
	/**
	 * 已选意向地区
	 */
	@Extra
	HashMap<String, JobObjectiveAreaDTO> selectedAreas = new HashMap<String, JobObjectiveAreaDTO>();
	
	/**
	 * 是否显示 “全国”
	 */
	@Extra
	boolean showQuanguo;

	/**
	 * 选择显示面板
	 */
	protected MultSelectionPannel multSelectionPannel;
	

	// 当前选中的索引字母，默认是A
	private int indexSelected = 0;

	/**
	 * 热门城市
	 */
	private List<HotCityDTO> hotCities;
	/**
	 * 所有城市
	 */
	private List<ZcdhArea> areaList = new ArrayList<ZcdhArea>();

	public void onCreate(Bundle b) {
		super.onCreate(b);
		finalDb = DbUtil.create(this);
		neaByService = RemoteServiceManager.getRemoteService(IRpcNearByService.class);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(!signle && selectedAreas.size()>0){
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.action_ok, menu);
		}
	    return true;
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
		
		//多选确定
	    if(item.getItemId()==R.id.action_OK){
	    	
	    	Intent data = new Intent();
	    	data.putExtra(kDATA_AREA_BUNLDE, selectedAreas);
	    	setResult(RESULT_OK, data);
	    	MyEvents.post(kEVENT_AREA_SELECTED, data);
	    }else{
				if(selectedAreas!=null && selectedAreas.size()>0){
					Intent data = new Intent();
			    	data.putExtra(kDATA_AREA_BUNLDE, selectedAreas);
			    	setResult(RESULT_OK, data);
			    	MyEvents.post(kEVENT_AREA_SELECTED, data);
				}
	    }
	    finish();
	    return true;
	}

	@AfterViews
	void bindViews() {
		SystemServicesUtils.setActionBarCustomTitle(this,
				getSupportActionBar(), getString(R.string.chosen_area));

		if(!signle){
			multSelectionPannel =new MultSelectionPannel(this, this);
		}
		
		areaListView.setOnItemClickListener(this);
		areaListView.setOnStickyHeaderChangedListener(this);
		if(NetworkUtils.isNetworkAvailable(AreaActivity.this)){
			loadHotAreas();
			indexs.add("热");
		}else{
			loadAreas();
		}
	}
	
	/**
	 * 加载热门城市
	 */
	@Background
	void loadHotAreas(){
		neaByService.findHotCity().identify(kREQ_ID_findHotCity=RequestChannel.getChannelUniqueID(), this);
	}

	@Background
	void loadAreas() {

		// 找出所有地区的字母索引
		String indexSQL = "select distinct name_sort from zcdh_area where length(code)=7 order by name_sort";
		List<DbModel> _indexs = finalDb.findDbModelListBySQL(indexSQL);
	
		for (int i = 0; i < _indexs.size(); i++) {
			indexs.add(_indexs.get(i).getString("name_sort"));
		}
		
		//查询所有地级市
//		String areasSQL = "select code, name, name_sort from zcdh_area where length(code)=7 order by name_sort";
		//areas = finalDb.findDbModelListBySQL(areasSQL); // length(code)=7
														// 是根据地级市和直辖市 的code
														// 为7的规律
		//增加 "全国"
		if(showQuanguo){
			ZcdhArea quanGuo = new ZcdhArea();
			quanGuo.setCode("0");
			quanGuo.setName("全国");
			quanGuo.setName_sort("热门");
			areaList.add(quanGuo);
		}
		
		//加入热门城市
		if(hotCities!=null){
			for (int i = 0; i < hotCities.size(); i++) {
				HotCityDTO hc = hotCities.get(i);
				ZcdhArea _area = new ZcdhArea();
				_area.setCode(hc.getAreaCode());
				_area.setName(hc.getAreaName());
				_area.setName_sort("热门");
				areaList.add(_area);
			}
		}
		List<ZcdhArea> _areas = finalDb.findAllByWhere(ZcdhArea.class, "length(code)=7", "name_sort");
		
		
		
		areaList.addAll(_areas);
		
		// int section = 0;
		sections = new String[indexs.size()];
		sectionIndics = new int[sections.length];
		int sectionPosition = 0;
		for (int i = 0; i < areaList.size(); i++) {
			ZcdhArea area = areaList.get(i);
			String currentStr = area.getName_sort();
			String previewStr = (i - 1) >= 0 ? areaList.get(i - 1).getName_sort() : " ";
			if (currentStr != null && previewStr != null
					&& !currentStr.equals(previewStr)) {
				sections[sectionPosition] = currentStr;
				sectionIndics[sectionPosition] = i;
				sectionMap.put(currentStr, sectionPosition);
				sectionPosition++;
			}
		}
		//加载到列表显示
		showData();
	}

	@UiThread
	void showData() {
		if (indexAdapter == null) {
			indexAdapter = new IndexAdapter();
			indexGridView.setAdapter(indexAdapter);
		} else {
			indexAdapter.notifyDataSetChanged();
		}
		if (areasAdapter == null) {
			areasAdapter = new AreaBaseAdapter(this, sectionIndics, sections);
			areaListView.setAdapter(areasAdapter);
		} else {
			areasAdapter.notifyDataSetChanged();
		}
		showSelectItems();
	}
	
	/**
	 * 显示已选择的项
	 */
	void showSelectItems(){
		if(selectedAreas!=null){
			HashMap<String, String> _items = new HashMap<String, String>();
			
			for (String key : selectedAreas.keySet()) {
				_items.put(key, selectedAreas.get(key).getName());
			}
			if(_items.size()>0){
				multSelectionPannel.refresh(_items);
			}
		}
	}

	@ItemClick(R.id.indexGridView)
	void onSelectedIndex(int position) {
		indexSelected = position;
		areaListView.setSelection(sectionIndics[indexSelected]);
		showData();
	}

	

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ZcdhArea area = areaList.get(position);
		String code = area.getCode();
		String name = area.getName();
		
		if(signle){ //如果是单选， 选中一个，关闭当前Activity
			Intent data = new Intent();
			data.putExtra(kDATA_AREA_CODE, code);
			data.putExtra(kDATA_AREA_NAME, name);
			
			setResult(RESULT_OK, data);
			finish();
		}else{ // 如果是多选
			if(selectedAreas!=null && selectedAreas.containsKey(code)){
				selectedAreas.remove(code);
			}else{
				if(selectedAreas!=null && MultSelectionPannel.max>selectedAreas.size()){
					JobObjectiveAreaDTO objPost = new JobObjectiveAreaDTO();
					objPost.setCode(code);
					objPost.setName(name);
					selectedAreas.put(code, objPost);
				}else{
					Toast.makeText(this, "最多选择" + MultSelectionPannel.max + "个地区", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			showSelectItems();
			multSelectionPannel.setHiden(false);
			areasAdapter.notifyDataSetChanged();
			supportInvalidateOptionsMenu();
		}
	}
	

	@Override
	public void onRemoveItem(Object key) {
		selectedAreas.remove(key);
	}

	
	@Override
	public void onStickyHeaderChanged(StickyListHeadersListView l, View header,
			int itemPosition, long headerId) {
//		String name_sort = areas.get(itemPosition).getString("name_sort");
//		indexSelected = sectionMap.get(name_sort);
//		indexAdapter.notifyDataSetChanged();
//		Toast.makeText(this, "oK", Toast.LENGTH_SHORT).show();
	}

	/**
	 * 地区列表适配器
	 * @author yangjiannan
	 *
	 */
	class AreaBaseAdapter extends BaseAdapter implements
			StickyListHeadersAdapter, SectionIndexer {

		private int[] mSectionIndices;
		private Character[] mSectionLetters;

		public AreaBaseAdapter(Context context, int[] mSectionIndices,
				Object[] mSectionLetters) {
			this.mSectionIndices = mSectionIndices;
		}

		@Override
		public int getCount() {
			return areaList.size();
		}

		@Override
		public Object getItem(int position) {
			return areaList.get(position).getName();
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.list_item_area, parent, false);
				holder.text = (TextView) convertView
						.findViewById(R.id.itemNameText);
				holder.checkImg = (ImageView) convertView.findViewById(R.id.checkImg);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.text.setText(areaList.get(position).getName());
			// // 设置选中颜色
			String code = areaList.get(position).getCode();
			if (selectedAreas!=null && selectedAreas.containsKey(code)) {
				convertView.setBackgroundResource(R.drawable.selected_area);
				holder.checkImg.setVisibility(View.VISIBLE);
			} else {
				convertView.setBackgroundResource(R.drawable.area_selector);
				holder.checkImg.setVisibility(View.GONE);
			}

			return convertView;
		}

		@Override
		public View getHeaderView(int position, View convertView,
				ViewGroup parent) {
			HeaderViewHolder holder;

			if (convertView == null) {
				holder = new HeaderViewHolder();
				convertView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.area_section_header, parent, false);
				holder.text = (TextView) convertView
						.findViewById(R.id.headText);
				convertView.setTag(holder);
			} else {
				holder = (HeaderViewHolder) convertView.getTag();
			}

			holder.text.setText(areaList.get(position).getName_sort());

			return convertView;
		}

		/**
		 * Remember that these have to be static, postion=1 should always return
		 * the same Id that is.
		 */
		@Override
		public long getHeaderId(int position) {
			return areaList.get(position).getName_sort().charAt(0);
		}

		@Override
		public int getPositionForSection(int section) {
			if (section >= mSectionIndices.length) {
				section = mSectionIndices.length - 1;
			} else if (section < 0) {
				section = 0;
			}
			return mSectionIndices[section];
		}

		@Override
		public int getSectionForPosition(int position) {
			for (int i = 0; i < mSectionIndices.length; i++) {
				if (position < mSectionIndices[i]) {
					return i - 1;
				}
			}
			return mSectionIndices.length - 1;
		}

		@Override
		public Object[] getSections() {
			return mSectionLetters;
		}

		public void clear() {

			notifyDataSetChanged();
		}

		public void restore() {

			notifyDataSetChanged();
		}

		class HeaderViewHolder {
			TextView text;
		}

		class ViewHolder {
			TextView text;
			ImageView checkImg;
		}

	}

	
	/**
	 * 索引GridView 的适配器
	 * 
	 * @author yangjiannan
	 * 
	 */
	class IndexAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return indexs.size();
		}

		@Override
		public Object getItem(int position) {
			return indexs.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView indexItem = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.index_item, null);
			}
			indexItem = (TextView) convertView.findViewById(R.id.indexItem);
			indexItem.setText((indexs.get(position)
					.toUpperCase(Locale.US)));
			if (position == indexSelected) {
				convertView.setBackgroundResource(R.drawable.selected_index);
			} else {
				convertView.setBackgroundResource(R.drawable.index_selector);
			}
			return convertView;
		}

	}


	@Override
	public void onRequestStart(String reqId) {
		
	}
	@SuppressWarnings("unchecked")
	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if(reqId.equals(kREQ_ID_findHotCity)){
			if(result!=null){
				 hotCities = (List<HotCityDTO>) result;
			}
		}
		loadAreas();
	}
	@Override
	public void onRequestFinished(String reqId) {
		
	}
	@Override
	public void onRequestError(String reqID, Exception error) {
		
	}
}
