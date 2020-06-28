package cn.cnyaoshun.oauth.common;

import lombok.Getter;

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
        DownLoadExcelTemplateType[] enumConstants = DownLoadExcelTemplateType.class.getEnumConstants();
        for (int i = 0; i < enumConstants.length; i++) {
            if (enumConstants[i].getType().equals(templateType)) {
                return enumConstants[i].getName();
            }
        }
        return null;
    }
}
