Zeby uruchomic aplikajce należy

Krok 1.
a)
stworzyć baze danych mysql:

url = localhost:3306/choir
username = root
password = root
b)
lub jakąś własną podmieniajac informacje w pliku application.properties

Krok 2.
odpalic skrypt choirSql.sql, który tworzy tabele i inicjalizuje rekordy w bazie

Krok 3.
Odpalic spring bootową aplikacje


endpointy
GET - zwraca posortowana liste 
http://localhost:8080/choir/choristers

POST - dodaje rekord
http://localhost:8080/choir/chorister
przykladowe body
    {
        "name": "Ania",
        "phoneNumber": "768987560"
    }

DELETE - usuwa rekord
http://localhost:8080/choir/chorister/{id}

PUT - zmienia rekord
http://localhost:8080/choir/chorister
przykladowe body
    {
        "id": 6,
        "name": "Ania",
        "phoneNumber": "768987560"
    }
