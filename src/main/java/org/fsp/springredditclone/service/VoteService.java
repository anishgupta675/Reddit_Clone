package org.fsp.springredditclone.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.fsp.springredditclone.dto.VoteDto;
import org.fsp.springredditclone.exceptions.SpringRedditException;
import org.fsp.springredditclone.model.Post;
import org.fsp.springredditclone.model.Vote;
import org.fsp.springredditclone.model.VoteType;
import org.fsp.springredditclone.repository.PostRepository;
import org.fsp.springredditclone.repository.VoteRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.fsp.springredditclone.model.VoteType.UPVOTE;

@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new SpringRedditException(""));
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, post.getUser());
        if(voteByPostAndUser.isPresent() &&
                voteByPostAndUser.get().getVoteType()
                        .equals(voteDto.getVoteType())) throw new SpringRedditException("You have already "
                + voteDto.getVoteType() + "`d for this post");
        if(UPVOTE.equals(voteDto.getVoteType())) post.setVoteCount(post.getVoteCount() + 1);
        else post.setVoteCount(post.getVoteCount() - 1);
        voteRepository.save(mapToDto(voteDto, post));
        postRepository.save(post);
    }

    private Vote mapToDto(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}
