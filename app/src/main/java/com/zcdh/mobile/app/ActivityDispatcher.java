/**
 * 
 * @author jeason, 2014-4-7 上午10:37:30
 */
package com.zcdh.mobile.app;

import com.zcdh.mobile.api.model.EntPostByOrderDTO;
import com.zcdh.mobile.api.model.JobEntPostDTO;
import com.zcdh.mobile.api.model.MyOrderDTO;
import com.zcdh.mobile.api.model.SearchConditionDTO;
import com.zcdh.mobile.app.activities.auth.LoginActivity_;
import com.zcdh.mobile.app.activities.auth.ResetPwdByEmailActivity_;
import com.zcdh.mobile.app.activities.auth.UpdateEmailActivity_;
import com.zcdh.mobile.app.activities.base.PhotoBrowser;
import com.zcdh.mobile.app.activities.detail.CompanyCommentsActivity_;
import com.zcdh.mobile.app.activities.detail.DetailsFrameActivity_;
import com.zcdh.mobile.app.activities.detail.EntAlbumActivity_;
import com.zcdh.mobile.app.activities.detail.EntCommentActivity_;
import com.zcdh.mobile.app.activities.detail.MoreProductsActivity_;
import com.zcdh.mobile.app.activities.detail.NavigatorActivity_;
import com.zcdh.mobile.app.activities.ent.MainEntLiuyanActivity_;
import com.zcdh.mobile.app.activities.job_fair.JobFairDetailActivityNew_;
import com.zcdh.mobile.app.activities.job_fair.JobFairsActivity_;
import com.zcdh.mobile.app.activities.job_fair.ParticipantsIndustryActivity_;
import com.zcdh.mobile.app.activities.job_fair.RegistrationListActivity_;
import com.zcdh.mobile.app.activities.messages.AppliedPostStatusActivity_;
import com.zcdh.mobile.app.activities.messages.InterviewInvitationActivity_;
import com.zcdh.mobile.app.activities.personal.AddEduExpActivity_;
import com.zcdh.mobile.app.activities.personal.AddTranningExpActivity_;
import com.zcdh.mobile.app.activities.personal.BasicInfoActivity_;
import com.zcdh.mobile.app.activities.personal.EditEduExpActivity_;
import com.zcdh.mobile.app.activities.personal.EditTranningExp;
import com.zcdh.mobile.app.activities.personal.EditTranningExp_;
import com.zcdh.mobile.app.activities.personal.EducationBackgroundActivity_;
import com.zcdh.mobile.app.activities.personal.EnterpriseBlackList_;
import com.zcdh.mobile.app.activities.personal.FavoritePostActivity_;
import com.zcdh.mobile.app.activities.personal.LanguageEditActivity_;
import com.zcdh.mobile.app.activities.personal.PurposeActivity_;
import com.zcdh.mobile.app.activities.personal.ResumeActivity_;
import com.zcdh.mobile.app.activities.personal.SchoolFinderActivity_;
import com.zcdh.mobile.app.activities.personal.SelfEvaluationActivity_;
import com.zcdh.mobile.app.activities.personal.SkillEditActivity_;
import com.zcdh.mobile.app.activities.register.BindAccountActivity_;
import com.zcdh.mobile.app.activities.search.CategoryMajorActivity_;
import com.zcdh.mobile.app.activities.search.CertificateFinderActivity;
import com.zcdh.mobile.app.activities.search.CertificateFinderActivity_;
import com.zcdh.mobile.app.activities.search.IndustryActivity;
import com.zcdh.mobile.app.activities.search.IndustryActivity_;
import com.zcdh.mobile.app.activities.search.MajorsActivity;
import com.zcdh.mobile.app.activities.search.PostsActivity;
import com.zcdh.mobile.app.activities.search.PostsActivity_;
import com.zcdh.mobile.app.activities.search.SearchResultsActivity_;
import com.zcdh.mobile.app.activities.security.UpdatePwdActivity_;
import com.zcdh.mobile.app.activities.settings.SettingsHomeActivity_;
import com.zcdh.mobile.app.activities.vacation.OrderDetailActivity;
import com.zcdh.mobile.app.activities.vacation.OrdersActivity;
import com.zcdh.mobile.app.activities.vacation.OrdersFmPaid;
import com.zcdh.mobile.app.activities.vacation.PurchaseActivity;
import com.zcdh.mobile.app.activities.vacation.SearchActivity;
import com.zcdh.mobile.app.activities.vacation.SubscriptionActivity;
import com.zcdh.mobile.app.activities.vacation.SummerjobsPostApplyActivity;
import com.zcdh.mobile.app.activities.vacation.TrackOrderActivity;
import com.zcdh.mobile.utils.SystemServicesUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * @author jeason, 2014-4-7 上午10:37:30
 */
