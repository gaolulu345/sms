package com.sms.admin.data.dto;

import com.github.crab2died.annotation.ExcelField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplyDTO {
    @ExcelField(title = "供应商编号", order = 1)
    private Integer id;
    @ExcelField(title = "供应商编码", order = 2)
    private String supplyCode;
    @ExcelField(title = "供应商名称", order = 3)
    private String supplyName;
    @ExcelField(title = "联系人", order = 4)
    private String contactName;
    @ExcelField(title = "联系电话", order = 5)
    private String contactPhone;
    @ExcelField(title = "联系地址", order = 6)
    private String contactAddress;
    @ExcelField(title = "传真", order = 7)
    private String fax;
    @ExcelField(title = "简介", order = 8)
    private String intros;
    @ExcelField(title = "创建时间", order = 9)
    private Timestamp createTime;

    private Timestamp modifyTime;

    private Boolean enable;

    private Boolean deleted;
}
