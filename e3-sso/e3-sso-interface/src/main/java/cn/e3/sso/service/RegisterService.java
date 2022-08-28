package cn.e3.sso.service;

import cn.e3.common.pojo.E3Result;
import cn.e3.pojo.TbUser;

/**
 * 用户注册服务
 *
 * @author yesyoungbaby
 * @date 2021/12/7 9:50
 */
public interface RegisterService {

    /**
     * 注册信息校验，确保注册的信息是库中没有的
     * @param param
     * @param type
     * @return
     */
    E3Result checkInfo(String param, int type);

    /**
     * 上面的校验通过后，还应该有个方法，确保用户填的信息是合规范的
     * 如 手机号位数  名字的格式等等
     * @param param
     * @param type
     * @return
     *//*
    E3Result checkAvailable(String param, int type);*/

    /**
     * 用户注册逻辑
     * @param user
     * @return
     */
    E3Result register(TbUser user);
}
