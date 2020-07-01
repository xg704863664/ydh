package cn.cnyaoshun.oauth.common;

import lombok.Getter;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum DownLoadExcelTemplateType {

    ORG_DEPARTMENT_TEMPLATE("org_department_template","organization_template.xlsx"),
    ACCOUNT_TEMPLATE("account_template","account_template.xlsx");

    @Getter
    String type;
    @Getter
    String name;

    DownLoadExcelTemplateType(String type,String name){
        this.type = type;
        this.name = name;
    }

    public static String getDownLoadTypeName(String templateType) {
        return Arrays.stream(DownLoadExcelTemplateType.class.getEnumConstants()).filter(downLoadExcelTemplateType -> templateType.equals(downLoadExcelTemplateType.getType())).map(DownLoadExcelTemplateType::getName).collect(Collectors.joining());
    }

}
