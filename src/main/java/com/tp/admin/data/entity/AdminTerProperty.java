package com.tp.admin.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminTerProperty {
    private Integer id;
    private Integer terId;
    private Integer netMethod;
    private String videoControl;
    private Integer bubbleLimit;
    private String terClientVersion;
    private Integer terBusiMode;
    private String terModel;
    private String terRemark;
    private Integer highLimit;
    private Integer wideLimit;
    private boolean adExist;
    private Integer screenWide;
    private Integer screenHigh;
}
