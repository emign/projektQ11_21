# Event Controller Documentation
___
### 1. Erstellen eines EventControllers
Erstellt wird ein EventController mit:
```kotlin
var eventController = EventController(scope)
```
wobei `scope` ein CoroutineScope sein muss, in dem der EventController ausgeführt werden soll.

### 2. Erstellen eines Events
Zuerst muss eine Klasse erstellt werden, die das leere Interface Event implementiert:
```kotlin
class MeinEvent(var meinWert : Any) : Event{
    //was auch immer hier rein soll.
}
```
Hierbei kann die Klasse nach Belieben gestaltet werden und Events können sogar vererbt werden.

Dann können callbacks für dieses Event registriert werden. Dazu wird die Instanz des EventControllers 
benötigt:
```kotlin
eventController.register<MeinEvent>{ event ->
    //tue was sinnvolles
}
```
Dieser callback wird dann jedes Mal ausgeführt, wenn `MeinEvent` getriggert wird.
Die Variable `event` ist dann das Event-Objekt, das gesendet wurde.
Im callback können natürlich auch Funktionen aufgerufen werden.
Der callback erwartet keine return Expression.

### 3. Triggern eines Events
Um ein Event zu triggern wird wieder die Instanz des EventControllers und eine Instanz des verwendeten Events benötigt:
```kotlin
eventController.send(MeinEvent("was auch immer"))
```
Das Event, welches hier angegeben wird, bestimmt welche callbacks aufgerufen werden und werden den callbacks 
auch als Parameter übergeben. Somit kann man auch Daten von dem Sender des Events zum Empfänger übertragen.

### 4. Beispielcode
```kotlin
class MeineScene() : Scene(){
    
    var tmp = "a"
    
    override suspend fun Container.sceneInit() {
        var eventController = EventController(this@MeineScene)
        
        eventController.register<MeinEvent>{ event -> 
            tmp = event.string
        }
        
    }


}

class MeinEvent(var string : String) : Event(){

}

class MeineKlasse(val eventController : EventController){
    
    init {
        eventController.send(MeinEvent("b"))
    }
    
}
```