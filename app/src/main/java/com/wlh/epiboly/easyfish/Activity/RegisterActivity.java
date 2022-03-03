package com.wlh.epiboly.easyfish.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wlh.epiboly.easyfish.FishConfig;
import com.wlh.epiboly.easyfish.R;
import com.wlh.epiboly.easyfish.Utils.User_InfoService;
import com.wlh.epiboly.easyfish.View.User_Info;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_register)
    Button btnRegister;
    Intent intent;
    List<User_Info> LoginList = new ArrayList<>();
    User_InfoService user_infoService;
    boolean logined = false;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    SharedPreferences FishSP;
    Dialog LoadingDialog;
    View LoadingView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initDialog();
        init();
    }

    private void initDialog() {
        LoadingView = LayoutInflater.from(RegisterActivity.this).inflate(R.layout.dialog_loading, null);//实例化一个view作为弹窗的内容view
        LoadingDialog = new Dialog(RegisterActivity.this, R.style.DialogTheme);
        LoadingDialog.setContentView(LoadingView);
        LoadingDialog.setCanceledOnTouchOutside(false);
        LoadingDialog.setCancelable(false);
    }

    private void init() {
        intent = new Intent();
        user_infoService = User_InfoService.getUser_InfoService();
        FishSP=getSharedPreferences("FishSP",MODE_PRIVATE);
    }

    @OnClick({R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                if (!etUsername.getText().toString().equals("")) {
                    if (!etPassword.getText().toString().equals("")) {
                        LoadingDialog.show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                LoginList = user_infoService.Login(etUsername.getText().toString());
                                if (LoginList.size() > 0) {
                                    for (int i = 0; i < LoginList.size(); i++) {
                                        if (LoginList.get(i).getUser_password().equals(etPassword.getText().toString())) {
                                            FishConfig.username = LoginList.get(i).getUser_name();
                                            FishConfig.password = LoginList.get(i).getUser_password();
                                            FishConfig.email = LoginList.get(i).getUser_email();
                                            logined = true;
                                        }
                                    }
                                    if(logined){
                                        SharedPreferences.Editor editor = FishSP.edit();
                                        editor.putString("email",  FishConfig.email);
                                        editor.putString("username", FishConfig.username);
                                        editor.putString("password",  FishConfig.password);
                                        editor.commit();
                                    }
                                } else {
                                    logined = false;
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        LoadingDialog.show();
                                        if (logined) {
                                            Toast.makeText(RegisterActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                            intent = new Intent();
                                            intent.setClass(RegisterActivity.this, IndexActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(RegisterActivity.this, "密码错误,请检查后重试", Toast.LENGTH_SHORT).show();
                                        }
                                        LoadingDialog.dismiss();
                                    }
                                });
                            }
                        }).start();
//                        LoadingDialog.dismiss();
                    } else {
                        Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @OnClick(R.id.tv_login)
    public void onClick() {
        intent=new Intent();
        intent.setClass(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);
    }
}
