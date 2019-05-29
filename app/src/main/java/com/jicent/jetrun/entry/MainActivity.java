package com.jicent.jetrun.entry;

import java.util.regex.Pattern;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.math.MathUtils;
import com.jicent.jetrun.R;
import com.jicent.jetrun.data.StaticVariable;
import com.jicent.jetrun.utils.HttpUtil;
import com.jicent.jetrun.utils.SPUtil;

public class MainActivity extends AndroidApplication implements OnClickListener{
	private GameMain gameMain;
	private ProgressDialog progressDialog;
	private Dialog dialog;
	private EditText et;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config=new AndroidApplicationConfiguration();
		config.useWakelock=true;
		config.maxSimultaneousSounds=10;
		gameMain=new GameMain(this);
		initialize(gameMain, config);
		
		handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0: 
					dialog = new Dialog(MainActivity.this, R.style.dialog);
					dialog.setContentView(R.layout.name_input_dialog);
					dialog.show();
					Button okBtn = (Button) dialog.findViewById(R.id.okBtn);
					okBtn.setOnClickListener(
							MainActivity.this);
					dialog.findViewById(R.id.randomBtn).setOnClickListener(
							MainActivity.this);
					dialog.findViewById(R.id.cancelBtn).setOnClickListener(
							MainActivity.this);
					et = (EditText) dialog.findViewById(R.id.name);
					break;
				case 1:
					progressDialog.dismiss();
					Toast.makeText(MainActivity.this, "该名字已经有人使用了，请换其他名字吧！",
							Toast.LENGTH_SHORT).show();
					break;
				case 2:
					progressDialog.dismiss();
					SPUtil.commit(gameMain.getSp(), "name", StaticVariable.name);
					dialog.dismiss();
					StaticVariable.nameOk=true;
					break;
				case 3:
					progressDialog.dismiss();
					Toast.makeText(MainActivity.this, "获取数据失败，请检查网络设置！",
							Toast.LENGTH_SHORT).show();
					break;
				case 4:
					progressDialog.dismiss();
					Toast.makeText(MainActivity.this, "该名字中有敏感词汇或特殊符号，请换其他名字吧！",
							Toast.LENGTH_SHORT).show();
					break;
				case 5:
					Toast.makeText(MainActivity.this, "获取数据失败，请检查网络设置！",
							Toast.LENGTH_SHORT).show();
					break;
				case 6:
					startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://m.play.cn")));
					break;
				}
			}
			
		};
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.okBtn:
			StaticVariable.name = et.getText().toString();
			if (StaticVariable.name.trim().length() == 0) {
				Toast.makeText(MainActivity.this, "请输入角色名称", Toast.LENGTH_SHORT)
						.show();
			} else if (Pattern.compile("\\s").matcher(StaticVariable.name).find()) {
				Toast.makeText(MainActivity.this, "角色名称不能包含空格字符，请重新输入",
						Toast.LENGTH_SHORT).show();
			} else {
				progressDialog = ProgressDialog.show(this, "昵称检查", "正在检查……");
				new Thread() {
					public void run() {
						Message msg = new Message();
						String resultStr = HttpUtil.nameCheck(
								MainActivity.this, StaticVariable.name);
						if (resultStr.equals("repeat")) {
							msg.what = 1;
						} else if (resultStr.equals("use")) {
							msg.what = 2;
						} else if (resultStr.equals("sensitive")) {
							msg.what = 4;
						} else {
							msg.what = 3;

						}
						handler.sendMessage(msg);
					}
				}.start();
			}
			break;
		case R.id.randomBtn:
			et.setText("Baby" + MathUtils.random(9999999));
			break;
		case R.id.cancelBtn:
			dialog.dismiss();
			break;
		}
	}
}
