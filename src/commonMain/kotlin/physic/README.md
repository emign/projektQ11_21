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

Zu bestehenden SolidRect-Objekten kann im Anschluss an die Registrierung des Listeners ein
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

Ist mit obigem Befehl eine Physikkomponente zu einem SolidRect hinzugefügt worden, kann auf
diese direkt mit der Variable ```physics``` zugegriffen werden. So kann man z.B. feststellen,
ob das Physikobjekt gerade am Boden ist:
````kotlin
solidrect.physics?.isGrounded
````
Wichtig zu beachten ist, dass ```physics``` ein nullable-type ist, d.h. immer mit einem
null-check geprüft werden muss. Dies liegt daran, dass das SolidRect zu diesem Zeitpunkt
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

### Beipielcode

Im folgenden Beispiel werden 2 PhysikObjekte erzeugt, wobei diese in Korge untereinander positioniert
werden. Das untere wird als fester Boden gesetzt ```(dynamic = false)```, das obere soll auf Gravitation etc.
reagieren:
````kotlin
val solidRectDynamic = SolidRect(width = 100, height = 100, color = Colors.RED).xy(50, 50)
val solidRectStatic = SolidRect(75, 120, Colors.YELLOW).xy(100, 500)

addChild(solidRectDynamic)
addChild(solidRectStatic)

setupPhysicsSystem()

solidRectDynamic.addPhysicsComponent(
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
      if (solidRectDynamic.physics?.isGrounded == true) {
          solidRectDynamic.physics?.addForce(Vec2(0.0f, -400.0f))
      }
  }
  if (views.keys[Key.LEFT]) {
      solidRectDynamic.physics?.addForce(Vec2(-10.0f, 0.0f))
  }
  if (views.keys[Key.RIGHT]) {
      solidRectDynamic.physics?.addForce(Vec2(10.0f, 0.0f))
  }
}
````
So wird beispielsweise ein steuerbares Objekt erstellt, welches auf Kollision mit
anderen Objekten reagiert und sich physikalisch bewegt.
