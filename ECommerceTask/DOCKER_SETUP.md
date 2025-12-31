# Docker Setup Guide

This guide explains how to run the E-Commerce application using Docker and Docker Compose.

## Prerequisites

- Docker Desktop installed (or Docker Engine + Docker Compose)
- Docker version 20.10 or higher
- Docker Compose version 2.0 or higher

## Quick Start

### Option 1: Using Docker Compose (Recommended)

1. **Build and start the application:**
   ```bash
   docker-compose up --build
   ```

2. **Start in detached mode (background):**
   ```bash
   docker-compose up -d --build
   ```

3. **View logs:**
   ```bash
   docker-compose logs -f ecommerce-app
   ```

4. **Stop the application:**
   ```bash
   docker-compose down
   ```

5. **Stop and remove volumes (clears database):**
   ```bash
   docker-compose down -v
   ```

### Option 2: Using Docker directly

1. **Build the Docker image:**
   ```bash
   docker build -t ecommerce-app .
   ```

2. **Run the container:**
   ```bash
   docker run -d \
     --name ecommerce-api \
     -p 8080:8080 \
     -v $(pwd)/data:/app/data \
     ecommerce-app
   ```

3. **View logs:**
   ```bash
   docker logs -f ecommerce-api
   ```

4. **Stop the container:**
   ```bash
   docker stop ecommerce-api
   docker rm ecommerce-api
   ```

## Docker Compose Services

### ecommerce-app
- **Port:** 8080
- **Build:** Uses Dockerfile in the current directory
- **Volumes:** 
  - `./data:/app/data` - Persists H2 database file
- **Environment Variables:**
  - `SPRING_DATASOURCE_URL` - H2 database file path
  - `JWT_SECRET` - JWT token secret key
  - `JWT_EXPIRATION` - JWT token expiration (milliseconds)

## Data Persistence

The H2 database file is stored in the `./data` directory, which is mounted as a volume. This ensures data persists even when containers are stopped or removed (unless using `docker-compose down -v`).

**Database location in container:** `/app/data/ecommerce.mv.db`  
**Database location on host:** `./data/ecommerce.mv.db`

## Environment Variables

You can customize the application by modifying environment variables in `docker-compose.yml`:

```yaml
environment:
  - SPRING_DATASOURCE_URL=jdbc:h2:file:/app/data/ecommerce
  - SPRING_DATASOURCE_USERNAME=sa
  - SPRING_DATASOURCE_PASSWORD=your_password
  - JWT_SECRET=your_secret_key_here
  - JWT_EXPIRATION=86400000
```

Or use a `.env` file:

1. Create `.env` file:
   ```env
   JWT_SECRET=your_secret_key_here
   JWT_EXPIRATION=86400000
   ```

2. Reference in docker-compose.yml:
   ```yaml
   environment:
     - JWT_SECRET=${JWT_SECRET}
     - JWT_EXPIRATION=${JWT_EXPIRATION}
   ```

## Accessing the Application

Once the container is running:

- **API Base URL:** `http://localhost:8080`
- **H2 Console:** `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:file:/app/data/ecommerce`
  - Username: `sa`
  - Password: (empty)

## Useful Docker Commands

### View running containers
```bash
docker-compose ps
```

### View logs
```bash
docker-compose logs -f ecommerce-app
```

### Restart service
```bash
docker-compose restart ecommerce-app
```

### Execute commands inside container
```bash
docker-compose exec ecommerce-app sh
```

### View container resource usage
```bash
docker stats ecommerce-api
```

### Remove everything (containers, networks, volumes)
```bash
docker-compose down -v
docker system prune -a
```

## Troubleshooting

### Port already in use
If port 8080 is already in use, change it in `docker-compose.yml`:
```yaml
ports:
  - "8081:8080"  # Use port 8081 on host
```

### Database file permission issues
If you encounter permission issues with the data directory:
```bash
sudo chown -R $USER:$USER ./data
```

### Container won't start
1. Check logs: `docker-compose logs ecommerce-app`
2. Verify Docker is running: `docker ps`
3. Check if port is available: `netstat -an | grep 8080`

### Rebuild after code changes
```bash
docker-compose up --build
```

## Production Considerations

For production deployment, consider:

1. **Use PostgreSQL or MySQL instead of H2:**
   - Update `docker-compose.yml` to include a database service
   - Modify `application.properties` for production profile
   - Update datasource configuration

2. **Use environment-specific configuration:**
   - Create `application-prod.properties`
   - Use environment variables for sensitive data
   - Use secrets management

3. **Add reverse proxy:**
   - Include nginx or traefik in docker-compose
   - Configure SSL/TLS

4. **Monitoring and logging:**
   - Add health checks
   - Configure log aggregation
   - Add monitoring services

5. **Security:**
   - Use strong JWT secrets
   - Enable HTTPS
   - Configure firewall rules
   - Use non-root user in container

## Example: Production Docker Compose with PostgreSQL

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:15-alpine
    container_name: ecommerce-db
    environment:
      POSTGRES_DB: ecommerce
      POSTGRES_USER: ecommerce_user
      POSTGRES_PASSWORD: secure_password
    volumes:
      - postgres-data:/var/lib/postgresql/data
    networks:
      - ecommerce-network

  ecommerce-app:
    build: .
    container_name: ecommerce-api
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/ecommerce
      SPRING_DATASOURCE_USERNAME: ecommerce_user
      SPRING_DATASOURCE_PASSWORD: secure_password
      JWT_SECRET: your_production_secret
    depends_on:
      - postgres
    networks:
      - ecommerce-network

networks:
  ecommerce-network:
    driver: bridge

volumes:
  postgres-data:
```

