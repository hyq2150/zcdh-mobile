/**
 * 
 * @author jeason, 2014-6-5 上午11:25:24
 */
package com.zcdh.mobile.app.activities.detail;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zcdh.comm.entity.Page;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobEnterpriseService;
import com.zcdh.mobile.api.model.EntProductDTO;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.activities.ent.MainEntProductDetailActivity_;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.app.views.ProductPreviewItem;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.SystemServicesUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
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
 * @author jeason, 2014-6-5 上午11:25:24
 * @deprecated
 * 原更多产品页 已弃用 改为跳转到公司详情页面的产品选项卡
 */
@EActivity(R.layout.main_ent_products)
public class MoreProductsActivity extends BaseActivity implements RequestListener,OnRefreshListener2<ListView>, OnItemClickListener {

	String kREQ_ID_findEntProductDTO;

	@ViewById(R.id.productsListView)
	PullToRefreshListView productsListView;

	ProductsAdapter productsAdapter;
	
	IRpcJobEnterpriseService jobEnterpriseService;

	List<EntProductDTO> productList = new ArrayList<EntProductDTO>();

	long entId;

	private Integer currentPage = 1;

	private Integer pageSize = 20;

	EmptyTipView emptyView;
	Page<EntProductDTO> page;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		SystemServicesUtils.displayCustomedTitle(this, getSupportActionBar(), "公司产品");

		entId = getIntent().getLongExtra(Constants.kENT_ID,0l);
		jobEnterpriseService = RemoteServiceManager.getRemoteService(IRpcJobEnterpriseService.class);
		super.onCreate(savedInstanceState);
	}

	@AfterViews
	void bindView() {
		emptyView = new EmptyTipView(this);
		productsListView.setEmptyView(emptyView);
		productsListView.setOnItemClickListener(this);
		productsListView.setOnRefreshListener(this);
		productsListView.setMode(Mode.PULL_FROM_END);
		productsAdapter = new ProductsAdapter();
		productsListView.setMode(Mode.BOTH);
		productsListView.setAdapter(productsAdapter);
		productsListView.setOnRefreshListener(this);
		loadData();
	}


	@Background
	void loadData() {

		jobEnterpriseService.findEntProductDTO(entId, currentPage, pageSize)
				.identify(kREQ_ID_findEntProductDTO=RequestChannel.getChannelUniqueID(), this);
		
	}


	/**
	 * 
	 * @author yangjiannan
	 * 
	 */
	class ProductsAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return productList.size();
		}

		public EntProductDTO getItem(int position) {
			return productList.get(position);

		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ProductPreviewItem product_view = null;
			// ViewHolder h = null;
			// if (convertView == null) {
			// convertView =
			// LayoutInflater.from(getActivity()).inflate(R.layout.product_item,
			// null);
			// h = new ViewHolder();
			// h.productNameTxt = (TextView)
			// convertView.findViewById(R.id.productNameTxt);
			// h.dateText = (TextView) convertView.findViewById(R.id.dateTxt);
			// h.productImg = (ImageView)
			// convertView.findViewById(R.id.productImg);
			// h.productDescTxt = (TextView)
			// convertView.findViewById(R.id.productDescTxt);
			// convertView.setTag(h);
			// } else {
			// h = (ViewHolder) convertView.getTag();
			// }

			if (convertView == null) {
				product_view = new ProductPreviewItem(MoreProductsActivity.this);
			} else {
				if (convertView instanceof ProductPreviewItem) {
					product_view = (ProductPreviewItem) convertView;
				}
			}
			product_view.initView(getItem(position));
			return product_view;
		}

		// class ViewHolder {
		// TextView productNameTxt;
		// TextView dateText;
		// ImageView productImg;
		// TextView productDescTxt;
		// }
	}

	@Override
	public void onRequestStart(String reqId) {

	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if (reqId.equals(kREQ_ID_findEntProductDTO)) {
			if (result != null) {
				page = (Page<EntProductDTO>) result;

				if (page.getCurrentPage() == 1) {
					productList.clear();
					productsAdapter.notifyDataSetChanged();
				}
				if (page.getElements() != null) {
					productList.addAll(page.getElements());

					productsAdapter.notifyDataSetChanged();
				}
			}
		}

	}

	@Override
	public void onRequestFinished(String reqId) {
		onComplete();
	}

	@UiThread
	void onComplete() {
		productsListView.onRefreshComplete();
	}

	@Override
	public void onRequestError(String reqID, Exception error) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2
	 * #onPullDownToRefresh
	 * (com.handmark.pulltorefresh.library.PullToRefreshBase)
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		getLatest();

	}

	/**
	 * 
	 * @author jeason, 2014-6-6 下午4:47:07
	 */
	private void getLatest() {
		currentPage = 0;
		loadData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2
	 * #onPullUpToRefresh(com.handmark.pulltorefresh.library.PullToRefreshBase)
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		getMore();
	}

	/**
	 * 
	 * @author jeason, 2014-6-6 下午4:47:05
	 */
	private void getMore() {
		if (page == null) {
			currentPage = 1;
		} else {
			if (page.hasNextPage()) {
				currentPage = page.getNextPage();
			} else {
				onComplete();
				Toast.makeText(this, getResources().getString(R.string.no_more_data), Toast.LENGTH_SHORT).show();
				return;
			}
		}
		loadData();
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		MainEntProductDetailActivity_.intent(this)
		.productUrl(productList.get(position-1).getUrl()).start();
	}
	
}