public class ActivityDispatcher {

	public static final String extraKey_ParamCategoryCode = "paramCategoryCode";

	/**
	 * 登录
	 * 
	 * @param activity
	 * @author jeason, 2014-4-7 下午5:17:27
	 */
	public static void to_login(Activity activity) {
		activity.startActivityForResult(new Intent(activity,
				LoginActivity_.class), Constants.REQUEST_CODE_LOGIN);
	}

	public static void toRegisterListActivity(Activity activity, long fairId) {
		RegistrationListActivity_.intent(activity).fairId(fairId).start();
	}

	/**
	 * 登录
	 * 
	 * @param activity
	 * @author jeason, 2014-4-7 下午5:17:27
	 */
	// public static void to_main(Activity activity) {
	//
	// // activity.startActivity(new Intent(activity, MainActivity_.class));
	// MainActivity2_.intent(activity).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
	// }

	/**
	 * 微博绑定zcdh_uid账号
	 */
	public static void to_bind(Context activity, String login_type,
			String thirdpart_uid) {
		BindAccountActivity_.intent(activity).login_type(login_type)
				.third_open_id(thirdpart_uid).start();

	}

	/** 根据条件搜索职位列表 */
	public static void toSearchResult(Activity activity,
			SearchConditionDTO conditions) {
		Intent intent = new Intent(activity, SearchResultsActivity_.class);
		intent.putExtra("search_conditions", conditions);
		activity.startActivity(intent);
	}

	/** 演示demo 入口 author:yangjiannan *//*
	public static void toDemoActivity(Activity activity) {
		Intent intent = new Intent(activity, DemoActivity_.class);
		activity.startActivity(intent);
	}*/

	// /**
	// * @description 岗位详情fragments
	// * @param activity
	// * @param post
	// * @author jeason, 2014-4-11 下午5:25:06
	// */
	// public static void toPostDetais(Activity activity, JobEntPostDTO post) {
	// Intent intent = new Intent(activity, PostDetailActivity_.class);
	// intent.putExtra("target_post", post);
	// activity.startActivity(intent);
	// }

	/**
	 * 
	 * @param activity
	 * @param posts
	 * @param current_index
	 * @author jeason, 2014-4-17 下午5:37:01
	 */
	public static void toDetailsFrameActivity(Activity activity, long postId,
			boolean switchable, List<JobEntPostDTO> posts, int current_index) {
		Intent intent = new Intent(activity, DetailsFrameActivity_.class);
		intent.putExtra("postId", postId);
		intent.putExtra("switchable", switchable);
		intent.putExtra("posts", (ArrayList<JobEntPostDTO>) posts);
		intent.putExtra("currentIndex", current_index);
		activity.startActivity(intent);
	}

	public static void toResetPwd(Activity activity) {
		Intent intent = new Intent(activity, ResetPwdByEmailActivity_.class);
		activity.startActivity(intent);
	}

	/**
	 * 行业选中列表
	 * 
	 * @param single
	 *            标识是否单选
	 */
	public static void to_Industry(boolean single, Activity context) {
		Intent i = new Intent(context, IndustryActivity_.class);
		i.putExtra("single", single);
		if (single) {
			context.startActivityForResult(i,
					IndustryActivity.kREQUEST_INDUSTRY);
		} else {
			context.startActivity(i);
		}
	}

	public static void to_ParcitipantsIndustry(boolean single, int type,long fairID,Activity context) {


		Intent i = new Intent(context, ParticipantsIndustryActivity_.class);
		i.putExtra("single", single);
		i.putExtra("fairID", fairID);
		i.putExtra("type", type);
		if (single) {
			context.startActivityForResult(i,
					Constants.kREQUEST_INDUSTRY);
		} else {
			context.startActivity(i);
		}

	}

	/**
	 * 选择职位
	 * 
	 * @param single
	 * @param context
	 */
	public static void to_Post(boolean single, Activity context) {

		Intent i = new Intent(context, PostsActivity_.class);
		i.putExtra("single", single);
		if (single) {
			context.startActivityForResult(i, PostsActivity.kREQUEST_POST);
		} else {
			context.startActivity(i);
		}
	}

	public static void toBasicInfo(Activity activity) {
		Intent intent = new Intent(activity, BasicInfoActivity_.class);
		activity.startActivityForResult(intent, 0x01);
	}

	public static void toEducationBackground(Activity activity) {
		Intent intent = new Intent(activity, EducationBackgroundActivity_.class);
		activity.startActivity(intent);
	}

	public static void toAddEduExpActivity(Activity activity, int request_code) {
		Intent intent = new Intent(activity, AddEduExpActivity_.class);
		activity.startActivityForResult(intent, request_code);
	}

