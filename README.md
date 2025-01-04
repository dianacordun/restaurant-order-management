## Sistem de Management al Comenzilor intr-un Restaurant (Restaurant Order Management)
Aplicatie pentru gestionarea comenzilor, preparatelor si rezervarilor intr-un restaurant. Permite preluarea comenzilor de la clienti, gestionarea meniului si a rezervarilor de mese.

### Business Requirements
1. Adaugare, modificare si stergere de preparate.
2. Preluarea comenzilor si asocierea lor cu clienti.
3. Gestionarea rezervarilor de mese.
4. Procesarea platilor pentru comenzi.
5. Monitorizarea stocului de ingrediente pentru preparate.
6. Generarea de rapoarte privind cele mai comandate preparate.
7. Monitorizarea statusului comenzilor.
8. Istoric al comenzilor per client.
9. Posibilitatea de plati cash sau card.
10. Control asupra disponibilitatii preparatelor in functie de stoc.

## Entitati si Relatii
* **Entitati:** Client, Order, Dish, Reservation, Ingredient, Payment
* **Relatii:** Order-Client (many-to-one), Order-Payment (one-to-one), Order-Dish (many-to-many), Dish-Ingredient (many-to-many), Client-Reservation (one-to-many)

## MVP features
1. CRUD pentru meniul de preparate.
2. Gestionarea comenzilor si a rezervarilor.
3. Administrarea stocului de ingrediente.
4. Gestionarea platilor pentru comenzi.
5. Rapoarte privind popularitatea preparatelor.



Swagger Ui: http://localhost:8080/swagger-ui/index.html

Documentatie JSON: http://localhost:8080/v3/api-docs

[Documentatie JSON.json](..%2F..%2F..%2FUsers%2Fdiana%2FOneDrive%2FDesktop%2Frestaurant%20spring%20app%20docs.json)