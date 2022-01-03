[![Java CI](https://github.com/rednit-team/tinder4j/actions/workflows/ci.yml/badge.svg)](https://github.com/rednit-team/tinder4j/actions/workflows/ci.yml)
[![Documentation](https://github.com/rednit-team/tinder4j/actions/workflows/docs.yml/badge.svg)](https://github.com/rednit-team/tinder4j/actions/workflows/docs.yml)
[![license-shield](https://img.shields.io/badge/License-Apache%202.0-lightgrey.svg)]()

<img align="right" alt="logo" src="https://github.com/rednit-team/tinder4j/blob/master/.github/assets/Tinder4J.png?raw=true" height="200" width="200">

# Tinder4j

comprehensive and feature rich wrapper of the Tinder API

<br>

**Note: This is an unofficial project, and I have nothing to do with Tinder nor their API. I take no responsibility for
 any potential damage, banned accounts or other troubles related to this project!** 

---

## Features
- asynchronous and intuitive interface
- completely wrapped Tinder models
- rate limiting
- caching

### Example 
```java
TinderClient client = new TinderClient("X-Auth-Token");
client.getRecommendations().queue(recommendations -> recommendations.forEach(recommendation -> {
    recommendation.like().queue();
    System.out.println("Liked user " + recommendation.getName());
});
```

### Authentication 
Tinder uses Basic Authentication with UUID strings. To get your token, first login to Tinder in your browser. Then,
 open the network tab and filter for api.gotinder.com. Choose any GET or POST request and go to the Request Headers. 
 There, you'll find the X-Auth-Token header containing the auth token. Please note: you might need to perform some 
 actions first (for example liking a user) before you see any requests.
 
### Rate Limiting
The Tinder API has no official rate limiting, but API spamming results in extra verification needed, shadow-bans
or complete account suspension. Thus, the default Ratelimiter of the library is pretty restrictive.
However, you can implement your own Ratelimiter: 
```java
public class CustomRatelimiter implements Ratelimiter {
    @Override
    public boolean shouldDelay(Request<?> request) {
        return true; // your delay calculation 
    }

    @Override
    public long getDelay(Request<?> request) {
        return 1000L; // your delay
    }
}
```
Then pass it to the TinderClient instance:
```java
client.setRatelimiter(new CustomRatelimiter());
```

### Lifetime
The client will terminate as soon as all pending API requests where sent. If you want to use this library for bots
or similar you have to keep the JVM or respectively the client alive by yourself. **This also results in callback
threads being killed before their execution is finished. Call 
[`TinderClient#awaitShutdown`](https://rednit-team.github.io/tinder4j/com/rednit/tinder4j/api/TinderClient.html#awaitShutdown()) to prevent 
this behaviour.**

## Download

### Maven
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```
```xml
<dependency>
    <groupId>com.github.rednit-team</groupId>
    <artifactId>tinder4j</artifactId>
    <version>VERSION</version>
</dependency>
```

### Gradle
```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
```groovy
dependencies {
    implementation 'com.github.rednit-team:tinder4j:VERSION'
}
```
