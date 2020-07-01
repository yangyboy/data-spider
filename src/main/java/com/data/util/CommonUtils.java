package com.data.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Slf4j
public class CommonUtils {
    /**
     * 读取json文件，返回json串
     *
     * @param fileName
     * @return
     */
    public static String readJsonFile(String fileName) {
        String jsonStr;
        FileReader fileReader = null;
        try {
            File jsonFile = new File(fileName);
            fileReader = new FileReader(jsonFile);

            int ch;
            StringBuffer sb = new StringBuffer();
            while ((ch = fileReader.read()) != -1) {
                sb.append((char) ch);
            }
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            log.error("读取文件：{} 失败", fileName, e);
            return null;
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    log.error("关闭文件流失败", e);
                }

            }
        }
    }
}
