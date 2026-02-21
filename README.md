# WĘDRÓWKA PRZEZ MROKI
## Konsolowa gra tekstowa RPG w JAVA

---

## 1. Strona tytułowa

**Tytuł projektu:** WĘDRÓWKA PRZEZ MROKI (Journey through Darkness)

**Autor:** Daniel

**Przedmiot:** Programowanie obiektowe (Java)

**Nauczyciel:** Wojciech mierza

**Data:** 2026

---

## 2. Cel i opis gry

### Opis ogólny
"Wędrówka przez Mroki" to konsolowa gra tekstowa RPG, w której gracz tworzy własną postać, eksploruje świat pełen niebezpieczeństw, walczy z przeciwnikami, unika pułapek, zbiera skarby i dąży do pokonania głównego antagonisty - Czarnego Władcy.

### Cel gry z perspektywy gracza
Gracz musi dotrzeć do Ruin starożytnej świątyni i pokonać Czarnego Władcę, który jest bossem końcowym. Aby tego dokonać, musi:
- Stworzyć postać o odpowiednich atrybutach
- Eksplorować różne lokacje
- Walczyć z przeciwnikami i zdobywać doświadczenie
- Znajdować skarby i przedmioty
- Awansować na wyższe poziomy

### Rozgrywka
1. **Tworzenie postaci** - gracz podaje imię i rozdziela punkty między Siłę, Zwinność i Inteligencję
2. **Eksploracja** - poruszanie się między lokacjami za pomocą komend
3. **Zdarzenia losowe** - walki, pułapki, skarby, dialogi
4. **Walka turowa** - system walki z turami (atak, leczenie, ucieczka)
5. **Rozwój postaci** - zdobywanie XP i awansowanie na poziomy
6. **Zakończenie** - zwycięstwo (pokonanie bossa) lub porażka (śmierć)

### Grupa docelowa
Gra przeznaczona dla graczy lubiących klasyczne gry tekstowe RPG, studentów uczących się programowania obiektowego w Javie oraz osób ceniących prostą, ale wciągającą rozgrywkę.

### Zakres funkcjonalny
- Tworzenie i rozwój postaci z atrybutami
- System walki turowej
- Zdarzenia losowe: walka, pułapka, skarb, dialog
- Ekwipunek: broń, pancerz, mikstury
- System poziomów i XP
- Zapis historii gry do pliku tekstowego
- Zapis i wczytywanie stanu gry (JSON)
- Mapa świata z wieloma lokacjami

---

## 3. Wymagania i środowisko

### Wersja Javy
- Java JDK 17 lub nowsza
- Testowano na OpenJDK 25

### IDE
- Dowolne IDE obsługujące Javę (IntelliJ IDEA, Eclipse, VS Code, itp.)

### Instrukcja uruchomienia

#### Kompilacja:
```bash
cd wanderdark
javac -d out src/main/java/com/rpg/**/*.java
```

#### Uruchomienie:
```bash
java -cp out com.rpg.engine.GameEngine
```

Lub uruchomienie bez kompilacji:
```bash
java -jar wanderdark.jar
```

### Wymagane pliki
- Brak zewnętrznych plików konfiguracyjnych
- Folder `saves/` - automatycznie tworzony dla zapisów
- Plik `historia.txt` - automatycznie tworzony dla historii

---

## 4. Projekt obiektowy (model klas)

### Diagram klas

