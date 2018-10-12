package com.tp.admin.data.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class TerLog {

    private int id;
    private String code;
    private int state;
    private String info;
    private int orderId;
    private Timestamp createTime;
    /*
     * needWait:标记热保护、补水等状态
     * 0:无需等待
     * 1:热保护等待
     * 2:补水等待
     */
    private int needWait; //标记热保护、补水等状态
    private boolean valid; //是否可以继续洗车
    private String detail; // 详情json

    /*
     * doneType:以何种方式完成订单/服务
     * 0:正常结束
     * 1:因故障结束
     * 2:超时没有二次启动而结束
     * 3:因暂停机制而结束
     */
    // private int doneType;

    /*
     * device:元件连接情况
     * 0:ok
     * 1:机械连接失败
     * 2:MODBUS连接失败
     * 4:PLC连接失败
     */
    //private int device;
    /*
     * errCode
     * 0:OK
     * 1:机械故障
     * 2:modbus故障
     * 4:plc故障
     */
    //private int errCode;
    /*
     * carState
     * 0:OK
     * 1:车辆太长
     * 2:车位置不对
     * 3:没有车(暂停的时候)
     */
//    private int carState;


}
