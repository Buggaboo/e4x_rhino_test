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
import java.sql.*;
//import java.net.InetAddress;
//import java.net.NetworkInterface;
//import java.net.SocketException;
//import java.net.UnknownHostException;
//import java.text.Format;
import java.util.Properties;
import com.mysql.jdbc.Driver;

public class RhinoJazzRecord {

	class DBDemo {
		// TODO - implement all this in js function (not member function)
		// TODO - implement in jazzrecord adapter
		// The JDBC Connector Class.
		private static final String dbClassName = "com.mysql.jdbc.Driver";
		private static final String CONNECTION = "jdbc:mysql://127.0.0.1/some_database";

		// Connection string. emotherearth is the database the program
		// is connecting to. You can include user and password after this
		// by adding (say) ?user=paulr&password=paulr. Not recommended!
		public DBDemo() throws ClassNotFoundException, SQLException {
			System.out.println(dbClassName);
			// Class.forName(xxx) loads the jdbc classes and
			// creates a drivermanager class factory
			Class.forName(dbClassName);

			// Properties for user and password
			Properties p = new Properties();
			p.put("user", "paulr");
			p.put("password", "paulr");

			// Now try to connect
			Connection c = DriverManager.getConnection(CONNECTION, p);

			System.out.println("It works !");
			c.close();
		}
	}

	// TODO - this is not necessary
//	class LoadJSFunction extends FunctionObject {
//
//		public LoadJSFunction(String arg0, Member arg1, Scriptable arg2) {
//			super(arg0, arg1, arg2);
//		}
//
//		/**
//		 * 
//		 */
//		private static final long serialVersionUID = 1L;
//
//	}

	public static void main(String[] args) {
		String jsFilename = "source/jazzrecordrhino.js";
		try {
			// xmlString =
			// readFileAsString("xml/b_metcommentszonderxmlkopje.xml");
			// xmlString =
			// cleanXMLForE4X(readFileAsString("xml/a_zondercomments.xml"));
			FileReader jsFileReader = new FileReader(jsFilename);
			Context cx = setupJSContext();
			Scriptable scope = setupJSScriptableScope(cx);
			// runJSScript(cx, scope, jsFilename, jsFileReader);
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
	 * declaratie van <?xml ...
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

	private static Scriptable enableImportFromJS(Context cx, Scriptable scope) {
		Scriptable new_scope = new ImporterTopLevel(cx);
		return new_scope;
	}

	// private static void enableLoadJS(Context cx, Scriptable scope)
	// {
	// cx.compileFunction(scope, source, sourceName, lineno, securityDomain)
	// //cx.getElements(object)
	// }

	/**
	 * Deze methode staat hier dubbel omdat java zo achterlijk is dat het geen
	 * optionele parameters ondersteunt.
	 */
	private static void runJSScript(Context cx, Scriptable scope,
			String jsFilename, FileReader jsFileReader) {
		enablePrintInJS(cx, scope);
		Scriptable new_scope = enableImportFromJS(cx, scope);
		try {
			cx.evaluateReader(new_scope, jsFileReader, jsFilename, 1, null);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Context.exit();
		}
	}

	/**
	 * Deze methode staat hier dubbel omdat java zo achterlijk is dat het geen
	 * optionele parameters ondersteunt.
	 */
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
