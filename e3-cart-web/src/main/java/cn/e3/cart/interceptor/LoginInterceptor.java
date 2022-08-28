package cn.e3.cart.interceptor;

import cn.e3.common.pojo.E3Result;
import cn.e3.common.util.CookieUtils;
import cn.e3.pojo.TbUser;
import cn.e3.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义拦截器
 *
 * @author yesyoungbaby
 * @date 2021/12/9 20:18
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;

    /**
     *  执行handler之前执行此方法。
     *    做购物车之前判断用户是否登录的逻辑
     *    结果是只判断了用户是否登录，所有情况都是放行
     * @param httpServletRequest
     * @param httpServletResponse
     * @param handler
     * @return 返回true，放行	 返回false：拦截
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        //1.从cookie中取token
        String token = CookieUtils.getCookieValue(httpServletRequest, "token");

        //2.如果没有token，未登录状态，直接放行
        if (StringUtils.isBlank(token)) {
            return true;
        }
        //3.取到token，需要调用sso系统的服务，根据token取用户信息
        E3Result e3Result = tokenService.getUserByToken(token);

        //4.没有取到用户信息。redis中用户登录信息过期，直接放行。
        if (e3Result.getStatus() != 200) {
            return true;
        }

        //5.取到用户信息。登录状态。
        TbUser user = (TbUser) e3Result.getData();

        //6.把用户信息放到request中。只需要在Controller中判断request中是否包含user信息。放行
        httpServletRequest.setAttribute("user", user);
        return true;
    }

    /**
     * handler执行之后，返回ModeAndView之前执行该方法
     * @param httpServletRequest
     * @param httpServletResponse
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 完成handler处理，并在返回ModelAndView之后执行该方法。
     * @param httpServletRequest
     * @param httpServletResponse
     * @param handler
     * @param e
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, Exception e) throws Exception {

    }
}
