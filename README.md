# ⚽ RENTABALL

> Android app for booking football fields in Lisbon — built with Jetpack Compose + Firebase.

---

## Screenshots

| Splash | Login | Home | Field Details |
|--------|-------|------|---------------|
| ![](Screenshots/splashscreen.png) | ![](Screenshots/loginscreen.png) | ![](Screenshots/homescreen.png) | ![](Screenshots/fielddetailsscreen.png) |

| Bookings | Profile | PRO | Explore |
|----------|---------|-----|---------|
| ![](Screenshots/reservationscreen.png) | ![](Screenshots/profilescreen.png) | ![](Screenshots/proscreen.png) | ![](Screenshots/explorescreen.png) |

---

## Project Structure

```
Campo/
├── app/src/main/java/com/example/campo/
│   ├── MainActivity.kt              ← Navigation host + bottom nav setup
│   ├── data/
│   │   └── BookingRepository.kt     ← All Firestore read/write calls
│   ├── model/
│   │   ├── SoccerField.kt           ← SoccerField data class + sampleFields()
│   │   └── Booking.kt               ← Booking data class + BookingStatus enum
│   ├── ui/
│   │   ├── components/
│   │   │   └── HomeComponents.kt    ← Reusable composables (SearchBar, FilterChips, Cards…)
│   │   ├── screens/
│   │   │   ├── splash/SplashScreen.kt
│   │   │   ├── login/LoginScreen.kt
│   │   │   ├── home/HomeScreen.kt
│   │   │   ├── explore/ExploreScreen.kt
│   │   │   ├── details/FieldDetailsScreen.kt
│   │   │   ├── reservations/ReservationsScreen.kt
│   │   │   └── profile/
│   │   │       ├── ProfileScreen.kt
│   │   │       ├── SettingsScreen.kt
│   │   │       ├── HelpScreen.kt
│   │   │       ├── TermsScreen.kt
│   │   │       └── ProScreen.kt
│   │   └── theme/
│   │       ├── Color.kt             ← App colour palette (DarkBackground, SoftGreen…)
│   │       └── Theme.kt             ← CampoTheme
├── app/src/main/res/
│   ├── values/strings.xml           ← Default strings (Portuguese)
│   ├── values-en/strings.xml        ← English strings
│   ├── values-es/strings.xml        ← Spanish strings
│   └── drawable/                    ← campo1…campo6 field images
├── Screenshots/                     ← App screenshots (all 11 screens)
└── RENTABALL_Relatorio_Final.pdf    ← Final project report
```

---

## Key Files

| File | What it does |
|------|-------------|
| [`MainActivity.kt`](app/src/main/java/com/example/campo/MainActivity.kt) | NavHost with all routes; bottom nav with 4 tabs |
| [`BookingRepository.kt`](app/src/main/java/com/example/campo/data/BookingRepository.kt) | `saveBooking`, `getUserBookings`, `getBookedTimesForField`, `setPro`, `getIsPro`, `cancelPro` |
| [`SoccerField.kt`](app/src/main/java/com/example/campo/model/SoccerField.kt) | Field model + `sampleFields()` list of 6 Lisbon fields |
| [`Booking.kt`](app/src/main/java/com/example/campo/model/Booking.kt) | Booking model with `isUpcoming` flag and `imageRes` |
| [`FieldDetailsScreen.kt`](app/src/main/java/com/example/campo/ui/screens/details/FieldDetailsScreen.kt) | Day selector → time slots (live from Firestore) → book |
| [`HomeComponents.kt`](app/src/main/java/com/example/campo/ui/components/HomeComponents.kt) | `FilterChipsRow`, `FeaturedFieldCard`, `NearbyFieldRow`, `CampoSearchBar` |
| [`Color.kt`](app/src/main/java/com/example/campo/ui/theme/Color.kt) | All colour tokens used across the app |

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin |
| UI | Jetpack Compose |
| Navigation | Navigation Compose |
| Auth | Firebase Authentication (Email/Password) |
| Database | Firebase Firestore |
| Multilingual | Android String Resources (PT / EN / ES) |
| Min SDK | API 26 (Android 8.0) |

---

## Firebase Setup

The app requires a connected Firebase project. The `google-services.json` is **not** committed to the repo for security reasons.

To run the project:
1. Create a Firebase project at [console.firebase.google.com](https://console.firebase.google.com)
2. Enable **Authentication → Email/Password**
3. Create a **Firestore** database in production mode
4. Download `google-services.json` and place it in `app/`
5. Create a **Firestore composite index** on collection `bookings`:
   - `fieldId` — Ascending
   - `eventTimestamp` — Ascending

### Firestore Collections

**`bookings`**
```
fieldId          String
userEmail        String
fieldName        String
location         String
dateLabel        String    e.g. "Sáb, 4 Jul"
timeLabel        String    e.g. "18:00"
durationHours    Number
format           String
price            Number
status           String    "CONFIRMED"
eventTimestamp   Number    Unix ms — used for upcoming/history split
```

**`users`** (document ID = user email)
```
isPro   Boolean
```

---

## Navigation Flow

```
Splash ──► Login / Register
                │
                ▼
    ┌───────────────────────┐
    │   Bottom Navigation   │
    │  Home · Explore ·     │
    │  Bookings · Profile   │
    └───────────────────────┘
         │              │
         ▼              ▼
   Field Details     Settings
   (day + slots      Help
    + book)          Terms & Conditions
                     RENTABALL PRO
```

---

## Multilingual Support

All UI strings are defined in Android String Resources:

| Folder | Language |
|--------|----------|
| [`res/values/strings.xml`](app/src/main/res/values/strings.xml) | Portuguese (default) |
| [`res/values-en/strings.xml`](app/src/main/res/values-en/strings.xml) | English |
| [`res/values-es/strings.xml`](app/src/main/res/values-es/strings.xml) | Spanish |

Filter chip internal keys (`"ALL"`, `"TONIGHT"`, `"Sintético"`) are language-independent — only display labels are localised.

---

## Report

The full project report (in Portuguese) is available at [`RENTABALL_Relatorio_Final.pdf`](RENTABALL_Relatorio_Final.pdf).
It includes wireframes, mockups, screenshots, E-A diagram, DB schema, UML class diagram, usability evaluation results and conclusions.

---

*ENIDH — Mobile Computing · 2025/2026 · Rodrigo Ribeiro · 14301*
