import org.mozilla.javascript.Context;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.ScriptableObject;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class JazzRecord {

	/**
	 * @param args
	 */
	static class MysqlDBConnect {
		/**
		 * Stolen from here:
		 * http://mxr.mozilla.org/mozilla/source/js/rhino/testsrc
		 * /org/mozilla/javascript/tests/DefineFunctionPropertiesTest.java
		 */
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
			//Class.forName(dbClassName);

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
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Class klass = Class.forClass("MysqlDBConnect");
		Class arg_types[] = new Class[4];
		arg_types[0] = String.TYPE;
		arg_types[1] = String.TYPE;
		arg_types[2] = String.TYPE;
		arg_types[3] = String.TYPE;
		Method mysql_meth = klass.getMethod("mysql_connect", arg_types);
		FunctionObject f_obj_load   = new FunctionObject("load", mysql_meth, );
		// TODO - scope.put("myclass?", scope, Scriptable)... or something
		/*
		
		FunctionObject f_obj_mysql  = new FunctionObject("mysql_connect", Method(), );
		FunctionObject f_obj_sqlite = new FunctionObject("sqlite_connect", Method(), );
		*/
	}

}
