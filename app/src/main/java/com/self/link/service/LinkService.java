package com.self.link.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.leconssoft.common.baseUtils.permssion.EasyPermission;

import org.greenrobot.eventbus.EventBus;

public class LinkService extends Service {
    Location mLocation;

    public LinkService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        location();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mLocation != null)
            EventBus.getDefault().postSticky(mLocation);
        return super.onStartCommand(intent, flags, startId);
    }

    protected void location() {
        String serviceString = Context.LOCATION_SERVICE;// 获取的是位置服务
        LocationManager locationManager =
                (LocationManager) this.getSystemService(serviceString);// 调用getSystemService()方法来获取LocationManager对象
        String provider = LocationManager.GPS_PROVIDER;// 指定LocationManager的定位方法

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.e("Service", "没有权限");
            return;
        }
        mLocation = locationManager.getLastKnownLocation(provider);// 调用getLastKnownLocation()方法获取当前的位置信息

        if (mLocation!=null){
            double lat = mLocation.getLatitude();//获取纬度
            double lng = mLocation.getLongitude();//获取经度
        }

        locationManager.requestLocationUpdates(provider, 2000, 10, locationListener);
    }

    protected final LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            mLocation = location;
            EventBus.getDefault().postSticky(mLocation);
//            Toast.makeText(LinkService.this, "Latitude:"+mLocation.getLatitude()+"; Longitude"+mLocation.getLongitude(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

    };


}
