package com.plasticene.fast.service;

import com.plasticene.fast.param.LoginParam;
import com.plasticene.fast.vo.LoginVO;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2023/1/4 16:53
 */
public interface AuthService {

    LoginVO login(LoginParam param);


}