```
                    ┌─────────────────────┐
                    │     <<abstract>>    │
                    │     Character       │
                    ├─────────────────────┤
                    │ - name: String      │
                    │ - hp: int           │
                    │ - maxHp: int        │
                    │ - strength: int      │
                    │ - agility: int       │
                    │ - intelligence: int │
                    │ - level: int        │
                    │ - xp: int           │
                    ├─────────────────────┤
                    │ + isAlive(): boolean│
                    │ + takeDamage()      │
                    │ + heal()            │
                    │ + levelUp()         │
                    │ + getDamage(): int  │
                    └──────────┬──────────┘
                               │
            ┌──────────────────┴──────────────────┐
            │                                     │
            ▼                                     ▼
┌─────────────────────┐              ┌─────────────────────┐
│       Player        │              │       Enemy         │
├─────────────────────┤              ├─────────────────────┤
│ - inventory: List  │              │ - xpReward: int     │
│ - equippedWeapon   │              │ - goldReward: int   │
│ - equippedArmor    │              └─────────────────────┘
│ - gold: int        │
│ - battlesWon       │
│ - battlesLost      │
├─────────────────────┤
│ + addItem()        │
│ + equipWeapon()    │
│ + equipArmor()     │
│ + usePotion()      │
└─────────────────────┘

┌─────────────────────┐     ┌─────────────────────┐
│     <<abstract>>    │     │    <<abstract>>     │
│        Item         │     │       Event         │
├─────────────────────┤     ├─────────────────────┤
│ - name: String      │     │ - description       │
│ - description      │     ├─────────────────────┤
│ - value: int       │     │ + getEventType()    │
├─────────────────────┤     └──────────┬──────────┘
└──────────┬──────────┘                 │
           │                            │
     ┌─────┴─────┬──────────┐    ┌──────┴──────┐
     ▼           ▼          ▼    ▼             ▼
┌─────────┐ ┌────────┐ ┌─────────┐ ┌─────────┐ ┌──────────┐
│ Weapon  │ │ Armor  │ │ Potion  │ │ Combat  │ │ Dialogue │
├─────────┤ ├────────┤ ├─────────┤ │  Event  │ │  Event   │
│-damage  │ │-defense│ │-healAmt │ │-enemy   │ │-npcName  │
│-reqLvl  │ │-reqLvl │ └─────────┘ └─────────┘ │-choices  │
└─────────┘ └────────┘              └─────────┘

┌─────────────────────┐
│     Location        │
├─────────────────────┤
│ - name: String      │
│ - description      │
│ - connectedLocations│
│ - possibleEvents   │
│ - isSafe: boolean  │
│ - isBossLocation   │
├─────────────────────┤
│ + addConnection()   │
│ + getConnection()  │
│ + addPossibleEvent │
└─────────────────────┘
```

### Opis klas

#### Character (klasa abstrakcyjna)
**Rola:** Bazowa klasa dla wszystkich postaci w grze (gracz i przeciwnicy)

**Pola:**
- `name` - imię postaci
- `hp/maxHp` - aktualne i maksymalne punkty życia
- `strength/agility/intelligence` - główne atrybuty
- `level` - poziom postaci
- `xp` - doświadczenie

**Metody:**
- `isAlive()` - sprawdza czy postać żyje
- `takeDamage(int)` - aplikuje obrażenia
- `heal(int)` - leczy postać
- `levelUp()` - sprawdza i wykonuje awans poziomu
- `getDamage()` - oblicza bazowe obrażenia

---

#### Player (extends Character)
**Rola:** Reprezentuje postać gracza

**Pola:**
- `inventory` - lista przedmiotów
- `equippedWeapon/equippedArmor` - wyposażone przedmioty
- `gold` - waluta
- `battlesWon/battlesLost/treasuresFound` - statystyki

**Metody:**
- `addItem(Item)` - dodaje przedmiot do ekwipunku
- `equipWeapon/Armor()` - wyposaża przedmiot
- `usePotion()` - używa mikstury

---

#### Enemy (extends Character)
**Rola:** Reprezentuje przeciwników

**Pola:**
- `xpReward/goldReward` - nagrody za pokonanie

---

#### Item (klasa abstrakcyjna)
**Rola:** Bazowa klasa dla przedmiotów

**Pola:**
- `name, description, value` - podstawowe właściwości

---

#### Weapon/Armor/Potion (extends Item)
**Rola:** Konkretne typy przedmiotów

**Pola specyficzne:**
- Weapon: `damage, requiredLevel`
- Armor: `defense, requiredLevel`
- Potion: `healAmount`

