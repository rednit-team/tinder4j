package com.rednit.tinder4j.api.entities.message;

import com.rednit.tinder4j.api.TinderClient;
import com.rednit.tinder4j.api.entities.Entity;
import com.rednit.tinder4j.api.entities.message.Attachment.AttachmentType;
import com.rednit.tinder4j.requests.DataObject;

import java.util.Optional;

/**
 * Represents a message inside a Tinder match.
 *
 * @author Kaktushose
 * @version 1.0.0
 * @since 1.0.0
 */
public class Message extends Entity {

    private final String matchId;
    private final String sentDate;
    private final String content;
    private final String authorId;
    private final String recipientId;
    private final AttachmentType attachmentType;
    private final Attachment attachment;

    /**
     * Constructs a new Message.
     *
     * @param message the {@link DataObject} to construct the message from
     * @param client  the corresponding {@link TinderClient} instance
     */
    public Message(DataObject message, TinderClient client) {
        super(message, client);
        matchId = message.getString("match_id");

        if (message.get("sent_date") instanceof String) {
            sentDate = message.getString("sent_date");
        } else {
            sentDate = message.getString("timestamp");
        }

        content = message.getString("message");
        authorId = message.getString("from");
        recipientId = message.getString("to");

        if (message.hasKey("type")) {
            attachmentType = AttachmentType.valueOf(message.getString("type").toUpperCase());
        } else {
            attachmentType = AttachmentType.NONE;
        }

        switch (attachmentType) {
            case GIF:
            case STICKER:
                attachment = new Attachment.GIFAttachment(message);
                break;
            case CONTACT_CARD:
                attachment = new Attachment.ContactCardAttachment(message);
                break;
            case SONG:
                attachment = new Attachment.SongAttachment(message);
                break;
            default:
                attachment = null;
                break;
        }
    }

    /**
     * Gets the match id.
     *
     * @return the match id
     */
    public String getMatchId() {
        return matchId;
    }

    /**
     * Gets the date the message was sent.
     *
     * @return the date the message was sent
     */
    public String getSentDate() {
        return sentDate;
    }

    /**
     * Gets the message content.
     *
     * @return the message content
     */
    public String getContent() {
        return content;
    }

    /**
     * Gets the author id.
     *
     * @return the author id
     */
    public String getAuthorId() {
        return authorId;
    }

    /**
     * Gets the recipient id.
     *
     * @return the recipient id
     */
    public String getRecipientId() {
        return recipientId;
    }

    /**
     * Gets the {@link AttachmentType AttachmentType}. Returns {@link AttachmentType#NONE AttachmentType.NONE}
     * if no attachment was sent.
     *
     * @return the {@link AttachmentType}
     */
    public AttachmentType getAttachmentType() {
        return attachmentType;
    }

    /**
     * Gets an {@link Optional} holding the {@link Attachment}
     *
     * @return an {@link Optional} holding the {@link Attachment}
     */
    public Optional<Attachment> getAttachment() {
        return Optional.ofNullable(attachment);
    }

    @Override
    public String toString() {
        return String.format("Message{id: %s / content: %s}", getId(), content);
    }
}
