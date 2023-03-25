package ren.yuda.encryption.entity;

import ren.yuda.encryption.Encrypt;

import java.time.LocalDateTime;

public class BlogT {
    private Long id;
    @Encrypt(algorithm = "des")
    private String title;
    private LocalDateTime createTime;
    @Encrypt(algorithm = "des")
    private Integer readNum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Integer getReadNum() {
        return readNum;
    }

    public void setReadNum(Integer readNum) {
        this.readNum = readNum;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", createTime=" + createTime +
                ", readNum=" + readNum +
                '}';
    }
}
