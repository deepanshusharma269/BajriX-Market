# BajriX Marketplace

A small demo marketplace for construction and home‑building products.

## Project structure
```
BajriX/
├─ backend/          (Spring Boot REST API)
│   ├─ src/main/java/com/bajrix/...   (entities, repositories, services, controllers)
│   ├─ src/main/resources/            (application.properties, Flyway migrations)
│   └─ pom.xml
├─ frontend/         (React + Vite UI)
│   ├─ src/          (components, pages, main.jsx)
│   ├─ public/       (index.html)
│   └─ package.json
├─ README.md
└─ AI_USAGE.md
```

## How to run
### Prerequisites
* **Windows** with Java 17 and Maven installed.
* **PostgreSQL** running locally (default DB URL `jdbc:postgresql://localhost:5432/bajrix`).
  * Create the database: `CREATE DATABASE bajrix;`
  * Use username/password `postgres/postgres` (adjust `application.properties` if needed).
* **Node.js** (v18+) and npm.

### Backend
```bash
cd BajriX/backend
mvn spring-boot:run
```
The API will start on `http://localhost:8080`. Flyway will automatically create the schema and insert sample data.

### Frontend
```bash
cd ../frontend
npm install
npm run dev
```
The UI is available at `http://localhost:5173`.

### Mock authentication
The UI includes a *Login as Seller (ID 1)* button. When clicked it adds the HTTP header
```
X-Mock-Seller-Id: 1
```
which the backend reads via `MockSellerFilter` and makes the seller ID available as a request attribute. All seller‑side endpoints use this attribute, ensuring a seller cannot modify another seller’s data.

## Architectural decisions
| Layer | Decision |
|-------|----------|
| **Domain model** | Separate `Product` (catalog) from `SellerListing` (seller‑specific offer) with a unique constraint to prevent duplicate listings for the same seller‑product pair. |
| **Seller status** | Only listings of sellers whose `status = APPROVED` are returned by the public product‑detail endpoint. Other statuses are still visible to the seller via `/api/seller/**` for management. |
| **Search** | `ProductRepository.findByNameContainingIgnoreCase` with Spring Data `Pageable`. This scales to large data sets because pagination is performed at the DB level. |
| **Concurrency** | Optimistic locking via a `@Version` column on `seller_listings`. Conflicting updates result in a `409 Conflict` (handled globally). |
| **Mock auth** | Simple request‑header filter – no real security framework required, but the code path is ready for real authentication (e.g., JWT). |
| **Validation** | Bean Validation annotations on DTOs (`@NotNull`, `@Min`). Service layer validates business rules (price > 0, stock ≥ 0, etc.) and throws `IllegalArgumentException` which is translated to `400 Bad Request`. |
| **Error handling** | `@ControllerAdvice` (`GlobalExceptionHandler`) returns a uniform JSON error shape (`{code, message}`). |
| **Testing** | Example unit test for `SellerListingService#createListing` and integration test for the product search endpoint are included under `src/test/java`. |
| **Frontend** | React 18 + Vite + Chakra UI. The buyer UI shows a searchable product list and a product‑detail page with a table of active seller offers. The seller UI (mock login) lets a seller view, create, edit, and deactivate their listings. |
| **CORS** | Spring `WebConfig` enables CORS for `http://localhost:5173`. |

## Sample data
The migration `V2__sample_data.sql` creates:
* 3 sellers (`APPROVED`, `PENDING`, `APPROVED`).
* 3 products (cement, bricks, steel rod).
* Multiple listings for the same cement product with different prices and stocks.

## Future improvements
* Replace the mock filter with JWT / OAuth2 security.
* Add full‑text search (e.g., PostgreSQL `tsvector`) for faster product lookup at massive scale.
* Implement proper pagination and sorting on the seller dashboard.
* Add caching (Redis) for product‑detail queries.
* Write more comprehensive integration tests and contract tests for the API.
* Deploy with Docker Compose, include health checks, and enable TLS.

---
*Happy coding!*
