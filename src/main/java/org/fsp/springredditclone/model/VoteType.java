package org.fsp.springredditclone.model;

public enum VoteType {

    UPVOTE(1), DOWVOTE(-1),
    ;

    VoteType(int direction) {

    }
}
