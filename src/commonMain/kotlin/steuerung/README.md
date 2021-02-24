# InputController Documentation

---

## 1. Maus und Tastatur InputController
### Erstellen des InputControllers
Um einen InputController für Maus und Tastatur zu erstellen wird folgender Code benötigt:
```kotlin
view.initKeyboardController(tracked = null)
view.initMouseController()
```
Für den KeyboardController muss die Parameter tracked angegeben werden,
der eine Liste aus Tastaturtasten, für die ein Event gesendet werden soll, ist.
Um zu Beginn keine Tasten zu definieren, einfach gleich null setzen.
Dies kann aber auch im nachhinein unter `InputController.trackedKeys` geändert werden.
Das view welches verwendet wird, ist im Fall des MouseControllers der Maßstab für die gesendeten Koordinaten.
Um somit allgemeine Koordinaten zu bekommen, sollte das verwendete view so groß wie die Scene sein.

### Empfangen der Events
Um ein Tastatur-Event zu empfangen, muss ein KeyEvent beim EventController registriert werden:
```kotlin
eventController.register<KeyEvent>{ event ->
    event.key//geänderte Taste
    event.down//true wenn die Taste gedrückt wird, false wenn die Taste losgelassen wird
}
```
Das KeyEvent gibt dan die Taste und einen Boolean, ob die Taste gedrückt oder losgelassen wurde, an.

Für die Maus muss ein MouseEvent beim EventController registriert werden:
```kotlin
eventController.register<MouseEvent> { event ->
    event.data//Ein MouseData Objekt
}
```
Das MouseEvent enthält im Gegensatz zum KeyEvent erstmal ein MouseData Objekt.
Dieses enthält dann wiederum alle relevanten Daten:
- X Die aktuelle Maus X-Koordinate
- Y Die aktuelle Maus Y-Koordinate
- mouseLeft Boolean für die linke Maustaste
- mouseLeft Boolean für die rechte Maustaste

## 2. Gamepad InputController
>Muss noch getestet werden
### Erstellen des GamepadControllers
Um einen GamepadController zu erstellen ist dieser Code nötig:
```kotlin
view.initGmapadController(gamePads = null,sticks = null,buttons = null)
```
Dazu werden drei Parameter benötigt:
1. gamePads: Eine Liste aus den ID's der registrierten Controller
2. sticks: Eine Liste aus den registrierten GameSticks
3. buttons: Eine Liste aus den registrierten GameButtons

Wenn zu Beginn nichts definiert werden soll, muss alles gleich null gesetzt werden.
Im nachhinein kann das auch wieder im InputController, mit den Werten `trackedGamePads` `trackedSticks` `trackedButtons`, geändert werden.

### Empfangen der Events

Die GamePad-Events müssen auch beim EventController registriert werden.
Der GamepadController ist anders als die anderen, da er gleich drei Events definiert:
1. GamepadConnectEvent: Wird gesendet, wenn ein Gamepad angeschlossen wird. Achtet nicht darauf, ob das Gamepad registriert ist
   und gibt die ID des Gamepads an.
2. GamepadStickEvent: Wird gesendet, wenn sich einer der registrierten GameSticks bewegt. Enthält einen Point für die 
   x und y Koordinate des Sticks, den GameStick(der sich geändert hat) und den Index des betreffenden Gamepads.
3. GamepadButtonEvent: Wird gesendet, wenn sich der Zustand eines registrierten GameButtons ändert. Enthält den GameButton,
   der seinen Zustand geändert hat, den aktuellen Zustand den GameButtons und den Index des Gamepads.
   
Registriert werden die Events mit der gewöhnlichen Syntax, wie oben schon beschrieben.
