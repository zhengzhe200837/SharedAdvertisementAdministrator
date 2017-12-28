package com.wind.main;

/**
 * Created by zhuyuqiang on 17-11-18.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wind.main.manager.NetWorkManager;
import com.wind.main.mode.interfaces.PhoneSignInInterface;
import com.wind.main.mode.results.SignInResult;
import com.wind.main.util.LogUtil;

import org.afinal.simplecache.ACache;


public class UserAuthenticationActivity extends AppCompatActivity {

    private TextView mLogInButton = null;
    private EditText mPhoneEdit;
    private ActionBar mSupportActionBar;
    private Button mLoginCancel;
    private ACache aCache =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//Appkey : 235cccde4a1ce | App Secret : 223cea512b095e5a036e8dfddac771ba
        setContentView(R.layout.message_authentication_layout);
        mLogInButton = (TextView) findViewById(R.id.mal_log_in);
        mLoginCancel = (Button) findViewById(R.id.login_cancel);
        mPhoneEdit = (EditText) findViewById(R.id.sd_login_phone);
        aCache =ACache.get(getApplicationContext());
        mSupportActionBar = getSupportActionBar();
        if (mSupportActionBar != null) {
            mSupportActionBar.setTitle(R.string.maa_authenticate);
            mSupportActionBar.setSubtitle(R.string.maa_tip);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        applyListener();

    }

    private void applyListener() {
        if (mLogInButton != null) {
            mLoginCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UserAuthenticationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
        if (mLogInButton != null) {
            mLogInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(UserAuthenticationActivity.this, AddAdActivity.class));
                    finish();
                    aCache.put("is_login",true,300);
                    /*Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create(new Gson()))
                            .build();
                    PhoneSignInInterface signIn = retrofit.create(PhoneSignInInterface.class);
                    final Call<SignInResult> call = signIn.getRequestResult(mPhoneEdit.getText().toString().trim());
                    if(NetWorkManager.isValidNetWork(UserAuthenticationActivity.this)){
                        call.enqueue(new Callback<SignInResult>() {
                            @Override
                            public void onResponse(Response<SignInResult> response, Retrofit retrofit) {
                                SignInResult result = response.body();
                                if (result != null) {
                                    String r = result.getResult();
                                    LogUtil.e("zyq", "result = " + r);
                                    if ("yes".equals(r)) {
                                        startActivity(new Intent(UserAuthenticationActivity.this, AddAdActivity.class));
                                        finish();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {

                            }
                        });
                    }else{
                        Toast.makeText(UserAuthenticationActivity.this,"",Toast.LENGTH_LONG).show();
                    }*/
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        cancelListener();
    }

    private void cancelListener() {
        if (mLogInButton != null) {
            mLoginCancel.setOnClickListener(null);
        }

        if (mLogInButton != null) {
            mLogInButton.setOnClickListener(null);
        }
    }
}

