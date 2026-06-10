
# Space Merchant

**Space Merchant** to w pełni terminalowa (TUI - Text User Interface) gra symulacyjno-ekonomiczna w klimacie science-fiction stworzona w JAVA. Projekt łączy w sobie elementy gier typu *space trader*, strategii ekonomicznych oraz gier RPG z systemem zarządzania zasobami ludzkimi. 

Gracz wciela się w rolę kapitana statku kosmicznego, który rozpoczyna swoją podróż w promie z minimalnym kapitałem. Celem ostatecznym jest dotarcie do kolonii „Nowy Eden”, aby tego dokonać, konieczne jest odkrycie całej mapy galaktyki, zbudowanie floty, odpowiednie wyszkolenie załogi oraz zgromadzenie majątku. Gra w całości operuje w trybie tekstowym, przypominając klasyczne, oldschoolowe produkcje i systemy operacyjne retro.

---

## 🚀 Głębokie Spojrzenie na Funkcjonalności i Mechaniki

Rozgrywka w *Space Merchant* opiera się na kilku wzajemnie przenikających się systemach, które zmuszają gracza do ciągłego balansowania ryzykiem i zasobami.

### 1. Złożony System Zarządzania Załogą (Crew Management)
Załoga to serce Twojego statku. Każdy zrekrutowany w kantynie najemnik to unikalna postać z własnym imieniem, nazwiskiem i generowanymi proceduralnie statystykami.
* **Role i Umiejętności:** Załoganci posiadają jedną z 10 ról (m.in. Pilot, Inżynier, Strzelec, Handlarz, Medyk). Każdy dysponuje czterema głównymi statystykami (Pilotaż, Walka, Inżynieria, Handel) ocenianymi w skali od 1 do 5 gwiazdek.
* **Rozwój (RPG):** Członkowie załogi zdobywają punkty doświadczenia (XP) za wykonywanie przypisanych im akcji (np. udany skok nadprzestrzenny daje XP do Pilotażu, a zyskowna transakcja rozwija Handel). Gracz może również płacić za ich trening na stacjach kosmicznych.
* **Ambulatorium:** Załoga odnosi rany podczas bitew lub buntów. Jeśli punkty zdrowia (HP) spadną do zera, załogant staje się nieprzytomny i jego bonusy przestają działać, dopóki nie zostanie wyleczony w portowym ambulatorium.
* **Bezwzględny System Morale i Długów:** Każdy skok generuje koszty w postaci żołdu. Jeśli graczowi zabraknie kredytów, statek wpada w długi. Serwisy gry obliczają "presję długu". Gdy ta wzrośnie, załoga zaczyna narzekać, co docelowo prowadzi do losowych zdarzeń krytycznych:
    * Sabotaż statku (uszkodzenia kadłuba),
    * Bójki pod pokładem (utrata HP załogantów),
    * Wyrzucenie części cennego ładunku w próżnię,
    * Dezercja na najbliższej stacji.

### 2. Ekonomia i Rynek Towarowy
Każda stacja w grze operuje w jednym z czterech typów gospodarki: Rolnicza, Przemysłowa, High-Tech lub Górnicza. 
* **Dynamiczne Ceny:** Moduł rynku płynnie przelicza bazową wartość przedmiotu. Na przykład stacje Górnicze sprzedają surowce taniej, ale żądają wysokich cen za technologie z systemów High-Tech.
* **Umiejętności Handlowe:** Algorytmy rynkowe skanują załogę w poszukiwaniu osoby z najwyższym atrybutem Handlu (Trade). Za każdy poziom tej umiejętności gracz otrzymuje procentową zniżkę przy zakupach i bonus przy sprzedaży, co czyni utrzymywanie sprawnego kupca kluczowym elementem strategii.

### 3. Nawigacja i Mapa Gwiezdna (Graficzna Reprezentacja ASCII)
Wszechświat to siatka połączonych węzłów.
* **Algorytmy Mapy:** Terminal renderuje dynamiczną mapę szlaków gwiezdnych (korzystając z algorytmów rysowania linii bezpośrednio w przestrzeni znakowej). Na mapie oznaczona jest pozycja gracza, stacje, puste punkty skoku oraz aktywne cele nawigacyjne.
* **Logistyka Skoku:** Każda trasa ma przypisany koszt paliwa. Skok nie odbędzie się, jeśli na pokładzie nie ma żywego pilota lub paliwa w zbiornikach. Brak pilota poza stacją kończy grę (dryfowanie w próżni).

