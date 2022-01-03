package com.rednit.tinder4j.api.cache;

import com.rednit.tinder4j.api.TinderClient;
import com.rednit.tinder4j.api.entities.Match;
import com.rednit.tinder4j.api.entities.message.Message;
import com.rednit.tinder4j.requests.async.CompletedRestAction;
import com.rednit.tinder4j.api.requests.RestAction;
import com.rednit.tinder4j.requests.async.RestActionImpl;
import com.rednit.tinder4j.requests.DataObject;
import com.rednit.tinder4j.requests.Route;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * A simple cache for {@link Match Matches}.
 *
 * @author Kaktushose
 * @version 1.0.0
 * @since 1.0.0
 */
public class MessageCacheView implements Iterable<Message> {

    private final String matchId;
    private final TinderClient client;
    private final LinkedList<Message> messages;
    private boolean initialFetchDone;
    private String pageToken;

    /**
     * Constructs a new MatchCacheView.
     *
     * @param matchId client the corresponding match id
     * @param client  client the corresponding {@link TinderClient} instance
     */
    public MessageCacheView(String matchId, TinderClient client) {
        messages = new LinkedList<>();
        this.matchId = matchId;
        this.client = client;
        initialFetchDone = false;
    }

    /**
     * Gets a {@link Message} by id. Will request the {@link Message} from the Tinder API, if the {@link Message} is not
     * present in the cache.
     *
     * @param id the id to get the {@link Message} from
     * @return a {@link RestAction} holding the {@link Message}
     */
    public RestAction<Message> getMessage(String id) {
        List<Message> filtered = messages.stream().filter(
                message -> message.getId().equals(id)
        ).collect(Collectors.toList());
        if (filtered.size() == 0) {
            return new RestActionImpl<>(client, Route.Self.GET_MESSAGE.compile(id), (response, request) ->
                    new Message(DataObject.fromJson(response.body()), client)
            );
        }
        return new CompletedRestAction<>(filtered.get(0));
    }

    /**
     * Gets the first 60 {@link Message Messages} of a Match.
     *
     * @return a {@link RestAction} holding the first 60 {@link Message Messages}
     */
    @SuppressWarnings("unchecked")
    public RestAction<List<Message>> getMessages() {
        if (initialFetchDone) {
            return new CompletedRestAction<>(new ArrayList<>(messages));
        }
        return new RestActionImpl<>(client, Route.Match.GET_MESSAGES.compile(matchId, 60), (response, request) -> {
            DataObject data = DataObject.fromJson(response.body()).getObject("data");
            data.getArray("messages").forEach(object ->
                    messages.addFirst(new Message(new DataObject((Map<String, Object>) object), client))
            );
            initialFetchDone = true;
            if (data.hasKey("next_page_token")) {
                pageToken = data.getString("next_page_token");
            } else {
                pageToken = null;
            }
            return messages;
        });
    }

    /**
     * Requests all messages from the Tinder API and fills the cache with them.
     * <p><b>This method is blocking.</b></p>
     *
     * @return a {@link List} of all {@link Message Messages}
     */
    public List<Message> loadAllMessages() {
        if (pageToken == null && initialFetchDone) {
            return messages;
        }
        loadMessages(pageToken).forEach(messages::addFirst);
        return messages;
    }

    @SuppressWarnings("unchecked")
    private List<Message> loadMessages(String pageToken) {
        Route.CompiledRoute route;
        if (pageToken == null) {
            route = Route.Match.GET_MESSAGES.compile(matchId, 60);
        } else {
            route = Route.Match.GET_MESSAGES_PAGE.compile(matchId, 60, pageToken);
        }
        RestAction<List<Message>> restAction = new RestActionImpl<>(client, route, (response, request) -> {
            DataObject data = DataObject.fromJson(response.body());
            List<Message> result = new LinkedList<>();
            data.getArray("messages").forEach(object ->
                    result.add(new Message(new DataObject((Map<String, Object>) object), client))
            );
            if (data.hasKey("next_page_token")) {
                this.pageToken = data.getString("next_page_token");
            } else {
                this.pageToken = null;
            }
            return result;
        });
        List<Message> result = restAction.complete();
        if (this.pageToken != null) {
            result.addAll(loadMessages(this.pageToken));
        }
        return result;
    }

    /**
     * Adds a {@link Message} to the cache.
     *
     * @param message the {@link Message} to add
     */
    public void addMessage(Message message) {
        messages.add(message);
    }

    /**
     * Gets the size of the cache.
     *
     * @return the size of the cache
     */
    public int size() {
        return messages.size();
    }

    @Override
    public Iterator<Message> iterator() {
        return messages.iterator();
    }

    @Override
    public void forEach(Consumer<? super Message> action) {
        messages.forEach(action);
    }

    @Override
    public Spliterator<Message> spliterator() {
        return messages.spliterator();
    }
}
