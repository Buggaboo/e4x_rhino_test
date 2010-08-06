import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class MainE4X {
	public static void main(String[] args) {
		String jsFilename = "scripts/efactuur_exact_e4x.js";
		String xmlString;
		try {
			// xmlString =
			// readFileAsString("xml/b_metcommentszonderxmlkopje.xml");
			xmlString = cleanXMLForE4X(readFileAsString("xml/a_zondercomments.xml"));
			FileReader jsFileReader = new FileReader(jsFilename);
			Context cx = setupJSContext();
			Scriptable scope = setupJSScriptableScope(cx);
			// runJSScript(cx, scope, jsFilename, jsFileReader);
			runJSScript(cx, scope, jsFilename, jsFileReader, xmlString);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String cleanXMLForE4X(String input) {
		return cleanEmptyLines(cleanXMLDeclaration(input));
	}

	/**
	 * Werkt niet bij multi-line
	 * TODO - fix in het geval van multi-line declaratie van <?xml ...
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
	 * Op deze manier kun je functies in de global namespace van je script declareren, op een uniforme manier.
	 * Geen onnodige gecut en gepaste dus.
	 */
	private static void enablePrintInJS(Context cx, Scriptable scope) {
		// enable sysout in js context
		String printString = "var print = function (str) { java.lang.System.out.println(str); };";
		cx.evaluateString(scope, printString, null, 0, null);
	}

	/**
	 * Deze methode staat hier dubbel omdat java zo achterlijk is dat het geen
	 * optionele parameters ondersteunt.
	 */
	private static void runJSScript(Context cx, Scriptable scope,
			String jsFilename, FileReader jsFileReader) {
		enablePrintInJS(cx, scope);
		try {
			cx.evaluateReader(scope, jsFileReader, jsFilename, 1, null);
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
