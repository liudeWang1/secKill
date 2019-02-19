package com.maxwang.miaosha.vo;

import com.maxwang.miaosha.validator.IsMobile;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginVo {

    @NotNull
    @IsMobile
    private String mobile;

    @NotNull
    private String password;
}
