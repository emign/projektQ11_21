# API XML Read-Write Documentation
___
## Read
Um eine XML-Datei auszulesen, muss zuerst die
```XmlReadWrite.read()```
Funktion aufgerufen werden:
```kotlin
val outputData = XmlReadWrite.read("Character/TestCharacter.xml", XmlType.CHARACTER)
```
Beim Aufrufen müssen folgende Parameter angegeben werden:

- Speicherort der auszulesenden Datei (relativ zum Resources Ordner)
- Typ der Xml Datei (momentan Character und Progess vorhanden)

Als Output erhält man ein Objekt der Klasse ```XmlData```.

## Write

Um eine XML-Datei zu schreiben, muss die ```XmlReadWrite.write()``` Funktion aufgerufen werden:
```kotlin
XmlReadWrite.write(outputData, "Savings/TestCharacterNeu.xml", XmlType.CHARACTER)
```
Beim Aufrufen müssen folgende Parameter angegeben werden:

- die ```XmlData```, die die zu schreibende Information enthält
- den Speicherort der neuen Xml Datei
- Typ der Xml Datei

Hierbei wird eine neue Xml-Datei im angegebenen Speicherort erstellt, sollte eine namensgleiche Datei schon
vorhanden sein, wird diese überschrieben.
