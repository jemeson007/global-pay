# Architecture

## System Design

### High-Level Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        Frontend Layer                        │
│  (React Web App, Flutter Mobile App, Admin Dashboard)        │
└────────────────────────────┬────────────────────────────────┘
                             │ HTTPS
┌────────────────────────────▼────────────────────────────────┐
│                     API Gateway / NGINX                      │
│                  (Load Balancing, Rate Limiting)             │
└────────────────────────────┬────────────────────────────────┘
                             │
┌────────────────────────────▼────────────────────────────────┐
│                   Spring Boot Backend                        │
│ ┌─────────────────────────────────────────────────────────┐ │
│ │  Controllers (REST API)                                 │ │
│ │  ├─ Auth Controller                                     │ │
│ │  ├─ Transaction Controller                              │ │
│ │  ├─ User Controller                                     │ │
│ │  ├─ Payment Method Controller                           │ │
│ │  └─ Currency Controller                                 │ │
│ └─────────────────────────────────────────────────────────┘ │
│ ┌─────────────────────────────────────────────────────────┐ │
│ │  Service Layer (Business Logic)                         │ │
│ │  ├─ UserService                                         │ │
│ │  ├─ TransactionService                                  │ │
│ │  ├─ CurrencyService                                     │ │
│ │  ├─ PaymentMethodService                                │ │
│ │  └─ KycService                                          │ │
│ └─────────────────────────────────────────────────────────┘ │
│ ┌─────────────────────────────────────────────────────────┐ │
│ │  Data Access Layer (Repositories)                       │ │
│ │  ├─ UserRepository                                      │ │
│ │  ├─ TransactionRepository                               │ │
│ │  ├─ PaymentMethodRepository                             │ │
│ │  └─ ExchangeRateRepository                              │ │
│ └─────────────────────────────────────────────────────────┘ │
└────────────────────────────┬────────────────────────────────┘
         ┌──────────────────┼──────────────────┐
         │                  │                  │
    ┌────▼────┐        ┌───▼───┐        ┌────▼────┐
    │PostgreSQL│        │ Redis │        │ Kafka   │
    │Database  │        │ Cache │        │ Broker  │
    └──────────┘        └───────┘        └────┬────┘
                                               │
         ┌─────────────────────────────────────┼────────────────────────────┐
         │                                     │                            │
    ┌────▼────────────────┐         ┌─────────▼──────────┐       ┌────────▼────────┐
    │  Event Consumers    │         │  Batch Jobs        │       │  Notifications  │
    │  ├─ Notifications   │         │  ├─ Rate Updates   │       │  ├─ Email       │
    │  ├─ Analytics       │         │  ├─ Settlement     │       │  ├─ SMS         │
    │  └─ Settlements     │         │  └─ Reconciliation │       │  └─ Push        │
    └────────────────────┘         └────────────────────┘       └─────────────────┘
```

### Component Responsibilities

#### Frontend
- **React Web App**: Desktop and tablet UI
- **Flutter Mobile**: iOS and Android apps
- **Admin Dashboard**: Internal operations dashboard

#### Backend
- **REST API**: RESTful endpoints for all operations
- **Authentication**: JWT-based with refresh tokens
- **Authorization**: Role-based access control
- **Business Logic**: Transaction processing, KYC verification, currency conversion
- **Event Processing**: Kafka-based async processing

#### Data Layer
- **PostgreSQL**: Relational data storage
- **Redis**: Caching and session management
- **Elasticsearch**: Log aggregation and search

#### Message Queue
- **Kafka**: Event streaming and async processing
- **Topics**: transaction-created, transaction-completed, user-notification, exchange-rate-update

### Data Flow

1. **User Registration**
   - Frontend sends registration request
   - Backend validates and creates user
   - JWT token generated and returned
   - User added to Kafka queue for welcome email

2. **Transaction Creation**
   - Frontend sends transaction request
   - Backend validates sender, recipient, and payment method
   - Calculates fees and exchange rates
   - Transaction created in PENDING state
   - Event published to Kafka
   - Event consumer processes and sends notifications

3. **Exchange Rate Updates**
   - Batch job fetches latest rates from external API
   - Rates stored in cache (Redis) and database
   - Rates streamed via Kafka to update frontend

### Security

- **HTTPS/TLS**: All communications encrypted
- **JWT Authentication**: Stateless authentication
- **SQL Injection Prevention**: Parameterized queries
- **Data Encryption**: Sensitive fields encrypted at rest
- **Rate Limiting**: 1000 requests/minute per user
- **CORS**: Configured for allowed origins

### Scalability

- **Horizontal Scaling**: Stateless backend services
- **Load Balancing**: NGINX load balancer
- **Database Optimization**: Indexes, partitioning
- **Caching**: Redis for frequently accessed data
- **Async Processing**: Kafka for long-running tasks
- **Auto-scaling**: Kubernetes HPA based on CPU/memory

### Monitoring & Observability

- **Prometheus**: Metrics collection
- **Grafana**: Metrics visualization
- **ELK Stack**: Centralized logging
- **Health Checks**: /actuator/health endpoint
- **Tracing**: Distributed tracing (future enhancement)
