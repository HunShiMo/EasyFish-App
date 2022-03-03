package com.wlh.epiboly.easyfish.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.wlh.epiboly.easyfish.FishConfig;
import com.wlh.epiboly.easyfish.R;
import com.wlh.epiboly.easyfish.Utils.User_InfoService;
import com.wlh.epiboly.easyfish.View.User_Info;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.wlh.epiboly.easyfish.Utils.CheckUtil.checkEmail;
import static com.wlh.epiboly.easyfish.Utils.User_InfoService.getUser_InfoService;


public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    User_InfoService user_infoService;
    int res = 0;
    Gson gson;
    Dialog LoadingDialog;
    View LoadingView;
    SharedPreferences FishSP;
    Intent intent;
    @BindView(R.id.iv_return)
    ImageView ivReturn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initDialog();
        init();
        ButterKnife.bind(this);
    }
    private void initDialog() {
        LoadingView = LayoutInflater.from(LoginActivity.this).inflate(R.layout.dialog_loading, null);//实例化一个view作为弹窗的内容view
        LoadingDialog = new Dialog(LoginActivity.this, R.style.DialogTheme);
        LoadingDialog.setContentView(LoadingView);
        LoadingDialog.setCanceledOnTouchOutside(false);
        LoadingDialog.setCancelable(false);
    }

    private void init() {
        gson = new Gson();
        FishSP = getSharedPreferences("FishSP", MODE_PRIVATE);
        user_infoService = getUser_InfoService();
        intent = new Intent();
    }

    @OnClick({R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if (!etEmail.getText().toString().equals("")) {
                    if (checkEmail(etEmail.getText().toString())) {
                        if (!etUsername.getText().toString().equals("")) {
                            if (!etPassword.getText().toString().equals("")) {
                                LoadingDialog.show();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        User_Info user_info = new User_Info();
                                        user_info.setUser_email(etEmail.getText().toString());
                                        user_info.setUser_name(etUsername.getText().toString());
                                        user_info.setUser_password(etPassword.getText().toString());
                                        res = user_infoService.insertUserData(user_info);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                LoadingDialog.dismiss();
                                                if (res == 1) {
                                                    Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                                    SharedPreferences.Editor editor = FishSP.edit();
                                                    editor.putString("email", etEmail.getText().toString());
                                                    editor.putString("username", etUsername.getText().toString());
                                                    editor.putString("password", etPassword.getText().toString());
                                                    if (editor.commit()) {
                                                        FishConfig.username = etEmail.getText().toString();
                                                        FishConfig.password = etUsername.getText().toString();
                                                        FishConfig.email = etPassword.getText().toString();
                                                        intent = new Intent();
                                                        intent.setClass(LoginActivity.this, IndexActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                } else {
                                                    Toast.makeText(LoginActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }).start();
                            } else {
                                Toast.makeText(LoginActivity.this, "请填写密码", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "请填写用户名", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "请检查邮箱格式", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "请填写邮箱", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @OnClick(R.id.iv_return)
    public void onClick() {
        finish();
    }
}
//首页调整