	public static void toAddTrainningExp(Activity activity, int request_code) {
		Intent intent = new Intent(activity, AddTranningExpActivity_.class);
		activity.startActivityForResult(intent, request_code);
	}

	public static void toMyResumeActivity(Activity activity) {
		Intent intent = new Intent(activity, ResumeActivity_.class);
		activity.startActivityForResult(intent,
				Constants.REQUEST_CODE_MODIFY_RESUME);
	}

	public static void toUpdatePassword(Activity activity) {
		Intent intent = new Intent(activity, UpdatePwdActivity_.class);
		activity.startActivity(intent);
	}

	public static void toUpdateEmail(Activity activity) {
		Intent intent = new Intent(activity, UpdateEmailActivity_.class);
		activity.startActivity(intent);
	}

	public static void toSchoolFinder(Activity activity, int requestCode) {
		Intent intent = new Intent(activity, SchoolFinderActivity_.class);
		activity.startActivityForResult(intent, requestCode);

	}

	public static void toBlackList(Activity activity) {
		Intent intent = new Intent(activity, EnterpriseBlackList_.class);
		activity.startActivity(intent);
	}

	public static void toSelfEvaluationActivity(Activity activity) {
		Intent intent = new Intent(activity, SelfEvaluationActivity_.class);
		activity.startActivity(intent);
	}

	// public static void toInfoCenter(Activity activity) {
	// Intent intent = new Intent(activity, InfoCenter_.class);
	// activity.startActivity(intent);
	// }

	public static void toMyFavorites(Activity activity) {
		Intent intent = new Intent(activity, FavoritePostActivity_.class);
		activity.startActivity(intent);
	}

	public static void toSkillsEdit(Activity activity) {
		Intent intent = new Intent(activity, SkillEditActivity_.class);
		activity.startActivity(intent);
	}

	public static void toLanguagesEdit(Activity activity, int request_code) {
		Intent intent = new Intent(activity, LanguageEditActivity_.class);
		activity.startActivityForResult(intent, request_code);
	}

	/**
	 * 1.08
	 * 
	 * @param activity
	 * @param order_num
	 * @author jeason, 2014-5-27 上午10:21:51
	 */
	public static void purchaseVacationJob(Activity activity, String order_num) {
		Intent intent = new Intent(activity, PurchaseActivity.class);
		// intent.putExtra("place_order", orderDTO);
		// intent.putExtra("post", post);
		// intent.putExtra("amount", amount);
		intent.putExtra("order_num", order_num);
		activity.startActivity(intent);

	}

	public static void toOrders(Activity activity) {
		Intent intent = new Intent(activity, OrdersActivity.class);
		activity.startActivity(intent);
	}

	public static void toApplyPost(Activity activity, EntPostByOrderDTO info) {
		Intent intent = new Intent(activity, SummerjobsPostApplyActivity.class);
		intent.putExtra("post", info);
		activity.startActivity(intent);
	}

	public static void toOrderInfo(Activity activity,
			MyOrderDTO info, String order_num) {
		Intent intent = new Intent(activity, OrderDetailActivity.class);
		intent.putExtra("info", info);
		intent.putExtra("order_num", order_num);
		activity.startActivityForResult(intent,
			OrdersFmPaid.REQUEST_CODE_ON_CACEL_ORDER);

	}

	public static void trackOrder(Activity activity, MyOrderDTO info,
			String order_num) {
		Intent intent = new Intent(activity, TrackOrderActivity.class);
		intent.putExtra("info", info);
		intent.putExtra("order_num", order_num);
		activity.startActivity(intent);
	}

	public static void toVacationSearch(Activity activity) {
		Intent intent = new Intent(activity, SearchActivity.class);
		activity.startActivity(intent);
	}

	public static void addMajor(Activity activity) {
		Intent intent = new Intent(activity, CategoryMajorActivity_.class);
		intent.putExtra("edu_type", "0");
		activity.startActivityForResult(intent, MajorsActivity.kREQUEST_MAJOR);

	}

	public static void addTranning(Activity activity) {
		Intent intent = new Intent(activity, CategoryMajorActivity_.class);
		intent.putExtra("edu_type", "1");
		activity.startActivityForResult(intent, MajorsActivity.kREQUEST_MAJOR);
	}

	public static void addCerticate(Activity activity) {
		Intent intent = new Intent(activity, CertificateFinderActivity_.class);
		activity.startActivityForResult(intent,
				CertificateFinderActivity.kREQUEST_CERTIFICATE);
	}

	public static void toReserve(Activity activity) {
		Intent intent = new Intent(activity, SubscriptionActivity.class);
		activity.startActivity(intent);
	}

	public static void toPurposeActivity(Activity activity, int request_code) {
		Intent intent = new Intent(activity, PurposeActivity_.class);
		activity.startActivityForResult(intent, request_code);
	}

