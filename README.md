*Behöver uppdateras*
![Server Test](https://raw.githubusercontent.com/fte10kso/Camera/master/Camera/hejhej.gif)
# Camera Server

## Klasser

### Server
Kör i main tråden. Skapar övriga objekt och hanterar sedan inkommande anslutningar. Skickar socket till `Monitor` vid anslutning.  

### ClientPackage
Innehåller header och JPEG som byte array på följande format


Mode är 0 för idle-mode och 1 för movie-mode.

Time anger tiden vid vilken bilden togs som long givet av kamerametoden  
`public void getTime(byte[] target, int offset);`

Length anger längden på bilden som int givet av kamerametoden  
`public int getJPEG(byte[] target, int offset);`

### CameraThread
Tråd. Hämtar bilder och tidsstämpel från kameran i full hastighet och kontrollerar rörelsedetektorn. Skapar ett `ClientPackage` utifrån hämtad data och placerar i monitorns buffer.  

### InputHandler
Tråd. Hanterar `InputStream` delen av anslutningen. Tar emot idle/movie/disconnect meddelanden från klienten och anropar motsvarande metoder i monitorn.  
Meddelandena är på följande format

Message är 0 för idle, 1 för movie och 2 för disconnect.  

### OutputHandler
Tråd. Hanterar `OutputStream` delen av anslutningen. Hämtar `ClientPackage` från monitorn, ett  var femte sekund i idle-mode och så snabbt som möjligt i movie-mode. Skickar sedan till klienten.  

### Monitor
Hanterar serverns tillstånd och gemensamma data. Exempelvis 

* om det finns någon client ansluten
* aktuellt mode (idle/move)
* buffer med `ClientPackage`.  

## Livscykel
Servern kan befinna sig i tre tillstånd waiting, idle och movie. 

Vid uppstart är alla trådar blockerade i väntan på anslutning till klient.  
När en klient har anslutit sig befinner sig servern initialt i idle-mode.  

Övergång till movie-mode kan ske på två sätt.  

1. `CameraThread` upptäcker rörelse i senaste bilden och anropar monitor metoden `setMovieMode(true)`. Klienten meddelas om ändringen i nästföljande `ClientPackage`.
2. `InputHandler` tar emot ett movie-mode meddelande från klienten och anropar`setMovieMode(true)`.

Övergång från movie- till idle-mode sker när `InputHandler` tar emot ett idle-mode meddelande från klienten.  

Servern kan återgå till waiting när `InputHandler` tar emot ett disconnect meddelande från klienten.  

# Camera Client

## Klasser

### Image 
Lagrar information om en bild. Vilken kamera den kom ifrån, tidsstämpel och bild-data.  

### InputHandler
En tråd per kamera. Ansvarar för `InputStream` för en socket. Skapar ett `Image` objekt utifrån data som läses på strömmen och placerar det i Monitorns `ImageBuffer`. Om tråden upptäcker att kameran har gått in i movie mode placeras ett movie meddelande i den andra serverns meddelande buffer (finns i monitorn).

### ImageBuffer
Prioritetskö med `Image` objekt sorterad efter tidsstämpel, håller reda på delay historiken för att kontrollera om synkront läge ska aktiveras/avaktiveras.

### MessageHandler
Tråd. Ansvarar för att skicka ut `ServerMessage` som finns i meddelande buffern till rätt server. Meddelande hamnar i buffern på grund av användarinteraktion eller placeras där av `InputHandler` enligt ovan.  

### ServerMessage
Ett meddelande av typen idle/movie/disconnect samt id för servern det ska skickas till.

### ImageUpdater
Tråd. Hämtar bilder från `ImageBuffer` i monitorn och skickar vidare de till Event Dispatch Thread för att visas i GUI. Vid synkront läge fördröjs bilderna en fix tid från sin tidsstämpel innan de visas.  

### Monitor
Hanterar tillstånd och gemensam data enligt ovan.  
