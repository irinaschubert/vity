Vity
-
### Beschreibung
Die Applikation bietet eine Plattform um Vorschläge für Freizeitaktivitäten auszutauschen. Alle Nutzer haben die Möglichkeit Aktivitäten zu erstellten, online zur veröffentlichen und alle bereits erstellen Vorschläge einzusehen. Mittels dieser App können "Geheimtipps" geteilt und Inspiration für eine abwechslungsreiche Freizeitgestaltung geholt werden.</br>

### Handling | Darstellung
Die App sollte möglichst intuitiv und einfach bedienbar sein.<br/>
1. Bootscreen<br/>
![alt text][bootscreen]<br/>
2. Map mit eingetragenen Freizeitaktivitäten<br/>
![alt text][map]<br/>
3. Menu<br/>
![alt text][menu]<br/>
4. Erstellen einer neuen Aktivität<br/>
![alt text][activity_new]<br/>
5. Aktivitäten in der Nähe finden<br/>
![alt text][activity_search]<br/>
6. Einstellungen<br/>
![alt text][settings]<br/>

### Technik
##### Speicher
- In einen ersten Schritt sollen allen neu erstellen Aktivitäten lokal auf dem Gerät gespeichert werden. 
- In einem zweiten Schritt wird eine zentrale, online zugängliche Datenbank eingerichtet. So können alle Vorschläge in einer grösseren Gruppe ausgetauscht werden.
- Alle den Einträgen angehängten Bilder werden auf einen Server hochgeladen und mittels Links bei Bedarf geladen.
##### Berechtigungen
- GPS-Sensor: Zu jeder neu erstellten Atkivität wird die aktuelle GPS-Position gespeichert. 
- Kamera: Die Bilder zu den Einträgen werden direkt aus der Applikation erstellt und verarbeitet. 
- Netzwerk: Im Falle einer zentralen Datenbank wird eine Internetverbindung vorausgesetzt.
 

[bootscreen]: /res/readme/bootscreen.jpg "Bootscreen"
[activity_new]: /res/readme/activity_new.jpg "Acitity New"
[activity_search]: /res/readme/activity_search.jpg "Acitity Search"
[map]: /res/readme/map.jpg "Map"
[menu]: /res/readme/menu.jpg "Menu"
[settings]: /res/readme/settings.jpg "Settings"