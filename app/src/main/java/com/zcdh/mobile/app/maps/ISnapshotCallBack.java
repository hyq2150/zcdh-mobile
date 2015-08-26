package com.zcdh.mobile.app.maps;

import android.graphics.Bitmap;

/**
 * 地图截图回调接口
 * @author yangjiannan
 *
 */
public interface ISnapshotCallBack {

	public void onSnapshotComplete(Bitmap bitmap);
}
