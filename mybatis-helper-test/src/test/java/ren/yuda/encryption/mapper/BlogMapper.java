package ren.yuda.encryption.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import ren.yuda.encryption.entity.Blog;

import java.util.List;
import java.util.Set;

public interface BlogMapper {

    @Insert("INSERT INTO `blog`(`title`,`create_time`,`read_num`)VALUES(#{title},#{createTime},#{readNum})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Long saveEntityNoParamAnnotation(Blog blog);

    @Insert("INSERT INTO `blog`(`title`,`create_time`,`read_num`)VALUES(#{blog.title},#{blog.createTime},#{blog.readNum})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Long saveEntityWithParamAnnotation(@Param("blog") Blog blog);

    @Insert("<script>" +
            "INSERT INTO `blog` (`title`,`create_time`,`read_num`)VALUES" +
            "<foreach collection = 'arg0' item='blog' open='(' close=')' separator=','> " +
            "#{blog.title},#{blog.createTime},#{blog.readNum}" +
            "</foreach >" +
            "</script>")
    Long batchSaveEntityNoParamAnnotation(Set<Blog> blog);

    @Insert("<script>" +
            "INSERT INTO `blog` (`title`,`create_time`,`read_num`)VALUES" +
            "<foreach collection = 'blogs' item='blog' open='(' close=')' separator=','> " +
            "#{blog.title},#{blog.createTime},#{blog.readNum}" +
            "</foreach >" +
            "</script>")
    Long batchSaveEntityWithParamAnnotation(@Param("blogs") List<Blog> blogs);

    @Select("select * from blog where id=#{id}")
    Blog selectById(Long id);

    @Select("select title,create_time createTime,read_num readNum from blog ")
    Set<Blog> list();

    @Select("select id,title,create_time createTime,read_num readNum from blog " +
            "where title=#{title}")
    List<Blog> selectByTitleEntity(Blog blog);

    @Select("select id,title,create_time createTime,read_num readNum from blog " +
            "where title=#{blog.title}")
    List<Blog> selectByTitleEntityAnno(@Param("blog") Blog blog);
}
