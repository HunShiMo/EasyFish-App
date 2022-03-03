package com.wlh.epiboly.easyfish.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.wlh.epiboly.easyfish.FishConfig;
import com.wlh.epiboly.easyfish.R;
import com.wlh.epiboly.easyfish.Utils.User_InfoService;
import com.wlh.epiboly.easyfish.View.User_Info;

import java.util.ArrayList;
import java.util.List;

import static com.wlh.epiboly.easyfish.Utils.User_InfoService.getUser_InfoService;


/*开机进入之后进行登录,登录完成后进入首页，然后每隔一段时间进行*/
public class WelcomeActivity extends AppCompatActivity {
    SharedPreferences FishSP;
    User_InfoService user_infoService;
    int res=0;
    List<User_Info> LoginList=new ArrayList<>();
    boolean logined=false;
    Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        init();
    }
    private void init() {
        FishSP=getSharedPreferences("FishSP",MODE_PRIVATE);
        user_infoService=getUser_InfoService();
        FishConfig.username =FishSP.getString("username","");
        FishConfig.password=FishSP.getString("password","");
        FishConfig.email=FishSP.getString("email","");
        if(!FishConfig.username.equals("")){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    LoginList=user_infoService.Login(FishConfig.username);
                    if(LoginList.size()>0){
                        for(int i=0;i<LoginList.size();i++){
                            if(LoginList.get(i).getUser_password().equals(FishConfig.password)){
                                logined=true;
                            }
                        }
                    }else{
                        logined=false;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(logined){
                                intent=new Intent();
                                intent.setClass(WelcomeActivity.this,IndexActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                intent=new Intent();
                                intent.setClass(WelcomeActivity.this,RegisterActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }).start();
        }else{
            intent=new Intent();
            intent.setClass(WelcomeActivity.this,RegisterActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
