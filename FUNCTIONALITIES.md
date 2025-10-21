# CV Career Coach - Funkcjonalności

## 📋 O projekcie

**Grupa docelowa:** Osoby szukające pracy

**Cel:** Optymalizacja CV, analiza braków kompetencyjnych, przygotowanie do przebranzowienia

**Model:** Darmowa aplikacja portfolio

**Deployment:** Railway

---

## ✅ MVP

**Estymacja:** 4-6 tygodni

### 1. Zarządzanie CV z cache'owaniem
- Upload PDF (drag & drop)
- Hash pliku (SHA-256) - reuse analizy
- Analiza AI (OpenAI GPT-4o-mini)
- Zapis do MySQL

### 2. Integracja z job boards
- JustJoin.it API
- Pracuj.pl (web scraping)
- Cache ofert (24h TTL)

### 3. Skills Gap Analysis ⭐ CORE FEATURE
- Porównanie CV skills vs Job requirements
- Missing skills + learning time
- Match score (0-100%)
- Rekomendacje nauki

### 4. Dopasowane oferty pracy
- Prawdziwe oferty z job boards
- AI matching + sorting
- Top 10 ofert
- Link do aplikowania

### 5. Cover Letter Generation
- AI generation pod konkretną ofertę
- Edycja tekstu
- Export PDF/DOCX
- Multi-language (PL/EN)

### 6. Alternatywne ścieżki kariery
- Oferty w innych branżach
- Transferowalne skills
- Przykład: Java Dev → Product Owner

### 7. Frontend - Dashboard
- Upload interface
- CV analysis results
- Job offers list
- Skills gap visualization
- Responsive design

---

## 🚀 Przyszłe funkcjonalności

### Phase 2 (2-3 miesiące)

#### 8. System użytkowników
- Rejestracja/logowanie (Spring Security)
- Profil użytkownika
- Multi-user support
- Izolacja danych

#### 9. CV Optimization Tips
- AI suggestions
- CV scoring (0-100)
- Action verbs, metrics, formatting

#### 10. Multi-language Support
- UI w PL/EN (i18n)
- Tłumaczenie CV/Cover Letter
- Auto-detect języka

#### 11. Learning Resources
- Udemy/Coursera integration
- Darmowe kursy (YouTube, freeCodeCamp)
- Certyfikaty (AWS, Azure, GCP)

#### 12. Analytics Dashboard
- Statystyki użytkownika
- Match score trends
- Most wanted skills

---

### Phase 3 (6+ miesięcy)

#### 13. Historia zmian CV
- Version control
- Diff viewer
- Restore old versions

#### 14. Interview Preparation
- AI-generated questions
- Mock interviews
- Audio/video recording
- Feedback od AI

#### 15. Application Tracking System
- Status aplikacji (Applied, Interview, Rejected, Offer)
- Przypomnienia follow-up
- Statystyki conversion rate

#### 16. AI CV Builder
- Kreator krok po kroku
- Templates (Modern, Classic, Creative)
- Real-time preview

#### 17. LinkedIn Integration
- Import CV z LinkedIn (OAuth)
- Auto-fill danych
- Sync updates

#### 18. Mobile App
- React Native / Flutter
- Push notifications
- Offline mode

---

## 🚦 Status

| # | Funkcjonalność | Status | Faza |
|---|---|---|---|
| 1 | Zarządzanie CV + cache | ⚠️ Partial | MVP |
| 2 | Job boards integration | ❌ TODO | MVP |
| 3 | Skills Gap Analysis | ❌ TODO | MVP |
| 4 | Dopasowane oferty | ⚠️ Refactor | MVP |
| 5 | Cover Letter | ❌ TODO | MVP |
| 6 | Alternatywne oferty | ⚠️ Refactor | MVP |
| 7 | Frontend Dashboard | ⚠️ Partial | MVP |
| 8 | System użytkowników | ❌ TODO | Phase 2 |
| 9 | CV Optimization | ❌ TODO | Phase 2 |
| 10 | Multi-language | ❌ TODO | Phase 2 |
| 11 | Learning Resources | ❌ TODO | Phase 2 |
| 12 | Analytics | ❌ TODO | Phase 2 |
| 13 | Historia CV | ❌ TODO | Phase 3 |
| 14 | Interview Prep | ❌ TODO | Phase 3 |
| 15 | ATS | ❌ TODO | Phase 3 |
| 16 | AI CV Builder | ❌ TODO | Phase 3 |
| 17 | LinkedIn | ❌ TODO | Phase 3 |
| 18 | Mobile App | ❌ TODO | Phase 3 |

---

## 💰 Koszty (estymacja)

**MVP:**
- OpenAI API: ~$20-50/miesiąc (cache ON)
- Railway: $5-20/miesiąc

**Phase 2:** +$30-100/miesiąc

**Phase 3:** Zależne od skali

---

## 🛠️ Tech Stack

**Backend:** Java 17, Spring Boot, Spring AI, MySQL, Apache PDFBox

**Frontend:** Vanilla JS → React.js (future), Tailwind CSS, Chart.js

**Infrastructure:** Railway, Redis (optional), AWS S3 (optional)

**APIs:** OpenAI GPT-4o-mini, JustJoin.it, Pracuj.pl

---

**Wersja:** 1.0 | **Status:** ✅ Approved