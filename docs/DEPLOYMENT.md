# Deployment Guide

## Prerequisites

- Docker and Docker Compose
- Kubernetes cluster (for production)
- PostgreSQL 14+
- Redis 7+
- Apache Kafka 3+

## Local Development

### Using Docker Compose

```bash
# Clone the repository
git clone https://github.com/jemeson007/global-pay.git
cd global-pay

# Start all services
docker-compose up -d

# Check service status
docker-compose ps

# View logs
docker-compose logs -f global-pay
```

### Manual Setup

```bash
# Backend
cd backend
mvn clean package -DskipTests
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# Frontend
cd frontend
npm install
npm start
```

## Production Deployment

### Kubernetes Deployment

```bash
# Create namespace
kubectl create namespace global-pay

# Apply configurations
kubectl apply -f kubernetes/configmap.yaml -n global-pay
kubectl apply -f kubernetes/secrets.yaml -n global-pay
kubectl apply -f kubernetes/deployment.yaml -n global-pay
kubectl apply -f kubernetes/ingress.yaml -n global-pay

# Verify deployment
kubectl get deployments -n global-pay
kubectl get pods -n global-pay
kubectl get services -n global-pay
```

### Environment Variables

```bash
# Database
SPRING_DATASOURCE_URL=jdbc:postgresql://prod-db:5432/globalpay
SPRING_DATASOURCE_USERNAME=<db-user>
SPRING_DATASOURCE_PASSWORD=<db-password>

# Redis
SPRING_REDIS_HOST=prod-redis
SPRING_REDIS_PORT=6379

# Kafka
SPRING_KAFKA_BOOTSTRAP_SERVERS=prod-kafka:9092

# JWT
JWT_SECRET=<secure-jwt-secret>
JWT_EXPIRATION=86400000

# Other
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=prod
```

### SSL/TLS Setup

```bash
# Create TLS secret
kubectl create secret tls global-pay-tls \
  --cert=path/to/cert.pem \
  --key=path/to/key.pem \
  -n global-pay
```

## Monitoring

### Access Dashboards

- **Grafana**: http://localhost:3000 (admin/admin)
- **Prometheus**: http://localhost:9090
- **Kibana**: http://localhost:5601

### Health Checks

```bash
# Backend health
curl http://localhost:8080/api/actuator/health

# Database
curl http://localhost:8080/api/actuator/health/db

# Kafka
curl http://localhost:8080/api/actuator/health/kafka
```

## Backup & Recovery

### Database Backup

```bash
pg_dump -U postgres globalpay > backup.sql
```

### Database Restore

```bash
psql -U postgres globalpay < backup.sql
```

## Troubleshooting

### Service Won't Start

```bash
# Check logs
docker-compose logs global-pay

# Check configuration
cat application-prod.yml

# Test database connection
psql -h localhost -U postgres -d globalpay
```

### Performance Issues

1. Check database indexes: `SELECT * FROM pg_stat_user_indexes;`
2. Monitor CPU/Memory: `docker stats`
3. Check Redis cache hit rate
4. Review slow query logs

## Updates & Rollback

```bash
# Rolling update
kubectl set image deployment/global-pay-backend \
  global-pay=jemeson007/global-pay:new-version \
  -n global-pay

# Rollback
kubectl rollout undo deployment/global-pay-backend -n global-pay

# Check rollout history
kubectl rollout history deployment/global-pay-backend -n global-pay
```
