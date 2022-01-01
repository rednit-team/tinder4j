package com.rednit.tinder4j.utils;

import com.rednit.tinder4j.TinderClient;
import com.rednit.tinder4j.entities.message.Message;
import com.rednit.tinder4j.internal.async.CompletedRestAction;
import com.rednit.tinder4j.internal.async.RestAction;
import com.rednit.tinder4j.internal.async.RestActionImpl;
import com.rednit.tinder4j.internal.requests.DataObject;
import com.rednit.tinder4j.internal.requests.Route;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MessageCacheView implements Iterable<Message> {

    private final String matchId;
    private final TinderClient client;
    private final LinkedList<Message> messages;
    private boolean initialFetchDone;
    private String pageToken;

    public MessageCacheView(String matchId, TinderClient client) {
        messages = new LinkedList<>();
        this.matchId = matchId;
        this.client = client;
    }

    public RestAction<Message> getMessage(String id) {
        List<Message> filtered = messages.stream().filter(
                message -> message.getId().equals(id)
        ).collect(Collectors.toList());
        if (filtered.size() == 0) {
            return new RestActionImpl<>(client, Route.Self.GET_MESSAGE.compile(id));
        }
        return new CompletedRestAction<>(filtered.get(0));
    }

    @SuppressWarnings("unchecked")
    public RestAction<List<Message>> getMessages() {
        if (initialFetchDone) {
            return new CompletedRestAction<>(new ArrayList<>(messages));
        }
        return new RestActionImpl<>(client, Route.Match.GET_MESSAGES.compile(matchId), (response, request) -> {
            DataObject data = DataObject.fromJson(response.body());
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

    public void addMessage(Message message) {
        messages.add(message);
    }

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
