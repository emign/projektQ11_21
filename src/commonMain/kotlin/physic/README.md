# Physik-System Documentation
___
### Initialisierung des PhysicsListeners

Damit später erzeugte Physikobjekte auch registriert und jeden Frame aktualisiert werden können,
muss zunächst der PhysicsListener zur Stage hinzugefügt werden:
```kotlin
setupPhysicsSystem(gravity = Vec2(0.0f, 9.81f))
```
Die Setup-Funktion erhält als Parameter die Gravitation(wird kein spezieller Wert angegeben, werden
9.81 m/s^2 verwendet) und kann nur von Container-Objekten aufgerufen werden. 

### Hinzufügen einer Physik-Komponente

Zu bestehenden SolidRect- bzw. Circle-Objekten kann im Anschluss an die Registrierung des Listeners ein
Physikobjekt hinzugefügt werden:
```kotlin
val solidrect = SolidRect(width = 100, height = 100, color = Colors.RED).xy(50, 50)

solidrect.addPhysicsComponent(
    friction = Vec2(2.0f, 0.5f),
    isDynamic = true,
    layer = 1,
    coefficient = Vec2(120.0f, 120.0f),
    collisionCallback = {}
)
```

Bei der Registrierung einer Physik-Komponente können folgende Parameter festgelegt werden:

- "friction" Der Luftwiderstand; wird kein Wert gesetzt, ist er Vec2(2.0f, 0.5f)
- "isDynamic" Wenn false, wird sich das Objekt nicht bewegen, aber dennoch auf Kollision reagieren
- "layer" Alle Objekte auf der gleichen Ebene können NICHT miteinander kollidieren
- "coefficient" Für die Umrechnung von Pixeln in Meter verwendet. Standard: Vec2(120.0f, 120.0f). Je
  höher er gesetzt wird, umso schneller bewegen sich die Objekte pro frame (kann aber 
  Kollisions-Bugs erzeugen)
- "collisionCallback" Diese Funktion legt fest, was bei einer Kollision ausgeführt werden soll. Als Parameter 
  besitzt sie das andere Objekt, mit dem das soeben definierte Physikobjekt gerade kollidiert.
  
### Zugriff auf bestehende Physik-Komponenten

Ist mit obigem Befehl eine Physikkomponente zu einem SolidRect/Circle hinzugefügt worden, kann auf
diese direkt mit der Variable ```physics``` zugegriffen werden. So kann man z.B. feststellen,
ob das Physikobjekt gerade am Boden ist:
````kotlin
solidrect.physics?.isGrounded
````
Wichtig zu beachten ist, dass ```physics``` ein nullable-type ist, d.h. immer mit einem
null-check geprüft werden muss. Dies liegt daran, dass das SolidRect/der Circle zu diesem Zeitpunkt
möglicherweise gar kein Physik-Objekt hat(z.B. wurde noch keins mit ```addPhysicsComponent```
hinzugefügt.)

### Manuelles Hinzufügen von Kräften

Um beispielsweise eine Steuerung hinzuzufügen, müssen bei bestimmten Tasten Kräfte auf
den Körper ausgeübt werden, damit er sich bewegt (einzig die Gravitation wirkt immer automatisch).
Dies geht per Zugriff auf die ```physics```-Variable:
````kotlin
    if (views.keys[Key.LEFT]) solidRect.physics?.addForce(Vec2(-10.0f, 0.0f))
    if (views.keys[Key.RIGHT]) solidRect.physics?.addForce(Vec2(10.0f, 0.0f))
````
Kräfte müssen immer als ```Vec2``` angeben werden (Mathematischer Vektor)

### Triggern von Events

Anstatt Kräfte direkt zu einer Physik-Komponente hinzuzufügen, kann dies auch über den ```EventController```
ausgeführt werden. Dafür stehen zwei Möglichkeiten zur Verfügung:
````kotlin
val solidrect = SolidRect(55.0, 100.0, Colors.PURPLE).xy(100, 300)
//add Physics component here 
//...
sendPhysicsEvent(PhysicsDirection.LEFT, 20.0f, solidrect)

//oder

sendPhysicsEvent(Vec2(20.0f, 0.0f), solidrect)
````

Diese beiden Funktionen triggern intern im ```EventController``` dasselbe Event, sie unterscheiden sich nur in ihren Parametern:

- ```fun sendPhysicsEvent(force: Vec2, receiver: View)``` lässt eine festgelegte Kraft(Vektor) auf das
Physikobjekt(receiver) wirken: Momentan werden als ```receiver``` nur SolidRect und Circle unterstützt
  
- ```fun sendPhysicsEvent(direction: PhysicsDirection, magnitude: Float, receiver: View)``` wendet eine Kraft
auf ein Physikobjekt an, wobei die Kraft hier als ```PhysicsDirection``` angegeben wird. Als Optionen stehen
  ```UP```, ```DOWN```, ```LEFT``` und ```RIGHT``` zur Verfügung. Hierbei wird durch ```magnitude``` die Stärke
  der Kraft in die jeweilige Richtung angegeben. ```receiver``` ist auch hier das Objekt, auf das die Kraft wirken soll.

### Beipielcode

Im folgenden Beispiel werden 2 PhysikObjekte erzeugt, wobei diese in Korge untereinander positioniert
werden. Das untere wird als fester Boden gesetzt ```(dynamic = false)```, das obere soll auf Gravitation etc.
reagieren und ein Kreis sein
````kotlin
val circleDynamic = Circle(radius = 100, Colors.RED).xy(50, 50)
val solidRectStatic = SolidRect(75, 120, Colors.YELLOW).xy(100, 500)

addChild(circleDynamic)
addChild(solidRectStatic)

setupPhysicsSystem()

circleDynamic.addPhysicsComponent(
  friction = Vec2(2.0f, 0.5f), 
  isDynamic = true, 
  layer = 1, 
  coefficient = Vec2(120.0f, 120.0f)) 
{ other ->
  //das hier ist der Callback, der bei Kollision ausgeführt werden soll.
  //Das andere Element(other) ist das jeweilige Objekt, mit dem die Kollision stattfindet
  println("Hey, ich kollidiere gerade mit $other")
}

solidRectStatic.addPhysicsComponent(isDynamic = false, layer = 2, collisionCallback = {/* Nothing */})

addUpdater {
  if (views.keys[Key.UP]) {
      if (circleDynamic.physics?.isGrounded == true) {
          sendPhysicsEvent(PhysicsDirection.UP, 400.0f, circleDynamic)
      }
  }
  if (views.keys[Key.LEFT]) {
      sendPhysicsEvent(PhysicsDirection.LEFT, 20.0f, circleDynamic)
  }
  if (views.keys[Key.RIGHT]) {
      sendPhysicsEvent(PhysicsDirection.RIGHT, 20.0f, circleDynamic)
  }
}
````
So wird beispielsweise ein steuerbares Objekt erstellt, welches auf Kollision mit
anderen Objekten reagiert und sich physikalisch bewegt.
