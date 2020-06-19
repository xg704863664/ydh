package cn.cnyaoshun.oauth.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class OrgDepartmentImportDomain {
    @ExcelProperty("组织机构名称")
    private String orgName;

    @ExcelProperty("一级部门")
    private String oneDepartment;

    @ExcelProperty("二级部门")
    private String twoDepartment;

    @ExcelProperty("三级部门")
    private String threeDepartment;

    @ExcelProperty("员工名称")
    private String userName;

    @ExcelProperty("员工身份证")
    private String cardNo;

    @ExcelProperty("手机号")
    private String phone;
}
