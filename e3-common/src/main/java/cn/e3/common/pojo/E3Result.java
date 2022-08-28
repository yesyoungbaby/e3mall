package cn.e3.common.pojo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * json格式的响应数据类
 * 联系http响应
 * @author
 */
public class E3Result implements Serializable{


    /**
     * 响应请求的状态
     */
    private Integer status;

    /**
     * 提示消息
     */
    private String msg;

    /**
     * 响应请求的数据
     */
    private Object data;

    /**
     * 定义jackson对象
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();


    /**
     * 有参构造方法   是不是该私有访问权限
     * @param status
     * @param msg
     * @param data
     */
    public E3Result(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 请求响应成功时的构造方法，状态默认给200
     * @param data
     */
    public E3Result(Object data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    /**
     * 空构造
     */
    public E3Result() {

    }

    /**
     * 静态方法，不依赖对象，当类被加载后即可通过类名进行调用
     * @param status
     * @param msg
     * @param data
     * @return
     */
    public static E3Result build(Integer status, String msg, Object data) {

        return new E3Result(status, msg, data);
    }

    /**
     * 无data的响应
     * @param status
     * @param msg
     * @return
     */
    public static E3Result build(Integer status, String msg) {

        return new E3Result(status, msg, null);
    }

    /**
     * 响应成功
     * @param data
     * @return
     */
    public static E3Result ok(Object data) {

        return new E3Result(data);
    }

    /**
     * 无data响应成功
     * @return
     */
    public static E3Result ok() {

        return new E3Result(null);
    }

    /**
     * 将包含data的json字符串转化为E3Result对象
     * @param jsonData json字符串
     * @param clazz E3Result data的object类型
     * @return  返回类型
     */
    public static E3Result formatToPojo(String jsonData, Class<?> clazz) {
        try {
            if (clazz == null) {
                return MAPPER.readValue(jsonData, E3Result.class);
            }

            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (clazz != null) {
                if (data.isObject()) {
                    obj = MAPPER.readValue(data.traverse(), clazz);
                } else if (data.isTextual()) {
                    obj = MAPPER.readValue(data.asText(), clazz);
                }
            }
            return build(jsonNode.get("status").intValue(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 没有object对象的转化
     *
     * @param json
     * @return
     */
    public static E3Result format(String json) {
        try {
            return MAPPER.readValue(json, E3Result.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将json字符串格式的list转换为包含data结果集的e3
     *
     * @param jsonData json数据
     * @param clazz 集合中的类型
     * @return
     */
    public static E3Result formatToList(String jsonData, Class<?> clazz) {
        try {
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (data.isArray() && data.size() > 0) {
                obj = MAPPER.readValue(data.traverse(),
                        MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
            }
            return build(jsonNode.get("status").intValue(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
