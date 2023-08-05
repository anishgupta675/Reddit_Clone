package org.fsp.springredditclone.controller;

import lombok.AllArgsConstructor;
import org.fsp.springredditclone.dto.PostRequest;
import org.fsp.springredditclone.dto.PostResponse;
import org.fsp.springredditclone.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity createPost(@RequestBody PostRequest postRequest) {
        postService.save(postRequest);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() { return status(HttpStatus.OK).body(postService.getAllPosts()); }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) { return status(HttpStatus.OK).body(postService.getPost(id)); }

    @GetMapping("by-subreddit/{id}")
    public ResponseEntity<List<PostResponse>> getPostsBySubreddit(Long id) { return status(HttpStatus.OK).body(postService.getPostsBySubreddit(id)); }
}
