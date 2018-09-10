package com.ozil.mesut.rocketlauncher.ui.activity;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.ozil.mesut.rocketlauncher.R;
import com.ozil.mesut.rocketlauncher.base.BaseActivity;
import com.ozil.mesut.rocketlauncher.util.FileUtil;
import com.ozil.mesut.rocketlauncher.view.LinearTabGroup;
import com.ozil.mesut.rocketlauncher.view.TabGroup;
import com.ozil.mesut.rocketlauncher.view.TabView;

import org.mozilla.javascript.ConsString;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;


/**
 * @author kui.liu
 * @time 2018/7/26 9:13
 */
public class FourteenActivity extends BaseActivity implements View.OnClickListener {
    private WebView webView;
    private String mData;
    private String mutiply;
    private String ts;
    private String sumn;
    private LinearTabGroup mTabContainer;

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        mData = FileUtil.getJson(this, R.raw.fix_assets_transer_json);
        mutiply = FileUtil.getJson(this, R.raw.mutiply);
        ts = FileUtil.getJson(this, R.raw.ts);
        sumn = FileUtil.getJson(this, R.raw.sumn);

        // 获取WebView的设置
        WebSettings webSettings = webView.getSettings();
        //设置编码
        webSettings.setDefaultTextEncodingName("utf-8");
        //设置背景颜色 透明
        webView.setBackgroundColor(Color.argb(0, 0, 0, 0));
        // 将JavaScript设置为可用，这一句话是必须的，不然所做一切都是徒劳的
        webSettings.setJavaScriptEnabled(true);
        // 通过webview加载html页面
        webView.loadUrl("file:///android_asset/process_parse.html");
        // 给webview添加JavaScript接口
        webView.addJavascriptInterface(new JsInterface(), "obj");
    }

    @Override
    protected void initView() {
        webView = (WebView) findViewById(R.id.activity_webview);
        mTabContainer = findViewById(R.id.ll_tab_group);

        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
        findViewById(R.id.btn5).setOnClickListener(this);
        findViewById(R.id.btn6).setOnClickListener(this);
        findViewById(R.id.btn7).setOnClickListener(this);

        //添加tab
        /*for (int i = 0; i < 5; i++) {
            TabView tabView = new TabView(this);
            tabView.setTabText("选择项" + i);
            tabView.setTextSize(16);
            //            tabView.setSelectedBitmap();
            tabView.setDrawableWidth(15);
            tabView.setDrawableHeight(15);
            tabView.setSelectedDrawable(R.mipmap.xuanzhong);
            tabView.setNormalDrawable(R.mipmap.weixuanzhong);
            tabView.setNormalColor(Color.BLACK);
            tabView.setSelectedColor(Color.BLUE);
            mTabContainer.addView(tabView);
        }*/
        mTabContainer.setOnTabSelectedListener(new TabGroup.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabView tab, int position, TabView[] tabs, TabGroup parent) {
                Toast.makeText(FourteenActivity.this, "position = " + position + " selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabCancle(TabView tab, int position, TabView[] tabs, TabGroup parent) {
                Toast.makeText(FourteenActivity.this, "position = " + position + " canceled", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_fourteen;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                webView.loadUrl("javascript:funFromjs()");
                break;
            case R.id.btn2:
                webView.loadUrl("javascript:funJs('Android端传入的信息，div标签中显示,含参数')");
                break;
            case R.id.btn3:
                webView.loadUrl("javascript:sum(6,6)");
                break;
            case R.id.btn4:
                /***
                 * Android 4.4之后使用evaluateJavascript即可。这里展示一个简单的交互示例
                 *
                 */
                webView.evaluateJavascript("sum(6,11)", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Toast.makeText(FourteenActivity.this, "返回值" + value, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.btn5:
                //脚本如何控制
                //                        String js = "ts('atData'" + ", " + mData + ")";
                String js = "ts('atData[0].assetsNo'" + ", " + mData + ")";
                //                String js = "ts3(" + mData + ")";
                //                String js = "ts( \"" + "procInsId" + "\"" + ",\"" + mData + "\"" + ")";
                String js1 = "ts(10, 10)";
                String js2 = "ts('a', 'b')";
                webView.evaluateJavascript(js, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Toast.makeText(FourteenActivity.this, "返回值" + value, Toast.LENGTH_SHORT).show();
                        Log.i("Fourteen", "value = " + value);

                                /*try {
                                    JSONArray jsonArray = new JSONArray(value);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        Object o = jsonArray.get(i);

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }*/
                    }
                });
                break;
            case R.id.btn6:
                String js4 = "ts1( \"" + "procInsId" + "\"" + ",\"" + mData + "\"" + ")";
                webView.evaluateJavascript(js4, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Toast.makeText(FourteenActivity.this, "返回值" + value, Toast.LENGTH_SHORT).show();
                        Log.i("Fourteen", "result = " + value);
                    }
                });
                break;
            case R.id.btn7:
               /* String result
                        = runScript(JAVA_CALL_JS_FUNCTION2, "mutiply", new Integer[]{10, 2});*/
                //                String result = runScript(JAVA_CALL_JS_FUNCTION1, "ts", new String[]{"procInsId", mData});
                //                String result = runScript(sumn, "sumn", new Integer[] {10, 2});
                //                String data = new Gson().toJson(mData);
                String result = runScript(ts, "ts", new String[]{"atData[0].assetsName", mData});
                Toast.makeText(FourteenActivity.this, result, Toast.LENGTH_SHORT).show();
                Log.i("Fourteen", "sumn = " + result);
            default:
                break;
        }
    }

    public String runScript(String js, String functionName, Object[] functionParams) {
        Context rhino = Context.enter();
        rhino.setOptimizationLevel(-1);
        try {
            Scriptable scope = rhino.initStandardObjects();

            ScriptableObject.putProperty(scope, "javaContext",
                    Context.javaToJS(FourteenActivity.this, scope));

            ScriptableObject.putProperty(scope, "javaLoader",
                    Context.javaToJS(FourteenActivity.class.getClassLoader(), scope));

            rhino.evaluateString(scope, js, "FourteenActivity", 1, null);

            Function function = (Function) scope.get(functionName, scope);

            Object result = function.call(rhino, scope, scope, functionParams);
            if (result instanceof String) {
                return (String) result;
            } else if (result instanceof NativeJavaObject) {
                return (String) ((NativeJavaObject) result).getDefaultValue(String.class);
            } else if (result instanceof NativeObject) {
                return (String) ((NativeObject) result).getDefaultValue(String.class);
            } else if (result instanceof ConsString) {
                return result.toString();
            } else if (result instanceof NativeArray) {
                return (String) ((NativeArray) result).getDefaultValue(String.class);
            }
            return result.toString();
        } finally {
            Context.exit();
        }
    }

    private static final String JAVA_CALL_JS_FUNCTION = "function Test(){ " +
            "return '农民伯伯 java call js Rhino'; " +
            "}";

    private static final String JAVA_CALL_JS_FUNCTION1 = "function ts(field,data){ " +
            "var result = eval('data.' + field);" +
            "return result;" +
            "}";

    private static final String JAVA_CALL_JS_FUNCTION2 = "function mutiply(x, y) { " +
            "var result = x * y;" +
            "return result;" +
            "}";

    private static final String JAVA_CALL_JS_FUNCTION3 = "function sumn(x, y) {" +
            "var result = x + y;" +
            "return result;" +
            "}";


    public class JsInterface {
        //JS中调用Android中的方法 和返回值处理的一种方法

        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(FourteenActivity.this, "你的商品价格是：￥" + toast, Toast.LENGTH_SHORT).show();
        }

        /***
         * Android代码调用获取J是中的返回值
         *
         * @param result
         */
        @JavascriptInterface
        public void onSum(int result) {
            Toast.makeText(FourteenActivity.this, "Android调用JS方法且有返回值+计算结果==" + result, Toast.LENGTH_SHORT).show();
        }

    }

}
