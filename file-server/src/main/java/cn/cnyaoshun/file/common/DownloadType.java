package cn.cnyaoshun.file.common;

import lombok.Getter;

public enum DownloadType {
    WEB("web"),
    MINIO("minio");

    @Getter
    String type;

    DownloadType(String type){
        this.type = type;
    }
}
