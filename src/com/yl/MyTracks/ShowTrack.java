package com.yl.MyTracks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.yl.MyTracks.db.TrackDbAdapter;
import com.yl.MyTracks.db.LocateDbAdapter;

public class ShowTrack extends MapActivity {
	// 定义菜单需要的常量
	private static final int MENU_NEW = Menu.FIRST + 1;
	private static final int MENU_CON = MENU_NEW + 1;
	private static final int MENU_DEL = MENU_CON + 1;
	private static final int MENU_MAIN = MENU_DEL + 1;

	private TrackDbAdapter mDbHelper;
	private LocateDbAdapter mlcDbHelper;

	private static final String TAG = "ShowTrack";
	private static MapView mMapView;
	private MapController mc;

	protected MyLocationOverlay mOverlayController;
	private Button mZin;
	private Button mZout;
	private Button mPanN;
	private Button mPanE;
	private Button mPanW;
	private Button mPanS;
	private Button mGps;
	private Button mSat;
	private Button mTraffic;
	private Button mStreetview;
	private String mDefCaption = "";
	private GeoPoint mDefPoint;

	private LocationManager lm;
	private LocationListener locationListener;

	private int track_id;
	private Long rowId;

	/** Called when the activity is first created. */
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.show_track);
		findViews();
		centerOnGPSPosition();
		revArgs();

		paintLocates();
		startTrackService();
	}
	/**
	 * 开启一个服务
	 */
	private void startTrackService() {
		Intent i = new Intent("com.yl.MyTracks.START_TRACK_SERVICE");
		i.putExtra(LocateDbAdapter.TRACKID, track_id);
        startService(i);		
	}
	/**
	 * 停止服务
	 */
	private void stopTrackService() {
        stopService(new Intent("com.yl.MyTracks..START_TRACK_SERVICE"));		
	}

	private void paintLocates() {
		mlcDbHelper = new LocateDbAdapter(this);
		mlcDbHelper.open();
		Cursor mLocatesCursor = mlcDbHelper.getTrackAllLocates(track_id);
		startManagingCursor(mLocatesCursor);
		Resources resources = getResources();
		Overlay overlays = new LocateOverLay(resources
				.getDrawable(R.drawable.icon), mLocatesCursor);
		mMapView.getOverlays().add(overlays);
		stopManagingCursor(mLocatesCursor);
		mlcDbHelper.close();
	}

	private void revArgs() {
		Log.d(TAG, "revArgs.");
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String name = extras.getString(TrackDbAdapter.NAME);
			//String desc = extras.getString(TrackDbAdapter.DESC);
			rowId = extras.getLong(TrackDbAdapter.KEY_ROWID);
			track_id = rowId.intValue();
			Log.d(TAG, "rowId=" + rowId);
			if (name != null) {
				setTitle(name);
			}
		}
	}

	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	private void findViews() {
		Log.d(TAG, "find Views");
		// 获取视图中的组件
		mMapView = (MapView) findViewById(R.id.mv);
		mc = mMapView.getController();

		SharedPreferences settings = getSharedPreferences(Setting.SETTING_INFOS, 0);
		String setting_gps = settings.getString(Setting.SETTING_MAP, "10");
		mc.setZoom(Integer.parseInt(setting_gps));

		// Set up the button for "Pan East"
		mPanE = (Button) findViewById(R.id.sat);
		mPanE.setOnClickListener(new OnClickListener() {
			// @Override
			public void onClick(View arg0) {
				panEast();
			}
		});
		//设置缩小按钮
		mZin = (Button) findViewById(R.id.zin);
		mZin.setOnClickListener(new OnClickListener() {
			// @Override
			public void onClick(View arg0) {
				zoomIn();
			}
		});
		//设置放大按钮
		mZout = (Button) findViewById(R.id.zout);
		mZout.setOnClickListener(new OnClickListener() {
			// @Override
			public void onClick(View arg0) {
				zoomOut();
			}
		});
		// 设置向北移动按钮
		mPanN = (Button) findViewById(R.id.pann);
		mPanN.setOnClickListener(new OnClickListener() {
			// @Override
			public void onClick(View arg0) {
				panNorth();
			}
		});

		// 设置向东移动按钮
		mPanE = (Button) findViewById(R.id.pane);
		mPanE.setOnClickListener(new OnClickListener() {
			// @Override
			public void onClick(View arg0) {
				panEast();
			}
		});

		// 设置向西移动按钮
		mPanW = (Button) findViewById(R.id.panw);
		mPanW.setOnClickListener(new OnClickListener() {
			// @Override
			public void onClick(View arg0) {
				panWest();
			}
		});
		// 设置向南移动
		mPanS = (Button) findViewById(R.id.pans);
		mPanS.setOnClickListener(new OnClickListener() {
			// @Override
			public void onClick(View arg0) {
				panSouth();
			}
		});

		// 设置GPS视图按钮
		mGps = (Button) findViewById(R.id.gps);
		mGps.setOnClickListener(new OnClickListener() {
			// @Override
			public void onClick(View arg0) {
				centerOnGPSPosition();
			}
		});
		// 设置卫星模式视力按钮
		mSat = (Button) findViewById(R.id.sat);
		mSat.setOnClickListener(new OnClickListener() {
			// @Override
			public void onClick(View arg0) {
				toggleSatellite();
			}
		});

		// 设置交通模式视图按钮
		mTraffic = (Button) findViewById(R.id.traffic);
		mTraffic.setOnClickListener(new OnClickListener() {
			// @Override
			public void onClick(View arg0) {
				toggleTraffic();
			}
		});

		// 设置街景模式视图按钮
		mStreetview = (Button) findViewById(R.id.streetview);
		mStreetview.setOnClickListener(new OnClickListener() {
			// @Override
			public void onClick(View arg0) {
				toggleStreetView();
			}
		});

		// 使用“位置管理器”获取GPS位置变化信息
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new MyLocationListener();
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,locationListener);
	}