### 4. System Zdarzeń Losowych (Encounters) i Walka
Z każdym skokiem nadprzestrzennym istnieje szansa na wyrzucenie statku ze szlaku przez anomalię, co zazwyczaj oznacza atak piratów.
* **Ocena Taktyczna:** Gra generuje statystyki wrogiego okrętu i zestawia je z łącznymi siłami Twojej załogi.
* **Wybór Reakcji:**
    * *Walka:* Szansa na zwycięstwo zależy od sumy punktów Walki (Combat) żywej załogi. Zwycięstwo daje łupy, porażka to potężne uszkodzenia kadłuba.
    * *Ucieczka:* Zależy od łącznych punktów Pilotażu (Piloting) i wymaga żywego pilota. Nieudana ucieczka to uszkodzenia statku i kradzież kredytów.
    * *Okup:* Gwarantowana utrata określonej sumy kredytów, ale 100% bezpieczeństwa dla kadłuba i załogi.

### 5. Stocznia: Personalizacja i Rozwój Floty
Gracz nie jest ograniczony do jednego statku. Na większych stacjach stocznie oferują:
* Zakup zupełnie nowych kadłubów (np. pojemne Frachtowce, ciężko opancerzone Korwety, Zwiadowcy z potężnymi zbiornikami). Każdy kadłub ma własny unikalny schemat ASCII rysowany w oknie diagnostyki.
* Moduły ulepszeń: Gracz może na stałe wzmocnić pancerz, powiększyć ładownię towarową lub dobudować rezerwowe zbiorniki paliwa.

### 6. Kampania i Cel Ostateczny
Gra posiada jasno zdefiniowane warunki zwycięstwa zarządzane centralnie. Aby wygrać, gracz musi jednocześnie:
* Dotrzeć do ukrytej lokacji końcowej ("Nowy Eden").
* Zbadać 100% dostępnych węzłów mapy (odkryć wszystkie szlaki).
* Zgromadzić kapitał w wysokości co najmniej 10,000 kredytów.
* Posiadać najdroższy i najlepszy kadłub statku.
* Zainstalować minimum 3 moduły ulepszeń.
* Dotrzeć do celu z przynajmniej jednym żywym członkiem załogi.

---

## 📂 Struktura Projektu (Drzewo Pakietów)

Projekt zachowuje ścisły podział odpowiedzialności (Separation of Concerns), oparty na architekturze MVC (Model-View-Controller) mocno wspieranej warstwą logiki biznesowej zlokalizowanej w serwisach.

```text
space_merchant/
├── pom.xml                                   # Konfiguracja Mavena, zależności (Lanterna, Jackson, JUnit)
├── savegame.json                             # (Generowany) Zrzut stanu gry
└── src/
    ├── main/java/spacemerchant/              # Kod źródłowy aplikacji
    │   ├── controller/                       # Cykl życia aplikacji i zarządzanie interfejsem graficznym TUI
    │   ├── data/                             # Rejestry statyczne (bazy statków, mapa świata) oraz system zapisu
    │   ├── exception/                        # Wyjątki domenowe chroniące zasady biznesowe przed złamaniem
    │   ├── model/                            # Czyste klasy domenowe (statki, załoganci, towary, lokacje mapy)
    │   ├── service/                          # "Serce" gry – algorytmy ekonomiczne, system morale, nawigacja i rozwój
    │   │   └── events/                       # Moduły mechanik specjalnych, w tym losowe potyczki w nadprzestrzeni
    │   └── view/                             # Klasy UI i okna gry renderowane za pomocą komponentów Lanterna
    │
    └── test/java/spacemerchant/              # Zestaw zautomatyzowanych testów sprawdzających niezawodność
        ├── data/                             # Testy integralności – np. czy z każdego miejsca na mapie da się wrócić
        └── service/                          # Testy jednostkowe chroniące logikę rynkową i poprawność transakcji

```

---

## 🛠 Użyte Technologie i Biblioteki (Pod Lupa)

Projekt udowadnia, jak można budować zaawansowane aplikacje bez używania "ciężkich" silników graficznych, bazując wyłącznie na ekosystemie Javy i czystych wzorcach projektowych.

### 1. Java (Docelowo Release 26) i Cechy Języka

Projekt intensywnie używa nowoczesnych paradygmatów Javy zlokalizowanych w całej strukturze.

* **Rekordy (Records):** Aby zapewnić niemutowalność i czystość nośników danych pomocniczych użyto systemowych rekordów. Przykłady to wewnętrzny rekord `PirateStats` w pakiecie `service.events` (przechowujący wylosowane parametry wroga), punkt na mapie gwiezdnej w pakiecie `view`, czy też szablon roli załoganta w fabrykach w pakiecie `service`.
* **Java Streams API:** Zastosowane w serwisach biznesowych do szybkiej i eleganckiej agregacji. Używane w pakiecie `service` (szczególnie przy nawigacji i potyczkach) by błyskawicznie wyliczyć zbiorcze parametry `Walki` czy `Pilotażu` dla tych załogantów, którzy są sprawni.

