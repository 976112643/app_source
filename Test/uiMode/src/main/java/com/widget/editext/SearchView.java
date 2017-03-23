package com.widget.editext;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wq.uicore.R;

public class SearchView extends LinearLayout  {
	TextView cancel;// 取消
	EditText search;// 输入框
	ImageView delet;// 删除

	public SearchView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}


	public SearchView(final Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.my_editext, this);
		delet=(ImageView) findViewById(R.id.delet);
		cancel=(TextView)findViewById(R.id.cancel);
		search=(EditText)findViewById(R.id.search);

//输入框输入处理
		search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub]

				if (arg0.length() == 0) {
					delet.setVisibility(View.GONE);
				} else {
					delet.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});	
		
	//关闭
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				((Activity) context).finish();
			}
		});

		
		//清空
		delet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				search.setText("");
			}
		});

		
	}


	
	/** 
     * 设置关闭文字
     */  
    public void setCleanText(String mg) {  
    	cancel.setText(mg);
    }  
  
    /** 
     * 设置输入框Hih 
     */  
    public void setEditextHint(String mg) {  
    	search.setHint(mg); 
    }  

    /** 
     * 获取输入框的值
     */  
    public String getEditextmg() {
		return search.getText().toString();  
    }  

    
}
