package com.vtqq.stream.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.vtqq.stream.model.Stream;
import com.vtqq.stream.model.StreamList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

public class TwitchApi {

    private static final String FOLLOWING_URL = "https://api.twitch.tv/kraken/streams/followed?scope=user_read";

    private static final String CLIENT_ID = System.getenv("CLIENT_ID");
    private static final String OAUTH = System.getenv("OAUTH");

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JodaModule());
    }

    public StreamList getLiveStreams() throws IOException, URISyntaxException {
        URI streamApi = new URI(FOLLOWING_URL);
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(streamApi);
        get.addHeader(new BasicHeader("Accept", "application/vnd.twitchtv.v5+json"));
        get.addHeader(new BasicHeader("Client-ID", CLIENT_ID));
        get.addHeader(new BasicHeader("Authorization", "OAuth " + OAUTH));

        HttpResponse response = client.execute(get);
        HttpEntity entity = response.getEntity();

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            throw new RuntimeException(String.format("got status code: %d\nmessage: %s", statusCode, response.getStatusLine().getReasonPhrase()));
        }

        String result = getApiResult(entity.getContent());
        try {
            return objectMapper.readValue(result, StreamList.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        StreamList emptyList = new StreamList();
        emptyList.setTotal(0);
        emptyList.setStreams(Collections.<Stream>emptyList());
        return emptyList;
    }

    private String getApiResult(InputStream content) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
        StringBuilder builder = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }

}
