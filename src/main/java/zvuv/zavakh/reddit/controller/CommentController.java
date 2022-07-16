package zvuv.zavakh.reddit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zvuv.zavakh.reddit.dto.CommentDto;
import zvuv.zavakh.reddit.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentDto> create(@RequestBody CommentDto commentDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.save(commentDto));
    }

    @GetMapping("/by-post/{postId}")
    public ResponseEntity<List<CommentDto>> getCommentsForPost(@PathVariable("postId") Long postId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.getCommentsForPost(postId));
    }

    @GetMapping("/by-user/{username}")
    public ResponseEntity<List<CommentDto>> getCommentsForUser(@PathVariable("username") String username) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.getCommentsForUser(username));
    }
}
