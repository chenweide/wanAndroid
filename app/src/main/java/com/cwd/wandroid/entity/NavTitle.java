package com.cwd.wandroid.entity;

import java.util.List;

public class NavTitle {

    private int cid;
    private String name;
    private List<NavInfo> articles;

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<NavInfo> getArticles() {
        return articles;
    }

    public void setArticles(List<NavInfo> articles) {
        this.articles = articles;
    }
}
