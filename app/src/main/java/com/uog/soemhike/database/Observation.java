package com.uog.soemhike.database;

public class Observation {

    public static final String O_ID = "OID";
    public static final String O_TITLE = "oTitle";
    public static final String O_YEAR = "oYear";
    public static final String O_CURRENT_TIME = "currentTime";

    public static final String AVATAR_FILE_PATH = "avatarFilePath";

    public static final String O_HIKEID = "hikeId";

    //

    private Integer id;
    private String title;
    private String year;
    private String currentTime;

    public String avatarFilePath;
    private int user_id;

    public Observation(String title,String year, String currentTime, String avatarFilePath, int user_id) {
        this.title = title;
        this.year = year;
        this.currentTime = currentTime;
        this.avatarFilePath = avatarFilePath;
        this.user_id = user_id;
    }

    public Observation (int id, String title, String year, String avatarFilePath){
        this.id = id;
        this.title = title;
        this.year = year;
        this.avatarFilePath = avatarFilePath;

    }
    public Observation(Integer id, String title, String year, String currentTime, String avatarFilePath, int user_id) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.currentTime = currentTime;
        this.avatarFilePath = avatarFilePath;
        this.user_id = user_id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

//
    public String getAvatarFilePath() {
        return avatarFilePath;
    }

    public void setAvatarFilePath(String avatarFilePath) {
        this.avatarFilePath = avatarFilePath;
    }
//

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }
}
