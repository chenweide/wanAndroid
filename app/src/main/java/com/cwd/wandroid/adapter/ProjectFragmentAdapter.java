package com.cwd.wandroid.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.cwd.wandroid.entity.ProjectCategory;
import com.cwd.wandroid.ui.fragment.ProjectListFragment;

import java.util.List;

public class ProjectFragmentAdapter extends FragmentStatePagerAdapter {

    private List<ProjectCategory> categoryList;

    public ProjectFragmentAdapter(FragmentManager fm, List<ProjectCategory> categoryList) {
        super(fm);
        this.categoryList = categoryList;
    }

    @Override
    public Fragment getItem(int position) {
        return ProjectListFragment.newInstance(categoryList.get(position).getId());
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return categoryList.get(position).getName();
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }
}
