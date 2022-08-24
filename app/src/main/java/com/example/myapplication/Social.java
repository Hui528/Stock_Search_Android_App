package com.example.myapplication;

public class Social {
    private int reddit_mention;
    private int reddit_positiveMention;
    private int reddit_negativeMention;
    private int twitter_mention;
    private int twitter_positiveMention;
    private int twitter_negativeMention;

    public Social(int reddit_mention, int reddit_positiveMention, int reddit_negativeMention, int twitter_mention, int twitter_positiveMention, int twitter_negativeMention) {
        this.reddit_mention = reddit_mention;
        this.reddit_positiveMention = reddit_positiveMention;
        this.reddit_negativeMention = reddit_negativeMention;
        this.twitter_mention = twitter_mention;
        this.twitter_positiveMention = twitter_positiveMention;
        this.twitter_negativeMention = twitter_negativeMention;
    }

    public int get_reddit_mention() {
        return this.reddit_mention;
    }

    public int get_reddit_positiveMention() {
        return this.reddit_positiveMention;
    }

    public int get_reddit_negativeMention() {
        return this.reddit_negativeMention;
    }

    public int get_twitter_mention() {
        return this.twitter_mention;
    }

    public int get_twitter_positiveMention() {
        return this.twitter_positiveMention;
    }

    public int get_twitter_negativeMention() {
        return this.twitter_negativeMention;
    }
}
