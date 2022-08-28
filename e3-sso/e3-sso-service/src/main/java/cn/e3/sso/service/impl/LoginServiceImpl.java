package cn.e3.sso.service.impl;

import cn.e3.common.pojo.E3Result;
import cn.e3.common.util.JedisClient;
import cn.e3.common.util.JsonUtils;
import cn.e3.mapper.TbUserMapper;
import cn.e3.pojo.TbUser;
import cn.e3.pojo.TbUserExample;
import cn.e3.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;

/**
 * TODO
 *
 * @author yesyoungbaby
 * @date 2021/12/7 15:27
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private TbUserMapper userMapper;
    @Autowired
    private JedisClient jedisClient;

    @Override
    public E3Result login(String username, String password) {
        // 根据参数进行登录校验
        // 首先校验用户名
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> list = userMapper.selectByExample(example);
        if(list == null || list.size() == 0) {
            return E3Result.build(400, "该用户不存在");
        }
        // 然后校验密码
        TbUser user = list.get(0);
        if(!user.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
            return E3Result.build(400, "密码错误");
        }

        //信息正确，生成登录凭证token
        String token = UUID.randomUUID().toString();
        //用户信息存入redis，但是不要把密码带到客户端
        user.setPassword(null);
        // token是k user信息是v
        jedisClient.set("SESSION:" + token, JsonUtils.objectToJson(user));
        jedisClient.expire("SESSION:" + token, 1800);

        // 返回token给客户端
        return E3Result.ok(token);
    }
}