	public static void toEntAlbum(Activity activity, long ent_id) {
		Intent intent = new Intent(activity, EntAlbumActivity_.class);
		intent.putExtra("entId", ent_id);
		activity.startActivity(intent);
	}

	public static void toCompanyComments(Activity activity, long entId) {
		Intent intent = new Intent(activity, CompanyCommentsActivity_.class);
		intent.putExtra("entId", entId);
		activity.startActivity(intent);
	}

	public static void toMoreProduct(Activity activity, long entId) {
		Intent intent = new Intent(activity, MoreProductsActivity_.class);
		intent.putExtra(Constants.kENT_ID, entId);
		activity.startActivity(intent);
	}

	public static void toComment(Activity activity, long entId) {
		Intent intent = new Intent(activity, EntCommentActivity_.class);
		intent.putExtra("entId", entId);
		activity.startActivityForResult(intent, Constants.REQUEST_CODE_COMMENT);
	}

	public static void toApp(Activity activity, String class_name,
			HashMap<String, String> params, long model_id, String title) {
		Class<?> activity_cls = SystemServicesUtils.getClass(class_name);
		if (activity_cls != null) {
			Intent intent = new Intent(activity, activity_cls);
			Bundle data = new Bundle();
			for (Entry<String, String> param : params.entrySet()) {
				data.putString(param.getKey(), param.getValue());
			}
			activity.startActivity(intent);
		}
	}

	public static void toActivity(Context activity, String class_name,
			HashMap<String, String> params, long id) {
		Class<?> activity_cls = SystemServicesUtils.getClass(class_name);
		if (activity_cls != null) {
			Intent intent = new Intent(activity, activity_cls);
			for (Entry<String, String> param : params.entrySet()) {
				intent.putExtra(param.getKey(), param.getValue());
			}
			intent.putExtra("id", id);
			intent.putExtra("fromMessageCenter", true);
			activity.startActivity(intent);
		}

	}

	public static void toPhotoBrowser(Activity activity, String imgUrl,
			String[] urls) {
		Intent intent = new Intent(activity, PhotoBrowser.class);
		intent.putExtra("imgUrl", imgUrl);
		intent.putExtra("urls", urls);
		activity.startActivity(intent);
	}

	public static void toTrackPostApplication(Activity activity, long postId) {
		Intent intent = new Intent(activity, AppliedPostStatusActivity_.class);
		intent.putExtra("postId", postId);
		activity.startActivity(intent);
	}

	public static void toJobFairsActivity(Activity activity) {
		Intent intent = new Intent(activity, JobFairsActivity_.class);
		activity.startActivity(intent);
	}

	public static void toJobFairDetailActivityNew(Activity activity,
			long jobfair_id, int state) {
		Intent intent = new Intent(activity, JobFairDetailActivityNew_.class);
		Bundle data = new Bundle();
		data.putString(Constants.JOBFAIR_ID_KEY, jobfair_id + "");
		data.putString(Constants.FROM_WHERE, "normalPathVisit");
		data.putString(Constants.FAIR_STATUS, String.valueOf(state));
		intent.putExtras(data);
		activity.startActivity(intent);
	}

	public static void toEditEduExpActivity(Activity activity, long edu_id,
			int request_code) {
		EditEduExpActivity_.intent(activity).edu_id(edu_id)
				.startForResult(request_code);

	}

	public static void toEditTrainExpActivity(Activity activity, long edu_id,
			int request_code) {
		Intent intent = new Intent(activity, EditTranningExp_.class);
		intent.putExtra(EditTranningExp.TRANNING_ID_KEY, edu_id);
		activity.startActivityForResult(intent, request_code);
	}

	public static void toEditTranningExp(Activity activity, long tranning_id) {
		Intent intent = new Intent(activity, EditTranningExp_.class);
		intent.putExtra(EditTranningExp.TRANNING_ID_KEY, tranning_id);
		activity.startActivity(intent);
	}

	public static void toSetting(Activity activity, int request_code) {
		Intent intent = new Intent(activity, SettingsHomeActivity_.class);
		activity.startActivityForResult(intent, request_code);
	}

	public static void toEntMsg(Activity activity, long ent_id, String type) {
		MainEntLiuyanActivity_.intent(activity).entId(ent_id).type(type)
				.start();
	}

	public static void toInterviewDetail(Activity activity, long interviewId) {
		InterviewInvitationActivity_.intent(activity).interViewId(interviewId)
				.start();
	}

	public static void toNavigate(Activity activity, Double lat, Double lon) {
		Intent intent = new Intent(activity, NavigatorActivity_.class);
		intent.putExtra("dest_latitude", lat);
		intent.putExtra("dest_longtitude", lon);
		activity.startActivity(intent);
	}
}
