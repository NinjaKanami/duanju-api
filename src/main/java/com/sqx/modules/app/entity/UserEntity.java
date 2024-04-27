package com.sqx.modules.app.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 用户
 *
 */
@Data
@ApiModel("用户")
@TableName("tb_user")
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@Excel(name = "用户id", orderNum = "1")
	@ApiModelProperty("用户id")
	@TableId(type = IdType.AUTO, value = "user_id")
	private Long userId;
	/**
	 * 用户名
	 */
	@Excel(name = "用户昵称", orderNum = "2")
	@ApiModelProperty("用户名")
	@TableField("user_name")
	private String userName;

	/**
	 * 每日更换昵称次数
	 */
	private Integer userNameNum;

	/**
	 * 手机号
	 */
	@Excel(name = "手机号", orderNum = "4")
	@ApiModelProperty("手机号")
	private String phone;

	/**
	 * 头像
	 */
	@Excel(name = "头像", orderNum = "3")
	@ApiModelProperty("头像")
	private String avatar;

	/**
	 * 每日更换头像次数
	 */
	private Integer avatarNum;

	/**
	 * 性别 1男 2女
	 */
	@ApiModelProperty("性别 1男 2女")
	private Integer sex;

	/**
	 * 微信小程序openid
	 */
	@ApiModelProperty("微信小程序openid")
	@TableField("open_id")
	private String openId;

	/**
	 * 微信小程序openid
	 */
	@ApiModelProperty("微信公众号openid")
	@TableField("wx_id")
	private String wxId;

	/**
	 * 微信app openid
	 */
	@ApiModelProperty("微信app openid")
	@TableField("wx_open_id")
	private String wxOpenId;

	/**
	 * 抖音小程序openId
	 */
	private String dyOpenId;

	/**
	 * 快手小程序openId
	 */
	private String ksOpenId;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 创建时间
	 */
	@Excel(name = "创建时间", orderNum = "13", width = 18)
	@TableField("create_time")
	private String createTime;

	/**
	 * 更新时间
	 */
	@TableField("update_time")
	private String updateTime;

	/**
	 * 苹果id
	 */
	@TableField("apple_id")
	private String appleId;

	/**
	 * 手机类型 1安卓 2ios
	 */
	@TableField("sys_phone")
	private Integer sysPhone;

	/**
	 * 状态 1正常 2禁用
	 */
	@Excel(name = "状态", orderNum = "13", replace = {"正常_1", "禁用_1"})
	private Integer status;

	/**
	 * 来源 app 小程序 公众号
	 */
	@Excel(name = "渠道来源", orderNum = "9")
	private String platform;

	/**
	 * 积分
	 */
	private Integer jifen;

	/**
	 * 邀请码
	 */
	@Excel(name = "邀请码", orderNum = "5")
	@TableField("invitation_code")
	private String invitationCode;

	/**
	 * 邀请人邀请码
	 */
	@Excel(name = "邀请人邀请码", orderNum = "6",width = 15)
	@TableField("inviter_code")
	private String inviterCode;

	private String clientid;

	@Excel(name = "支付宝账号", orderNum = "8", width = 18)
	private String zhiFuBao;

	@Excel(name = "支付宝名称", orderNum = "8", width = 18)
	private String zhiFuBaoName;

	private String wxImg;

	/**
	 * 最后一次在线时间
	 */
	private String onLineTime;

	/**
	 * 渠道码
	 */
	private String qdCode;

	/**
	 * 第几级剧荐管用户 1 2 3总共三级用户
	 */
	private Integer qdUserType;

	/**
	 * 是否是新用户 1否
	 */
	private Integer isNewUser;

	/**
	 * 代剧达人 1普通 2高级
	 */
	private Integer agencyIndex;

	/**
	 * 达人到期时间
	 */
	private String agencyEndTime;

	/**
	 * 达人开始时间
	 */
	private String agencyStartTime;

	/**
	 * 剧荐管标识
	 */
	private String channelCode;

	/**
	 * 剧荐管 1是
	 */
	private Integer isChannel;

	/**
	 * 渠道商推荐人
	 */
	private Long recommendUserId;

	/**
	 * 推荐人 1是
	 */
	private Integer isRecommend;

	/**
	 * 身份证号
	 */
	private String idNumberNo;

	/**
	 * 身份证到期时间
	 */
	private String idNumberEndTime;

	/**
	 * 真实姓名
	 */
	private String idNumberName;

	/**
	 * 是否同意自动扣费协议
	 */
	private Integer autoPrice;

	@TableField(exist = false)
	private String sysUserName;


	@TableField(exist = false)
	private Integer member;

	@TableField(exist = false)
	private Integer counts;

	@TableField(exist = false)
	private BigDecimal money;

	@TableField(exist = false)
	private String endTime;

	@TableField(exist = false)
	private Integer vipType;


}
