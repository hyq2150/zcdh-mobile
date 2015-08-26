/**
 * 
 * @author jeason, 2014-5-20 上午9:46:36
 */
package com.zcdh.mobile.app.activities.personal;

import com.markmao.pulltorefresh.widget.XListView;
import com.markmao.pulltorefresh.widget.XListView.IXListViewListener;
import com.zcdh.comm.entity.Page;
import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobPostService;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.JobEntPostDTO;
import com.zcdh.mobile.api.model.JobUserFavoritesDTO;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.DataLoadInterface;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.activities.detail.DetailsFrameActivity_;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.app.views.TagsContainer;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.DateUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jeason, 2014-5-20 上午9:46:36
 * 职位收藏列表
 */
@EActivity(R.layout.listview)
public class FavoritePostActivity extends BaseActivity implements RequestListener, OnItemClickListener, DataLoadInterface, IXListViewListener {

	@ViewById(R.id.listview)
	XListView listview;

	private PostsAdapter adapter;

	private IRpcJobUservice userService;

	private IRpcJobPostService jobService;

	private Page<JobUserFavoritesDTO> postsListDto;
	
	@ViewById(R.id.emptyView)
	EmptyTipView emptyTipView;

	private int currentPage = 1;

	private final int pageSize = 10;

	private String kREQ_ID_FINDJOBUSERFAVORITESBYUSERID;

	private String kREQ_ID_UPDATEFAVORITE;

	private LayoutInflater inflater;

	private boolean delete_mode = false;
	private boolean is_edited;
	
	/**
	 *  取消收藏的职位id
	 */
	private List<Long> cancelFavoritePostIds = new ArrayList<Long>();

