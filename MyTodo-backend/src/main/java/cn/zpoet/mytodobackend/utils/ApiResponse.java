package cn.zpoet.mytodobackend.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private int code;   // 状态码（200、400、500等）
    private String msg; // 消息（"成功"、"用户名或密码错误"等）
    private T data;     // 具体数据（比如 token、用户信息等）

    // 成功响应
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "Success", data);
    }

    // 失败响应
    public static <T> ApiResponse<T> error(int code, String msg) {
        return new ApiResponse<>(code, msg, null);
    }
}
