package com.cwd.wandroid.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.cwd.wandroid.entity.ProjectCategory;
import com.cwd.wandroid.entity.SystemDetail;
import com.cwd.wandroid.ui.fragment.ProjectListFragment;

import java.util.List;

public class SystemDetailFragmentAdapter extends FragmentStatePagerAdapter {

    private List<ProjectCategory> systemDetailList;

    public SystemDetailFragmentAdapter(FragmentManager fm, List<ProjectCategory> systemDetailList) {
        super(fm);
        this.systemDetailList = systemDetailList;
    }

    @Override
    public Fragment getItem(int position) {
        return ProjectListFragment.newInstance(systemDetailList.get(position).getId(),true);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return systemDetailList.get(position).getName();
    }

    @Override
    public int getCount() {
        return systemDetailList.size();
    }
}
