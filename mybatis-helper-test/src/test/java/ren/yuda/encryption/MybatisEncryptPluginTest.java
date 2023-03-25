package ren.yuda.encryption;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ren.yuda.encryption.entity.Blog;
import ren.yuda.encryption.mapper.BlogMapper;
import ren.yuda.encryption.support.DESEncryptAlgorithm;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MybatisEncryptPluginTest {

    private static final Logger log = LoggerFactory.getLogger(MybatisEncryptPluginTest.class);
    private SqlSession sqlSession;
    private BlogMapper mapper;

    @Before
    public void init() {
        String resource = "./mybatis-test.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        sqlSession = sqlSessionFactory.openSession(true);
        mapper = sqlSession.getMapper(BlogMapper.class);
        AlgorithmRegister.getInstance().registerAlgorithm(new DESEncryptAlgorithm("12345678"));
    }

    @After
    public void destroy() {
        if (sqlSession != null) {
            sqlSession.close();
        }
    }

    @Test
    public void testSaveEntityNoParamAnnotation() throws IOException {
        Blog b = new Blog();
        b.setTitle("哈姆雷特");
        b.setCreateTime(LocalDateTime.now());
        b.setReadNum(10);
        mapper.saveEntityNoParamAnnotation(b);
        log.info("current blog ={}", b);
    }

    @Test
    public void testSaveEntityWithParamAnnotation() throws IOException {
        Blog b = new Blog();
        b.setTitle("哈姆雷特");
        b.setCreateTime(LocalDateTime.now());
        b.setReadNum(10);
        mapper.saveEntityWithParamAnnotation(b);
        log.info("current blog ={}", b);
    }

    @Test
    public void testBatchSaveEntityNoParamAnnotation() throws IOException {
        Blog b = new Blog();
        b.setTitle("哈姆雷特");
        b.setCreateTime(LocalDateTime.now());
        b.setReadNum(10);
        Set<Blog> bs = new HashSet<>();
        bs.add(b);
        mapper.batchSaveEntityNoParamAnnotation(bs);
        log.info("current blog ={}", bs);

    }

    @Test
    public void testBatchSaveEntityWithParamAnnotation() throws IOException {
        Blog b = new Blog();
        b.setTitle("哈姆雷特");
        b.setCreateTime(LocalDateTime.now());
        b.setReadNum(10);
        List<Blog> bs = new ArrayList<>();
        bs.add(b);
        mapper.batchSaveEntityWithParamAnnotation(bs);
        log.info("current blog ={}", bs);
    }

    @Test
    public void testSelectById() throws IOException {
        Blog blog = mapper.selectById(55L);
        Assert.assertTrue(blog.getTitle() != null && blog.getTitle().equals("哈姆雷特"));
    }

    @Test
    public void testSelectByTitleEntity() throws IOException {
        Blog b = new Blog();
        b.setTitle("哈姆雷特");
        List<Blog> list = mapper.selectByTitleEntity(b);
        Assert.assertFalse(list.isEmpty());
        Assert.assertEquals("哈姆雷特", list.get(0).getTitle());
    }

    @Test
    public void testSelectByTitleEntityAnno() throws IOException {
        Blog b = new Blog();
        b.setTitle("哈姆雷特");
        List<Blog> list = mapper.selectByTitleEntityAnno(b);
        Assert.assertFalse(list.isEmpty());
        Assert.assertEquals("哈姆雷特", list.get(0).getTitle());
    }

}
