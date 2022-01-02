package com.rednit.tinder4j.utils;

import com.rednit.tinder4j.TinderClient;
import com.rednit.tinder4j.entities.Match;
import com.rednit.tinder4j.internal.async.CompletedRestAction;
import com.rednit.tinder4j.internal.async.RestAction;
import com.rednit.tinder4j.internal.async.RestActionImpl;
import com.rednit.tinder4j.internal.requests.DataObject;
import com.rednit.tinder4j.internal.requests.Route;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MatchCacheView implements Iterable<Match> {

    private final TinderClient client;
    private final List<Match> matches;
    private String pageToken;

    public MatchCacheView(TinderClient client) {
        this.client = client;
        matches = new ArrayList<>();
    }

    public RestAction<Match> getMatch(String id) {
        List<Match> filtered = matches.stream().filter(
                match -> match.getId().equals(id)
        ).collect(Collectors.toList());
        if (filtered.size() == 0) {
            return new RestActionImpl<>(client, Route.Match.GET_MATCH.compile(id), (response, request) ->
                    new Match(DataObject.fromJson(response.body()).getObject("data"), client)
            );
        }
        return new CompletedRestAction<>(filtered.get(0));
    }

    public List<Match> loadAllMatches() {
        matches.clear();
        return loadMatches(null);
    }

    @SuppressWarnings("unchecked")
    private List<Match> loadMatches(String pageToken) {
        Route.CompiledRoute route;
        if (pageToken == null) {
            route = Route.Self.GET_MATCHES.compile(60);
        } else {
            route = Route.Self.GET_MATCHES_PAGE.compile(60, pageToken);
        }
        RestAction<List<Match>> restAction = new RestActionImpl<>(client, route, (response, request) -> {
            List<Match> matches = new ArrayList<>();
            DataObject data = DataObject.fromJson(response.body()).getObject("data");
            data.getArray("matches").forEach(object ->
                    matches.add(new Match(new DataObject((Map<String, Object>) object), client))
            );
            if (data.hasKey("next_page_token")) {
                this.pageToken = data.getString("next_page_token");
            } else {
                this.pageToken = null;
            }
            return matches;
        });
        List<Match> result = restAction.complete();
        if (this.pageToken != null) {
            result.addAll(loadMatches(this.pageToken));
        }
        return result;
    }

    public List<Match> getMatches() {
        return new ArrayList<>(matches);
    }

    public List<Match> getMatchesByName(String name) {
        return matches.stream().filter(
                match -> match.getMatchedUser().getName().equals(name)
        ).collect(Collectors.toList());
    }

    public List<Match> getMatchesByAge(int age) {
        return matches.stream().filter(
                match -> match.getMatchedUser().getAge() == age
        ).collect(Collectors.toList());
    }

    public void add(Match match) {
        matches.add(match);
    }

    public void remove(Match match) {
        remove(match.getId());
    }

    public void remove(String matchId) {
        matches.removeIf(match -> match.getId().equals(matchId));
    }

    public int size() {
        return matches.size();
    }

    @NotNull
    @Override
    public Iterator<Match> iterator() {
        return matches.iterator();
    }

    @Override
    public void forEach(Consumer<? super Match> action) {
        matches.forEach(action);
    }

    @Override
    public Spliterator<Match> spliterator() {
       return matches.spliterator();
    }
}
