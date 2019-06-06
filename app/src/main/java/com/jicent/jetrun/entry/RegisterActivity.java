package com.jicent.jetrun.entry;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.jicent.jetrun.AschConfig;
import com.jicent.jetrun.Constants;
import com.jicent.jetrun.R;
import com.jicent.jetrun.utils.TransactionUtils;

public class RegisterActivity extends Activity implements OnClickListener{


    private Button bt_zhuce;
    private Button bt_denglu;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.layout_register);

        initView();
        initData();


    }

    private void initData() {

    }

    private void initView() {
        bt_denglu = findViewById(R.id.bt_denglu);
        bt_denglu.setOnClickListener(this);
        bt_zhuce = findViewById(R.id.bt_zhuce);
        bt_zhuce.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.bt_denglu:
                Toast.makeText(this,"ontouch bt_denglu",Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_zhuce:
                Toast.makeText(this,"ontouch bt_zhuce",Toast.LENGTH_SHORT).show();
                registerGameUser("11888",Constants.registerSecret[29],"");
                break;
        }
    }

    private static void registerGameUser(String Invitation,String registerSecret,String secondSecret) {
        Log.d("shuai---> " ,"register begin");
        Object[] args = new Object[1];
        args[0] = Invitation;
        String ret = TransactionUtils.postGameTransactionMethod(registerSecret, secondSecret, "registerUser", args);
        System.out.println("ret = " + ret);
        Log.d("shuai---> " ,"ret is " + ret);
    }
}
