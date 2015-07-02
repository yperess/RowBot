package com.concept2.api.rowbot.model;

public interface RowBotActivity {

    void setActionBarTitle(int resId);

    void setUserName(String userName);

    String getUserName();

    void setClubName(String clubName);

    String getClubName();

    void setSharingData(boolean sharingData);

    boolean isSharingData();

    void onNewMainFragment(Class fragmentClass);
}
