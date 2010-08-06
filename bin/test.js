var book =
<html><head>bullshit onzin</head>
<body>
<div><title>Title: gone with the wind</title><author>by Someone</author><br/></div>
</body></html>;

out.println("test start");
var book0 = new XML(html);
for each (i in [0,1]) 
	out.println(book0.body.div[i].toString());          
out.println("test end");

var title_str = book.body.div.title.toString();
//title_str.replace(new RegExp("Title: "), "");
out.println(title_str.replace(/Title: /, ""));

var author_str = book.body.div.author.toString();
out.println(author_str.replace(/^by /, ""));



//book.author;

