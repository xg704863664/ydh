package cn.cnyaoshun.file.service.impl;

import cn.cnyaoshun.file.common.ApiCode;
import cn.cnyaoshun.file.common.exception.ExceptionValidation;
import cn.cnyaoshun.file.entity.FileRecord;
import cn.cnyaoshun.file.repository.FileRecordRepository;
import cn.cnyaoshun.file.service.DownLoadBaseService;
import cn.cnyaoshun.file.service.FileService;
import cn.cnyaoshun.file.service.handler.HandlerContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@RefreshScope
public class FileServiceImpl implements FileService {

    @Value("${upload.file.path}")
    private String uploadPath;

    @Value("${download.type}")
    private String downloadType;

    private final FileRecordRepository fileRecordRepository;

    private final HandlerContext handlerContext;


    /**
     * 文件上传
     *
     * @param multipartFile
     */
    @Override
    @Transactional
    public FileRecord uploadFile(MultipartFile multipartFile) {
        File file = new File(uploadPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName = multipartFile.getOriginalFilename();
//        String filePrefix= fileName.substring(0,fileName.lastIndexOf("."));
        String fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String uuid = UUID.randomUUID().toString();
        String path = file.getPath() + File.separator + uuid + "." + fileSuffix;
        try {
            multipartFile.transferTo(new File(path));
        } catch (IOException e) {
            throw new ExceptionValidation(ApiCode.UPLOAD_File_FAIL.getCode(), ApiCode.UPLOAD_File_FAIL.getMsg());
        }
        FileRecord fileRecord = new FileRecord();
        fileRecord.setPath(path);
        fileRecord.setFileType(fileSuffix);
        fileRecord.setName(fileName);
        fileRecord = fileRecordRepository.save(fileRecord);
        //预留上传文件备份minio
//        FileServiceImpl fileService = (FileServiceImpl) AopContext.currentProxy();
//        fileService.backMinio(multipartFile);
        return fileRecord;
    }


    @Async
    public void backMinio(MultipartFile multipartFile){

    }

    /**
     * 文件下载
     * @param fileId
     * @return
     */
    @Override
    public ResponseEntity<byte[]> downLoadFile(Long fileId) {
        Optional<FileRecord> optionalFileRecord = fileRecordRepository.findById(fileId);
        if (optionalFileRecord.isPresent()){
            FileRecord fileRecord = optionalFileRecord.get();
            //策略模式获取下载service
            DownLoadBaseService downLoadBaseService = handlerContext.getInstance(downloadType);
            return downLoadBaseService.downLoadFile(fileRecord);
        }
        return null;
    }


    /***
     * 图片预览
     * @param fileId
     * @return
     */
    @Override
    public ResponseEntity<Resource> previewImage(Long fileId) {
        Optional<FileRecord> optionalFileRecord = fileRecordRepository.findById(fileId);
        InputStreamResource inputStreamResource = null;
        if (optionalFileRecord.isPresent()) {
            FileRecord fileRecord = optionalFileRecord.get();
            if(fileRecord.getFileType().toLowerCase().equals("gif") || fileRecord.getFileType().toLowerCase().equals("png") || fileRecord.getFileType().toLowerCase().equals("jpeg")|| fileRecord.getFileType().toLowerCase().equals("jpg")){
                try {
                    FileInputStream fileInputStream = new FileInputStream(new File(fileRecord.getPath()));
                    inputStreamResource = new InputStreamResource(fileInputStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        if (inputStreamResource == null){
            throw new ExceptionValidation(ApiCode.IMAGE_NOT_EXIST.getCode(),ApiCode.IMAGE_NOT_EXIST.getMsg());
        }
        return new ResponseEntity<>(inputStreamResource,HttpStatus.OK);
    }
}