---

#### Location
**Rola:** Reprezentuje lokację w świecie gry

**Pola:**
- `name, description` - identyfikacja
- `connectedLocations` - mapa połączeń między lokacjami
- `possibleEvents` - lista możliwych zdarzeń
- `isSafe, isBossLocation` - flagi specjalne

---

#### Event (klasa abstrakcyjna)
**Rola:** Bazowa klasa dla zdarzeń losowych

**Podklasy:**
- `CombatEvent` - walka z przeciwnikiem
- `TrapEvent` - pułapka
- `TreasureEvent` - znalezienie skarbu
- `DialogueEvent` - interakcja z NPC

---

#### GameState
**Rola:** Przechowuje stan gry do zapisu/odczytu

**Pola:**
- `player` - obiekt gracza
- `currentLocationName` - nazwa bieżącej lokacji
- `stats` - statystyki rozgrywki

---

### Polimorfizm i interfejsy
- **Polimorfizm:** Klasy `Player` i `Enemy` dziedziczą po `Character`, klasy `Weapon`, `Armor`, `Potion` dziedziczą po `Item`, klasy zdarzeń dziedziczą po `Event`
- **Klasy abstrakcyjne:** `Character`, `Item`, `Event` - definiują wspólne zachowania
- **Serializacja:** Wszystkie klasy modelowe implementują `Serializable`

---

## 5. Logika gry i przepływ sterowania

### Pętla gry (Main Loop)

```
START
  │
  ▼
┌──────────────────────┐
│ TWORZENIE POSTACI    │
│ - Podaj imię         │
│ - Rozdziel punkty    │
│ - Utwórz Player      │
└──────────────────────┘
  │
  ▼
┌──────────────────────┐
│ INICJALIZACJA ŚWIATA │
│ - Utwórz lokacje     │
│ - Połącz lokacje     │
│ - Ustaw start        │
└──────────────────────┘
  │
  ▼
┌──────────────────────┐
│ GŁÓWNA PĘTLA GRY     │
│                      │
│ ┌──────────────────┐ │
│ │ Losuj zdarzenie │ │
│ │ (jeśli niebez.) │ │
│ └──────────────────┘ │
│         │            │
│         ▼            │
│ ┌──────────────────┐ │
│ │ Obsłuż zdarzenie│ │
│ │ - Walka          │ │
│ │ - Pułapka        │ │
│ │ - Skarb          │ │
│ │ - Dialog         │ │
│ └──────────────────┘ │
│         │            │
│         ▼            │
│ ┌──────────────────┐ │
│ │ Czekaj na komendę│ │
│ └──────────────────┘ │
│         │            │
│         ▼            │
│ ┌──────────────────┐ │
│ │ Wykonaj akcję    │ │
│ └──────────────────┘ │
│         │            │
│         ▼            │
│ Czy koniec gry? ────NO──► wróć do pętli
│         │
│        YES
│         │
│         ▼
┌──────────────────────┐
│ ZAKOŃCZENIE GRY     │
│ - Statystyki końcowe│
│ - Zapisz historię   │
└──────────────────────┘
```

### System walki

**Kolejność tur:**
1. Tura gracza
2. Akcja gracza (atak/leczenie/ucieczka)
3. Jeśli przeciwnik żywy → tura przeciwnika
4. Atak przeciwnika (z możliwością uniknięcia)
5. Powtarzaj aż do wygranej/przegranej

**Obrażenia:**
```
obrażenia = siła_gracza + broń.obrażenia + losowa_wariacja(-2 do +2)
```

**Unik:**
```
szansa_na_unik = zwinność * 2%
rzut = losowy(0-99) < szansa → unik udany
```

**Ucieczka:**
```
szansa_na_ucieczkę = zwinność * 3% + 20%
```

### System poziomów

**Zdobywanie XP:**
- Za każdego pokonanego przeciwnika: 15-100 XP (zależnie od typu)

