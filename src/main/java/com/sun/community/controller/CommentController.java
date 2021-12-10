package com.sun.community.controller;

import com.sun.community.entity.Comment;
import com.sun.community.service.CommentService;
import com.sun.community.service.DiscussPostService;
import com.sun.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    DiscussPostService discussPostService;

    @Autowired
    CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    @PostMapping("/add/{discussPost}")
    public String addComment(@PathVariable("discussPost") int discussId, Comment comment){
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        commentService.addComment(comment);

        return "redirect:/discuss/detail/"+discussId;
    }
}
