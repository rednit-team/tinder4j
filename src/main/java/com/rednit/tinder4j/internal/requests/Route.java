package com.rednit.tinder4j.internal.requests;

@SuppressWarnings("unused")
public class Route {

    public static class Self {
        public static final Route GET_SELF = new Route(Method.GET, "/profile");
        public static final Route DELETE_SELF = new Route(Method.DELETE, "/profile");
        public static final Route UPDATE_INTERESTS = new Route(Method.POST, "/v2/profile");
        public static final Route DELETE_INTERESTS = new Route(Method.DELETE, "/v2/profile/userinterests");
        public static final Route MODIFY_DESCRIPTORS = new Route(Method.POST, "/v2/profile");
        public static final Route MODIFY_JOB = new Route(Method.POST, "v2/profile/job");
        public static final Route MODIFY_BIO = new Route(Method.POST, "v2/profile");
        public static final Route MODIFY_SCHOOL = new Route(Method.POST, "v2/profile/school");
        public static final Route UPDATE_CITY = new Route(Method.POST, "v2/profile/city");
        public static final Route DELETE_CITY = new Route(Method.DELETE, "v2/profile/city");
        public static final Route MODIFY_GENDER = new Route(Method.POST, "v2/profile");
        public static final Route MODIFY_SEARCH_PREFERENCES = new Route(Method.POST, "v2/profile");
        public static final Route GET_UPDATES = new Route(Method.POST, "/updates");
        public static final Route GET_RECOMMENDATIONS = new Route(Method.GET, "/user/recs");
        public static final Route GET_LIKED_USERS = new Route(Method.GET, "/v2/my-likes");
        public static final Route GET_LIKE_PREVIEWS = new Route(Method.GET, "/v2/fast-match/teasers");
        public static final Route GET_MESSAGE = new Route(Method.GET, "/message/{id}");
        public static final Route GET_RECENTLY_ACTIVE = new Route(
                Method.GET, "/v2/fast-match/teaser?type=recently-active"
        );
        public static final Route GET_MATCHES = new Route(
                Method.GET, "/v2/matches?count={count}"
        );
        public static final Route GET_MATCHES_PAGE = new Route(
                Method.GET, "/v2/matches?count={count}&page_token={page_token}"
        );
    }

    public static class User {
        public static final Route GET_USER = new Route(Method.GET, "/user/{id}");
        public static final Route GET_USER_BY_NAME = new Route(Method.GET, "/user/{name}");
        public static final Route LIKE = new Route(Method.POST, "/like/{id}");
        public static final Route DISLIKE = new Route(Method.POST, "/pass/{id}");
        public static final Route SUPERLIKE = new Route(Method.POST, "/like/{id}/super");
        public static final Route REPORT = new Route(Method.POST, "/report/{id}");
    }

    public static class Match {
        public static final Route GET_MATCH = new Route(Method.GET, "/v2/matches/{id}");
        public static final Route DELETE_MATCH = new Route(Method.DELETE, "match/{id}");
        public static final Route SEND_MESSAGE = new Route(Method.POST, "user/matches/{id}");
        public static final Route GET_MESSAGES = new Route(
                Method.GET, "/v2/matches/{id}/messages?count={count}"
        );
        public static final Route GET_MESSAGES_PAGE = new Route(
                Method.GET, "/v2/matches/{id}/messages?count={count}&page_token={page_token}"
        );

    }

    private final Method method;
    private final String route;

    private Route(Method method, String route) {
        this.method = method;
        this.route = route;
    }

    public CompiledRoute compile(Object... params) {
        return new CompiledRoute(route, method, params);
    }

    public String getRoute() {
        return route;
    }

    public Method getMethod() {
        return method;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", method.name(), route);
    }

    public enum Method {
        GET,
        POST,
        DELETE
    }

    public static class CompiledRoute {

        private final String route;
        private final Method method;

        CompiledRoute(String route, Method method, Object... params) {
            if (route.endsWith("/")) {
                route = route.substring(0, route.length() - 1);
            }
            if (!route.startsWith("/")) {
                route = "/" + route;
            }
            while (route.contains(" ")) {
                route = route.replaceAll(" ", "");
            }
            for (Object param : params) {
                route = route.replaceFirst("\\{.+?(?=})}", param.toString());
            }

            this.method = method;
            this.route = route;
        }

        public String getRoute() {
            return route;
        }

        public Method getMethod() {
            return method;
        }

        @Override
        public String toString() {
            return String.format("%s: %s", method.name(), route);
        }
    }
}
