package cn.cnyaoshun.oauth.common;

import lombok.Data;

@Data
public class PageDomain {
    private Long totalElements = 0L;// 总条数
    private Integer totalPages = 0;// 总页数
    private Integer pageNumber = 0;// 第几页
    private Integer pageSize = 10;// 每页多少条

    public PageDomain() {}

    public PageDomain(Long totalElements, Integer totalPages, Integer pageNumber, Integer pageSize) {
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public static Integer getTotalPages(Long totalElements,Integer pageSize){
        return Integer.parseInt(totalElements+"")/pageSize+(Integer.parseInt(totalElements+"")%pageSize>0?1:0);
    }


}
