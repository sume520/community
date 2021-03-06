package com.sun.community.dao;

import com.alibaba.fastjson.JSONObject;
import com.sun.community.CommunityApplication;
import com.sun.community.dao.elasticsearch.DiscussPostRepository;
import com.sun.community.entity.DiscussPost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class ElasticsearchTest {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private DiscussPostRepository discussPostRepository;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    @Test
    public void testInsert() {
        discussPostRepository.save(discussPostMapper.selectDiscussPostById(241));
        discussPostRepository.save(discussPostMapper.selectDiscussPostById(242));
        discussPostRepository.save(discussPostMapper.selectDiscussPostById(243));
    }

    @Test
    public void testInsertList() {
        /*discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(101, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(102, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(103, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(111, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(112, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(131, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(132, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(133, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(134, 0, 100));*/
    }

    @Test
    public void testSearchByRepository() throws IOException {

        // 1. ?????????????????? searchRequest
        SearchRequest searchRequest = new SearchRequest("discusspost");  // discusspost???????????????????????????
        // 2.???????????? HighlightBuilder
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title"); //?????????????????????????????????????????????
        highlightBuilder.field("content");
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.preTags("<span style='color:red'>");
        // ??????????????????????????????html??????  ???????????????????????????????????????
        highlightBuilder.postTags("</span>");
        // 3.??????????????????
        SearchSourceBuilder searchQuery = new SearchSourceBuilder()
                .query(QueryBuilders.multiMatchQuery("???????????????", "title", "content"))
                .sort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .sort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .sort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .from(0).size(10)
                .highlighter(highlightBuilder);

        //4.???????????????????????????????????????
        Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1));
        searchRequest.scroll(scroll);
        searchRequest.source(searchQuery);
        //5.???????????????????????????
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        List<DiscussPost> list = new LinkedList<>();
//        System.out.println(searchResponse.getHits().getHits().length);//????????????????????????
//        System.out.println(searchResponse.getHits().getTotalHits().value);//??????????????????
//        int onePageNum = searchResponse.getHits().getHits().length;
//        long total = searchResponse.getHits().getTotalHits().value;
//        int pageSize = (int) Math.ceil(total / onePageNum);

//        String scrollId = searchResponse.getScrollId();

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
            System.out.println(discussPost);
            list.add(discussPost);
        }
    }

}
