package jazzrecord;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;

import jazzrecord.sql.MysqlDBConnect;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

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
		Class klass = null;
		String className = "jazzrecord.sql.MysqlDBConnect";
		try {
			klass = Class.forName(className, true, Thread.currentThread().getContextClassLoader());

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				klass = Class.forName( className );
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		try {
			Class[] args = new Class[] { String.class, String.class, String.class, String.class };
			Method mysql_fn = MysqlDBConnect.class.getMethod("mysql_connect", args)
			FunctionObject fobj_mysql = new FunctionObject("mysql_connect", mysql_fn, scope);
			ScriptableObject.putProperty(scope, "mysql_connect", fobj_mysql);
			/*
			mysql_meth = klass.getMethod("mysql_connect", arg_types);
			FunctionObject f_obj_mysql = new FunctionObject("mysql_connect",
					mysql_meth, scope);
			scope.put("mysql_connect", , f_obj_mysql);
			*/
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
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
	
	private static void enablePrintInJS(Context cx, Scriptable scope) {
		/**
		 * I know there's a prettier (more java) way, this has to do for now.
		 * No point in optimizing, the cost is constant.
		 */
		// enable sysout in js context
		String printString = "var print = function (str) { java.lang.System.out.println(str); };";
		cx.evaluateString(scope, printString, null, 0, null);
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
			enablePrintInJS(cx, scope);
			System.out.println("test 4");
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
