package com.example.firebaseauthgoogle;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Upload{

    public String name;
    public String url;
    public String title;
    public String desc;
    public String ctgory;
    public String video_url;
    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Upload() {
    }

    public Upload(String name, String url ,String title,String desc,String ctgory,String video_url) {
        this.name = name;
        this.url= url;
        this.title = title;
        this.desc = desc;
        this.ctgory = ctgory;
        this.video_url = video_url;
    }

    public Upload(String name, String url) {
        this.name = name;
        this.url= url;

    }

    public Upload(String name, String url ,String video_url) {
        this.name = name;
        this.url= url;
        this.video_url = video_url;
    }
    public Upload(String title) {
       this.title = title;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
    public String getvideo_url() {
        return video_url;
    }

    public String getTitle() {
        return title;
    }
    public String getDesc() {
        return desc;
    }
    public String getCategory() {
        return ctgory;
    }
}
