package org.fsp.springredditclone.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import org.fsp.springredditclone.dto.PostRequest;
import org.fsp.springredditclone.dto.PostResponse;
import org.fsp.springredditclone.model.Post;
import org.fsp.springredditclone.model.Subreddit;
import org.fsp.springredditclone.model.User;
import org.fsp.springredditclone.model.VoteType;
import org.fsp.springredditclone.repository.CommentRepository;
import org.fsp.springredditclone.repository.VoteRepository;
import org.fsp.springredditclone.service.AuthService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import static org.fsp.springredditclone.model.VoteType.DOWVOTE;
import static org.fsp.springredditclone.model.VoteType.UPVOTE;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private AuthService authService;

    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    public abstract Post map(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "userName", source = "user.username")
    public abstract PostResponse mapToDto(Post post);

    Integer commentCount(Post post) { return commentRepository.findByPost(post).size(); }

    String getDuration(Post post) { return TimeAgo.using(post.getCreatedDate().toEpochMilli()); }

    boolean isPostUpVoted(Post post) { return checkVoteType(post, UPVOTE); }

    boolean isPostDownVoted(Post post) { return checkVoteType(post, DOWVOTE); }

    private boolean checkVoteType(Post post, VoteType voteType) { return false; }
}
