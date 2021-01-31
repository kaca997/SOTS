# SOTS PROJEKAT
Predmet projekta je platforma za testiranje učenickog znanja  implementirana u okviru predmetnog projekta iz predmeta "Savremene obrazovne tehnologije i standardi" na master studijama Fakulteta tehničkih nauka u Novom Sadu.
U nastavku je dat opis osnovnih funkcionalnosti i način pokretanja platforme.

## FUNKCIONALNOSTI
Platforma omogućava testiranje učenika kroz testove sastavljene od sekcija i pitanja. Korisnici sistema su profesori i studenti.

### PROFESOR
Za profesora je neophodno da najpre unese lozinku i korisničko ime kako bi se ulogovao na sistem. Njegove funkcionalnosti su:
- Pregled svih svojih predmeta
- Definisanje očekivanog prostora znanja u vidu grafa za svoje predmete
- Pregled grafa očekivanog i realnog prostora znanja (realni prostor znanja generiše se primenom IITA algoritma), kao i uvid u poređenje grafova očekivanog i realnog prostora znanja (*graph edit distance*)
- Pregled svih mogućih stanja znanja u vidu grafa (na osnovu realnog stanja znanja ukoliko postoji, u suprotnom na osnovu očekivanog stanja znanja)
- Dodavanje novih testova za svoje predmete, gde su testovi podeljeni na više sekcija, a svaka sekcija sastoji se iz više pitanja. Svako pitanje vezuje se za jedan problem iz domena i može da ima jedan ili više tačnih odgovra
- Eksportovanje testa u IMS QTI format

### STUDENT
Za studenta je neophodno da najpre unese lozinku i korisničko ime kako bi se ulogovao na sistem. Njegove funkcionalnosti su:
- Pregled svih testova koje je uradio iz kursa koji je njemu dodeljen uz pregled izabranih i tačnih odgovora u testu
- Rešavanje testa na uobičajen način prolazeći kroz sva pitanja čiji je redosled određen na osnovu prostora znanja (realnog ili očekivanog)
- Rešavanje testa vođenim testiranjem, uz mogućnost uvida u stanje znanja studenta nakon odrađenog testa
- Eksportovanje testa u IMS QTI format.
#### ALGORITAM VOĐENOG TESTIRANJA
Vođeno testiranje podrazumeva postavljanje jednog po jednog pitanja uz mogućnost da se ne postave sva pitanja za određivanje stanje znanja učenika.
Algoritam vođenog testiranja je odrađen na osnovu stohastičke Markovljeve procedure i sastoji se iz sledećih koraka:
- Na samom početku se inicijalizuju verodostojnosti svih stanja znanja koje odgovoraju prostoru znanja testa koji se radi. Verodostojnost svakog stanja znanja
		je srazmerna broju studenata koji su prethodno radili taj test i nalaze se u tom stanju znanja.
- Pri izboru prvog pitanja svakom problemu se dodeljuju težine kao suma prethodno izračunatih verodostojnosti svih stanja u kojima se nalazi taj problem. Nakon toga studentu se postavlja pitanje koje odgovara problemu kome je dodeljena najveća težina (uz pretpostavku da jednom problemu odgovara tačno jedno pitanje). Na osnovu ovog algoritma izabrano je pitanje na koje su studenti najčešće davali tačan odgovor. 	
- Nakon svakog studentovog odgovora na pitanje u testu, verodostojnosti stanja znanja se ažuriraju na osnovu pravila ažuriranja Markovljeve procedure. U zavisnosti od toga
		da li je student odgovorio tačno ili netačno na pitanje, verodostojnosti stanja znanja u kojima se nalazi pitanje koje pripada tom problemu se:
    - smanjuju (ukoliko je odgovorio netačno), dok se verodostojnosti ostalih stanja znanja povećavaju;
    - povećavaju (ukoliko je odgovorio tačno), dok se verodostojnosti ostalih stanja znanja umanjuju. 
- Izbor svakog narednog pitanja se ogleda u računanju težina problema na osnovu verodostojnosti stanja znanja, te odabirom pitanja koje odgovara problemu sa najvećom težinom.
- Nakon ažuriranja verodostojnosti stanja znanja u svakom koraku se računa da li je ispunjen uslov za završetak testa. Uslov podrazumeva da se verodostojnost jednog stanja
		izdvojila u odnosu na sva ostala (nijedna druga verodostojnost ne dostiže više od 50% najveće verodostojnosti). Nakon što je ispunjen uslov završetka testa, studentu se prikazuje njegovo stanje znanja na grafu svih stanja znanja.	

## POKRETANJE
Aplikacija se sastoji iz 3 dela:

#### Flask-app
Za pokretanje *Python* aplikacije gde je implementiran IITA algoritam najpre je potrebno preuzeti pakete smeštene u *requrements.txt* fajlu unutar *flask-app* foldera pokretanjem komande ```pip install -r requirements.txt```. Nakon toga, aplikacija se pokreće pomoću komande ```python ks.py``` i izvršava se na adresi http://localhost:5000.

#### Project
Pokretanje serverske strane (backend-a) implementirane kroz *Java* programski jezik i *Spring Boot* okruženje podrazumeva da postoji MySQL servis pokrenut na lokalnoj mašini sa 
odgovarajućim kredencijalima sadržanim u *application.properties* fajlu. Potrebno je pokrenuti *ProjectApplication.java* fajl koji se nalazi na putanji *src/main/java/com/sots/project/*. Nakon pokretanja serverska aplikacija je dostupna na adresi http://localhost:8080.

#### Frontend
Za implementaciju klijentskog dela aplikacije koristi se *Angular* razvojno okruženje. Pre pokretanja potrebno je instalirati pakete. Treba se pozicionirati unutar *frontend* foldera u komendnoj liniji i zatim uneti komandu ```npm install```. Potom se aplikacija pokreće pomoću komande ```npm start``` i aplikaciji se pristupa iz veb pretraživača na adresi http://localhost:4200.

## TIM
Tim čine:
- Nikolina Batinić, R2 14-2020
- Katarina Aleksić, R2 12-2020
