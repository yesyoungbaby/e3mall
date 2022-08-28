package cn.e3.sso.service;

import cn.e3.common.pojo.E3Result;
import cn.e3.pojo.TbUser;

/**
 * 实现sso的核心部分：
 *      各系统（首页，详情，购物回显用户信息）根据token查询登录信息
 *
 * @author yesyoungbaby
 * @date 2021/12/8 17:03
 */
public interface TokenService {

    /**
     * 根据token查用户信息
     * @param token
     * @return
     */
    E3Result getUserByToken(String token);
}
