package com.zcdh.mobile.app.activities.ent;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zcdh.comm.entity.Page;
import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobEnterpriseService;
import com.zcdh.mobile.api.model.EntProductDTO;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.DataLoadInterface;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.app.views.ProductPreviewItem;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import java.util.List;

/**
 * 企业主页产品
 * 
 * @author yangjiannan
 * 
 */
@EFragment(R.layout.main_ent_products)
public class MainEntProductFragment extends Fragment implements
		RequestListener, OnRefreshListener2<ListView>, OnItemClickListener,
		DataLoadInterface {

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
		entId = getArguments().getLong(Constants.kENT_ID);
		jobEnterpriseService = RemoteServiceManager
				.getRemoteService(IRpcJobEnterpriseService.class);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		loadData();

	}

	@AfterViews
	void bindView() {
		emptyView = new EmptyTipView(getActivity());
		productsListView.setEmptyView(emptyView);
		productsListView.setOnItemClickListener(this);
		productsListView.setOnRefreshListener(this);
		productsListView.getRefreshableView().setDivider(null);
		productsListView.getRefreshableView().setDividerHeight(5);

		productsAdapter = new ProductsAdapter();
		productsListView.setAdapter(productsAdapter);

	}

	@Background
	public void loadData() {
		jobEnterpriseService
				.findEntProductDTO(entId, currentPage, pageSize)
				.identify(
						kREQ_ID_findEntProductDTO = RequestChannel
								.getChannelUniqueID(),
						this);

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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// ViewHolder h = null;
			ProductPreviewItem previewItem = null;
			if (convertView == null) {
				previewItem = new ProductPreviewItem(getActivity());

			} else {
				// h = (ViewHolder) convertView.getTag();
				previewItem = (ProductPreviewItem) convertView;
			}
			previewItem.initView(getItem(position));
			return previewItem;
		}

		class ViewHolder {
			TextView productNameTxt;
			TextView dateText;
			ImageView productImg;
			TextView productDescTxt;
		}
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

			emptyView.isEmpty(!(productList.size() > 0));
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
		emptyView.showException((ZcdhException) error, this);
	}

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
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.no_more_data), Toast.LENGTH_SHORT)
						.show();
				return;
			}
		}
		loadData();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		MainEntProductDetailActivity_.intent(getActivity())
				.productUrl(productList.get(position - 1).getUrl()).start();
	}

}
