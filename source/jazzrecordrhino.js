/**
 * Import jazzrecord
 */
// You need to define your own load function, probably steal it.
print("test a");
/*
load("association_loader.js");
load("core.js");
load("model/new_query.js");
load("model/save.js");
load("model/destroy.js");
load("model/model.js");
load("model/find.js");
load("model/query.js");
load("model/util.js");
load("save.js");
load("record/save.js");
load("record/record.js");
load("record/is_changed.js");
load("record/validate.js");
load("adapters.js");
load("util.js");
load("migrations/schema_operations.js");
load("migrations/migrate.js");
*/

print("a1");
importPackage("java.sql");
importClass("com.mysql.jdbc.Driver");
print("a2");
print("b");
var username = "testuser";
print("b1");
var password = "testpass";
print("b2");
var url = "jdbc:mysql://localhost/test";
print("b3");
var driver = new Driver();
print("b4");
var conn = DriverManager.getConnection(url, username, password);
print("b5");
print ("Connection established with " + url);
jprint("b6");

//var my_connection = mysql_connect("localhost", "mysql", "root","harmless");
//print("test b");