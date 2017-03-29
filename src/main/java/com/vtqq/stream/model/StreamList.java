package com.vtqq.stream.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StreamList {

    @JsonProperty("_total")
    private long total;

    @JsonProperty("streams")
    private List<Stream> streams;

}
