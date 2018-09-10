package com.ozil.mesut.rocketlauncher.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author kui.liu
 * @time 2018/7/26 9:28
 */
public class FileUtil {

    public static String getJson(Context context, int resId) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            InputStream inputStream = context.getResources().openRawResource(resId);
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(
                    new InputStreamReader(inputStream));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
