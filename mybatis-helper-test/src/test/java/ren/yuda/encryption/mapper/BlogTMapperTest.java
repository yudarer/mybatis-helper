package ren.yuda.encryption.mapper;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ren.yuda.encryption.SpringBootMapperTest;
import ren.yuda.encryption.entity.BlogT;

import java.time.LocalDateTime;


public class BlogTMapperTest extends SpringBootMapperTest {
    @Autowired
    private BlogTMapper mapper;

    @Test
    public void saveEntityNoParamAnnotation() {
        BlogT b = new BlogT();
        b.setTitle("哈姆雷特");
        b.setCreateTime(LocalDateTime.now());
        b.setReadNum(10);
        mapper.saveEntityNoParamAnnotation(b);
        log.info("current BlogT ={}", b);
    }

    @Test
    public void saveEntityWithParamAnnotation() {
    }

    @Test
    public void batchSaveEntityNoParamAnnotation() {
    }

    @Test
    public void batchSaveEntityWithParamAnnotation() {
    }

    @Test
    public void selectById() {
        BlogT blogT = mapper.selectById(59L);
        log.info("{}", blogT);
        Assert.assertEquals("哈姆雷特", blogT.getTitle());
    }

    @Test
    public void list() {
    }

    @Test
    public void selectByTitleEntity() {
    }

    @Test
    public void selectByTitleEntityAnno() {
    }
}