package com.why.hoolai.sango.cheater;

import java.io.File;
import java.io.FileReader;
import java.util.List;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ListEngineFactoryDemo {

	/**
	 * 打印Script引擎
	 */
	public static void printAllEngine() {
		ScriptEngineManager manager = new ScriptEngineManager();
		List<ScriptEngineFactory> factoryList = manager.getEngineFactories();
		for (ScriptEngineFactory factory : factoryList) {
			System.out.println(factory.getEngineName());
			System.out.println(factory.getEngineVersion());
			System.out.println(factory.getLanguageName());
			System.out.println(factory.getLanguageVersion());
			System.out.println(factory.getExtensions());
			System.out.println(factory.getMimeTypes());
			System.out.println(factory.getNames());
		}
	}

	/**
	 * 执行JavaScript代码
	 */
	public static void exeJSForCode() {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("js");
		String script = "print ('http://blog.163.com/nice_2012/')";
		try {
			engine.eval(script);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 执行JavaScript文件代码
	 */
	public static void exeJSForFile() {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("js");
		String jsPath = ListEngineFactoryDemo.class.getResource("/c_login_2.js").getPath();
		File file = new File(jsPath);
		if(!file.exists()){
			System.out.println("\n"+file.getAbsolutePath()+" not exists");
		}
		try {
			FileReader reader = new FileReader(file);
			engine.eval(reader);
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Binding AND Exception
	 */
	public static void exeJSForBinding() {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("js");
		engine.put("a", 1);
		engine.put("b", 5);
		Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
		Object a = bindings.get("a");
		Object b = bindings.get("b");
		System.out.println("a = " + a);
		System.out.println("b = " + b);
		try {
			Object result = engine.eval("c = a + b;");
			System.out.println("a + b = " + result);
		} catch (ScriptException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Function
	 */
	public static void exeJSForFunction() {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("js");
		try {
			engine.eval("function add (a, b) {c = a + b; return c; }");
			Invocable jsInvoke = (Invocable) engine;
			Object result1 = jsInvoke.invokeFunction("add", new Object[] { 10,
					5 });
			System.out.println(result1);
			Adder adder = jsInvoke.getInterface(Adder.class);
			int result2 = adder.add(10, 5);
			System.out.println(result2);
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Compilable
	 */
	public static void exeJSForCompilable() {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("js");
		Compilable jsCompile = (Compilable) engine;
		try {
			CompiledScript script = jsCompile
					.compile("function hi () {print ('hello" + "'); }; hi ();");
			for (int i = 0; i < 5; i++) {
				script.eval();
			}
		} catch (ScriptException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 执行 JavaScript
	 */
	public static void exeJavaScript() {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("javascript");
		try {
			Double hour = (Double) engine.eval("var date = new Date();"
					+ "date.getHours();");
			String msg;
			if (hour < 10) {
				msg = "Good morning";
			} else if (hour < 16) {
				msg = "Good afternoon";
			} else if (hour < 20) {
				msg = "Good evening";
			} else {
				msg = "Good night";
			}
			System.out.println(hour);
			System.out.println(msg);
		} catch (ScriptException e) {
			System.err.println(e);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		printAllEngine();
		exeJSForCode();
		exeJSForFile();
		exeJSForBinding();
		exeJSForFunction();
		exeJSForCompilable();
		exeJavaScript();
	}

}