### 2. Lanterna (v3.1.2) - Silnik Renderujący TUI

**Gdzie się znajduje:** Cały pakiet `spacemerchant.view` oraz inicjalizator w pakiecie `spacemerchant.controller`.
Lanterna to potężna biblioteka, która operuje bezpośrednio na buforze konsoli poprzez sekwencje ANSI. Pozwala zignorować standardowe wyjście tekstowe na rzecz rysowania interaktywnych okien w określonych koordynatach terminala.

* **Inicjalizacja:** Projekt tworzy ekran i wywołuje menedżera wielu okien. Klasy konfiguracyjne definiują niestandardowy schemat kolorów wymuszający ścisłą monochromatyczność (czarne tło, biały tekst), pasującą do terminali statków kosmicznych.
* **Budowa UI:** Ekrany (np. panele stoczni, kokpit) dziedziczą po oknach bazowych z biblioteki. Layouty układane są za pomocą kontenerów grupowych i mechanizmów siatki kolumnowej. Elementy interaktywne takie jak kontrolki wprowadzania wartości są walidowane za pomocą wyrażeń regularnych, aby upewnić się, że gracz poprawnie wprowadza ilość kupowanych sztuk towaru czy paliwa.

### 3. Jackson Databind (v2.17.0) - Zapisywanie i Ładowanie (Serializacja)

**Gdzie się znajduje:** Całość logiki zapisu zamknięta jest w pakiecie `spacemerchant.data`.
Standardowa serializacja obiektowa napotyka potężne trudności, gdy grafy zależności w obiekcie tworzą cykle (jak np. mapa wszechświata, gdzie każdy układ gwiezdny odsyła z powrotem do poprzedniego).

* **Rozwiązanie (Wzorzec DTO):** Wewnątrz klas zarządzających stanem dyskowym utworzono spłaszczone obiekty do transferu danych (Data Transfer Objects).
* Zamiast zapisywać rekursywnie całą galaktykę, system zrzuca do pliku jedynie tekstowe identyfikatory i bazowe statystyki (stan paliwa, kredytów, stan HP załogantów).
* Podczas ładowania system Jackson wczytuje czysty JSON, a gra na nowo "ożywia" obiekt statku, odszukując powiązane systemy czy konkretne typy towarów z wewnętrznych i statycznych baz danych projektu, przywracając oryginalne referencje w locie.

### 4. JUnit Jupiter 5 (v5.10.2) - Automatyzacja Testów

**Gdzie się znajduje:** Zlokalizowane w drzewie katalogów `src/test/java/spacemerchant/`.
Ponieważ gra zawiera znaczącą ilość logiki matematycznej i biznesowej (ceny rynkowe, wypłaty, kalkulacje szans), kluczowe komponenty chronione są przed błędami wywołanymi ewentualnymi zmianami w kodzie za pomocą zestawu testów.

* Weryfikują one nie tylko oczywistości (jak np. zapobieganie zakupom z niewystarczającym saldem czy blokowanie możliwości rekrutacji przy przepełnionych kojach na statku), ale dbają o fizykę wirtualnego świata. Testują zliczanie wagi asortymentu statku w ładowni czy analizują spójność grafu nawigacyjnego z uwzględnieniem minimalnej wymaganej ilości skoków do różnych sektorów.

---

## 🎮 Instrukcja Uruchomienia

Dzięki wykorzystaniu frameworka Maven do budowy narzędzia, kompilacja i odpalenie projektu jest banalnie proste na każdej platformie (Windows, Linux, macOS) wyposażonej w środowisko Java.

1. Upewnij się, że posiadasz zainstalowane **JDK (Java Development Kit)** (najlepiej w wersji 21 lub wyższej) oraz menedżer budowania **Maven**.
2. Sklonuj repozytorium do wybranego katalogu na swoim dysku.
3. Otwórz wiersz poleceń / terminal, przejdź do głównego katalogu projektu (tam gdzie znajduje się plik `pom.xml`) i zbuduj projekt komendą:
```bash
mvn clean package

```


4. Maven pobierze wszystkie wymagane biblioteki (Lanterna, Jackson) i skompiluje kod źródłowy.
5. Uruchom aplikację za pomocą swojego IDE (np. IntelliJ IDEA, Eclipse), wybierając główną klasę projektu zawierającą metodę `main`.
> **Uwaga:** Aby gra wyświetlała się poprawnie i wspierała obsługę okien, konsola / emulator terminala musi w pełni obsługiwać znaki ucieczki ANSI. IntelliJ IDEA domyślnie wspiera ten tryb w wbudowanej konsoli po uruchomieniu aplikacji.



```

