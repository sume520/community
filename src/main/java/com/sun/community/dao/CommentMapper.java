package com.sun.community.dao;

import com.sun.community.entity.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Select({"select id,user_id,entity_type,entity_id,target_id,content,status,create_time ",
            "from comment where status=0 and entity_type=#{entityType} and entity_id=#{entityId} ",
            "order by create_time asc limit #{offset},#{limit}"})
    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    @Select("select count(id) from comment where status=0 and entity_type=#{entityType} and entity_id=#{entityId}")
    int selectCountByEntity(int entityType, int entityId);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert({"insert comment (user_id,entity_type,entity_id,target_id,content,status,create_time) ",
            "values (#{userId},#{entityType},#{entityId},#{targetId},#{content},#{status},#{createTime})"})
    int insertComment(Comment comment);

    @Select("select id,user_id,entity_type,entity_id,target_id,content,status,create_time " +
            "from comment where id=#{id}")
    Comment selectCommentById(int id);

}
