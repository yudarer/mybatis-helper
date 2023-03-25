package ren.yuda.encryption.mapper;

import org.apache.ibatis.annotations.*;
import ren.yuda.encryption.entity.BlogT;

import java.util.List;
import java.util.Set;

@Mapper
public interface BlogTMapper {

    @Insert("INSERT INTO `blog`(`title`,`create_time`,`read_num`)VALUES(#{title},#{createTime},#{readNum})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Long saveEntityNoParamAnnotation(BlogT blog);

    @Insert("INSERT INTO `blog`(`title`,`create_time`,`read_num`)VALUES(#{blog.title},#{blog.createTime},#{blog.readNum})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Long saveEntityWithParamAnnotation(@Param("blog") BlogT blog);

    @Insert("<script>" +
            "INSERT INTO `blog` (`title`,`create_time`,`read_num`)VALUES" +
            "<foreach collection = 'arg0' item='blog' open='(' close=')' separator=','> " +
            "#{blog.title},#{blog.createTime},#{blog.readNum}" +
            "</foreach >" +
            "</script>")
    Long batchSaveEntityNoParamAnnotation(Set<BlogT> blog);

    @Insert("<script>" +
            "INSERT INTO `blog` (`title`,`create_time`,`read_num`)VALUES" +
            "<foreach collection = 'blogs' item='blog' open='(' close=')' separator=','> " +
            "#{blog.title},#{blog.createTime},#{blog.readNum}" +
            "</foreach >" +
            "</script>")
    Long batchSaveEntityWithParamAnnotation(@Param("blogs") List<BlogT> blogs);

    @Select("select * from blog where id=#{id}")
    BlogT selectById(Long id);

    @Select("select title,create_time createTime,read_num readNum from blog ")
    Set<BlogT> list();

    @Select("select id,title,create_time createTime,read_num readNum from blog " +
            "where title=#{title}")
    List<BlogT> selectByTitleEntity(BlogT blog);

    @Select("select id,title,create_time createTime,read_num readNum from blog " +
            "where title=#{blog.title}")
    List<BlogT> selectByTitleEntityAnno(@Param("blog") BlogT blog);
}
