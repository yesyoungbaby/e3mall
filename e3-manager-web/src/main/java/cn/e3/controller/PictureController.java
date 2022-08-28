package cn.e3.controller;

import cn.e3.common.util.FastDFSClient;
import cn.e3.common.util.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author yesyoungbaby
 * @date 2021/10/27 16:24
 */
@Controller
public class PictureController {

    @Value("${IMAGE_SERVER_URL}")
    private String IMAGE_SERVER_URL;

    /**
     * 上传文件
     * @param uploadFile 接收到的参数  注意类型
     * @return 注意类型  若直接返回Map会存在浏览器兼容问题
     */
    @RequestMapping(value = "/pic/upload")
    @ResponseBody
    public String fileUpload(MultipartFile uploadFile) {
        try {
            // 1.取文件的扩展名，jpg pgn
            String originalFilename = uploadFile.getOriginalFilename();
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

            //2、创建FastDFS的客户端
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/client.conf");

            //3、执行上传处理
            String path = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);

            //4、拼接返回的url和ip地址，拼装成完整的url
            String url = IMAGE_SERVER_URL + path;

            //5、按前端需求格式返回结果，封装到map
            Map result = new HashMap<>();
            result.put("error", 0);
            result.put("url", url);

            //6、转换为json格式的字符串
            return JsonUtils.objectToJson(result);

        } catch (Exception e) {
            e.printStackTrace();

            Map result = new HashMap<>();
            result.put("error", 1);
            result.put("message", "图片上传失败");
            return JsonUtils.objectToJson(result);
        }
    }

}
