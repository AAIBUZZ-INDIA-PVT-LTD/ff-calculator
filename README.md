# SeaRoute Wrapper

Spring Boot REST wrapper around [Eurostat SeaRoute](https://github.com/eurostat/searoute) for sea distance calculation.

## Requirements
- Java 17+
- Maven 3.6+
- SeaRoute v3.5 installed to local Maven repo

## Setup

### 1. Install SeaRoute to local Maven
```bash
cd C:\searoute-searoute-3.5\searoute-searoute-3.5
mvn install -DskipTests
```

### 2. Build
```bash
cd searoute-wrapper
mvn clean package -DskipTests
```

### 3. Run
```bash
java -jar target/searoute-wrapper-1.0.0.jar --server.port=8090
```

## API

```
GET /searoute?olon={lon1}&olat={lat1}&dlon={lon2}&dlat={lat2}
```

Returns GeoJSON Feature with `properties.length` in km.

## Integration

Add to `ff-api/.env`:
```
SEAROUTE_URL=http://localhost:8090
```
