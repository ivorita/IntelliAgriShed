package com.antelope.android.intelliagrished.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import com.antelope.android.intelliagrished.db.Weather;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        //获得AlarmManager实例对象
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        int anHour = 8 * 60 * 60 * 1000;

        //SystemClock.elapsedRealtime() 返回系统启动到现在的毫秒数，包括休眠时间
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this,AutoUpdateService.class);

        /*PendingIntent可以理解为Intent的封装包，简单的说就是在Intent上在加个指定的动作。
        在使用Intent的时候，我们还需要在执行startActivity、startService或sendBroadcast才能使Intent有用。
        而PendingIntent的话就是将这个动作包含在内了。*/

        //通过启动服务来实现闹钟提示的话，PendingIntent对象的获取就应该采用PendingIntent.getService(Context c,int i,Intent intent,int j)
        PendingIntent pi = PendingIntent.getService(this,0,i,0);
        alarmManager.cancel(pi);

        /**
         * 一次性闹钟
         * @param int type AlarmManager.ELAPSED_REALTIME_WAKEUP （闹钟类型） 表示闹钟会在睡眠状态下唤醒系统并执行提示功能，该状态下闹钟也使用相对时间，状态值为2
         * @param long startTime 闹钟的第一次执行时间，以毫秒为单位
         * @param long intervalTime 对于后两个方法来说，存在本属性，表示两次闹钟执行的间隔时间，也是以毫秒为单位。
         * @param PendingIntent pi 绑定了闹钟的执行动作，比如发送一个广播、给出提示等。
         */
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }

    //更新天气信息
    private void updateWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather",null);

        if (weatherString != null){

            //有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            //String weatherId = weather.basic.weatherId;

            String weatherUrl = "http://guolin.tech/api/weather?cityid=" + "CN101280210" + "&key=ae28aa2c3d514239b930c3cad5187404";
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string();
                    Log.d("responseText: ", responseText);
                    Weather weather = Utility.handleWeatherResponse(responseText);
                    if (weather != null && "ok".equals(weather.status)){
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("weather",responseText);
                        editor.apply();
                    }
                    Log.d("AutoUpdateService", "刷新成功");
                }
            });

        }
    }

}