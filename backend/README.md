# Cum să configurezi backend-ul pentru **Quizzy**hh

Acest ghid te va ajuta să configurezi backend-ul pentru proiectul **Quizzy** pas cu pas.

## Partea 1: Clonarea Repository-ului

1. Deschide GitHub-ul și accesează repository-ul **'Quizzy'**.
2. Apasă pe butonul albastru **`<> CODE`** și copiază URL-ul de clonare al repository-ului.
3. Deschide un terminal și navighează în locația unde dorești să clonezi repository-ul folosind comanda `cd <calea_dorita>`.
4. Rulează comanda următoare pentru a clona repository-ul:
   ```bash
   git clone https://github.com/IP-A2-2025/Quizzy.git
5. Deschide **Intelij**:
   - Mergi la **File** -> **Open Project**.
   - Căută folderul **Quizzy** unde l-ai clonat.
   - Accesează folderul **'backend'** și apasă pe **Open**.
  
## Partea 2: Configuratea Inteliji
1. În IntelliJ, mergi la **File** -> **Project Structure**.
2. În secțiunea **Platform Settings** -> **SDKs**, apasă pe **+** și alege **Download JDK**.
3. Căută **Amazon Corretto** (pentru **Mac**) sau **JDK 23** (pentru **Windows**) la **Vendor** și alege **Version 23**. Apasă pe **Download**.
4. După finalizarea instalării, în fereastra **Project Structure**, accesează **Project** și selectează **Corretto-23** (Mac)/ **JDK 23** (Windows) la câmpul **SDK**.
5. Apasă **OK** pentru a salva modificările.

## Partea 3: Configurarea Aplicației Spring Boot
1. În fereastra principală, mergi sus și caută **Backend Application**.
2. Apasă pe **Edit Configurations**.
3. Apasă pe **+** în partea stângă sus și alege **Spring Boot**.
4. La câmpul **Name**, scrie **'Backend'**.
5. La câmpul **Main Class**, scrie: `com.backend.BackendApplication`.
6. Apoi o sa trebuiasca sa dai copy la niste informatii trimise in privat.
7. La secțiunea **Build and Run**, apasă pe **Modify options** -> **Environment variables**.
8. Apasă pe iconița cea mai din dreapta din campul nou aparut (seamana cu o lista) și lipește variabilele de mediu (la care ai dat copy) în fereastra care apare.
9. Apasă **OK** și apoi **Apply**.

### Partea 4: Rulează Aplicația
1. În fereastra principală, în partea dreaptă, vei vedea mai multe iconițe, printre care și iconița **M** (de la Maven).
2. Apasă pe **M** -> **backend** -> **lifecycle** și apasă pe **clean**.
3. Dacă totul a fost configurat corect, în terminal vei vedea mesajul **'Process finished with exit code 0'**, ceea ce înseamnă că procesul a avut succes.

---

**Felicitări!** Acum ai configurat corect backend-ul pentru **Quizzy**. Dacă întâmpini dificultăți, nu ezita să ceri ajutor!
