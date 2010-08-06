var xmlnew = new XML(_xml);
print("-------voor begin");
print(xmlnew.GLTransactions.GLTransaction.toString());
print("-------voor eind");
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * CRUD in E4X
 */
/// CReate, insert
xmlnew.GLTransactions.GLTransaction.x += <x><a attr_a="yomomma"><b>7</b></a></x>;
xmlnew.GLTransactions.GLTransaction.y += <y><a><b attr_b="issofat...">7</b></a></y>;
xmlnew.GLTransactions.GLTransaction.compleetanders += <z attr_c="gnuf"><a><b>7</b></a></z>;
var shortcut = xmlnew.GLTransactions.GLTransaction; // scheelt tijd voor de hele keten inlezen
print(shortcut.x.toString());
print(shortcut.y.toString());
print(shortcut.compleetanders.toString()); // niks.
print(shortcut.z.toString()); // nu, gemapt naar child node z
/// Update
shortcut.z.@attr_c = "she wears an equator for a belt";
print (shortcut.z.toString());
print(shortcut.z.@attr_c.toString()); 
/// Delete
print((delete shortcut.z.@attr_c).toString()); // delete attribuut gelukt! Het antwoordt met "true".
print((delete shortcut.z).toString()); // delete node gelukt!
print(shortcut.toString()); // Kijk'ns, hij is er niet meer. 
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
print("-------na begin");
print(xmlnew.GLTransactions.GLTransaction.toString());
print("-------na eind");

