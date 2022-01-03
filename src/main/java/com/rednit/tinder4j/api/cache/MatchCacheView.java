package com.rednit.tinder4j.api.cache;

import com.rednit.tinder4j.api.TinderClient;
import com.rednit.tinder4j.api.entities.Match;
import com.rednit.tinder4j.requests.async.CompletedRestAction;
import com.rednit.tinder4j.api.requests.RestAction;
import com.rednit.tinder4j.requests.async.RestActionImpl;
import com.rednit.tinder4j.requests.DataObject;
import com.rednit.tinder4j.requests.Route;
import org.jetbrains.annotations.NotNull;

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
public class MatchCacheView implements Iterable<Match> {

    private final TinderClient client;
    private final List<Match> matches;
    private String pageToken;

    /**
     * Constructs a new MatchCacheView.
     *
     * @param client client the corresponding {@link TinderClient} instance
     */
    public MatchCacheView(TinderClient client) {
        this.client = client;
        matches = new ArrayList<>();
    }

    /**
     * Gets a {@link Match} by id. Will request the {@link Match} from the Tinder API, if the {@link Match} is not
     * present in the cache.
     *
     * @param id the id to get the {@link Match} from
     * @return a {@link RestAction} holding the {@link Match}
     */
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

    /**
     * Requests all matches from the Tinder API and fills the cache with them. This will invalidate all old
     * {@link Match Matches} inside the cache.
     * <p><b>This method is blocking.</b></p>
     *
     * @return a {@link List} of all {@link Match Matches}
     */
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

    /**
     * Gets all {@link Match Matches} inside the cache. Use {@link #loadAllMatches()} to request matches from the
     * Tinder API.
     *
     * @return all {@link Match Matches} inside the cache
     * @see #loadAllMatches()
     */
    public List<Match> getMatches() {
        return new ArrayList<>(matches);
    }

    /**
     * Gets all matches filtered by the name of the matched user. This will only consider cached {@link Match Matches}.
     *
     * @param name the name to get matches by
     * @return a possibly empty {@link List} of the filtered matches
     */
    public List<Match> getMatchesByName(String name) {
        return matches.stream().filter(
                match -> match.getMatchedUser().getName().equals(name)
        ).collect(Collectors.toList());
    }

    /**
     * Gets all matches filtered by the age of the matched user. This will only consider cached {@link Match Matches}.
     *
     * @param age the age to get matches by
     * @return a possibly empty {@link List} of the filtered matches
     */
    public List<Match> getMatchesByAge(int age) {
        return matches.stream().filter(
                match -> match.getMatchedUser().getAge() == age
        ).collect(Collectors.toList());
    }

    /**
     * Adds a {@link Match} to the cache.
     *
     * @param match the {@link Match} to add
     */
    public void add(Match match) {
        matches.add(match);
    }

    /**
     * Removes a {@link Match} from the cache.
     *
     * @param match the {@link Match} to remove
     */
    public void remove(Match match) {
        remove(match.getId());
    }

    /**
     * Removes a {@link Match} from the cache by id.
     *
     * @param matchId the id of the {@link Match} to remove
     */
    public void remove(String matchId) {
        matches.removeIf(match -> match.getId().equals(matchId));
    }

    /**
     * Gets the size of the cache.
     *
     * @return the size of the cache
     */
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
