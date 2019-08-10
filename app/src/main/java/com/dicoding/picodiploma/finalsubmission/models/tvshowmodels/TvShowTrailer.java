package com.dicoding.picodiploma.finalsubmission.models.tvshowmodels;

import com.google.gson.annotations.SerializedName;

public class TvShowTrailer {

    @SerializedName("site")
    private String site;

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private String id;

    @SerializedName("key")
    private String key;

    public void setSite(String site) {
        this.site = site;
    }

    public String getSite() {
        return site;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return
                "TvShowTrailer{" +
                        "site = '" + site + '\'' +
                        ",name = '" + name + '\'' +
                        ",id = '" + id + '\'' +
                        ",key = '" + key + '\'' +
                        "}";
    }
}