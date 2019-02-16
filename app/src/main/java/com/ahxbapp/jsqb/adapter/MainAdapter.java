package com.ahxbapp.jsqb.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Admin on 2016/8/9.
 */
public class MainAdapter extends FragmentPagerAdapter {
    List<Fragment> fragmentList;
    FragmentManager fragmentManager;

    public MainAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
        this.fragmentManager = fm;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        Fragment fragment = fragmentList.get(position);
//        if (!fragment.isAdded()) { // 如果fragment还没有added
//            FragmentTransaction ft = fragmentManager.beginTransaction();
//            ft.add(fragment, fragment.getClass().getSimpleName());
//            ft.commit();
//            /**
//             * 在用FragmentTransaction.commit()方法提交FragmentTransaction对象后
//             * 会在进程的主线程中，用异步的方式来执行。
//             * 如果想要立即执行这个等待中的操作，就要调用这个方法（只能在主线程中调用）。
//             * 要注意的是，所有的回调和相关的行为都会在这个调用中被执行完成，因此要仔细确认这个方法的调用位置。
//             */
//            fragmentManager.executePendingTransactions();
//        }
//
//        if (fragment.getView().getParent() == null) {
//            container.addView(fragment.getView()); // 为viewpager增加布局
//        }
//        return fragment.getView();
//
//    }
}