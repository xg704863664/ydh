package cn.cnyaoshun.from.common;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageDataDomain<T> {
    private List<T>  records=new ArrayList<>();
    //总记录数
    private Long total=0L;
    //当前页大小
    private Integer size=10;
    //当前页
    private Integer current=0;
    //总页数
    private Integer pages=0;

}
