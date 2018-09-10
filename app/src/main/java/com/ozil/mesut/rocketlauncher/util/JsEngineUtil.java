package com.ozil.mesut.rocketlauncher.util;

import android.util.Log;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

/**
 * @author kui.liu
 * @time 2018/8/29 15:31
 */
public class JsEngineUtil {
    private Class clazz;
    private String allFunctions = "";

    public JsEngineUtil() {
        clazz = JsEngineUtil.class;
        initJsStr();
    }

    private void initJsStr() {
        //通过类名、类加载器、反射去加载JsEngine的类和方法
        allFunctions =
                " var ScriptAPI = java.lang.Class.forName(\"" + JsEngineUtil.class.getName() + "\", true, javaLoader);\n" +
                        " var methodGetValue=  ScriptAPI.getMethod(\"getValue\", [java.lang.String]);\n" +
                        " function getValue(key) {\n" +
                        "       return  methodGetValue.invoke(javaContext,key);\n" +
                        " }\n" +
                        " var methodSetValue=ScriptAPI.getMethod(\"setValue\",[java.lang.Object,java.lang.Object]);\n" +
                        " function setValue(key,value) {\n" +
                        "       methodSetValue.invoke(javaContext,key,value);\n" +
                        " }\n";
    }

    public void setValue(Object object1, Object object2) {
        Log.i("JsEngineUtil", "out : "
                + object1.toString() + " , " + object2.toString());
    }

    public String getValue(String keyStr) {
        Log.i("JsEngineUtil", "out : " + keyStr);
        return keyStr + keyStr;
    }

    public void runJsScript(String js) {
        String javaScript = allFunctions + "/n" + js;
        Context context = Context.enter();
        context.setOptimizationLevel(-1);

        ScriptableObject scriptable = context.initStandardObjects();
        //配置当前的Context
        ScriptableObject.putProperty(scriptable, "javaContext",
                Context.javaToJS(this, scriptable));
        //配置当前的类加载器
        ScriptableObject.putProperty(scriptable, "javaLoader",
                Context.javaToJS(clazz, scriptable));
        context.evaluateString(scriptable, javaScript, "JsEngineUtil",
                1, null);
        //当前的Context退出
        Context.exit();
    }

}
