package com.cy.decrypt;

import cn.hutool.json.JSONUtil;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * 解码的备用方法，运行JavaScript脚本，速度较慢
 *
 * @author CY
 */
public class ImoocDecoder {

    /**
     * JavaScript解码算法执行器
     */
    private static Invocable invocable;

    /* 读取JS文件，并构造执行器 */
    static {
        InputStream inputStream = ImoocDecoder.class.getClassLoader().getResourceAsStream("decode.js");
        if (inputStream != null) {
            String javaScript = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("Nashorn");
            try {
                engine.eval(javaScript);
            } catch (ScriptException e) {
                e.printStackTrace();
            }
            invocable = (Invocable) engine;
        }
    }

    /**
     * 解码成字符串
     *
     * @param crypto 待解码的字符串
     * @return 解码后的字符串
     */
    public static String decoderString(String crypto) {
        return invoke("decode2String", crypto);
    }

    /**
     * 获取慕课网视频的密匙key
     *
     * @param crypto 待解码的字符串
     * @return 字节数组
     */
    public static byte[] decoderKey(String crypto) {
        return JSONUtil.toBean(invoke("decode2Bytes", crypto), byte[].class);
    }

    /**
     * 调用JavaScript方法
     *
     * @param methodName   方法名
     * @param encodeString 待解码的字符串
     * @return 解码后的结果
     */
    private static String invoke(String methodName, String encodeString) {

        try {
            return invocable.invokeFunction(methodName, encodeString).toString();
        } catch (ScriptException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
