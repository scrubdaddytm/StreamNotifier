package com.vtqq.stream.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.joda.time.DateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Channel {

    @JsonProperty("_id")
    private String id;

    @JsonProperty("broadcaster_language")
    private String broadcasterLanguage;

    @JsonProperty("created_at")
    private DateTime createdAt;

    @JsonProperty("display_name")
    private String displayName;

    @JsonProperty("followers")
    private long followers;

    @JsonProperty("game")
    private String game;

    @JsonProperty("language")
    private String language;

    @JsonProperty("logo")
    private String logoUrl;

    @JsonProperty("mature")
    private boolean mature;

    @JsonProperty("name")
    private String name;

    @JsonProperty("partner")
    private boolean partner;

    @JsonProperty("profile_banner")
    private String profileBannerUrl;

    @JsonProperty("profile_banner_background_color")
    private String profileBannerBackgroundColor;

    @JsonProperty("status")
    private String status;

    @JsonProperty("updated_at")
    private DateTime updatedAt;

    @JsonProperty("url")
    private String url;

    @JsonProperty("video_banner")
    private String videoBannerUrl;

    @JsonProperty("views")
    private long views;

}