//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		Log.d(TAG, "onKeyDown");
//		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
//			panWest();
//			return true;
//		} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
//			panEast();
//			return true;
//		} else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
//			panNorth();
//			return true;
//		} else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
//			panSouth();
//			return true;
//		}else if(keyCode == KeyEvent.KEYCODE_BACK){
//			return false;
//		}
//		return false;
//	}

	public void panWest() {
		GeoPoint pt = new GeoPoint(mMapView.getMapCenter().getLatitudeE6(),
				mMapView.getMapCenter().getLongitudeE6()
						- mMapView.getLongitudeSpan() / 4);
		mc.setCenter(pt);
	}

	public void panEast() {
		GeoPoint pt = new GeoPoint(mMapView.getMapCenter().getLatitudeE6(),
				mMapView.getMapCenter().getLongitudeE6()
						+ mMapView.getLongitudeSpan() / 4);
		mc.setCenter(pt);
	}

	public void panNorth() {
		GeoPoint pt = new GeoPoint(mMapView.getMapCenter().getLatitudeE6()
				+ mMapView.getLatitudeSpan() / 4, mMapView.getMapCenter()
				.getLongitudeE6());
		mc.setCenter(pt);
	}

	public void panSouth() {
		GeoPoint pt = new GeoPoint(mMapView.getMapCenter().getLatitudeE6()
				- mMapView.getLatitudeSpan() / 4, mMapView.getMapCenter()
				.getLongitudeE6());
		mc.setCenter(pt);
	}

	public void zoomIn() {
		mc.zoomIn();
	}

	public void zoomOut() {
		mc.zoomOut();
	}

	public void toggleSatellite() {
		mMapView.setSatellite(true);
		mMapView.setStreetView(false);
		mMapView.setTraffic(false);
	}

	public void toggleTraffic() {
		mMapView.setTraffic(true);
		mMapView.setSatellite(false);
		mMapView.setStreetView(false);
	}

	public void toggleStreetView() {
		mMapView.setStreetView(true);
		mMapView.setSatellite(false);
		mMapView.setTraffic(false);
	}
	/**
	 * 单击GPS所触发的方法
	 */
	private void centerOnGPSPosition() {
		double Latitude = 0.00;
		double Longitude = 0.00;
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
       criteria.setAccuracy(Criteria.ACCURACY_FINE);//设置定位精确度ACCURACY_COARSE 比较粗略，ACCURACY_FINE则比较精细,还可以是NO_REQUIREMENT
       criteria.setAltitudeRequired(false);//设置是否需要海拔信息 Altitude
       criteria.setBearingRequired(false);//设置是否需要方位信息 Bearing
       criteria.setCostAllowed(true);//设置是否允许运营商收费
       criteria.setPowerRequirement(Criteria.POWER_LOW);////要求低耗电
       
       String provider = lm.getBestProvider(criteria, false);
		Location loc = lm.getLastKnownLocation(provider);
		
		if (loc == null) {
			loc=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}
		if (loc == null) {
			loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		
		//第一次获得设备的位置
		if (loc != null) {
			Latitude = loc.getLatitude();
			Longitude = loc.getLongitude();
		}
		Log.d(TAG, "Latitude:"+Latitude+"Longitude:"+Longitude);
		//找出当前位置
		mDefPoint = new GeoPoint((int)(Latitude * 1000000),(int)(Longitude * 1000000));
		mDefCaption = "我当前的位置";
		mc.animateTo(mDefPoint);
		mc.setCenter(mDefPoint);
		// show Overlay on map.
		MyOverlay mo = new MyOverlay();
		mo.onTap(mDefPoint, mMapView);
		mMapView.getOverlays().add(mo);
	}
	
	 /**  
	    * 这里一定要对LocationManager进行重新设置监听  
	    * mgr获取provider的过程不是一次就能成功的  
	    * mgr.getLastKnownLocation很可能返回null  
	    * 如果只在initProvider()中注册一次监听则基本很难成功  
	    */
	protected void onResume() {
		String provider = "gps";
	    super.onResume();   
	    lm.requestLocationUpdates(provider, 60000, 1, locationListener);   
	}
	
	protected void onPause() {   
	    super.onPause();   
	    lm.removeUpdates(locationListener);   
	}  
	// This is used draw an overlay on the map
	protected class MyOverlay extends Overlay {
		@Override
		public void draw(Canvas canvas, MapView mv, boolean shadow) {
			Log.d(TAG, "MyOverlay::darw..mDefCaption=" + mDefCaption);
			super.draw(canvas, mv, shadow);

			if (mDefCaption.length() == 0) {
				return;
			}

			Paint p = new Paint();
			int[] scoords = new int[2];
			int sz = 5;

			// Convert to screen coords
			Point myScreenCoords = new Point();
			mMapView.getProjection().toPixels(mDefPoint, myScreenCoords);
			// mMapView.set
			// mv.getPointXY(mDefPoint, scoords);
			// Draw point caption and its bounding rectangle
			scoords[0] = myScreenCoords.x;
			scoords[1] = myScreenCoords.y;
			p.setTextSize(14);
			p.setAntiAlias(true);

			int sw = (int) (p.measureText(mDefCaption) + 0.5f);
			int sh = 25;
			int sx = scoords[0] - sw / 2 - 5;
			int sy = scoords[1] - sh - sz - 2;
			RectF rec = new RectF(sx, sy, sx + sw + 10, sy + sh);

			p.setStyle(Style.FILL);
			p.setARGB(128, 255, 0, 0);

			canvas.drawRoundRect(rec, 5, 5, p);

			p.setStyle(Style.STROKE);
			p.setARGB(255, 255, 255, 255);
			canvas.drawRoundRect(rec, 5, 5, p);
			// canvas.d

			canvas.drawText(mDefCaption, sx + 5, sy + sh - 8, p);

			// Draw point body and outer ring
			p.setStyle(Style.FILL);
			p.setARGB(88, 255, 0, 0);
			p.setStrokeWidth(1);
			RectF spot = new RectF(scoords[0] - sz, scoords[1] + sz, scoords[0]
					+ sz, scoords[1] - sz);
			canvas.drawOval(spot, p);

			p.setARGB(255, 255, 0, 0);
			p.setStyle(Style.STROKE);
			canvas.drawCircle(scoords[0], scoords[1], sz, p);
		}
	}

	//
	protected class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location loc) {
			Log.d(TAG, "MyLocationListener::onLocationChanged..");
			if (loc != null) {
				Toast.makeText(
						getBaseContext(),
						"Location changed : Lat: " + loc.getLatitude()
								+ " Lng: " + loc.getLongitude(),
						Toast.LENGTH_SHORT).show();
				// Set up the overlay controller
				// mOverlayController = mMapView.createOverlayController();
				mDefPoint = new GeoPoint((int) (loc.getLatitude() * 1000000),
						(int) (loc.getLongitude() * 1000000));
				mc.animateTo(mDefPoint);
				mc.setCenter(mDefPoint);
				// show on the map.
				mDefCaption = "Lat: " + loc.getLatitude() + ",Lng: "
						+ loc.getLongitude();
				MyOverlay mo = new MyOverlay();
				mo.onTap(mDefPoint, mMapView);
				mMapView.getOverlays().add(mo);
				// //////////
				//if(mlcDbHelper == null){
				//	mlcDbHelper.open();
				//}
				//mlcDbHelper.createLocate(track_id,  loc.getLongitude(),loc.getLatitude(), loc.getAltitude());
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
			Toast.makeText(
					getBaseContext(),
					"ProviderDisabled.",
					Toast.LENGTH_SHORT).show();		}

		@Override
		public void onProviderEnabled(String provider) {
			Toast.makeText(
					getBaseContext(),
					"ProviderEnabled,provider:"+provider,
					Toast.LENGTH_SHORT).show();		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}
	}

	// 初始化菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_CON, 0, R.string.menu_con).setIcon(
				R.drawable.con_track).setAlphabeticShortcut('C');
		menu.add(0, MENU_DEL, 0, R.string.menu_del).setIcon(R.drawable.delete)
				.setAlphabeticShortcut('D');
		menu.add(0, MENU_NEW, 0, R.string.menu_new).setIcon(
				R.drawable.new_track).setAlphabeticShortcut('N');
		menu.add(0, MENU_MAIN, 0, R.string.menu_main).setIcon(R.drawable.icon)
				.setAlphabeticShortcut('M');
		return true;
	}

	// 当一个菜单被选中的时候调用
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent();
		switch (item.getItemId()) {
		case MENU_NEW:
			intent.setClass(ShowTrack.this, NewTrack.class);
			startActivity(intent);
			return true;
		case MENU_CON:
			// TODO: 继续跟踪选择的记录
			startTrackService();
			return true;
		case MENU_DEL:
			 mDbHelper = new TrackDbAdapter(this);
			 mDbHelper.open();
			if (mDbHelper.deleteTrack(rowId)) {
				mDbHelper.close();
				intent.setClass(ShowTrack.this, iMyTracks.class);
				startActivity(intent);
			}else{
				mDbHelper.close();
			}
			return true;
		case MENU_MAIN:
			intent.setClass(ShowTrack.this, iMyTracks.class);
			startActivity(intent);
			break;
		}
		return true;
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG, "onStop");
		// mDbHelper.close();
		//mlcDbHelper.close();
	}
	
	@Override
    public void onDestroy() {
		Log.d(TAG, "onDestroy.");
        super.onDestroy();
        stopTrackService();
        }
}

