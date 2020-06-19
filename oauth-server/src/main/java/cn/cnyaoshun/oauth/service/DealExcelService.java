package cn.cnyaoshun.oauth.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public  interface DealExcelService<T>{
     void dealExcel(MultipartFile multipartFile);
     void dealData(List<T> list);
}
