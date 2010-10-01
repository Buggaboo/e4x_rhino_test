import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Member;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

// TODO - see http://www.developer.com/java/data/article.php/3417381/Using-JDBC-with-MySQL-Getting-Started.htm
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException; //import java.net.InetAddress;
//import java.net.NetworkInterface;
//import java.net.SocketException;
//import java.net.UnknownHostException;
//import java.text.Format;
import java.util.Properties;

import junit.framework.TestCase;

public class RhinoJazzRecord {

/*
	static class MysqlDBConnect {
		/**
		 * Stolen from here:
		 * http://mxr.mozilla.org/mozilla/source/js/rhino/testsrc
		 * /org/mozilla/javascript/tests/DefineFunctionPropertiesTest.java
		 *
		ScriptableObject global;
		static final Object key = (String) "DBConnect"; // TestCase";

		// TODO - implement all this in js function (not member function)
		// TODO - implement in jazzrecord adapter
		// The JDBC Connector Class.
		private static final String dbClassName = "com.mysql.jdbc.Driver";

		// @Override
		// public void setUp() {
		public MysqlDBConnect() {
			Context cx = Context.enter();
			try {
				global = cx.initStandardObjects();
				String[] names = { "mysql_connect" };
				global.defineFunctionProperties(names, MysqlDBConnect.class,
						ScriptableObject.DONTENUM);
			} finally {
				Context.exit();
			}
		}

		// Connection string. emotherearth is the database the program
		// is connecting to. You can include user and password after this
		// by adding (say) ?user=paulr&password=paulr. Not recommended!
		public void mysql_connect(String hostname, String database,
				String username, String password)
				throws ClassNotFoundException, SQLException {
			// System.out.println(dbClassName);
			// Class.forName(xxx) loads the jdbc classes and
			// creates a drivermanager class factory
			Class.forName(dbClassName);

			String CONNECTION = "jdbc:mysql://" + hostname + "/" + database;

			// Properties for user and password
			Properties p = new Properties();
			p.put("user", username);
			p.put("password", password);

			// Now try to connect
			Connection c = DriverManager.getConnection(CONNECTION, p);

			// System.out.println("It works !");
			c.close();
		}
	}

	static class StdGlobalFunctions {
		ScriptableObject global;
		Context cx;
		Scriptable scope;

		public StdGlobalFunctions(Scriptable scope) {
			scope = this.scope;
			cx = Context.enter();
			try {
				global = cx.initStandardObjects();
				String[] names = { "load", "print" };
				global.defineFunctionProperties(names, StdGlobalFunctions.class,
						ScriptableObject.DONTENUM);
			} finally {
				Context.exit();
			}
		}

		public void load(String filename) {
			try {
				FileReader jsFileReader = new FileReader(filename);
				cx.evaluateReader(scope, jsFileReader, filename, 1, null);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				Context.exit();
			}
		}

		public void print(String s) {
			System.out.println(s);
		}
	}*/

	public static void main(String[] args) {
		String jsFilename = "source/jazzrecordrhino.js";
		try {
			// xmlString =
			// readFileAsString("xml/b_metcommentszonderxmlkopje.xml");
			// xmlString =
			// cleanXMLForE4X(readFileAsString("xml/a_zondercomments.xml"));
			FileReader jsFileReader = new FileReader(jsFilename);
			Context cx = setupJSContext();
			//MysqlDBConnect mysql = new MysqlDBConnect();
			Scriptable scope = setupJSScriptableScope(cx);
//			StdGlobalFunctions globals = new StdGlobalFunctions(scope);
			runJSScript(cx, scope, jsFilename, jsFileReader);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String cleanXMLForE4X(String input) {
		return cleanEmptyLines(cleanXMLDeclaration(input));
	}

	/**
	 * Werkt niet bij multi-line TODO - fix in het geval van multi-line
	 * declaratie van &lt;?xml ...
	 */
	private static String cleanXMLDeclaration(String input) {
		String pattern = ".*<?xml.*?>";
		return input.replaceFirst(pattern, "");
	}

	private static String cleanEmptyLines(String input) {
		return input.replaceAll("(\r\n|\r|\n)", "");
	}

	private static String readFileAsString(final String filePath)
			throws java.io.IOException {
		/**
		 * Collapse xml file to string
		 */
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		return fileData.toString();
	}

	private static Context setupJSContext() {
		ContextFactory contextFactory = new ContextFactory();
		return contextFactory.enterContext();
	}

	private static Scriptable setupJSScriptableScope(Context cx) {
		// Setup javascript context
		cx.setLanguageVersion(Context.VERSION_1_7);
		return cx.initStandardObjects();
	}

	/**
	 * Op deze manier kun je functies in de global namespace van je script
	 * declareren, op een uniforme manier. Geen onnodige gecut en gepaste dus.
	 * 
	 * There is another way:
	 * http://stackoverflow.com/questions/1237072/how-can-i
	 * -define-static-properties-for-rhinos-scriptableobject
	 */
	private static void enablePrintInJS(Context cx, Scriptable scope) {
		// enable sysout in js context
		String printString = "var print = function (str) { java.lang.System.out.println(str); };";
		cx.evaluateString(scope, printString, null, 0, null);
	}

	private static Scriptable enableImportFromJS(Context cx) {
		/**
		 * Enable importPackage and importClass functions, to import java package and classes.
		 */
		Scriptable new_scope = new ImporterTopLevel(cx);
		return new_scope;
	}

	// private static void enableLoadJS(Scriptable scope)
	// {
	// FunctionObject f_obj = new FunctionObject("load", , scope);
	//		 
	// }

	/**
	 * Deze methode staat hier dubbel omdat java zo achterlijk is dat het geen
	 * optionele parameters ondersteunt. Maar daarom doe ik dit ook, om hiervan
	 * bevrijd te worden.
	 */
	private static void runJSScript(Context cx, Scriptable scope,
			String jsFilename, FileReader jsFileReader) {
		enablePrintInJS(cx, scope);
		Scriptable new_scope = enableImportFromJS(cx);
		try {
			cx.evaluateReader(new_scope, jsFileReader, jsFilename, 1, null);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Context.exit();
		}
	}

	private static void runJSScript(Context cx, Scriptable scope,
			String jsFilename, FileReader jsFileReader, String xmlString) {
		enablePrintInJS(cx, scope);
		try {
			Object xml = Context.javaToJS(xmlString, scope);
			ScriptableObject.putProperty(scope, "_xml", xml);
			// ScriptableObject.putConstProperty(obj, name, value)
			cx.evaluateReader(scope, jsFileReader, jsFilename, 1, null);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Context.exit();
		}
	}
}
