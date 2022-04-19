package com.xxxx.seckill.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author Winky
 * @since 2022-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TUser对象", description="用户表")
public class TUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID,手机号码")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String nickname;

    @ApiModelProperty(value = "MD5(MD5(pass明文+固定salt)+salt)")
    private String password;

    private String salt;

    @ApiModelProperty(value = "头像")
    private String head;

    @ApiModelProperty(value = "注册时间")
    private Date registerDate;

    @ApiModelProperty(value = "最后一次登录事件")
    private Date lastLoginDate;

    @ApiModelProperty(value = "登录次数")
    private Integer loginCount;


}
