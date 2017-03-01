package usst.knowmorse.activity;

import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import usst.knowmorse.R;
import usst.knowmorse.fragment.AbouFragment;
import usst.knowmorse.fragment.DecodeConfigFragment;
import usst.knowmorse.fragment.DecodeFragment;
import usst.knowmorse.fragment.EncodeFragment;
import usst.knowmorse.fragment.MapFragment;

/**
 * 功能：程序入口
 * @author cunbao   <1063994955@qq.com>
 * @version  v1.0
 * @time    20160508
 * @修改时间
 * @修改人
 */
public class MainActivity extends AppCompatActivity {

	public static final int CUSTOM_TOP_BAR_COLOR_API = 21;

	ViewPager viewPager;
	Pager pager;
	ResourcesCompat rc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//创建资源
		rc = new ResourcesCompat();

		//创建FRAGMENT  添加tab
		pager = new Pager(getSupportFragmentManager());

		//得到VIEWPAGER
		viewPager = (ViewPager)findViewById(R.id.view_pager);
		viewPager.setAdapter(pager);

		//把TAB 添加到TabLayout上
		TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_layout);

		//设置tab显示的内容
		tabLayout.setupWithViewPager(viewPager);
		tabLayout.getTabAt(0).setText("闪光灯编码");
		tabLayout.getTabAt(3).setText("闪光灯解码");
		tabLayout.getTabAt(2).setText("参数设置 ");
		tabLayout.getTabAt(1).setText("对照表 ");
		tabLayout.getTabAt(4).setText("关于我们");
		viewPager.setOffscreenPageLimit(0);
		//添加导航条颜色
		if(Build.VERSION.SDK_INT >= CUSTOM_TOP_BAR_COLOR_API){
			Window window = getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(rc.getColor(getResources(), R.color.bold, null));
			window.setNavigationBarColor(rc.getColor(getResources(), R.color.dull, null));
		}

	}

	class Pager extends FragmentStatePagerAdapter {

		public static final int FRAGMENTS = 5;

		public Pager(FragmentManager fm){
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch(position){
				case 0:
					return new EncodeFragment();
				case 3:
					return new DecodeFragment();
				case 2:
					return new DecodeConfigFragment();
				case 1:
					return new MapFragment();
				case 4:
					return new AbouFragment();


				default:

					return null; //Error, item position out of bounds
			}
		}

		@Override
		public int getCount() {
			return FRAGMENTS;
		}
	}
}


