package com.wq.businessdirectory;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.flyco.tablayout.SlidingTabLayout;
import com.tencent.bugly.beta.Beta;
import com.wq.businessdirectory.common.adapter.ViewPagerFragmentAdapter;
import com.wq.businessdirectory.common.ui.RecycleViewFragment;
import com.wq.businessdirectory.test.PhoneFragment;
import com.wq.businessdirectory.test.SmsFragment;
import com.wq.businessdirectory.test.TestFragment;
import com.wq.support.utils.ToastUtil;
import com.wq.support.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 主体
 */
public class MainActivity extends AppCompatActivity {
    @Bind(R.id.vp_main_center)
    ViewPager vpMainCenter;
    @Bind((R.id.fab))
    FloatingActionButton mFab;
    private List<Fragment> baseFragments;
    private RecycleViewFragment homeFragment;
    ViewPagerFragmentAdapter mainVPageAdapter;
    Fragment mCurrFragment;
    @Bind((R.id.sliding_tabs))
    SlidingTabLayout sliding_tabs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Beta.checkUpgrade(true,true);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RecycleViewFragment) mCurrFragment).smoothScrollToTop();
            }
        });
        mFab.setBackgroundColor(Color.parseColor("#5C6BC0"));
        baseFragments = new ArrayList<>();
//        baseFragments.add(HomeFragment.newInstance());
//        baseFragments.add(CollectFragment.newInstance());
        baseFragments.add(TestFragment.newInstance());
        baseFragments.add(SmsFragment.newInstance());
        baseFragments.add(PhoneFragment.newInstance());

        mainVPageAdapter = new ViewPagerFragmentAdapter(this.getSupportFragmentManager());
        mainVPageAdapter.setFragments(baseFragments);
        mainVPageAdapter.addTitle("测试");
        mainVPageAdapter.addTitle("短信");
        mainVPageAdapter.addTitle("电话");
        vpMainCenter.setAdapter(mainVPageAdapter);
        sliding_tabs.setViewPager(vpMainCenter);
    }

    @Override
    public void onBackPressed() {
        if (Utils.isQuck(1000)) {
            //moveTaskToBack(true);//假退出
            super.onBackPressed();
        } else {
            ToastUtil.shortM("再按一次退出程序");
        }
    }
//    public void setSelected(int currentIndex) {
//        if(vpMainCenter.getCurrentItem()!=currentIndex) {
//            vpMainCenter.setCurrentItem(currentIndex);//false 不显示翻页动画效果
//        }
//        mCurrFragment= baseFragments.get(currentIndex);
//        for (int i = 0; i < linMainButtom.getChildCount(); i++) {
//            linMainButtom.getChildAt(i).setSelected(currentIndex == i);
//        }
//    }
//    @OnClick({R.id.lin1, R.id.lin2, R.id.lin3})
//    public void onClick(View view) {
//        int currentIndex=0;
//        switch (view.getId()) {
//            case R.id.lin1:
//                currentIndex=0;
//                break;
//            case R.id.lin2:
//                currentIndex=1;
//                break;
//            case R.id.lin3:
//                currentIndex=2;
//                break;
//        }
//        setSelected(currentIndex);
//    }
}
