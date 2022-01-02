package com.rednit.tinder4j.entities;

import com.rednit.tinder4j.internal.requests.DataObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Update {

    private final List<String> newMatches;
    private final List<NewMessage> newMessages;
    private final DataObject response;

    @SuppressWarnings("unchecked")
    public Update(DataObject update) {
        newMatches = new ArrayList<>();
        newMessages = new ArrayList<>();
        response = update;
        update.getArray("matches").forEach(matchObject -> {
            DataObject match = new DataObject((Map<String, Object>) matchObject);
            boolean seen = true;
            if (match.hasKey("seen")) {
                seen = match.getObject("seen").getBoolean("match_seen");
            }
            if (seen) {
                match.getArray("messages").forEach(messageObject -> {
                    DataObject message = new DataObject((Map<String, Object>) messageObject);
                    newMessages.add(new NewMessage(message.getString("_id"), message.getString("match_id")));
                });
            }
        });
    }

    public List<String> getNewMatches() {
        return newMatches;
    }

    public List<NewMessage> getNewMessages() {
        return newMessages;
    }

    public DataObject getResponse() {
        return response;
    }

    public static class NewMessage {

        private final String matchId;
        private final String messageId;

        public NewMessage(String matchId, String messageId) {
            this.matchId = matchId;
            this.messageId = messageId;
        }

        public String getMatchId() {
            return matchId;
        }

        public String getMessageId() {
            return messageId;
        }

        @Override
        public String toString() {
            return String.format("NewMessage{matchId: %s / messageId: %s}", matchId, messageId);
        }
    }

    @Override
    public String toString() {
        return String.format("Update{newMatches: %d / newMessages: %d}", newMatches.size(), newMessages.size());
    }
}
