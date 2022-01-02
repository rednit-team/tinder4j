package com.rednit.tinder4j.entities.message;

import com.rednit.tinder4j.entities.socials.Spotify;
import com.rednit.tinder4j.internal.requests.DataObject;

/**
 * Top level representation of a message attachment.
 *
 * @author Kaktushose
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class Attachment {

    private final AttachmentType type;

    protected Attachment(AttachmentType type) {
        this.type = type;
    }

    /**
     * Gets the attachment type
     *
     * @return the {@link AttachmentType}
     */
    public AttachmentType getType() {
        return type;
    }

    /**
     * An enum holding the different attachment types.
     *
     * @author Kaktushose
     * @version 1.0.0
     * @since 1.0.0
     */
    public enum AttachmentType {
        /**
         * a Tenor GIF
         */
        GIF,

        /**
         * a contact card holding account information for various social media platforms
         */
        CONTACT_CARD,

        /**
         * a Spotify song
         *
         * @see SongAttachment
         */
        SONG,

        /**
         * a Tenor GIF
         *
         * @see AttachmentType#GIF
         */
        STICKER,

        /**
         * no attachment
         */
        NONE
    }

    /**
     * A GIF attachment.
     *
     * @author Kaktushose
     * @version 1.0.0
     * @see com.rednit.tinder4j.entities.message.Attachment Attachment
     * @since 1.0.0
     */
    public static class GIFAttachment extends Attachment {

        private final String url;

        protected GIFAttachment(DataObject attachment) {
            super(AttachmentType.GIF);
            url = attachment.getString("fixed_height");
        }

        /**
         * Gets the url of the GIF.
         *
         * @return the url of the GIF
         */
        public String getUrl() {
            return url;
        }
    }

    /**
     * A contact card attachment.
     *
     * @author Kaktushose
     * @version 1.0.0
     * @see com.rednit.tinder4j.entities.message.Attachment Attachment
     * @since 1.0.0
     */
    public static class ContactCardAttachment extends Attachment {

        private final String contactId;
        private final String contactType;
        private final String url;

        protected ContactCardAttachment(DataObject attachment) {
            super(AttachmentType.CONTACT_CARD);
            contactId = attachment.getString("contact_id");
            contactType = attachment.getString("contact_type");
            url = attachment.getString("deeplink");
        }

        /**
         * Gets the contact id, e.g. the phone number, Instagram name, etc.
         *
         * @return the contact id
         */
        public String getContactId() {
            return contactId;
        }

        /**
         * Gets the type of contact card, e.g. phone, Instagram, Facebook, etc.
         *
         * @return the type of contact card
         */
        public String getContactType() {
            return contactType;
        }

        /**
         * If present, gets the url to the social media profile.
         *
         * @return the url to the social media profile
         */
        public String getUrl() {
            return url;
        }
    }

    /**
     * A song attachment.
     *
     * @author Kaktushose
     * @version 1.0.0
     * @see com.rednit.tinder4j.entities.message.Attachment Attachment
     * @see com.rednit.tinder4j.entities.socials.Spotify.SpotifySongAttachment SpotifySongAttachment
     * @since 1.0.0
     */
    public static class SongAttachment extends Attachment {

        private final Spotify.SpotifySongAttachment song;

        protected SongAttachment(DataObject attachment) {
            super(AttachmentType.SONG);
            song = new Spotify.SpotifySongAttachment(attachment.getObject("song"));
        }

        /**
         * Gets the {@link com.rednit.tinder4j.entities.socials.Spotify.SpotifySongAttachment SpotifySongAttachment}.
         *
         * @return the {@link com.rednit.tinder4j.entities.socials.Spotify.SpotifySongAttachment SpotifySongAttachment}
         */
        public Spotify.SpotifySongAttachment getSong() {
            return song;
        }
    }
}
