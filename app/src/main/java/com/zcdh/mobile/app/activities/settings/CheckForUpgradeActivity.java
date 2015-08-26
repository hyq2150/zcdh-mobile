package com.zcdh.mobile.app.activities.settings;

import com.zcdh.mobile.R;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.upgrade.UpdateAppService;
import com.zcdh.mobile.framework.upgrade.UpdateAppService.DownloadProgressListner;
import com.zcdh.mobile.utils.SystemServicesUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 检查更新
 *
 * @author yangjiannan
 */
@WindowFeature({Window.FEATURE_PROGRESS})
@EActivity(R.layout.activity_check_upgrade)
public class CheckForUpgradeActivity extends BaseActivity implements DownloadProgressListner {

    @ViewById(R.id.loadingImg)
    ImageView loadingImg;

    @ViewById(R.id.currentVersionText)
    TextView currentVersionText;

    @ViewById(R.id.msgText)
    TextView msgText;

    @ViewById(R.id.checkUpgradePB)
    ProgressBar checkUpgradeBB;

    @ViewById(R.id.upgradeBtn)
    Button upgradeNowBtn;

    private UpdateAppService updateAppService;

    private int upgradeStatus = -1; // 0 正在更新 1更新成功

    private int mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_PROGRESS);
    }


    public boolean onOptionsItemSelected(MenuItem menu) {
        if (menu.getItemId() == android.R.id.home || menu.getItemId() == 0) {
            finish();
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @AfterViews
    void bindView() {

        //设置标题
        SystemServicesUtils.setActionBarCustomTitle(this, getSupportActionBar(),
                getString(R.string.checkForUpgrade));

        // 显示当前版本号
        String currentVersionStr = getString(R.string.currentVersion);
        currentVersionStr += updateAppService.getVerName(getApplicationContext());
        currentVersionText.setText(currentVersionStr);

        updateAppService = new UpdateAppService(getApplicationContext(), this);
        checkUpgrade();
    }

    @Click(R.id.upgradeBtn)
    void onUpgradeBtn() {
        checkUpgradeBB.setVisibility(View.VISIBLE);
        upgradeNowBtn.setVisibility(View.GONE);
        msgText.setText("正在更新...");
        if (upgradeStatus == 1) {///已下载apk， 更新app
            updateAppService.installApk();
        } else {
            updateAppService.downloadApk();
        }
    }


    @UiThread
    void showUpgradeMessage(boolean shouldUpgrade) {
        checkUpgradeBB.setVisibility(View.GONE);
        if (shouldUpgrade) {
            String newest_version_msg = getString(R.string.newest_version) + updateAppService
                    .getUpdateInfo().getVersionName();
            //显示更新按钮
            upgradeNowBtn.setVisibility(View.VISIBLE);
            msgText.setText(newest_version_msg);
        } else {
            upgradeNowBtn.setVisibility(View.GONE);
            msgText.setText(getString(R.string.newed_version_msg));
        }

    }

    /**
     * 检查更新
     */
    @Background
    void checkUpgrade() {
        boolean shouldUpgrade = updateAppService.checkIsUpdate();
        showUpgradeMessage(shouldUpgrade);
    }

	/* ==============  下载监听事件 ==================== */

    @Override
    public void onDownloadStart() {

    }


    @UiThread
    @Override
    public void onDownloadFinished() {

//		msgText.setText("最新版已经下载");
//		upgradeNowBtn.setText("马上安装");
    }


    @Override
    public void onDownloadProgress(int p) {
        //Normalize our progress along the progress bar's scale
        //Toast.makeText(this, p+"", Toast.LENGTH_SHORT).show();
        setSupportProgress((p + 1) * 100);
        msgText.setText("正在更新...  " + ((p + 1)) + "%");
    }


    @UiThread
    @Override
    public void onError() {
        msgText.setText("更新失败,请稍后再试");
        checkUpgradeBB.setVisibility(View.GONE);
    }


    @UiThread
    @Override
    public void onDownloadSuccess() {
        msgText.setText("下载完成");
        upgradeNowBtn.setText("安装更新");
        upgradeNowBtn.setVisibility(View.VISIBLE);
        checkUpgradeBB.setVisibility(View.GONE);
        updateAppService.installApk();
    }


    @Override
    public void onCancel() {
        finish();
    }


}
