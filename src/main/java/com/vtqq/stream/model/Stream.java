package com.vtqq.stream.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.joda.time.DateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Stream {

    @JsonProperty("_id")
    private String id;

    @JsonProperty("game")
    private String game;

    @JsonProperty("community_id")
    private String communityId;

    @JsonProperty("viewers")
    private long viewers;

    @JsonProperty("video_height")
    private long videoHeight;

    @JsonProperty("average_fps")
    private double average_fps;

    @JsonProperty("delay")
    private String delay;

    @JsonProperty("created_at")
    private DateTime createdAt;

    @JsonProperty("is_playlist")
    private boolean isPlaylist;

    @JsonProperty("preview")
    private Preview preview;

    @JsonProperty("channel")
    private Channel channel;

}
