var xml = new XML(html);

// Input BeautifulSoup:
//"<html><head><title>boo</title><body><div>title: Tetten</div><div><br>author: Tetten de Groot</div>"
//+ "<div></div><b>edition: 1234<div>publisher: Pubby 2008</div><table>description: DDDDDDDDDDDDDDDDDescriiiiiiiiiiiiiiiiipshun</body></html>";
/// =>
// Output:
//  <body>
//    <div>title: Tetten</div>
//    <div>
//      <br/>
//      author: Tetten de Groot
//    </div>
//    <div/>
//    <b>
//      edition: 1234
//      <div>publisher: Pubby 2008</div>
//      <table>description: DDDDDDDDDDDDDDDDDescriiiiiiiiiiiiiiiiipshun</table>
//    </b>
//  </body>

//print(xml.toXMLString());

var title_xml = xml.div[0];
var author_xml = xml.div[1];

var temp_xml = xml.b;
var edition_xml = temp_xml;
var publisher_xml = temp_xml.div;
var description_xml = temp_xml.table;

var url_xml = xml.td[0];
var url = url_xml.toString()

/*var title = title_xml.toString().replace(/title: /,"");
var author = author_xml.text().toString().replace(/author: /,"");
var edition = edition_xml.text().toString().replace(/edition: /,"");
var publisher = publisher_xml.text().toString().replace(/publisher: /,"");
var description = description_xml.toString().replace(/description: /,"");

print("title: " + title);
print("author: " + author);
print("edition: " + edition);
print("publisher: " + publisher);
print("description: " + description);*/
print("it's working!" + url + " aaaa");