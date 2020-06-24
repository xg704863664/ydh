package cn.cnyaoshun.oauth.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class AccountImportDomain {
    @ExcelProperty("账户名")
    private String accountName;

    @ExcelProperty("密码")
    private String passWord;

    @ExcelProperty("姓名")
    private String userName;

    @ExcelProperty("手机号")
    private String phone;
}
