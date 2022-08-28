package cn.e3.test;


import cn.e3.common.util.FastDFSClient;

/**
 *
 *
 * @author yesyoungbaby
 * @date 2021/10/26 15:18
 */
public class FastDFSTest {
    public static void main(String[] args) throws Exception {

        // 上传测试
        FastDFSClient fastDFSClient = new FastDFSClient("E:\\e3-new\\e3-manager-web\\src\\main\\resources\\conf\\client.conf");
        String file = fastDFSClient.uploadFile("C:\\Users\\yesyoungbaby\\Pictures\\e3\\1636084979(1).jpg");
        System.out.println(file);



    }
}
