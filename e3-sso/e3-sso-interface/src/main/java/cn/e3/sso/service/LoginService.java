package cn.e3.sso.service;

import cn.e3.common.pojo.E3Result;

/**
 * TODO
 *
 * @author yesyoungbaby
 * @date 2021/12/7 15:27
 */
public interface LoginService {

    /**
     * 登录逻辑
     * @param username
     * @param password
     * @return
     */
    E3Result login(String username, String password);
}