**Awans poziomu:**
- Próg XP = poziom * 100
- Przy awansie:
  - Poziom +1
  - MaxHP +10 (odnowienie HP)
  - Siła +2
  - Zwinność +2
  - Inteligencja +2

---

## 6. Obsługa plików

### Historia gry (historia.txt)

**Zapisywane zdarzenia:**
- Rozpoczęcie/zakończenie gry
- Wejście do lokacji
- Rozpoczęcie/zakończenie walki
- Znalezienie skarbu
- Uderzenie w pułapkę
- Awans poziomu
- Użycie/wyposażenie przedmiotów

**Format:**
```
[YYYY-MM-DD HH:mm:ss] <opis zdarzenia>
```

**Lokalizacja:** Plik w katalogu roboczym gry (`historia.txt`)

---

### Zapis stanu gry (JSON)

**Plik:** `saves/zapis.json`

**Struktura:**
```json
{
  "player": {
    "name": "Bohater",
    "hp": 75,
    "maxHp": 80,
    "strength": 6,
    "agility": 5,
    "intelligence": 4,
    "level": 2,
    "xp": 50,
    "xpToNextLevel": 200,
    "gold": 150,
    "inventory": [...],
    "equippedWeapon": {...},
    "equippedArmor": {...}
  },
  "currentLocation": "Las",
  "stats": {
    "battlesWon": 5,
    "battlesLost": 1,
    "treasuresFound": 3,
    "totalTurns": 42
  }
}
```

**Proces zapisu:**
1. `GameStateManager.saveGame()` serializuje stan do JSON
2. Zapis do pliku `saves/zapis.json`

**Proces wczytywania:**
1. `GameStateManager.loadGame()` odczytuje JSON
2. Parsuje i odtwarza obiekt Player
3. Odtwarza stan gry

---

## 7. Interfejs użytkownika

### Dostępne komendy

| Komenda | Aliasy | Opis |
|---------|--------|------|
| `idź <kierunek>` | idz | Przemieszczanie między lokacjami |
| `pomoc` | help | Wyświetla listę komend |
| `statystyki` | stats | Pokazuje statystyki postaci |
| `ekwipunek` | inv, inventory | Pokazuje ekwipunek |
| `użyj <przedmiot>` | uzyj, use | Używa mikstury |
| `wyposaż <przedmiot>` | wyposaz, equip | Wyposaża broń/pancerz |
| `mapa` | map | Wyświetla mapę świata |
| `zapisz` | save | Zapisuje grę |
| `wczytaj` | load | Wczytuje grę |
| `wyjdź` | wyjdz, exit | Kończy grę |

### Przykładowy przebieg rozgrywki

```
==================================================
   WĘDRÓWKA PRZEZ MROKI
   (Journey through Darkness)
==================================================

Tworzenie nowej postaci...
Podaj imię swojego bohatera: Ezreal

Rozdź punkty atrybutów (łącznie 15 punktów):
Minimalnie 3, maksymalnie 7 na atrybut.

Siła (15 pkt pozostało): 6
Zwinność (9 pkt pozostało): 5
Inteligencja (4 pkt pozostało): 4

=== TWÓJ BOHATER ===

=== STATYSTYKI POSTACI ===
Imię: Ezreal
Poziom: 1
Doświadczenie: 0/100
HP: 80/80
Siła: 6
Zwinność: 5
Inteligencja: 4
Złoto: 50
Obrażenia: 6
==========================

Witaj w świecie Mroków, Ezreal!
Twoim celem jest dotrzeć do Ruin i pokonać Czarnego Władcę.

========================================
LOKACJA: WIOSKA
Spokojna wioska na skraju lasu. Mieszkańcy żyją tu bezpiecznie.
Dostępne kierunki: bagna, las
========================================

> idz las

========================================
LOKACJA: LAS
Gęsty, ciemny las pełen niebezpieczeństw. Ścieżki giną w mroku.
Dostępne kierunki: wioska, jaskinia, bagna, krypta
========================================

--- Napotkałeś wrogiego Bandysta! ---

Napotkałeś: Bandysta (HP: 35, Siła: 7)
Dostępne akcje:
  atak   - zaatakuj przeciwnika
  leczenie - użyj mikstury (jeśli masz)
  ucieczka - spróbuj uciec z walki

> atak

Zadajesz 6 obrażeń wrogowi Bandysta (HP: 29/35)
Bandysta zadaje Ci 7 obrażeń! (HP: 73/80)

...

> Pokonałeś Bandysta!
Walka wygrana! Zdobyto 25 XP i 15 złota.

=== AWANS! ===
Twoja postać osiągnęła wyższy poziom!
Odnowiono HP, zwiększono Siłę, Zwinność i Inteligencję!

=== STATYSTYKI POSTACI ===
Imię: Ezreal
Poziom: 2
Doświadczenie: 25/200
HP: 90/90
Siła: 8
Zwinność: 7
Inteligencja: 6
Złoto: 65
Obrażenia: 8
==========================
```

