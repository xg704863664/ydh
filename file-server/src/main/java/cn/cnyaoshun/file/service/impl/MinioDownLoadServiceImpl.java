package cn.cnyaoshun.file.service.impl;

import cn.cnyaoshun.file.common.DownloadType;
import cn.cnyaoshun.file.common.annotation.HandlerType;
import cn.cnyaoshun.file.entity.FileRecord;
import cn.cnyaoshun.file.service.DownLoadBaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@HandlerType(value = DownloadType.MINIO)
public class MinioDownLoadServiceImpl implements DownLoadBaseService {
    @Override
    public ResponseEntity<byte[]> downLoadFile(FileRecord fileRecord) {

        return null;
    }
}
