package com.wq.businessdirectory;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bilibili.boxing.Boxing;
import com.bilibili.boxing.model.config.BoxingConfig;
import com.bilibili.boxing_impl.ui.BoxingActivity;
import com.bilibili.boxing_impl.ui.BoxingViewActivity;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.tencent.bugly.beta.Beta;
import com.wq.businessdirectory.common.adapter.ViewPagerFragmentAdapter;
import com.wq.businessdirectory.common.mode.BottomTab;
import com.wq.businessdirectory.common.utils.BoxingUtils;
import com.wq.businessdirectory.test.PhoneFragment;
import com.wq.businessdirectory.test.SmsFragment;
import com.wq.businessdirectory.test.TestFragment;
import com.wq.support.utils.ToastUtil;
import com.wq.support.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 主体
 */
public class MainActivity extends AppCompatActivity {
    private List<Fragment> baseFragments;
    ViewPagerFragmentAdapter mVPAdapter;
    private String[] mTitles = {"首页", "收藏", "个人"};
    private int[] mIconUnselectIds = {
            R.drawable.ic_home_indigo_200_24dp, R.drawable.ic_favorite_indigo_200_24dp,
            R.drawable.ic_person_indigo_200_24dp};
    private int[] mIconSelectIds = {
            R.drawable.ic_home_indigo_500_24dp, R.drawable.ic_favorite_indigo_400_24dp,
            R.drawable.ic_person_indigo_400_24dp};
    @Bind(R.id.vp_main_center)
    ViewPager mViewPager;
    @Bind(R.id.tab_layout)
    CommonTabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Beta.checkUpgrade(false, false);
        baseFragments = new ArrayList<>();
//        baseFragments.add(HomeFragment.newInstance());
//        baseFragments.add(CollectFragment.newInstance());
        baseFragments.add(TestFragment.newInstance());
        baseFragments.add(SmsFragment.newInstance());
        baseFragments.add(PhoneFragment.newInstance());
        mVPAdapter = new ViewPagerFragmentAdapter(this.getSupportFragmentManager());
        mVPAdapter.setFragments(baseFragments);
        mViewPager.setAdapter(mVPAdapter);
        setTabData(mTabLayout, mTitles, mIconSelectIds, mIconUnselectIds);
        bindTabEvent(mTabLayout, mViewPager);
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BoxingUtils.startBoxing(MainActivity.this,9);
//                ((RecycleViewFragment) mCurrFragment).smoothScrollToTop();
            }
        });
        mTabLayout.showDot(0);
        mTabLayout.showMsg(1, (int) (Math.random() * 100));
    }

    /**
     * 设置标签视图内容,
     *
     * @param tabLayout
     * @param titles        文字
     * @param selDrawable   图标/选中
     * @param unselDrawable 图标/未选中
     */
    public void setTabData(CommonTabLayout tabLayout, String titles[], int selDrawable[], int unselDrawable[]) {
        ArrayList<CustomTabEntity> customTabEntities = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            customTabEntities.add(BottomTab.createTab(titles[i], selDrawable[i], unselDrawable[i]));
        }
        tabLayout.setTabData(customTabEntities);
    }

    /**
     * 绑定标签视图和viewPager 事件
     *
     * @param tabLayout
     * @param viewPager
     */
    void bindTabEvent(final CommonTabLayout tabLayout, final ViewPager viewPager) {
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
}
