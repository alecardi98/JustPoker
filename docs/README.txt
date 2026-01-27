# JustPoker

## Descrizione
Questo progetto è un'applicazione Java basata su Maven che implementa il gioco del poker multiplayer su un server cloud.

## Tecnologie utilizzate
- Java 17
- Maven
   ├ JUnit
   ├ H2
   ├ Log4j
   └ JavaFX	

## Struttura del progetto
poker/
├── pom.xml						#file per gestire le dipendenza Maven
├── data/							#file del database
├── src/	
    ├── main/
    │    ├── java/
    │    │   └── THRProject/
    │    │       ├── card/			
    │    │       │   ├── logic/	#contiene la logica delle carte
    │    │       │   └── model/	#contiene il modello delle carte
    │    │       ├── client/		
    │    │       ├── database/
    │    │       ├── game/
    │    │       ├── gui/			#contiene il MainGUI
    │    │       ├── message/		
    │    │       ├── player/
    │    │       └── server/		#contiene il ServerMain
    │    └── resources/
    └── test/						#cartella di test
         └── java/				
             └── THRProject/	
                 ├── card/			
                 ├── game/
                 └── server/

## Build del progetto ed esecuzione
Per compilare ed eseguire il progetto basta eseguire in ordine:
	Run -> Run As -> Maven Build… -> Name: startPoker		 -> Run
					 Goals: clean install javafx:run 
poi rimarrà memorizzato
	Run -> Run Configurations... -> Maven Build -> startPoker -> Run
oppure
	Run History -> startPoker
solo quando il server cloud è già attivo.

## Autore
THRProject Team