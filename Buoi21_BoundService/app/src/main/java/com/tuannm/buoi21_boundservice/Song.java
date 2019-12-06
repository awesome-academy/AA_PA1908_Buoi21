package com.tuannm.buoi21_boundservice;

public class Song {
    private String mTitle;
    private int mResId;

    public Song() {
    }

    public Song(String title, int resId) {
        mTitle = title;
        mResId = resId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getResId() {
        return mResId;
    }

    public void setResId(int resId) {
        mResId = resId;
    }
}
