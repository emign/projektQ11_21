# Event Controller Documentation
___
### 1. Erstellen eines Events
Zuerst muss eine Klasse erstellt werden, die das leere Interface Event implementiert:
```kotlin
class MeinEvent(var meinWert : Any) : Event{
    //was auch immer hier rein soll.
}
```
Hierbei kann die Klasse nach Belieben gestaltet werden und Events können sogar vererbt werden.

Dann können callbacks für dieses Event registriert werden. Dazu wird der EventController
benötigt:
```kotlin
EventController.register<MeinEvent>{ event ->
    //tue was sinnvolles
}
```
Dieser callback wird dann jedes Mal ausgeführt, wenn `MeinEvent` getriggert wird.
Die Variable `event` ist dann das Event-Objekt, das gesendet wurde.
Im callback können natürlich auch Funktionen aufgerufen werden.
Der callback erwartet keine return Expression.

### 2. Triggern eines Events
Um ein Event zu triggern wird wieder der EventController und eine Instanz des verwendeten Events benötigt:
```kotlin
EventController.send(MeinEvent("was auch immer"))
```
Das Event, welches hier angegeben wird, bestimmt welche callbacks aufgerufen werden und werden den callbacks 
auch als Parameter übergeben. Somit kann man auch Daten von dem Sender des Events zum Empfänger übertragen.

### 3. Beispiel

Beispielcode:
```kotlin
class MeineScene() : Scene(){
    
    var tmp = "a"
    
    override suspend fun Container.sceneInit() {
        EventController.register<MeinEvent>{ event -> 
            tmp = event.string
        }
        
        println(tmp)
        
        val meinObjekt = MeineKlasse()
        
        println(tmp)
        
    }


}

class MeinEvent(var string : String) : Event{

}

class MeineKlasse(){
    
    init {
        EventController.send(MeinEvent("b"))
    }
    
}
```

Ausgabe:
```
a
b
```