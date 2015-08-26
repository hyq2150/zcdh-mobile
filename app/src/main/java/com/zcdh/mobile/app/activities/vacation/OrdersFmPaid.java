/**
 * 
 * @author jeason, 2014-4-23 下午5:40:49
 */
package com.zcdh.mobile.app.activities.vacation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;
import com.zcdh.comm.entity.Page;
import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcHolidayService;
import com.zcdh.mobile.api.model.MyOrderDTO;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.DataLoadInterface;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.activities.base.BaseFragment;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;

/**
 * @author jeason, 2014-4-23 下午5:40:49
 * 已支付Fragment
 */
@EFragment(R.layout.order_list)
public class OrdersFmPaid extends BaseFragment implements OnRefreshListener2<ListView>, RequestListener, DataLoadInterface {
	public static final int TYPE_PENDING = 1;
	public static final int TYPE_PAID = 0;
	public static final int REQUEST_CODE_ON_CACEL_ORDER = 0x01;

	private static final String TAG = OrdersFmPaid.class.getSimpleName();
	// private View rootView;

	@ViewById(R.id.listview)
	PullToRefreshListView listview;

	private int type;

	private List<MyOrderDTO> orders = new ArrayList<MyOrderDTO>();

	private OrdersAdapter adapter;

	private IRpcHolidayService service;

	private Page<MyOrderDTO> page_order;

	private final int page_size = 10;
	private EmptyTipView empty_view;

