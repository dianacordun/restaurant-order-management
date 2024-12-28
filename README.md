## Sistem de Management al Comenzilor intr-un Restaurant (Restaurant Order Management)
Aplicatie pentru gestionarea comenzilor, meniurilor si rezervarilor intr-un restaurant. Permite preluarea comenzilor de la clienti, gestionarea meniului si a rezervarilor de mese.

### Business Requirements
1. Adaugare, modificare si stergere de preparate din meniu.
2. Preluarea comenzilor de la clienti.
3. Gestionarea rezervarilor de mese.
4. Procesarea platilor pentru comenzi.
5. Monitorizarea stocului de ingrediente pentru preparate.
6. Generarea de rapoarte privind cele mai comandate preparate.
7. Monitorizarea statusului comenzilor.
8. Istoric al comenzilor per client.
9. Posibilitatea de a face comenzi online pentru preluare sau livrare.
10. Control asupra disponibilitatii preparatelor in functie de stoc.

## Entitati si Relatii
* **Entitati:** Client, Comanda, Preparat, Rezervare, Ingredient, Plata
* **Relatii:** Comanda-Client (many-to-one), Comanda-Plata (one-to-one), Comanda-Preparat (many-to-many), Preparat-Ingredient (many-to-many), Client-Rezervare (one-to-many)

## MVP features
1. CRUD pentru meniul de preparate si ingrediente.
2. Gestionarea comenzilor si a rezervarilor.
3. Administrarea stocului de ingrediente.
4. Gestionarea platilor pentru comenzi.
5. Rapoarte privind popularitatea preparatelor.
