package cn.e3.search.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理器
 *
 * @author yesyoungbaby
 * @date 2021/12/3 19:14
 */
public class GlobalExceptionResolver implements HandlerExceptionResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest,
                                         HttpServletResponse httpServletResponse,
                                         Object handler,
                                         Exception e) {
        // 异常打印到服务端控制台
        e.printStackTrace();
        // 异常写入日志  不同的日志级别
        LOGGER.info("发生异常", e);
        LOGGER.error("发生异常", e);
        // 发生异常后的动作 邮件、短信 这里是返回一场页面
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error/exception");
        return modelAndView;

    }
}
