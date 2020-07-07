package cn.cnyaoshun.form.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.stream.Collectors;

@AllArgsConstructor
public enum DatabaseDriverType {
    ORG_DEPARTMENT_TEMPLATE("mysql","com.mysql.cj.jdbc.Driver"),
    ACCOUNT_TEMPLATE("oracle","oracle.jdbc.OracleDriver");
    @Getter
    String type;
    @Getter
    String driverName;

    public static String getDatabaseDriver(String type) {
        return Arrays.stream(DatabaseDriverType.class.getEnumConstants()).filter(downLoadExcelTemplateType -> type.equals(downLoadExcelTemplateType.getType())).map(DatabaseDriverType::getDriverName).collect(Collectors.joining());
    }
}
