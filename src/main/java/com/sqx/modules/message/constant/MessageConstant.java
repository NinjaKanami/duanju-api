package com.sqx.modules.message.constant;

public class MessageConstant {
    // State字段
    public static final Integer StateNotice = 1;   // 公告
    public static final Integer StateFeedBack = 2; // 用户反馈
    public static final Integer StateSystem = 3;   // 系统消息
    public static final Integer StateOrder = 4;    // 订单信息
    public static final Integer StateUser = 5;     // 用户消息(用户端通知消息模块会查这个State的消息)
    public static final Integer StateCustom = 6;   // 客服消息


    // IsSee字段, 是否已读
    public static final Integer IsSeeNo = 0;  // 未读
    public static final Integer IsSeeYes = 2; // 已读
    // NOTE: 1和2似乎都是已读，外包交过来就没有定义常量，这里具体几是已读已经无法考究了


}