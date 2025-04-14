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

![Diagramma UML](../images/uml.png)

## Scelte progettuali
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
* **Aeroporto di origine**: Napoli; 
Per i voli in arrivo a Napoli: 
* **Aeroporto di destinazione**: Napoli;

## Gestione della "Prenotazione": 
La classe **Prenotazione** rappresenta un bilietto acquistato e contiene: 
* **Numero biglietto**: identificativo per verificare la validità dello stesso; 
* **Posto assegnato**; 
* **Stato prenotazione**: rappresentato come un _enumerativo_ a cui sarà assegnato un valore per ogni stato della prenotazione(confermata, in attesa, cancellata); 

## checkIn
L'operazione del **checkIn** è rappresentata come una _classe associativa_ in quanto ha validità solo nel momento in cui un passeggero deve salire a bordo del volo 
e effettuare un checkIn, senza passeggero o senza prenotazione(biglietto valido), il checkIn non può essere effettuato.

a





