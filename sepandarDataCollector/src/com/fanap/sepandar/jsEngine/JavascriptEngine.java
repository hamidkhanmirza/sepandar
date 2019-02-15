package com.fanap.sepandar.jsEngine;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.IOUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Map;

/**
 * Created by admin123 on 2/14/2019.
 */
public class JavascriptEngine {

    static Map<String, String> file2JavascriptContentMap = new HashedMap();

    public static String evaluateInput(String fileName, String starterFunctionName, String inputData) throws Exception {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        String javascriptContent;
        javascriptContent = file2JavascriptContentMap.get(fileName);

        if (javascriptContent == null) {
            InputStream inputStream = (BufferedInputStream) JavascriptEngine.class.getResource("/jsParsers/" + fileName + ".js").getContent();
            StringWriter writer = new StringWriter();
            IOUtils.copy(inputStream, writer, "UTF-8");
            javascriptContent = writer.toString();
            file2JavascriptContentMap.put(fileName, javascriptContent);
        }

        engine.eval(javascriptContent);

        Invocable invocable = (Invocable) engine;
        return (String) invocable.invokeFunction(starterFunctionName, inputData);
    }

    public static void main(String[] args) throws Exception {
//        evaluateInput();
    }
}
