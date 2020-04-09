package com.example.instagram.Models;

import androidx.annotation.NonNull;

public class UserDetails {

    private String follower;
    private String following;
    private String post;
    private String profile_pic;
    private String biography;

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    private String webSite;

    @NonNull
    @Override
    public String toString() {
        return "UserDetails{" +
                "follower='" + follower + '\'' +
                ", following='" + following + '\'' +
                ", post='" + post + '\'' +
                ", profile_pic='" + profile_pic + '\'' +
                ", biography='" + biography + '\'' +
                ", webSite='" + webSite + '\'' +
                '}';
    }

    public UserDetails(String follower, String following, String post, String profile_pic, String biography, String webSite) {
        this.follower = follower;
        this.following = following;
        this.post = post;
        this.profile_pic = profile_pic;
        this.biography = biography;
        this.webSite = webSite;
    }

    public UserDetails() {
    }
}