---

## 8. Testowanie

### Przetestowane scenariusze

| Scenariusz | Kroki | Oczekiwany wynik | Wynik |
|------------|-------|------------------|-------|
| Tworzenie postaci | Podanie imienia i atrybutów | Utworzenie postaci zgodnie z danymi | OK |
| Poruszanie się | Komenda `idź las` | Zmiana lokacji | OK |
| Walka - zwycięstwo | `atak` do pokonania wroga | Wygrana, zdobycie XP | OK |
| Walka - porażka | Utrata HP do 0 | Koniec gry z komunikatem | OK |
| Pułapka - unik | Test zwinności >= poziom trudności | Brak obrażeń | OK |
| Pułapka - trafienie | Test zwinności < poziom trudności | Utrata HP | OK |
| Skarb | Wejście do lokacji | Znalezienie złota/przedmiotu | OK |
| Awans poziomu | Zdobycie progu XP | Zwiększenie statystyk | OK |
| Ekwipunek - mikstura | `użyj mikstura` | Leczenie HP | OK |
| Ekwipunek - broń | `wyposaż miecz` | Zwiększenie obrażeń | OK |
| Zapis gry | Komenda `zapisz` | Utworzenie pliku JSON | OK |
| Wczytywanie gry | Komenda `wczytaj` | Odtworzenie stanu | OK |
| Historia | Wykonanie akcji | Dopisanie do pliku | OK |

### Napotkane błędy i naprawy

1. **Błąd:** Konflikt nazwy `Character` z `java.lang.Character`
   - **Naprawa:** Użycie pełnej nazwy `java.lang.Character` w metodzie parsującej JSON

2. **Błąd:** Historia zapisywana w złym katalogu
   - **Naprawa:** Użycie ścieżki względnej dla pliku historii

---

## 9. Podsumowanie i możliwe rozszerzenia

### Ocena własna

**Co działa dobrze:**
- Pełna implementacja wszystkich wymaganych funkcji
- Działający system walki, zdarzeń i rozwoju postaci
- Poprawny zapis i wczytywanie stanu gry
- Historia gry zapisuje wszystkie ważne zdarzenia
- Przejrzysta architektura obiektowa
- Działający system bossa końcowego

**Co można poprawić:**
- Bardziej rozbudowane dialogi z NPC
- System questów
- Więcej typów przedmiotów i przeciwników

### Propozycje rozszerzeń

1. **Więcej lokacji** - dodanie nowych obszarów (np. góry, jezioro)
2. **System questów** - zadania do wykonania z nagrodami
3. **Handel** - kupowanie i sprzedawanie przedmiotów
4. **GUI** - interfejs graficzny (JavaFX/Swing)
5. **Wiele slotów zapisu** - możliwość tworzenia wielu postaci
6. **Przeciwnicy boss** - mini-bossowie w każdej lokacji
7. **Klasy postaci** - wojownik, mag, złodziej z unikalnymi zdolnościami

