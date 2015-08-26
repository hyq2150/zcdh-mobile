package com.zcdh.mobile.app.activities.job_fair;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobFairService;
import com.zcdh.mobile.api.model.EntIndustryTypeDTO;
import com.zcdh.mobile.api.model.EntPostTypeDTO;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.biz.entities.ZcdhCategoryPost;
import com.zcdh.mobile.biz.entities.ZcdhIndustry;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.DbUtil;

/**
 * 下拉分类
 * 
 * @author yangjiannan
 *
 *         2015-02-09
 */
public class CategoryDropDownFilter extends LinearLayout implements
		OnItemClickListener, RequestListener {

	private String kREQ_ID_findEntPostType;
	private String kREQ_ID_findEntIndustry;

	/**
	 * 显示所有职位类别
	 */
	private StickyListHeadersListView categoryListView;
	private CateogryStickAdapter categoryAdapter;
	private onFilterListener filterListener;
	/**
	 * 0： 职位类别 1: 行业类别
	 */
	private int type;
	/**
	 * 职位类别分类
	 */
	private List<EntPostTypeDTO> categorys = new ArrayList<EntPostTypeDTO>();
	/**
	 * 职位类别
	 */
	private List<EntPostTypeDTO> categorysPost = new ArrayList<EntPostTypeDTO>();

	/**
	 * 行业类别分类
	 */
	private List<EntIndustryTypeDTO> industriesParent = new ArrayList<EntIndustryTypeDTO>();

	/**
	 * 行业
	 */
	private List<EntIndustryTypeDTO> industries = new ArrayList<EntIndustryTypeDTO>();

	private String[] sections;
	private int[] sectionIndices;
	private IRpcJobFairService fairService;
	private FinalDb finalDb;
	private EmptyTipView emptyView;

	public CategoryDropDownFilter(Context context, AttributeSet attrs) {
		super(context, attrs);
		finalDb = DbUtil.create(context);
		fairService = RemoteServiceManager
				.getRemoteService(IRpcJobFairService.class);
		inflate(getContext(), R.layout.post_category_filter_layout, this);
		bindViews();
	}

	public CategoryDropDownFilter(Context context) {
		this(context, null);
	}

	public void setOnFilterListener(onFilterListener filterListener) {
		this.filterListener = filterListener;
	}

	/*
	 * 绑定视图
	 */
	public void bindViews() {
		emptyView = (EmptyTipView) findViewById(R.id.emptyView);
		emptyView.startLoadingAnim();

		categoryListView = (StickyListHeadersListView) findViewById(R.id.categoryListView);
		categoryListView.setOnItemClickListener(this);
		categoryAdapter = new CateogryStickAdapter();
		categoryListView.setAdapter(categoryAdapter);
	}

	/**
	 * 
	 */
	public void loadData(final long bannerId, final int type) {
		new Thread() {
			public void run() {
				if (categoryAdapter.getCount() > 0)
					return;
				if (type == 0) { // 职位类别
					fairService
							.findEntPostType(bannerId)
							.identify(
									kREQ_ID_findEntPostType = RequestChannel
											.getChannelUniqueID(),
									CategoryDropDownFilter.this);
				}
				if (type == 1) { // 专业类别
					fairService
							.findEntIndustryType(bannerId)
							.identify(
									kREQ_ID_findEntIndustry = RequestChannel
											.getChannelUniqueID(),
									CategoryDropDownFilter.this);
				}
			}
		}.start();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		if (this.filterListener != null) {
			String code = null;
			String name = null;
			if (type == 0 && categorysPost.get(position) != null) {
				code = categorysPost.get(position).getPostTypeCode();
				name = categorysPost.get(position).getPostTypeName();
			}
			if (type == 1 && industries.get(position) != null) {
				code = industries.get(position).getEntIndustryCode();
				name = industries.get(position).getEntIndustryName();
			}
			filterListener.onFilter(code, name);
		}
	}

	@Override
	public void onRequestStart(String reqId) {

	}

	@SuppressWarnings("unchecked")
	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if (reqId.equals(kREQ_ID_findEntPostType)) {
			if (result != null) {
				categorys = (List<EntPostTypeDTO>) result;

				int size = categorys.size();

				sections = new String[size + 1];
				sectionIndices = new int[size + 1];

				sections[0] = "0";
				sectionIndices[0] = 0;

				categorysPost.add(null);// 表示全部
				for (int i = 0; i < size; i++) {
					EntPostTypeDTO postTypeDTO = categorys.get(i);
					categorysPost.addAll(postTypeDTO.getSubPostTypes());
					sections[i + 1] = postTypeDTO.getPostTypeCode();
					sectionIndices[i + 1] = i + 1;
				}

				categoryAdapter.notificationDataChanged(0);
			}
		}

		if (reqId.equals(kREQ_ID_findEntIndustry)) {
			if (result != null) {
				industriesParent = (List<EntIndustryTypeDTO>) result;

				int size = industriesParent.size();

				sections = new String[size + 1];
				sectionIndices = new int[size + 1];

				sections[0] = "0";
				sectionIndices[0] = 0;

				industries.add(null);
				for (int i = 0; i < size; i++) {
					EntIndustryTypeDTO industryTypeDTO = industriesParent
							.get(i);
					industries.addAll(industryTypeDTO.getSubEntIndustries());

					sections[i + 1] = industryTypeDTO.getEntIndustryCode();
					sectionIndices[i + 1] = i + 1;
				}
				categoryAdapter.notificationDataChanged(1);
			}
		}
	}

	@Override
	public void onRequestFinished(String reqId) {
		emptyView.isEmpty(categoryAdapter.getCount() == 0);
		categoryListView.setVisibility(View.VISIBLE);
	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		// TODO Auto-generated method stub

	}

	class CateogryStickAdapter extends BaseAdapter implements
			StickyListHeadersAdapter, SectionIndexer {

		@Override
		public int getCount() {
			if (type == 0) {
				return categorysPost.size();
			}
			if (type == 1) {
				return industries.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(getContext()).inflate(
						R.layout.simple_listview_item_industry, parent, false);
				holder.text = (TextView) convertView
						.findViewById(R.id.itemNameText);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (type == 0) {

				EntPostTypeDTO post = categorysPost.get(position);
				if (post == null) {
					holder.text.setText("全部");
				} else {
					holder.text.setText(post.getPostTypeName());
				}
			}
			if (type == 1) {
				EntIndustryTypeDTO industryTypeDTO = industries.get(position);
				if (industryTypeDTO == null) {
					holder.text.setText("全部");
				} else {
					holder.text.setText(industryTypeDTO.getEntIndustryName());
				}
			}
			return convertView;
		}

		@Override
		public Object[] getSections() {
			return sections;
		}

		@Override
		public int getPositionForSection(int section) {
			if (section >= sectionIndices.length) {
				section = sectionIndices.length - 1;
			} else if (section < 0) {
				section = 0;
			}
			return sectionIndices[section];
		}

		@Override
		public int getSectionForPosition(int position) {
			for (int i = 0; i < sectionIndices.length; i++) {
				if (position < sectionIndices[i]) {
					return i - 1;
				}
			}
			return sectionIndices.length - 1;
		}

		@Override
		public View getHeaderView(int position, View convertView,
				ViewGroup parent) {
			HeaderViewHolder holder;

			if (convertView == null) {
				holder = new HeaderViewHolder();
				convertView = LayoutInflater.from(getContext()).inflate(
						R.layout.section_header, parent, false);
				holder.text = (TextView) convertView
						.findViewById(R.id.headText);
				convertView.setTag(holder);
			} else {
				holder = (HeaderViewHolder) convertView.getTag();
			}

			if (type == 0) {
				if (categorysPost.get(position) == null) {
					holder.text.setText("全部");
					return convertView;
				}
				String parent_code = categorysPost.get(position)
						.getParentCode();
				Log.i("parent_code", parent_code);
				List<ZcdhCategoryPost> parents = (List<ZcdhCategoryPost>) finalDb
						.findAllByWhere(ZcdhCategoryPost.class,
								String.format("code='%s'", parent_code));
				if (parents != null && parents.size() > 0) {
					holder.text.setText(parents.get(0).getName());
				}
			}
			if (type == 1) {
				if (industries.get(position) == null) {
					holder.text.setText("全部");
					return convertView;
				}
				String ind_parent_code = industries.get(position)
						.getParentCode();
				List<ZcdhIndustry> indus_parents = finalDb.findAllByWhere(
						ZcdhIndustry.class,
						String.format("code='%s'", ind_parent_code));
				if (indus_parents != null && indus_parents.size() > 0) {
					holder.text.setText(indus_parents.get(0).getName());
				}
			}
			return convertView;

		}

		@Override
		public long getHeaderId(int position) {
			if (type == 0 && categorysPost.get(position) != null) {
				String parent_code = categorysPost.get(position)
						.getParentCode();
				return Long.valueOf(parent_code.replace(".", ""));
			}
			if (type == 1 && industries.get(position) != null) {
				String parent_code = industries.get(position).getParentCode();
				return Long.valueOf(parent_code.replace(".", ""));
			}

			return 0;
		}

		class HeaderViewHolder {
			TextView text;
		}

		class ViewHolder {
			TextView text;
		}

		private void notificationDataChanged(int type) {
			CategoryDropDownFilter.this.type = type;
			notifyDataSetChanged();
		}
	}

	public interface onFilterListener {
		public void onFilter(String code, String name);
	}
}
