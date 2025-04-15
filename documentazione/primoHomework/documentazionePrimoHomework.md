## Nome del progetto:
**Creazione di un sistema informativo aeroportuale**
## Componenti del gruppo:
_Leonardi Karol & Mauro Salvatore_

## Obiettivo del progetto:
L'obiettivo è quello di realizzare un sistema informativo che sia compatibile con le richieste espresse dalla traccia numero 3 che
riguarda l'aeroporto di Napoli.

# Il sistema dovrà:
-Gestire il login degli utenti identificandoli come utenti generici o amministratori(admin);
-Gestire i voli in partenza e in arrivo all'aeroporto;
-Gestire i gate di imbarco identificati da numeri, assegnadoli ai voli in partenza;
-Consentire agli utenti di visualizzare tutti gli aggiornamenti disponibili sui voli programmati all'interno di una propria area personale;

## **Diagramma UML**

Il seguente diagramma rappresenta le classi principali del sistema e le loro relazioni:

![Diagramma UML](../../images/uml.png)

## Scelte progettuali per le classi

## Utenti
Possiamo considerare 2 tipologie di utenti principali:
* **Utenti generici**: i quali hanno la possibilità di prenotare
  i voli(anche per qualcuno che non corrisponda all'utente che effettua la prenotazione) e consultare la propria area personale;
* **Admin**: che inseriscono all'interno del sistema nuovi voli programmati, aggiornano le informazioni dei voli preesistenti e assegnano
  i gate ai relativi voli.

Queste due classi derivano da una generalizzazione di una superclasse chiamata **Utente**. La classe **utente** porta con sé due informazioni fondamentali:
* **Login**;
* **Password**.

Questi due attributi saranno comuni alle sottoclassi che poi potranno svilupparsi nelle loro specializzazioni.
Tutti gli utenti, admin o generici che siano hanno la possibilità di vedere tutti i voli programmati disponibili, dunque anche l'implementazione del metodo per visualizzare i voli
apparterrà alla superclasse.

## Gestione della "Prenotazione":
La classe **Prenotazione** rappresenta un bilietto acquistato e contiene:
* **Numero biglietto**: identificativo per verificare la validità dello stesso;
* **Posto assegnato**;
* **Stato prenotazione**: rappresentato come un _enumerativo_ a cui sarà assegnato un valore per ogni stato della prenotazione(confermata, in attesa, cancellata);

## Passeggero
Non è detto che un utente generico prenoti un volo per sé stesso. Dunque al momento della prenotazione sarà necessario inserire i dati di chi effettivamente usufruirà del volo. 
è necessaria dunque, una nuova classe chiamata _"Passeggero"_ per identificare dunque la persona che effettivamente prenderà il volo.
Questa classe ha diversi attributi: 
* **Nome del passeggero**; 
* **Cognome del passeggero**; 
* **Numero del documento del passeggero**; 

## checkIn
L'operazione del **checkIn** è rappresentata come una _classe associativa_ in quanto ha validità solo nel momento in cui un passeggero deve salire a bordo del volo
e effettuare un checkIn, senza passeggero o senza prenotazione(biglietto valido), il checkIn non può essere effettuato.

## Gate
Ogni Gate è associato a diversi Voli nel corso di una giornata. **La gestione dei gate è riservata all’Admin**, che attraverso l’operazione _assegnaGate()_ può collegare un gate a un volo specifico.
Gli attributi del gate sono: 
* Codice identificativo del gate;
* Il volo a cui il gate è stato assegnato. 
## Volo
Il concetto di **volo** è rappresentato da una classe composta da diversi attributi come:
* **codice univoco**: necessario ad identificare il volo;
* **compagnia aerea**;
* **Aeroporto di origine**;
* **Aeroporto di destinazione**;
* **Orario di arrivo**;
* **Eventuale ritardo**;
* **Stato del volo**: rappresentato come un tipo _enumerativo_ per qualificare ogni stato del volo(decollato, programmato, in ritardo, atterrato, cancellato)

Per quanto riguarda gli aerei che partono da e arrivano a Napoli, c'è da specificare il valore che l'attributo aeroporto di origine/destinazione assumerà:

Per i voli in partenza da Napoli:
* **Aeroporto di origine**: Napoli.

Per i voli in arrivo a Napoli:
* **Aeroporto di destinazione**: Napoli.

## Molteplicità utilizzate nel diagramma

Passeggero ↔ Prenotazione.
1 Passeggero → può avere 0 o più Prenotazioni (*).
Ogni Prenotazione → è associata a 1 solo Passeggero.

Molteplicità:
Lato Passeggero: 1.
Lato Prenotazione: *.

-----------------------------------------------------------

Prenotazione ↔ Volo
Ogni Prenotazione → è riferita a 1 solo Volo.
Ogni Volo → può avere 0 o più Prenotazioni.

Molteplicità:
Lato Prenotazione: *.
Lato Volo: 1.

-----------------------------------------------------------

Prenotazione ↔ Gate
Ogni Prenotazione → è associata a 1 solo Gate.
Ogni Gate → può essere associato a 0 o più Prenotazioni.

 Molteplicità:
Lato Prenotazione: *.
Lato Gate: 1.

-----------------------------------------------------------

UtenteGenerico ↔ Prenotazione
Ogni Prenotazione → è fatta da 1 solo UtenteGenerico.
Ogni UtenteGenerico → può fare 0 o più Prenotazioni.

Molteplicità:
Lato Prenotazione: 1
Lato UtenteGenerico: *

-----------------------------------------------------------

UtenteGenerico ↔ Volo
Ogni UtenteGenerico → può interagire con 0 o più Voli.
Ogni Volo → può essere gestito da 0 o più UtentiGenerici.

Molteplicità:
Lato UtenteGenerico: *
Lato Volo: *

-----------------------------------------------------------

Volo ↔ voliPartenza / voliArrivo (generalizzazione)
Ogni voliPartenza / voliArrivo → è una specializzazione di Volo.
Un Volo è o una partenza o un arrivo.

Molteplicità:
Lato Volo: 1
Lato voliPartenza/voliArrivo: 1

-----------------------------------------------------------

Checkin ↔ Prenotazione (dipendenza)
Relazione tratteggiata, non rappresenta un'associazione con molteplicità.
Usata per indicare una dipendenza funzionale (uso del metodo checkin()






