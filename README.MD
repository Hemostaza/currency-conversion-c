# Currency Conversion Console
"Aplikacja w języku JAVA, która przeliczy kwotę z USD na PLN
w oparciu o REST API NPB na dany dzień.
Wynik zostanie zapisany do pliku XML oraz bazy danych i następnie
wyświetli dane z zapisanej bazy danych w odpowiednim formacie."

## Apliakcja uruchamiana z konstoli razem z argumentami:

### add [name] [RRRR-MM-DD] [usd]
Dodaje do bazy danych rekord przyjmując nazwę [name]
datę w formacie [RRRR-MM-DD] oraz koszt w USD [usd],
Następnie wyświetla utworzony rekord i dodaje go do pliku Saved_records.xml
### show-all
Wyświetla wszystkie rekordy w bazie danych.
Można użyć sortowania przy wyświetleniu używając dodatkowo argumentów **sort-by name/date asc/desc**
- name/date - należy wybrać jedno do określenia względem czego ma się odbyć sortowanie
- asc/desc - argument opcjonalny do określenia sposobu sortowania (rosnąco/malejąco)

### find-by name/date [search]
Wyszukuje rekordy w bazie danych.
- name/date - Należy wybrać jedno do określenia czy szukamy po nazwie czy dacie
- [search] - Nalezy podać czego szukamy;

Można użyć sortowania przy wyświetleniu używając **sort-by name/date asc/desc** jak przy show-all

### export-xml fileName
Eksportuje rekordy z bazydanych do pliku xml.
- fileName - nazwa pliku do jakeigo mają zostać zapisane dane.
Można użyć sortowania przy wyświetleniu używając doatkowo **sort-by name/date asc/desc** jak przy show-all, by zapisać tylko wybrane dane.
