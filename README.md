# Setup Docker

## Instalare

- **Windows**: Instalează [Docker Desktop](https://www.docker.com/products/docker-desktop) și pornește-l.
- **macOS**: Instalează [Docker Desktop](https://www.docker.com/products/docker-desktop) și pornește-l.
- **Linux**: Instalează Docker Engine și Docker Compose ([ghid aici](https://docs.docker.com/engine/install/)).

## Cum rulezi proiectul

1. Asigură-te că Docker este pornit.
2. Deschide un terminal.
3. Mergi în folderul principal al proiectului (cel care conține fișierul `docker-compose.yml`).
4. Rulează comanda:

   ```bash
   docker compose up --build -d
   ```

Aceasta va porni automat:
- baza de date
- backend-ul (disponibil pe [http://localhost:3000](http://localhost:3000))
- frontend-ul (disponibil pe [http://localhost:8080](http://localhost:8080))

Pentru a opri serviciile:

```bash
docker compose down
```
