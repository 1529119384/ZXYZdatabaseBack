package uno.acloud.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 文件类型工具类
 * 返回：
 * 0 压缩包(zip/rar)
 * 1 word(doc/docx)
 * 2 ppt(ppt/pptx)
 * 3 excel(xls/xlsx)
 * 4 pdf
 * 5 图片(png/jpg/jpeg)
 * 6 音频(mp3)
 * 7 视频(mp4)
 * 8 txt
 * 9 其他/未知
 */
public final class FileTypeUtil {

    private FileTypeUtil() {}

    /* ===========================================
       魔数缓存 ——  key: 文件头16进制字符串（统一转小写）
       value: 返回码 0-9
    =========================================== */
    private static final Map<String, Integer> MAGIC_MAP = new HashMap<>();

    /* 后缀兜底映射（当前面没命中魔数时） */
    private static final Map<String, Integer> EXT_MAP = new HashMap<>();

    static {
        /* --- 压缩包 --- */
        MAGIC_MAP.put("504b0304", 0);   // zip
        MAGIC_MAP.put("52617221", 0);   // rar

        /* --- 图片 --- */
        MAGIC_MAP.put("89504e47", 5);   // png
        MAGIC_MAP.put("ffd8ffe0", 5);   // jpg（FFE0/FFE1/FFE2 都归到jpg）
        MAGIC_MAP.put("ffd8ffe1", 5);
        MAGIC_MAP.put("ffd8ffe2", 5);

        /* --- PDF --- */
        MAGIC_MAP.put("25504446", 4);   // %PDF

        /* --- 音频 --- */
        MAGIC_MAP.put("49443303", 6);   // ID3  → mp3

        /* --- 视频 --- */
        // mp4 没有统一文件头，这里用 ftyp 家族做近似
        MAGIC_MAP.put("00000018667479704d534e56", 7); // ftypmsnv
        MAGIC_MAP.put("000000186674797069736f6d", 7); // ftypisom
        MAGIC_MAP.put("000000206674797069736f6d", 7); // 20 字节偏移

        /* --- txt --- */
        // 无明确魔数，后面通过后缀兜底
    }

    static {
        /* 后缀兜底（当魔数没命中时） */
        // 压缩
        EXT_MAP.put("zip", 0);
        EXT_MAP.put("rar", 0);
        // word
        EXT_MAP.put("doc", 1);
        EXT_MAP.put("docx", 1);
        // ppt
        EXT_MAP.put("ppt", 2);
        EXT_MAP.put("pptx", 2);
        // excel
        EXT_MAP.put("xls", 3);
        EXT_MAP.put("xlsx", 3);
        // pdf
        EXT_MAP.put("pdf", 4);
        // 图片
        EXT_MAP.put("png", 5);
        EXT_MAP.put("jpg", 5);
        EXT_MAP.put("jpeg", 5);
        // 音频
        EXT_MAP.put("mp3", 6);
        // 视频
        EXT_MAP.put("mp4", 7);
        // txt
        EXT_MAP.put("txt", 8);
    }

    /**
     * 根据文件流+原始文件名 判断类型
     *
     * @param in              文件输入流（会读取前 28 字节，然后 reset）
     * @param originalFilename 原始文件名（含后缀）
     * @return 0-9 数字码
     */
    public static int classify(InputStream in, String originalFilename) throws IOException {
        if (in == null) return 9;

        /* 1. 读文件头（最多 28 字节） */
        byte[] header = new byte[28];
        in.mark(32);
        int read = in.read(header);
        in.reset();
        if (read <= 0) return 9;

        String hex = bytesToHex(header, read);

        /* 2. 魔数优先 */
        Integer code = MAGIC_MAP.get(hex);
        if (code != null) return code;

        /* 3. 前缀模糊匹配（mp4 有多头） */
        if (hex.startsWith("000000") && hex.contains("66747970")) {
            return 7; // mp4
        }

        /* 4. 后缀兜底 */
        String ext = getExtension(originalFilename);
        if (ext != null) {
            Integer extCode = EXT_MAP.get(ext.toLowerCase(Locale.ROOT));
            if (extCode != null) return extCode;
        }

        return 9;
    }

    /* ==================== 辅助方法 ==================== */

    private static String bytesToHex(byte[] src, int len) {
        StringBuilder sb = new StringBuilder(len * 2);
        for (int i = 0; i < len; i++) {
            sb.append(String.format("%02x", src[i]));
        }
        return sb.toString();
    }

    /* 如果不想引 commons-io，就换成自己写 */
    private static String getExtension(String filename) {
        if (filename == null) return null;
        int dot = filename.lastIndexOf('.');
        return (dot == -1 || dot == filename.length() - 1) ? null
                : filename.substring(dot + 1);
    }

    /* ==================== 快速测试 ==================== */
    public static void main(String[] args) throws Exception {
        java.io.File f = new java.io.File("D:/test/a.pptx");
        try (java.io.InputStream in = new java.io.FileInputStream(f)) {
            System.out.println("code = " + classify(in, f.getName()));
        }
    }
}