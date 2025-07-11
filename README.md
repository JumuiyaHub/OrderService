# Order Service

A Spring Boot microservice that handles comprehensive order processing with inventory validation, event-driven notifications, and resilient communication patterns. Built with modern microservices architecture principles and production-ready features.

## ✨ Features

- **Order Management**: Complete order lifecycle from placement to confirmation
- **Inventory Validation**: Real-time inventory availability checking
- **Event-Driven Architecture**: Kafka integration for order event notifications
- **Resilience Patterns**: Circuit breaker, retry, and fallback mechanisms using Resilience4j
- **API Documentation**: Interactive OpenAPI/Swagger documentation
- **Database Persistence**: MySQL with Flyway migrations
- **Containerization**: Docker and Docker Compose support
- **Testing**: Comprehensive integration tests with Testcontainers and WireMock
- **Monitoring**: Actuator health checks and metrics

## 🛠 Tech Stack

- **Java 21**
- **Spring Boot 3.3.1**
- **Spring Cloud 2023.0.1**
- **MySQL 8.3**
- **Apache Kafka**
- **Resilience4j Circuit Breaker**
- **OpenAPI 3.0 / Swagger (SpringDoc 2.3.0)**
- **Flyway Database Migration**
- **Docker & Docker Compose**
- **Maven**
- **Testcontainers 1.19.3**
- **WireMock 3.3.1**
- **Lombok 1.18.30**
- **REST Assured**

## 📋 Project Structure

```plaintext
order-service/
├── docker/
│   └── mysql/
│       ├── init/                       # MySQL initialization scripts
│       └── init.sql                    # Database setup
├── docker-compose.yml                  # Infrastructure services
├── src/
│   ├── main/
│   │   ├── java/com/kiru/microservice/order/
│   │   │   ├── client/
│   │   │   │   └── InventoryClient.java        # External service client
│   │   │   ├── config/
│   │   │   │   ├── OpenAPIConfig.java          # API documentation config
│   │   │   │   └── RestClientConfig.java       # HTTP client configuration
│   │   │   ├── controller/
│   │   │   │   └── OrderController.java        # REST API endpoints
│   │   │   ├── dto/
│   │   │   │   └── OrderRequest.java           # Data transfer objects
│   │   │   ├── event/
│   │   │   │   └── OrderPlacedEvent.java       # Kafka event models
│   │   │   ├── model/
│   │   │   │   └── Order.java                  # Domain entities
│   │   │   ├── repository/
│   │   │   │   └── OrderRepository.java        # Data access layer
│   │   │   ├── service/
│   │   │   │   └── OrderService.java           # Business logic
│   │   │   └── OrderServiceApplication.java    # Main application
│   │   └── resources/
│   │       ├── db/migration/
│   │       │   ├── V1__init.sql                # Initial schema
│   │       │   └── V2__table_name.sql          # Schema updates
│   │       └── application.properties          # Application config
│   └── test/
│       ├── java/com/kiru/microservice/order/
│       │   ├── stubs/
│       │   │   └── InventoryClientStub.java    # Test stubs
│       │   ├── OrderServiceApplicationTests.java
│       │   ├── TestcontainersConfiguration.java
│       │   └── TestOrderServiceApplication.java
│       └── resources/
│           └── application.properties          # Test configuration
├── pom.xml                             # Maven dependencies
└── README.md
```

## 🚀 Getting Started

### Prerequisites

- **Java 21** or higher
- **Docker** and **Docker Compose**
- **Maven 3.8+**
- **Available Ports**: 8082, 3307, 9092, 2181, 8085, 8086

### Quick Start

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd order-service
   ```

2. **Start infrastructure services:**
   ```bash
   docker-compose up -d
   ```

3. **Verify services are running:**
   ```bash
   docker-compose ps
   ```

4. **Run the application:**
   ```bash
   ./mvnw spring-boot:run
   ```

5. **Access the application:**
    - Order Service: `http://localhost:8082`
    - Swagger UI: `http://localhost:8082/swagger-ui.html`
    - Kafka UI: `http://localhost:8086`

## 🗄️ Infrastructure Services

### Database Configuration
- **MySQL Server**: localhost:3307
- **Database**: order_service
- **Username**: root
- **Password**: mysql

