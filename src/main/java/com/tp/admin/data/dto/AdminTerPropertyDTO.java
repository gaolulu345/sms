package com.tp.admin.data.dto;

import com.github.crab2died.annotation.ExcelField;
import com.tp.admin.enums.AdminNetMethodEnum;
import com.tp.admin.enums.AdminTerBusinessModeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminTerPropertyDTO {

    @ExcelField(title = "设备Id",order = 1)
    private Integer id;
    @ExcelField(title = "设备关联网点Id",order = 2)
    private Integer terId;
    private Integer netMethod;
    @ExcelField(title = "视频供应商",order = 4)
    private String videoControl;
    @ExcelField(title = "泡沫使用限制",order = 5)
    private Integer bubbleLimit;
    @ExcelField(title = "网点客户端版本",order = 6)
    private String terClientVersion;
    private Integer terBusiMode;
    @ExcelField(title = "网点型号",order = 8)
    private String terModel;
    @ExcelField(title = "网点信息",order = 9)
    private String terRemark;
    @ExcelField(title = "限高",order = 10)
    private Integer highLimit;
    @ExcelField(title = "限宽",order = 11)
    private Integer wideLimit;
    private boolean adExist;
    private Integer screenWide;
    private Integer screenHigh;
    private String propertyRemark;
    @ExcelField(title = "联网方式",order = 3)
    private String netMethodDesc;//联网方式描述
    @ExcelField(title = "网点运营模式",order = 7)
    private String terBusiModeDesc;//网店运营模式描述

    public void build(){
        if (terBusiMode != null){
            this.terBusiModeDesc = AdminTerBusinessModeEnum.getByValue(this.terBusiMode).getDesc();
        }
        if (netMethod != null){
            this.netMethodDesc = AdminNetMethodEnum.getByValue(this.netMethod).getDesc();
        }
    }




}
