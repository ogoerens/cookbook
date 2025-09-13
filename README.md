# Bitenote Docs

## Run Application Backend

* source .env
* ./gradlew bootrun

## Build Application BE

* ./graldew clean bootJar
* Output in: build/libs/

-----

## Endpoints

### Register

curl -v -X POST http://pi:8080/api/register/user \
-H "Content-Type: application/json" \
-d '{"email":"test@example.com", "password":"supersecure123"}'

### LOGIN

curl -v -X POST http://localhost:8080/api/login/user \
-H "Content-Type: application/json" \
-d '{"email":"test@example.com", "password":"supersecure123"}'

### GET ALL BOOKMARKS

curl -v -X GET http://localhost:8080/api/bookmarks \
-H "Authorization: Bearer
eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI1NDYyNjllYS0yODFlLTQ4MGUtODY1Mi1kODFhMzFmM2JjODEiLCJpYXQiOjE3NDc1ODEwNDIsImV4cCI6MTc0NzY2NzQ0Mn0.jdJEyFsp_AM90PfKqzmdUPKPnpjcwHotJnng1T90z2zd356ya5OPZx4HKq1SjLKbQsedTZ5l2zGOU3soOSlKqQ"

### Get BOOKMARK BY TAG

curl -v -X GET "http://localhost:8080/api/bookmarks/filter?tags=dinner" \
-H "Authorization: Bearer
eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI1NDYyNjllYS0yODFlLTQ4MGUtODY1Mi1kODFhMzFmM2JjODEiLCJpYXQiOjE3NDMyODE5NjksImV4cCI6MTc0MzM2ODM2OX0.d2bfVh8wdAycsEPLVnPOpCt7EAWQZc2OzMIy7E2vlxHzGi3DtyB08ATasQyl4PT6u6s7aeY9ekKIOooMwF6euA"

### ADD BOOKMARK

curl -v -X POST http://localhost:8080/api/bookmarks \
-H "Authorization: Bearer
eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI1NDYyNjllYS0yODFlLTQ4MGUtODY1Mi1kODFhMzFmM2JjODEiLCJpYXQiOjE3NDMyODE5NjksImV4cCI6MTc0MzM2ODM2OX0.d2bfVh8wdAycsEPLVnPOpCt7EAWQZc2OzMIy7E2vlxHzGi3DtyB08ATasQyl4PT6u6s7aeY9ekKIOooMwF6euA" \
-H "Content-Type: application/json" \
-d '{"name": "Simple Pasta", "referenceType": "URL", "url": "https://example.com/pasta", "picture": null, "
tags": ["easy", "dinner"]}'

