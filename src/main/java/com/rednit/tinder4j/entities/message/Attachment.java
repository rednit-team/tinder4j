package com.rednit.tinder4j.entities.message;

import com.rednit.tinder4j.entities.socials.Spotify;
import com.rednit.tinder4j.internal.requests.DataObject;

public abstract class Attachment {

    private final AttachmentType type;

    protected Attachment(AttachmentType type) {
        this.type = type;
    }

    public AttachmentType getType() {
        return type;
    }

    public static class GIFAttachment extends Attachment {

        private final String url;

        protected GIFAttachment(DataObject attachment) {
            super(AttachmentType.GIF);
            url = attachment.getString("fixed_height");
        }

        public String getUrl() {
            return url;
        }
    }

    public static class ContactCardAttachment extends Attachment {

        private final String contact_id;
        private final String contact_type;
        private final String url;

        protected ContactCardAttachment(DataObject attachment) {
            super(AttachmentType.CONTACT_CARD);
            contact_id = attachment.getString("contact_id");
            contact_type = attachment.getString("contact_type");
            url = attachment.getString("deeplink");
        }

        public String getContact_id() {
            return contact_id;
        }

        public String getContact_type() {
            return contact_type;
        }

        public String getUrl() {
            return url;
        }
    }

    public static class SongAttachment extends Attachment {

        private final Spotify.SpotifySongAttachment song;

        protected SongAttachment(DataObject attachment) {
            super(AttachmentType.SONG);
            song = new Spotify.SpotifySongAttachment(attachment.getObject("song"));
        }

        public Spotify.SpotifySongAttachment getSong() {
            return song;
        }
    }

    public enum AttachmentType {
        GIF,
        CONTACT_CARD,
        SONG,
        STICKER,
        NONE
    }
}
