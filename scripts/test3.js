var xml = new XML(html);

// Input BeautifulSoup:
//"<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Strict//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd'><html xmlns='http://www.w3.org/1999/xhtml'><head><meta name='generator' content='HTML Tidy for Java (vers. 26 sep 2004), see www.w3.org' /><title></title></head><body>Author: Tetten de Groot<script type=\"javascript/text\">var text='fuck';</script><b>Title: Tetten</body></html>";
/// =>
// Output:
//<body>
//Author: Tetten de Groot
//<script type="javascript/text">
// var text='fuck';
//</script>
//<b>
// Title: Tetten
//</b>
//</body>

//print(xml.toXMLString());

var author = xml.text().toString().replace(/Author: /,"");
var title = xml.b.toString().replace(/Title: /,"");

print("title: " + title);
print("author: " + author);


