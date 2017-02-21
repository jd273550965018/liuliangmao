package com.jd61.liuliangmao;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import cn.jpush.android.api.JPushInterface;
public class MainActivity extends Activity {
    private AnimationSet set;
    private RelativeLayout activity_main;
    private String url="";
    private GetData ziliao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        activity_main= (RelativeLayout) findViewById(R.id.activity_main);

        // 缩放动画
        ScaleAnimation animScale = new ScaleAnimation(0, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animScale.setDuration(1000);
        animScale.setFillAfter(true);// 保持动画结束状态

        // 渐变动画
        AlphaAnimation animAlpha = new AlphaAnimation(0, 1);
        animAlpha.setDuration(2000);// 动画时间
        animAlpha.setFillAfter(true);// 保持动画结束状态

        // 动画集合
        set = new AnimationSet(true);
        set.addAnimation(animScale);
        set.addAnimation(animAlpha);
        activity_main.startAnimation(set);
        set.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                OkGo.post("http://wx.jd61.com/liuliangmao.php/Home/Api/androidJump")// 请求方式和请求url
                        .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                        .cacheKey("cacheKey")            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                        .cacheMode(CacheMode.DEFAULT)    // 缓存模式
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, okhttp3.Call call, okhttp3.Response response) {
                                Gson gson = new Gson();
                                ziliao = gson.fromJson(s, GetData.class);
                                if(ziliao!=null){
                                    if(ziliao.getData()!=null){
                                        url=ziliao.getData().getUrl();
                                        Intent intent = new Intent();
                                        intent.setAction("android.intent.action.VIEW");
                                        Uri content_url = Uri.parse(url);
                                        intent.setData(content_url);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }
                        });
            }
        });
    }
}
