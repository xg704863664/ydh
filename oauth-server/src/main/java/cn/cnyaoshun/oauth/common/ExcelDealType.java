package cn.cnyaoshun.oauth.common;

import lombok.Getter;

public enum  ExcelDealType {

    ORG_DEPARTMENT_DEAL("org_department_deal");

    @Getter
    String type;

    ExcelDealType(String type){
        this.type = type;
    }
}
