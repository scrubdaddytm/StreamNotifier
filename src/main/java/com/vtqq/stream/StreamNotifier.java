package com.vtqq.stream;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import com.vtqq.stream.api.TwitchApi;
import com.vtqq.stream.model.Stream;
import com.vtqq.stream.model.StreamList;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamNotifier implements RequestStreamHandler {

    private static final String SLACK_AUTH_TOKEN = System.getenv("SLACK_AUTH_TOKEN");

    private static final long LIVE_WINDOW = (DateTimeConstants.MILLIS_PER_MINUTE * 15);
    private static final String SLACK_CHANNEL_NAME = "strims";

    private TwitchApi twitchApi;

    public void init() {
        twitchApi = new TwitchApi();
    }

    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        LambdaLogger logger = context.getLogger();
        init();
        checkStreams(logger);
    }

    private void checkStreams(LambdaLogger logger) {
        try {
            StreamList liveStreams = twitchApi.getLiveStreams();

            if (liveStreams.getStreams().isEmpty()) {
                logger.log("no one's live :(\n");
                return;
            }

            SlackSession session = SlackSessionFactory.createWebSocketSlackSession(SLACK_AUTH_TOKEN);
            session.connect();

            SlackChannel channel = session.findChannelByName(SLACK_CHANNEL_NAME);

            for (Stream stream : liveStreams.getStreams()) {
                String displayName = stream.getChannel().getDisplayName();
                logger.log(String.format("checkout this motha fucka : %s\n", displayName));

                long streamLength = DateTime.now().minus(stream.getCreatedAt().getMillis()).getMillis();
                logger.log(String.format("%s started streaming %d millis ago\n", displayName, streamLength));

                if (streamLength > LIVE_WINDOW) {
                    logger.log(String.format("%s has already notified the channel\n", displayName));
                    continue;
                }
                session.sendMessage(channel, getMessage(stream));
            }
            session.disconnect();

        } catch (Exception e) {
            logger.log(String.format("ERROR T_T : %s\n", e.getMessage()));
        }
    }

    private String getMessage(Stream stream) {
        return String.format("*%s* is streaming *%s* : _%s_\n%s",
                stream.getChannel().getDisplayName(),
                stream.getGame(),
                stream.getChannel().getStatus(),
                stream.getChannel().getUrl());
    }

}
