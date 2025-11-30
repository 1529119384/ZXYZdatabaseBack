package uno.acloud.utils;

import java.util.UUID;

public class FileNameUtil {
    /**
     * 生成 uuid 文件名，保留原扩展名
     * 例：  hello world.PPTX  ->  7f3a2c1e-4b9d-4f64-a66d-27a2c92b63f0.pptx
     */
    public static String uuidName(String originalFilename) {
        String ext = getExtension(originalFilename);   // 自己写的工具，空串或null时返回""
        return UUID.randomUUID().toString() + (ext.isEmpty() ? "" : "." + ext);
    }

    private static String getExtension(String name) {
        if (name == null) return "";
        int dot = name.lastIndexOf('.');
        return (dot == -1 || dot == name.length() - 1) ? ""
                : name.substring(dot + 1).toLowerCase();
    }
}