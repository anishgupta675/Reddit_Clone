package org.fsp.springredditclone.service;

import lombok.AllArgsConstructor;
import org.fsp.springredditclone.dto.CommentsDto;
import org.fsp.springredditclone.exceptions.SpringRedditException;
import org.fsp.springredditclone.mapper.CommentMapper;
import org.fsp.springredditclone.model.Comment;
import org.fsp.springredditclone.model.NotificationEmail;
import org.fsp.springredditclone.model.Post;
import org.fsp.springredditclone.model.User;
import org.fsp.springredditclone.repository.CommentRepository;
import org.fsp.springredditclone.repository.PostRepository;
import org.fsp.springredditclone.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CommentService {

    private static final String POST_URL = "";
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public void save(CommentsDto commentsDto) {
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new SpringRedditException(commentsDto.getPostId().toString()));
        Comment comment = commentMapper.map(commentsDto, post, authService.getCurrentUser());
        commentRepository.save(comment);
        String message = mailContentBuilder.build(post.getUser().getUsername() + " posted a comment on your post." + POST_URL);
        sendCommentNotification(message, post.getUser());
    }

    private void sendCommentNotification(String message, User user) { mailService.sendMail(new NotificationEmail(user.getUsername() + " Commented on your post", user.getEmail(), message)); }

    public List<CommentsDto> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new SpringRedditException(postId.toString()));
        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto).collect(toList());
    }

    public List<CommentsDto> getAllCommentsForUser(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new SpringRedditException(userName));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(toList());
    }
}
