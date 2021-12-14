package com.sun.community.service;

import com.alibaba.fastjson.JSONObject;
import com.sun.community.dao.elasticsearch.DiscussPostRepository;
import com.sun.community.entity.DiscussPost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class ElasticsearchService {

    @Autowired
    private DiscussPostRepository discussPostRepository;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    public void saveDiscussPost(DiscussPost discussPost) {
        discussPostRepository.save(discussPost);
    }

    public void deleteDiscussPost(int id) {
        discussPostRepository.deleteById(id);
    }

    public Map<String, Object> searchDiscussPost(String keyword, int current, int limit) throws IOException {
        // 1. 创建搜索请求 searchRequest
        SearchRequest searchRequest = new SearchRequest("discusspost");  // discusspost是索引名，就是表名
        // 2.配置高亮 HighlightBuilder
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title"); //为哪些字段匹配到的内容设置高亮
        highlightBuilder.field("content");
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.preTags("<span style='color:red'>");
        // 相当于把结果套了一点html标签  然后前端获取到数据就直接用
        highlightBuilder.postTags("</span>");
        // 3.构建搜索条件
        SearchSourceBuilder searchQuery = new SearchSourceBuilder()
                .query(QueryBuilders.multiMatchQuery(keyword, "title", "content"))
                .sort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .sort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .sort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .from(current).size(limit)
                .highlighter(highlightBuilder);

        //4.将搜索条件参数传入搜索请求
        searchRequest.source(searchQuery);
        //5.使用客户端发送请求
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        List<DiscussPost> list = new LinkedList<>();

        for (SearchHit hit : searchResponse.getHits().getHits()) {
            DiscussPost discussPost = JSONObject.parseObject(hit.getSourceAsString(), DiscussPost.class);
            HighlightField titleFiled = hit.getHighlightFields().get("title");
            if (titleFiled != null) {
                discussPost.setTitle(titleFiled.getFragments()[0].toString());
            }
            HighlightField contentFiled = hit.getHighlightFields().get("content");
            if (contentFiled != null) {
                discussPost.setContent(contentFiled.getFragments()[0].toString());
            }
            list.add(discussPost);
        }

        Map<String, Object> postMap = new HashMap<>();
        postMap.put("posts", list);
        postMap.put("totalPages", searchResponse.getHits().getTotalHits().value);

        return postMap;
    }
}
