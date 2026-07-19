# Global Pay - International Money Transfer Platform

A comprehensive payment solution for international money transfers. This platform enables users to send and receive money across borders with real-time exchange rates and low fees.

## Features

- **Multi-Currency Support**: Transfer between 150+ currencies
- **Real-Time Exchange Rates**: Live market rates with competitive margins
- **User Accounts**: Complete KYC/AML compliance
- **Payment Methods**: Bank transfers, debit cards, credit cards
- **Transaction Management**: Track all transfers with detailed receipts
- **Recipient Management**: Save and reuse recipient accounts
- **Webhooks**: Real-time transaction status updates
- **Admin Dashboard**: Monitoring and reporting

## Technology Stack

### Backend
- **Java 21**
- **Spring Boot 3.5**
- **Spring Security** - Authentication and Authorization
- **Spring Cloud** - Microservices infrastructure
- **Spring Data JPA** - ORM and database access
- **Spring Batch** - Batch processing
- **Spring Integration** - Enterprise integration patterns

### Database
- **PostgreSQL** - Relational data store
- **Redis** - Caching layer and session store

### Messaging
- **Apache Kafka** - Event streaming and async processing

### Infrastructure
- **Docker** - Containerization
- **Kubernetes** - Orchestration
- **NGINX** - Reverse proxy and load balancing
- **Prometheus** - Metrics collection
- **Grafana** - Metrics visualization
- **ELK/OpenSearch** - Centralized logging

### Frontend
- **React** - UI framework
- **TypeScript** - Type-safe development
- **TailwindCSS** - Utility-first CSS

### Mobile
- **Flutter** - Cross-platform mobile app

## Project Structure

```
├── backend/
│   ├── pom.xml
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/globalpay/
│   │   │   │   ├── GlobalPayApplication.java
│   │   │   │   ├── config/
│   │   │   │   ├── api/controller/
│   │   │   │   ├── service/
│   │   │   │   ├── repository/
│   │   │   │   ├── entity/
│   │   │   │   ├── event/
│   │   │   │   ├── kafka/
│   │   │   │   ├── batch/
│   │   │   │   ├── security/
│   │   │   │   ├── exception/
│   │   │   │   ├── util/
│   │   │   │   └── client/
│   │   │   └── resources/
│   │   └── test/
│   └── docker/
├── frontend/
│   ├── package.json
│   ├── tsconfig.json
│   ├── src/
│   └── tailwind.config.js
├── mobile/
│   ├── lib/
│   └── pubspec.yaml
├── docker-compose.yml
├── kubernetes/
├── monitoring/
└── docs/
```

## Installation & Setup

### Prerequisites
- Java 21
- Docker & Docker Compose
- Node.js 18+
- Flutter SDK
- PostgreSQL 14+
- Redis 7+
- Apache Kafka 3+

### Backend Setup

```bash
cd backend
mvn clean package -DskipTests
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

### Frontend Setup

```bash
cd frontend
npm install
npm start
```

### Docker Compose (Full Stack)

```bash
docker-compose up -d
```

## Contributing

See `CONTRIBUTING.md` for guidelines.

## License

MIT
