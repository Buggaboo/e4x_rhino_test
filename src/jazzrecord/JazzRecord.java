package jazzrecord;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;

public class JazzRecord {
	static Scriptable scope;

	/**
	 * @param args
	 */
	private static Context setupJSContext() {
		ContextFactory contextFactory = new ContextFactory();
		return contextFactory.enterContext();
	}

	private static Scriptable setupJSScriptableScope(Context cx) {
		// Setup javascript context
		cx.setLanguageVersion(Context.VERSION_1_7);
		return cx.initStandardObjects();
	}

	private static void add_mysql_connect() {
		Class klass;
		try {
			klass = Class.forName("jazzrecord.MysqlDBConnect");
			Class arg_types[] = new Class[4];
			Class str_klass_obj = Class.forName("java.lang.String");
			arg_types[0] = str_klass_obj;
			arg_types[1] = str_klass_obj;
			arg_types[2] = str_klass_obj;
			arg_types[3] = str_klass_obj;
			Method mysql_meth;
			try {
				mysql_meth = klass.getMethod("mysql_connect", arg_types);
				FunctionObject f_obj_load = new FunctionObject("mysql_connect",
						mysql_meth, scope);
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Scriptable enableImportFromJS(Context cx, Scriptable scope) {
		Scriptable new_scope = new ImporterTopLevel(cx);
		return new_scope;
	}

	private static void runJSScript(Context cx, Scriptable scope,
			String jsFilename, FileReader jsFileReader) {
		try {
			cx.evaluateReader(scope, jsFileReader, jsFilename, 1, null);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Context.exit();
		}
	}

	public static void main(String[] args) {
		String jsFilename = "source/jazzrecordrhino.js";
		try {
			FileReader jsFileReader = new FileReader(jsFilename);
			Context cx = setupJSContext();
			scope = setupJSScriptableScope(cx);
			System.out.println("test 1");
			add_mysql_connect();
			System.out.println("test 2");
			scope = enableImportFromJS(cx, scope);
			System.out.println("test 3");
			runJSScript(cx, scope, jsFilename, jsFileReader);
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*
		 * FunctionObject f_obj_sqlite = new FunctionObject("sqlite_connect",
		 * Method(), );
		 */
	}
}
