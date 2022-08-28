package cn.e3.sso.service.impl;

import cn.e3.common.pojo.E3Result;
import cn.e3.common.util.JedisClient;
import cn.e3.common.util.JsonUtils;
import cn.e3.pojo.TbUser;
import cn.e3.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TODO
 *
 * @author yesyoungbaby
 * @date 2021/12/8 17:07
 */
@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private JedisClient jedisClient;

    /**
     *
     * @param token 从哪来？客户端的url cookie
     * @return
     */
    @Override
    public E3Result getUserByToken(String token) {
        // 从redis中取用户信息，k是token的值，v是用户信息
        String json = jedisClient.get("SESSION:" + token);
        // 如果为空，返回登录过期的提示信息
        if(StringUtils.isBlank(json)) {
            return E3Result.build(202,"用户登录信息过期");
        }
        // 取到了，先更新token的过期时间 30min
        jedisClient.expire(token, 1800);
        // 换成USER返回给客户端
        TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
        return E3Result.ok(user);
    }
}