### Kafka Configuration
- **Kafka Broker**: localhost:9092
- **Zookeeper**: localhost:2181
- **Schema Registry**: localhost:8085
- **Kafka UI**: localhost:8086

### Service Dependencies
- **Inventory Service**: http://localhost:8083 (external dependency)

## 📚 API Documentation

### Access Points
- **Swagger UI**: `http://localhost:8082/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8082/api-docs`
- **OpenAPI YAML**: `http://localhost:8082/api-docs.yaml`

### Key Endpoints
- `POST /api/orders` - Place a new order
- `GET /api/orders/{id}` - Get order details
- `GET /api/orders` - List all orders
- `GET /actuator/health` - Health check

### Sample Request
```json
{
  "customerId": "12345",
  "items": [
    {
      "productId": "SKU001",
      "quantity": 2,
      "price": 29.99
    }
  ]
}
```

## 🔄 Order Processing Flow

1. **Order Validation**: Validates incoming order request
2. **Inventory Check**: Calls inventory service to verify availability
3. **Order Persistence**: Saves order to MySQL database
4. **Event Publishing**: Publishes OrderPlacedEvent to Kafka
5. **Response**: Returns order confirmation to client

## 🛡️ Resilience Patterns

### Circuit Breaker
- **Component**: Inventory service calls
- **Failure Threshold**: 5 failures in 10 calls
- **Recovery Time**: 30 seconds
- **Fallback**: Returns cached inventory or default response

### Retry Mechanism
- **Max Attempts**: 3
- **Backoff Strategy**: Exponential (2s, 4s, 8s)
- **Retry Conditions**: Connection timeouts, 5xx errors

### Configuration Example
```properties
# Circuit Breaker Configuration
resilience4j.circuitbreaker.instances.inventory-service.failure-rate-threshold=50
resilience4j.circuitbreaker.instances.inventory-service.wait-duration-in-open-state=30s
resilience4j.circuitbreaker.instances.inventory-service.sliding-window-size=10

# Retry Configuration
resilience4j.retry.instances.inventory-service.max-attempts=3
resilience4j.retry.instances.inventory-service.wait-duration=2s
```

### Maven Project Information
```xml
<groupId>com.kiru.microservice</groupId>
<artifactId>order-service</artifactId>
<version>0.0.1-SNAPSHOT</version>
<name>order-service</name>
<description>Order Service</description>
```

## 🔧 Configuration

### Build Configuration
The project uses Maven with specific compiler and annotation processing configuration:
```xml
<properties>
  <java.version>21</java.version>
  <spring-cloud.version>2023.0.1</spring-cloud.version>
  <testcontainers.version>1.19.3</testcontainers.version>
  <lombok.version>1.18.30</lombok.version>
</properties>
```

### Application Properties
```properties
# Server Configuration
server.port=8082

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3307/order_service
spring.datasource.username=root
spring.datasource.password=mysql
spring.jpa.hibernate.ddl-auto=validate

# Kafka Configuration
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer

# External Services
inventory.service.url=http://localhost:8083

# Flyway Migration
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true

# OpenAPI Documentation
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

## 🐳 Docker Operations

### Infrastructure Management
```bash
# Start all infrastructure services
docker-compose up -d

# Stop all services
docker-compose down

# View service logs
docker-compose logs -f kafka
docker-compose logs -f mysql

# Restart specific service
docker-compose restart kafka

# Clean up volumes (caution: deletes data)
docker-compose down -v
```

### Service Status Check
```bash
# Check running containers
docker-compose ps

# Monitor resource usage
docker-compose stats

# Access container shell
docker-compose exec mysql bash
```

## 🧪 Testing

### Running Tests
```bash
# Run all tests
./mvnw test

# Run with coverage
./mvnw test jacoco:report

# Run integration tests only
./mvnw test -Dtest=*IntegrationTest

# Run specific test class
./mvnw test -Dtest=OrderServiceApplicationTests
```

### Test Categories
- **Unit Tests**: Service and controller layer testing
- **Integration Tests**: End-to-end testing with Testcontainers (MySQL, Kafka)
- **Contract Tests**: API contract validation with Spring Cloud Contract
- **Stub Tests**: External service mocking with WireMock Standalone 3.3.1
- **API Tests**: REST API testing with REST Assured

### Test Configuration
```properties
# Test Database (Testcontainers MySQL)
spring.datasource.url=jdbc:tc:mysql:8.3:///order_service_test
spring.test.database.replace=none