	public String kREQ_ID_findMyOrderDTOByPaid;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(!getArguments().isEmpty()){
			type = getArguments().getInt("type");
		}
		service = RemoteServiceManager.getRemoteService(IRpcHolidayService.class);

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
	void afterviews() {
		adapter = new OrdersAdapter();
		empty_view = new EmptyTipView(getActivity());
		listview.setEmptyView(empty_view);
		listview.setAdapter(adapter);
		listview.setMode(Mode.BOTH);
		listview.setOnRefreshListener(this);
		// listview.setRefreshing();
		listview.getRefreshableView().setDivider(getResources().getDrawable(R.drawable.divider_horizontal_timeline));
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				ActivityDispatcher.toOrderInfo(getActivity(), adapter.getItem(arg2 - 1), adapter.getItem(arg2 - 1).getOrderNum());
			}
		});
		loadData();
	}

	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState) {
	// if (rootView == null) {
	// rootView = inflater.inflate(R.layout.order_list, null);
	// }
	// ViewGroup parent = (ViewGroup) rootView.getParent();
	// if (parent != null) {
	// parent.removeView(rootView);
	// }
	//
	//
	// listview = (PullToRefreshListView) rootView.findViewById(R.id.listview);
	// listview.setEmptyView(empty_view);
	// listview.setAdapter(adapter);
	// listview.setMode(Mode.BOTH);
	// listview.setOnRefreshListener(this);
	// // listview.setRefreshing();
	// listview.getRefreshableView().setDivider(getResources().getDrawable(R.drawable.divider_horizontal_timeline));
	// listview.setOnItemClickListener(new OnItemClickListener() {
	//
	// @Override
	// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long
	// arg3) {
	// ActivityDispatcher.toOrderInfo(getActivity(), adapter.getItem(arg2 - 1),
	// adapter.getItem(arg2 - 1).getOrderNum());
	// }
	// });
	// return rootView;
	// }

	public class OrdersAdapter extends BaseAdapter {
		// int temp = 3;

		@Override
		public int getCount() {
			// return temp;
			return orders.size();
		}

		public void populate() {
			// temp = temp + 3;
			notifyDataSetChanged();
		}

		@Override
		public MyOrderDTO getItem(int position) {
			return orders.get(position);
		}

		public void clearItems() {
			orders.clear();
			notifyDataSetChanged();

		}

		public void updateItems(List<MyOrderDTO> list) {
			orders.clear();
			orders.addAll(list);
			notifyDataSetChanged();
		}

		public void removeItemByOrderId(String order_id) {
			for (MyOrderDTO order : orders) {
				if (order.getOrderNum().equals(order_id)) {
					orders.remove(order);
					notifyDataSetChanged();
					break;
				}
			}
		}

		public void addItems(List<MyOrderDTO> list) {
			orders.addAll(list);
			notifyDataSetChanged();
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				// inflate a layout
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.order_item, null);
				holder = new ViewHolder();
				// set holder's reference to views in converview
				holder.handle_order = (TextView) convertView.findViewById(R.id.handle_order);

				holder.order_no = (TextView) convertView.findViewById(R.id.order_no);

				holder.order_note = (TextView) convertView.findViewById(R.id.order_note);

				holder.order_status = (TextView) convertView.findViewById(R.id.order_status);

				holder.order_time = (TextView) convertView.findViewById(R.id.order_time);

				holder.order_title = (TextView) convertView.findViewById(R.id.order_title);

				holder.price = (TextView) convertView.findViewById(R.id.price);

				holder.purchase = (TextView) convertView.findViewById(R.id.purchase);

				holder.count = (TextView) convertView.findViewById(R.id.count);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			// initial views in holder
			holder.handle_order.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					trackOrder(getItem(position));
				}
			});

			holder.order_no.setText(getItem(position).getOrderNum());
			holder.order_note.setText(getItem(position).getEntName());
			if (getItem(position).getStatus() == 1) {
				holder.order_status.setText("未付款");
			} else {
				holder.order_status.setText("已付款");

			}

			String orderTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getItem(position).getOrderDate());
			holder.order_time.setText(orderTimeStr);
			holder.order_title.setText(getItem(position).getPostName());
			holder.price.setText(String.format("总价:￥%s", getItem(position).getPayMoney().toPlainString()));
			if (type == TYPE_PAID) {
				holder.purchase.setVisibility(View.GONE);
			} else {
				holder.purchase.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						purchase(getItem(position));
					}

				});
			}

			holder.count.setText("数量:" + getItem(position).getPostCount());
			return convertView;
		}

	}

	/**
	 * 跟踪订单
	 * 
	 * @param myOrderDTO
	 * @author jeason, 2014-4-28 下午3:40:08
	 */
	private void trackOrder(MyOrderDTO myOrderDTO) {
		ActivityDispatcher.trackOrder(getActivity(), myOrderDTO, myOrderDTO.getOrderNum());
	}

	/**
	 * 付款
	 * 
	 * @param item
	 * @author jeason, 2014-4-28 下午3:40:18
	 */
	private void purchase(MyOrderDTO myOrderDTO) {
		ActivityDispatcher.purchaseVacationJob(getActivity(), myOrderDTO.getOrderNum());
	}

	private class ViewHolder {
		TextView order_no;
		TextView order_time;
		TextView handle_order;
		TextView order_title;
		TextView order_note;
		TextView price;
		TextView order_status;
		TextView purchase;
		TextView count;
	}

	public static OrdersFmPaid getInstance(int type) {
		OrdersFmPaid_ fm = new OrdersFmPaid_();
		Bundle bundle = new Bundle();
		bundle.putInt("type", type);
		fm.setArguments(bundle);
		return fm;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pulltorefresh.PullToRefreshBase.OnRefreshListener2#onPullDownToRefresh
	 * (pulltorefresh.PullToRefreshBase)
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		loadData();
	}

	/**
	 * 
	 * @author jeason, 2014-4-25 上午10:13:43
	 */
	public void loadData() {
		new AsyncTask<Void, Void, Page<MyOrderDTO>>() {

			@Override
			protected Page<MyOrderDTO> doInBackground(Void... params) {
				try {
					// service.findMyOrderDTO(ZcdhApplication.getInstance().getZcdh_uid(),
					// type, 1, page_size);
					service.findMyOrderDTOByPaid(ZcdhApplication.getInstance().getZcdh_uid(), 1, page_size).identify(kREQ_ID_findMyOrderDTOByPaid = RequestChannel.getChannelUniqueID(), OrdersFmPaid.this);
				} catch (Exception e) {

				}
				return null;
			}

			@Override
			protected void onPostExecute(Page<MyOrderDTO> result) {
				// if (result != null) {
				// adapter.updateItems(result.getElements());
				// page_order = result;
				// }
				// else {
				//
				// Toast.makeText(getActivity(),
				// R.string.no_result,Toast.LENGTH_SHORT).show();
				// }
				listview.onRefreshComplete();
				super.onPostExecute(result);
			}

		}.execute();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pulltorefresh.PullToRefreshBase.OnRefreshListener2#onPullUpToRefresh(
	 * pulltorefresh.PullToRefreshBase)
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		getMoreOrders();
	}

	/**
	 * 
	 * @author jeason, 2014-4-25 上午10:13:47
	 */
	private void getMoreOrders() {
		new AsyncTask<Void, Void, Page<MyOrderDTO>>() {

			@Override
			protected Page<MyOrderDTO> doInBackground(Void... params) {
				int target_page = 1;
				if (page_order == null) {
					target_page = 1;
				} else if (page_order.hasNextPage()) {
					target_page++;
				} else {
					return null;
				}
				Page<MyOrderDTO> page = null;
				try {
					// page =
					// service.findMyOrderDTO(ZcdhApplication.getInstance().getZcdh_uid(),
					// type, target_page, page_size);
					service.findMyOrderDTOByPaid(ZcdhApplication.getInstance().getZcdh_uid(), target_page, page_size).identify(kREQ_ID_findMyOrderDTOByPaid = RequestChannel.getChannelUniqueID(), OrdersFmPaid.this);

				} catch (Exception e) {
				}
				return page;
			}

			@Override
			protected void onPostExecute(Page<MyOrderDTO> result) {
				// if (result != null) {
				// adapter.addItems(result.getElements());
				// page_order = result;
				// } else {
				// Toast.makeText(getActivity(), R.string.no_result,
				// Toast.LENGTH_SHORT).show();
				// }
				// adapter.populate();
				listview.onRefreshComplete();

				super.onPostExecute(result);
			}

		}.execute();
	}

	public void removeOrder(String order_num) {
		if (order_num != null) {
			adapter.removeItemByOrderId(order_num);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zcdh.mobile.framework.nio.RequestListener#onRequestStart(java.lang
	 * .String)
	 */
	@Override
	public void onRequestStart(String reqId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {

		if (reqId.equals(kREQ_ID_findMyOrderDTOByPaid)) {
			if (result != null) {
				page_order = (Page<MyOrderDTO>) result;
				if (page_order.getCurrentPage() == 1) {
					adapter.updateItems(page_order.getElements());
				} else {
					adapter.addItems(page_order.getElements());
				}
			} else {
				// Toast.makeText(getActivity(), R.string.no_result,
				// Toast.LENGTH_SHORT).show();
			}
			empty_view.isEmpty(!(adapter.getCount()>0));
		}
		onComplete();
	}

	void onComplete() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listview.onRefreshComplete();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zcdh.mobile.framework.nio.RequestListener#onRequestFinished(java.
	 * lang.String)
	 */
	@Override
	public void onRequestFinished(String reqId) {
		// TODO Auto-generated method stub
		onComplete();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zcdh.mobile.framework.nio.RequestListener#onRequestError(java.lang
	 * .String, java.lang.Exception)
	 */
	@Override
	public void onRequestError(String reqID, Exception error) {
		// TODO Auto-generated method stub
		onComplete();
		empty_view.showException((ZcdhException)error, this);
	}

}