	private boolean hasNextPage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SystemServicesUtils.displayCustomedTitle(this, getSupportActionBar(), R.string.favorite_posts);
		userService = RemoteServiceManager.getRemoteService(IRpcJobUservice.class);
		jobService = RemoteServiceManager.getRemoteService(IRpcJobPostService.class);
		adapter = new PostsAdapter();
		adapter.setShowImg(true);
		inflater = LayoutInflater.from(this);
	}

	@OnActivityResult(Constants.kREQUEST_FAVORITE)
	void onResultActivity(int result, Intent data){
		if(result==RESULT_OK){
			long cancelFavoritePostId = data.getExtras().getLong(Constants.kPOST_ID);
			cancelFavoritePostIds.add(cancelFavoritePostId);
			for (JobUserFavoritesDTO dto : postsListDto.getElements()) {
				if(dto.getPostId()==cancelFavoritePostId){
					
					List<JobUserFavoritesDTO> favorites = new ArrayList<JobUserFavoritesDTO>();
					favorites.addAll(postsListDto.getElements());
					favorites.remove(dto);
					adapter.updateItems(favorites);
					adapter.notifyDataSetChanged();
					postsListDto.setElements(favorites);
					break;
				}
				
			}
		}
	}

	
	@AfterViews
	void bindViews() {
		

		listview.setEmptyView(emptyTipView);
		listview.setAdapter(adapter);
		
		listview.setPullRefreshEnable(false);
		listview.setPullLoadEnable(true);
		listview.setAutoLoadEnable(false);
		listview.setXListViewListener(this);
		listview.setOnItemClickListener(this);
		listview.setDivider(null);
		listview.setDividerHeight((int) getResources().getDimension(R.dimen.dividerHeightXX));
		
		loadData();
	}


	/**
	 * 
	 * @author jeason, 2014-5-19 下午5:06:51
	 */
	@Background
	public void loadData() {
		userService.findJobUserFavoritesByUserId(ZcdhApplication.getInstance().getZcdh_uid(), currentPage, pageSize)
		.identify(kREQ_ID_FINDJOBUSERFAVORITESBYUSERID = RequestChannel.getChannelUniqueID(), this);
		currentPage++;
	}


	@Override
	public void onRequestStart(String reqId) {

	}


	@Override
	public void onRequestSuccess(String reqId, Object result) {

		if (reqId.equals(kREQ_ID_FINDJOBUSERFAVORITESBYUSERID)) {
			if (result != null) {
				postsListDto = (Page<JobUserFavoritesDTO>) result;
				
				if (currentPage == 2) {
					adapter.updateItems(postsListDto.getElements()); 
				} else {
					adapter.addItemsToBottom(postsListDto.getElements());
				}
				hasNextPage = postsListDto.hasNextPage();
				listview.setPullLoadEnable(hasNextPage);
			}
			
			emptyTipView.isEmpty(!(adapter.getItems()!=null && adapter.getItems().size()>0));

		}

		if (reqId.equals(kREQ_ID_UPDATEFAVORITE)) {
			//Toast.makeText(this, "取消成功", Toast.LENGTH_SHORT).show();
			boolean empty = !(adapter.getItems()!=null && adapter.getItems().size()>0);
			emptyTipView.isEmpty(empty);
		}
		is_edited = adapter.getCount()>0;
		supportInvalidateOptionsMenu();
		onComplete();

	}

	@Override
	public void onRequestFinished(String reqId) {
		onComplete();
	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		onComplete();
		emptyTipView.showException((ZcdhException)error, this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		JobUserFavoritesDTO dto = ((JobUserFavoritesDTO) adapter.getItem(position - 1));
		List<JobEntPostDTO> temps = new ArrayList<JobEntPostDTO>();
		for (JobUserFavoritesDTO jobEntPostDTO : adapter.getItems()) {
			JobEntPostDTO post = new JobEntPostDTO();
			post.setPostId(jobEntPostDTO.getPostId());
			post.setPostAliases(jobEntPostDTO.getPostName());
			post.setEntId(jobEntPostDTO.getEndId());
			temps.add(post);
		}
		DetailsFrameActivity_.intent(this).postId(dto.getPostId())
		.switchable(true)
		//.posts(temps)
		.entId(dto.getEndId())
		.currentIndex(position-1)
		.startForResult(Constants.kREQUEST_FAVORITE);
		
		

	}

	public class PostsAdapter extends BaseAdapter {

		private boolean showImg = false;
		private List<JobUserFavoritesDTO> mPosts = new ArrayList<JobUserFavoritesDTO>();

		public void updateItems(List<JobUserFavoritesDTO> list) {
			mPosts = list;
			notifyDataSetChanged();
		}

		public List<JobUserFavoritesDTO> getItems() {
			return mPosts;
		}

		public void removeItem(int position) {
			getItems().remove(position);
			notifyDataSetChanged();
		}

		public void clearItems() {
			mPosts.clear();
			notifyDataSetChanged();
		}

		public void addItemsToTop(List<JobUserFavoritesDTO> posts) {
			mPosts.addAll(0, posts);
			notifyDataSetChanged();
		}

		public void addItemsToBottom(List<JobUserFavoritesDTO> list) {
			mPosts.addAll(list);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mPosts.size();
		}

		@Override
		public JobUserFavoritesDTO getItem(int position) {
			return mPosts.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.rect_post_item, null);
				holder = new ViewHolder();
				holder.content = (TextView) convertView.findViewById(R.id.content);
				holder.distance = (TextView) convertView.findViewById(R.id.distance);
				holder.ll_tags_container = (TagsContainer) convertView.findViewById(R.id.ll_tags);
				holder.location_and_requirement = (TextView) convertView.findViewById(R.id.location_and_education);
				holder.publish_time = (TextView) convertView.findViewById(R.id.publish_time);
				holder.salary = (TextView) convertView.findViewById(R.id.salary);
				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.deleteBtn = (ImageView) convertView.findViewById(R.id.btn_delete);
				holder.accessoryImg = (ImageView) convertView.findViewById(R.id.accessoryImg);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			JobUserFavoritesDTO post = mPosts.get(position);
			holder.location_and_requirement.setText(post.getAreaName() + "/" + post.getDegreeName());
			holder.publish_time.setText(DateUtils.getDateByFormatYMD(post.getFavorityDate()));// .getDateByFormatNUM(post.getPublishDate()));
			holder.salary.setText(post.getSalary());
			holder.title.setText(post.getPostName());
			holder.content.setText(mPosts.get(position).getEntName());
			if (showImg) {
				holder.accessoryImg.setVisibility(View.VISIBLE);
			} else {
				holder.accessoryImg.setVisibility(View.GONE);
			}
			double distance = 0d;
			if (distance == 0) { // 如果为0 不显示
				holder.distance.setVisibility(View.GONE);
			} else if (distance < 1000) { // 如果小于1千米，以米为单位
				holder.distance.setText(String.valueOf(distance) + "米");
			} else if (distance > 1000) {
				holder.distance.setText(String.valueOf(distance / 1000) + "米");
			}

			holder.ll_tags_container.setVisibility(View.GONE);
			if (delete_mode) {
				holder.accessoryImg.setVisibility(View.GONE);
				holder.deleteBtn.setVisibility(View.VISIBLE);
				holder.deleteBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						removeFavorite(getItem(position));
						removeItem(position);
					}

				});
			} else {
				holder.accessoryImg.setVisibility(View.VISIBLE);
				holder.deleteBtn.setVisibility(View.GONE);
			}

			return convertView;
		}

		public boolean isShowImg() {
			return showImg;
		}

		public void setShowImg(boolean showImg) {
			this.showImg = showImg;
			notifyDataSetChanged();
		}
	}

	public class ViewHolder {
		TextView title;
		TextView publish_time;
		TextView content;
		TextView salary;
		TextView location_and_requirement;
		TextView distance;
		TagsContainer ll_tags_container;
		ImageView deleteBtn;
		ImageView accessoryImg;
	}

	@OptionsItem(android.R.id.home)
	void onBack() {
		finish();
	}

	@Background
	public void removeFavorite(JobUserFavoritesDTO item) {
		jobService.updateFavorite(getUserId(), item.getPostId()).identify(kREQ_ID_UPDATEFAVORITE = RequestChannel.getChannelUniqueID(), this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.clear();
		if(is_edited){
			getMenuInflater().inflate(R.menu.action_edit, menu);
			MenuItem menu_item = menu.findItem(R.id.action_edit);
			if (delete_mode) {
				menu_item.setTitle(R.string.done);
			}
		}
		return true;
	}

	@OptionsItem(R.id.action_edit)
	void setDeleteMode() {
		delete_mode = !delete_mode;
		supportInvalidateOptionsMenu();
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadMore() {
		if (hasNextPage) {
			loadData();
		} else {
			onComplete();
		}		
	}

	@UiThread
	void onComplete() {
		listview.stopLoadMore();
	}
}
