# Railway Deployment Guide - CV Career Coach

Krok po kroku instrukcja wdrożenia aplikacji na Railway.app (darmowy hosting).

---

## 📋 Wymagania wstępne

✅ Konto GitHub  
✅ Klucz API OpenAI (z credits)  
✅ Projekt zbudowany lokalnie (`mvn clean package` działa)

---

## 🚀 Krok 1: Przygotuj projekt do deploymentu

### 1.1 Utwórz plik `.gitignore` w głównym katalogu

```gitignore
# Maven
target/
pom.xml.tag
pom.xml.releaseBackup
pom.xml.versionsBackup
pom.xml.next
release.properties

# IDE
.idea/
*.iml
.vscode/
.settings/
.classpath
.project

# OS
.DS_Store
Thumbs.db

# Application
data/
cv/
*.log
application-local.properties

# Secrets
.env
```

### 1.2 Dodaj `Procfile` (opcjonalnie, Railway wykryje Spring Boot automatycznie)

W głównym katalogu utwórz plik `Procfile`:

```
web: java -Dserver.port=$PORT $JAVA_OPTS -jar target/cv-career-coach-1.0.0.jar --spring.profiles.active=prod
```

### 1.3 Sprawdź `application-prod.properties`

Upewnij się, że plik `src/main/resources/application-prod.properties` używa zmiennych środowiskowych:

```properties
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
spring.ai.openai.api-key=${OPENAI_API_KEY}
```

---

## 🎯 Krok 2: Utwórz repozytorium GitHub

### 2.1 Inicjalizuj Git (jeśli jeszcze nie zrobiłeś)

```bash
cd cv-career-coach
git init
git add .
git commit -m "Initial commit - CV Career Coach"
```

### 2.2 Utwórz repozytorium na GitHub

1. Wejdź na https://github.com/new
2. Nazwa: `cv-career-coach`
3. Publiczne lub prywatne (Railway obsługuje oba)
4. **NIE dodawaj** README, .gitignore, license (masz już lokalnie)
5. Kliknij **Create repository**

### 2.3 Połącz z GitHub

```bash
git remote add origin https://github.com/TWOJA_NAZWA/cv-career-coach.git
git branch -M main
git push -u origin main
```

---

## 🚂 Krok 3: Utwórz konto Railway

1. Wejdź na https://railway.app
2. Kliknij **Login with GitHub**
3. Autoryzuj Railway do dostępu do Twojego GitHub
4. ✅ Otrzymasz **$5 darmowego kredytu** miesięcznie

---

## 🔧 Krok 4: Deploy aplikacji na Railway

### 4.1 Utwórz nowy projekt

1. W Railway Dashboard kliknij **New Project**
2. Wybierz **Deploy from GitHub repo**
3. Wybierz repozytorium: `cv-career-coach`
4. Railway automatycznie wykryje Spring Boot i zacznie budować

### 4.2 Dodaj bazę MySQL

1. W projekcie kliknij **+ New**
2. Wybierz **Database** → **Add MySQL**
3. Railway automatycznie utworzy bazę i ustawi zmienne środowiskowe:
    - `MYSQL_URL`
    - `MYSQL_USER`
    - `MYSQL_PASSWORD`
    - `MYSQL_DATABASE`

### 4.3 Skonfiguruj zmienne środowiskowe dla aplikacji

1. Kliknij na swoją aplikację (service z ikoną Java)
2. Przejdź do zakładki **Variables**
3. Dodaj następujące zmienne:

```bash
OPENAI_API_KEY=sk-twoj-klucz-tutaj

SPRING_DATASOURCE_URL=jdbc:mysql://${MYSQLHOST}:${MYSQLPORT}/${MYSQLDATABASE}

SPRING_DATASOURCE_USERNAME=${MYSQLUSER}

SPRING_DATASOURCE_PASSWORD=${MYSQLPASSWORD}

SPRING_PROFILES_ACTIVE=prod
```

**WAŻNE:** Railway automatycznie wstrzyknie zmienne MySQL (`MYSQLHOST`, `MYSQLPORT`, etc.) - użyj ich!

### 4.4 Połącz aplikację z bazą

1. Kliknij na aplikację
2. Przejdź do **Settings** → **Service**
3. W sekcji **Networking** upewnij się, że oba serwisy są w tej samej sieci

---

## 🎉 Krok 5: Wdrożenie i testowanie

### 5.1 Railway automatycznie zbuduje projekt

Railway wykona:
```bash
mvn clean package -DskipTests
java -jar target/cv-career-coach-1.0.0.jar
```

Monitoruj logi w zakładce **Deployments**.

### 5.2 Uzyskaj URL aplikacji

1. Kliknij na swoją aplikację
2. Przejdź do **Settings** → **Networking**
3. W sekcji **Public Networking** kliknij **Generate Domain**
4. Otrzymasz URL typu: `https://cv-career-coach-production.up.railway.app`

### 5.3 Przetestuj aplikację

Otwórz swój URL w przeglądarce:
```
https://twoja-aplikacja.up.railway.app
```

Powinien pojawić się frontend z formularzem upload PDF!

---

## 🔍 Krok 6: Debugowanie (jeśli coś nie działa)

### 6.1 Sprawdź logi

W Railway Dashboard:
1. Kliknij na aplikację
2. Zakładka **Deployments** → wybierz najnowszy deployment
3. Kliknij **View Logs**

Szukaj błędów typu:
- `Connection