# Test Kafka (Testcontainers Kafka)
spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}

# WireMock Configuration
inventory.service.url=http://localhost:${wiremock.server.port}

# REST Assured Configuration
rest-assured.port=8082
rest-assured.base-path=/api
```

### Maven Dependencies
The project uses comprehensive testing dependencies:
- **Spring Boot Test Starter**: Core testing framework
- **Testcontainers**: MySQL and Kafka containers for integration tests
- **WireMock Standalone**: External service mocking
- **REST Assured**: API testing framework
- **Spring Cloud Contract**: Contract testing support

## 📊 Monitoring and Observability

### Health Checks
- **Application Health**: `GET /actuator/health`
- **Circuit Breaker Status**: `GET /actuator/health/circuitbreakers`
- **Database Health**: `GET /actuator/health/db`
- **Kafka Health**: `GET /actuator/health/kafka`

### Metrics
- **JVM Metrics**: `GET /actuator/metrics`
- **Custom Metrics**: Order processing rates, inventory call success rates
- **Circuit Breaker Metrics**: State changes, failure rates

### Logging
```properties
# Logging Configuration
logging.level.com.kiru.microservice.order=DEBUG
logging.level.org.springframework.kafka=INFO
logging.level.org.springframework.web=DEBUG

# Logback Pattern
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %logger{36} - %msg%n
```

## 🔐 Security Considerations

### Development Environment
- Default credentials for local development
- No authentication required for API endpoints
- Open access to actuator endpoints

### Production Recommendations
- Implement OAuth2/JWT authentication
- Secure actuator endpoints
- Use encrypted database connections
- Implement rate limiting
- Set up proper CORS configuration

## 🚀 Deployment

### Environment Variables
```bash
# Database Configuration
SPRING_DATASOURCE_URL=jdbc:mysql://production-db:3306/order_service
SPRING_DATASOURCE_USERNAME=${DB_USERNAME}
SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD}

# Kafka Configuration
SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka-cluster:9092

# External Services
INVENTORY_SERVICE_URL=http://inventory-service:8083

# Application Configuration
SERVER_PORT=8082
SPRING_PROFILES_ACTIVE=production
```

### Production Checklist
- [ ] Configure external database
- [ ] Set up Kafka cluster
- [ ] Configure monitoring and alerting
- [ ] Set up centralized logging
- [ ] Implement security measures
- [ ] Configure backup strategies
- [ ] Set up CI/CD pipeline
- [ ] Performance testing
- [ ] Disaster recovery plan

## 🔄 Database Migrations

### Flyway Commands
```bash
# Check migration status
./mvnw flyway:info

# Run migrations
./mvnw flyway:migrate

# Validate migrations
./mvnw flyway:validate

# Clean database (development only)
./mvnw flyway:clean
```

### Migration Best Practices
- Always backup before migrations
- Test migrations in staging environment
- Use reversible migrations when possible
- Keep migrations small and focused

## 🎯 Performance Tuning

### Database Optimization
```properties
# Connection Pool Configuration
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000

# JPA Optimization
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true
```

### Kafka Optimization
```properties
# Producer Configuration
spring.kafka.producer.batch-size=16384
spring.kafka.producer.linger-ms=5
spring.kafka.producer.compression-type=snappy

# Consumer Configuration
spring.kafka.consumer.fetch-min-size=1024
spring.kafka.consumer.fetch-max-wait=500ms
```

## 🤝 Contributing

### Development Workflow
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Write tests for new functionality
4. Implement the feature
5. Run tests and ensure they pass
6. Update documentation
7. Commit changes (`git commit -m 'Add amazing feature'`)
8. Push to branch (`git push origin feature/amazing-feature`)
9. Create a Pull Request

### Code Standards
- Follow Java coding conventions
- Maintain test coverage above 80%
- Use meaningful variable and method names
- Add comprehensive JavaDoc comments
- Follow REST API design principles

## 📝 License

This project is licensed under the Apache License 2.0 - see the LICENSE file for details.

## 📞 Support

For support and questions:
- Create an issue in the repository
- Contact the development team(ikruruto@gmail.com)
- Check the project wiki
- Review the troubleshooting guide