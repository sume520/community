package com.sun.community.controller;

import com.sun.community.entity.DiscussPost;
import com.sun.community.entity.Page;
import com.sun.community.service.ElasticsearchService;
import com.sun.community.service.LikeService;
import com.sun.community.service.UserService;
import com.sun.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SearchController implements CommunityConstant {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @GetMapping("/search")
    public String search(String keyword, Page page, Model model) throws IOException {
        //搜索帖子
        Map<String, Object> discusses = elasticsearchService.searchDiscussPost(keyword, page.getCurrent() - 1, page.getLimit());
        List<DiscussPost> postList = (List<DiscussPost>) discusses.get("posts");
        //聚合数据
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (postList != null) {
            for (DiscussPost post : postList) {
                Map<String, Object> map = new HashMap<>();
                //帖子
                map.put("post", post);
                //作者
                map.put("user", userService.findUserById(post.getUserId()));
                //点赞数量
                map.put("likeCount", likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId()));

                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);
        model.addAttribute("keyword", keyword);

        //分页信息
        page.setPath("/search?keyword=" + keyword);
        page.setRows(discusses.get("totalPages") == null ? 0 : ((Long) discusses.get("totalPages")).intValue());

        return "/site/search";
    }
}
