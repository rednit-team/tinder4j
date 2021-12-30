package com.rednit.tinder4j.entities;

import com.rednit.tinder4j.TinderClient;
import com.rednit.tinder4j.internal.requests.DataObject;
import com.rednit.tinder4j.entities.Attachment.AttachmentType;

import java.util.Optional;

public class Message extends Entity {

    private final String matchId;
    private final String sentDate;
    private final String content;
    private final String authorId;
    private final String recipientId;
    private final AttachmentType attachmentType;
    private final Attachment attachment;

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

    public String getMatchId() {
        return matchId;
    }

    public String getSentDate() {
        return sentDate;
    }

    public String getContent() {
        return content;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public AttachmentType getAttachmentType() {
        return attachmentType;
    }

    public Optional<Attachment> getAttachment() {
        return Optional.ofNullable(attachment);
    }
}
