package com.zcdh.mobile.app.maps.bmap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.zcdh.core.utils.JsonUtil;
import com.zcdh.mobile.utils.StringUtils;

/**
 * 反地理编码
 * 
 * @author yangjiannan
 * 
 */
public class GeoCodingRequest {

	private static final String GEO_API_KEY = "A5d3900f3a654bb077a19be61aadeab7";
	/**
	 * 根据具体的地名查询对应的地理坐标
	 * 
	 * @param address
	 * @return
	 */
	public static LatLng geoCodingLatlngByAddress(String address) {
		String resultJson = requestGeoCoding(false, 0, 0, address);
		LatLng latlng = null;
		if(StringUtils.isBlank(resultJson)){
			return null;
		}
		try {
			Map obj = (Map) JsonUtil.toObject(resultJson, Map.class);
			Map result = null;
			if (obj != null) {
				result = (Map) obj.get("result");
			}

			Map location = (Map) result.get("location");
			if (location != null) {

				Double lat = (Double) location.get("lat");
				Double lon = (Double) location.get("lng");
				latlng = new LatLng(lat, lon);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return latlng;

	}

	/**
	 * 根据经纬度查询出具体的地名
	 * 
	 * @param lat
	 * @param lng
	 * @return
	 */
	public static String geoCodingAddressByLatlng(double lat, double lng) {
		String resultJson = requestGeoCoding(true, lat, lng, null);

		String addressName = null;

		try {

			if (!StringUtils.isBlank(resultJson)) {

				Log.i("LOAD_GEO_ADDRESS:", resultJson + "");

				Map obj = (Map) JsonUtil.toObject(resultJson, Map.class);

				Map result = null;
				if (obj != null) {
					result = (Map) obj.get("result");
				}

				Map addressComponent = null;
				if (result != null) {
					addressComponent = (Map) result.get("addressComponent");
				}

				addressName = (String) addressComponent.get("city");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return addressName;

	}

	/**
	 * 反地理编码定位 1）将具体的地名 转换成 对应的经纬度 2)
	 */
	public static String requestGeoCoding(final boolean iscoordinate,
			final double lat, final double lon, final String areaName) {
		// Toast.makeText(this, "load geo", Toast.LENGTH_SHORT).show();
		String resultJson = null;
		HttpURLConnection loc_api_conn = null;
		InputStream is = null;
		BufferedReader in = null;
		try {
			Log.i("load geo ......", "loading");
			String urlStr = null;
			if (iscoordinate) {// 根据地理坐标查询地址

				urlStr = String
						.format("http://api.map.baidu.com/geocoder/v2/?ak=%s&&location=%s,%s&output=json&pois=0",
								GEO_API_KEY, lat + "", lon + "");
			} else { // 根据地址名称查询地理坐标

				urlStr = String
						.format("http://api.map.baidu.com/geocoder/v2/?ak=%s&&output=json&address=%s",
								GEO_API_KEY,
								java.net.URLEncoder.encode(areaName, "utf-8"));
			}
			Log.i("geo url", urlStr);
			URL url = new URL(urlStr);
			
			loc_api_conn = (HttpURLConnection) url.openConnection();
			loc_api_conn.setConnectTimeout(5000);
			is = loc_api_conn.getInputStream();
			in = new BufferedReader(new InputStreamReader(is));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
			if (buffer != null) {
				resultJson = buffer.toString();
			}
			
			// 首次加载职位

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
				try {
					if(in!=null)in.close();
					if(is!=null)is.close();
					if(loc_api_conn!=null)loc_api_conn.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			
		}
		return resultJson;
	}
}
