package com.rednit.tinder4j.api.entities;

import com.rednit.tinder4j.requests.DataObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Describes an update sent by Tinder containing information about new matches and messages.
 *
 * @author Kaktushose
 * @version 1.0.0
 * @since 1.0.0
 */
public class Update {

    private final List<String> newMatches;
    private final List<NewMessage> newMessages;
    private final DataObject response;

    /**
     * Constructs a new Update.
     *
     * @param update the {@link DataObject} to construct the Update from
     */
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

    /**
     * A possibly empty {@link List} of new matches.
     *
     * @return a possibly empty {@link List} of new matches
     */
    public List<String> getNewMatches() {
        return newMatches;
    }

    /**
     * A possibly empty {@link List} of {@link NewMessage new messages}.
     *
     * @return a possibly empty {@link List} of {@link NewMessage new messages}
     */
    public List<NewMessage> getNewMessages() {
        return newMessages;
    }

    /**
     * Gets the raw {@link DataObject} holding the response. This class doesn't wrap the complete update object, thus
     * this method can be used to access the other fields.
     *
     * @return the raw {@link DataObject} holding the response
     */
    public DataObject getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return String.format("Update{newMatches: %d / newMessages: %d}", newMatches.size(), newMessages.size());
    }

    /**
     * Container for a new message holding the message and match id.
     *
     * @author Kaktushose
     * @version 1.0.0
     * @since 1.0.0
     */
    public static class NewMessage {

        private final String matchId;
        private final String messageId;

        /**
         * Constructs a new NewMessage.
         *
         * @param matchId   corresponding the match id
         * @param messageId corresponding the message id
         */
        public NewMessage(String matchId, String messageId) {
            this.matchId = matchId;
            this.messageId = messageId;
        }

        /**
         * Gets the message id.
         *
         * @return the message id
         */
        public String getMatchId() {
            return matchId;
        }

        /**
         * Gets the message id.
         *
         * @return the message id
         */
        public String getMessageId() {
            return messageId;
        }

        @Override
        public String toString() {
            return String.format("NewMessage{matchId: %s / messageId: %s}", matchId, messageId);
        }
    }
}
