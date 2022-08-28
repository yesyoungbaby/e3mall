package cn.e3.sso.service.impl;

import cn.e3.common.pojo.E3Result;
import cn.e3.mapper.TbUserMapper;
import cn.e3.pojo.TbUser;
import cn.e3.pojo.TbUserExample;
import cn.e3.sso.service.RegisterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * TODO
 *
 * @author yesyoungbaby
 * @date 2021/12/7 9:52
 */
@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private TbUserMapper userMapper;

    @Override
    public E3Result checkInfo(String param, int type) {
        //根据不同的type(校验信息类型)生成不同的查询条件
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();

        //根据type设置查询条件
        // type决定要验证的信息  1：用户名 2：手机号 3：邮箱
        if(type == 1) {
            criteria.andUsernameEqualTo(param);
        }else if(type == 2) {
            criteria.andPhoneEqualTo(param);
        }else if(type == 3){
            criteria.andEmailEqualTo(param);
        }else {
            return E3Result.build(400, "数据类型错误");
        }

        //执行查询
        List<TbUser> list = userMapper.selectByExample(example);

        // 库中已有该信息 res的data设置为fales
        if (list != null && list.size()>0) {
            return E3Result.ok(false);
        }
        //如果没有返回true
        return E3Result.ok(true);
    }

    @Override
    public E3Result register(TbUser user) {
        if(StringUtils.isBlank(user.getUsername()) ||
                StringUtils.isBlank(user.getPassword()) ||
                StringUtils.isBlank(user.getPhone())) {
            return E3Result.build(400, "注册信息不完整，请检查");
        }

        // 走注册逻辑时需要再走一遍？
        // 手机号和手机不能重复
        E3Result res = checkInfo(user.getUsername(), 1);
        // 如果库中有该消息，data值为fals
        if(!(boolean)res.getData()) {
            return E3Result.build(400, "此用户名已经被占用");
        }
        res = checkInfo(user.getPhone(), 2);
        if (!(boolean)res.getData()) {
            return E3Result.build(400, "手机号已经被占用");
        }

        // 通过校验补全信息
        user.setCreated(new Date());
        user.setUpdated(new Date());

        //密码加密
        String md5pwd = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5pwd);

        // 落库
        userMapper.insert(user);

        return E3Result.ok();
    }
}
