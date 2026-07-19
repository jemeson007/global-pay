# Global Pay - API Documentation

## Overview

Global Pay is an international money transfer platform similar to Wise. This document provides comprehensive API documentation.

## Base URL

- Development: `http://localhost:8080/api`
- Production: `https://api.globalpay.com/api`

## Authentication

All protected endpoints require a Bearer token in the Authorization header:

```
Authorization: Bearer <access_token>
```

## Endpoints

### Authentication

#### Register
```
POST /auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "+1234567890",
  "password": "SecurePassword123!"
}

Response: 201 Created
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400,
  "user": {
    "id": "user-123",
    "email": "user@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "phoneNumber": "+1234567890"
  }
}
```

#### Login
```
POST /auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "SecurePassword123!"
}

Response: 200 OK
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400,
  "user": { ... }
}
```

### Users

#### Get Profile
```
GET /users/profile
Authorization: Bearer <token>

Response: 200 OK
{
  "id": "user-123",
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "+1234567890",
  "kycStatus": "VERIFIED",
  "accountBalance": 5000.00,
  "defaultCurrency": "USD",
  "emailVerified": true,
  "phoneVerified": true,
  "twoFactorEnabled": false,
  "createdAt": "2024-07-19T12:00:00Z",
  "lastLoginAt": "2024-07-19T14:30:00Z"
}
```

### Transactions

#### Create Transaction
```
POST /transactions
Authorization: Bearer <token>
Content-Type: application/json

{
  "recipientId": "recipient-123",
  "sendAmount": 1000.00,
  "sendCurrency": "USD",
  "paymentMethodId": "method-123",
  "description": "Monthly allowance"
}

Response: 201 Created
{
  "id": "txn-123",
  "transactionRef": "TXN-ABC12345",
  "senderId": "user-123",
  "recipientId": "recipient-123",
  "sendAmount": 1000.00,
  "sendCurrency": "USD",
  "receiveAmount": 920.00,
  "receiveCurrency": "EUR",
  "exchangeRate": 0.92,
  "fee": 10.00,
  "status": "PENDING",
  "type": "TRANSFER",
  "createdAt": "2024-07-19T15:00:00Z"
}
```

#### Get Transaction History
```
GET /transactions?page=0&size=20&sortBy=createdAt
Authorization: Bearer <token>

Response: 200 OK
{
  "content": [ ... ],
  "totalElements": 45,
  "totalPages": 3,
  "currentPage": 0,
  "hasNext": true
}
```

### Currencies

#### Get Exchange Rate
```
GET /currencies/rates?from=USD&to=EUR

Response: 200 OK
{
  "fromCurrency": "USD",
  "toCurrency": "EUR",
  "rate": 0.92,
  "midMarketRate": 0.92,
  "margin": 0.02,
  "ourRate": 0.9384
}
```

#### Convert Currency
```
POST /currencies/convert?from=USD&to=EUR&amount=1000

Response: 200 OK
938.40
```

#### Calculate Fee
```
GET /currencies/fee?amount=1000&currency=USD

Response: 200 OK
10.00
```

### Payment Methods

#### Add Payment Method
```
POST /payment-methods
Authorization: Bearer <token>
Content-Type: application/json

{
  "type": "BANK_ACCOUNT",
  "name": "My Bank Account",
  "isDefault": true,
  "bankName": "Chase Bank",
  "accountHolderName": "John Doe",
  "accountNumber": "1234567890",
  "routingNumber": "021000021"
}

Response: 201 Created
{ ... }
```

#### Get Payment Methods
```
GET /payment-methods
Authorization: Bearer <token>

Response: 200 OK
[ { ... }, { ... } ]
```

## Error Responses

All error responses follow this format:

```json
{
  "timestamp": "2024-07-19T15:00:00Z",
  "status": 400,
  "error": "Validation Failed",
  "message": "Input validation failed",
  "validationErrors": {
    "email": "Email should be valid",
    "password": "Password must be between 8 and 100 characters"
  },
  "path": "/api/auth/register"
}
```

## Status Codes

- `200 OK` - Successful request
- `201 Created` - Resource created successfully
- `204 No Content` - Successful request with no content
- `400 Bad Request` - Invalid input
- `401 Unauthorized` - Authentication failed
- `403 Forbidden` - Access denied
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

## Rate Limiting

API endpoints are rate-limited at 1000 requests per minute per user.

## WebHooks

Global Pay can send webhooks for important events:

- `transaction.created`
- `transaction.completed`
- `transaction.failed`
- `kyc.verified`
- `kyc.rejected`

## Support

For API support, contact: api-support@globalpay.com
