# Bitenote Docs

## Run Application Backend

* source .env
* ./gradlew bootrun

## Build Application BE

* source .env
* ./graldew clean bootJar
* Output in: build/libs/

-----

## Endpoints

### Register

curl -v -X POST https://www.bitenote.com/api/register/user \
-H "Content-Type: application/json" \
-d '{"email":"test2@example.com", "password":"supersecure123"}'

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

### ADD NOTE

curl -v -X PUT http://localhost:8080/api/note/d8c8f833-466f-42a1-b64a-f0790d4a7217 \
-H "Authorization: Bearer \
eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI1NjllYmM0MS1kMmRhLTRmNzEtOTBmYy1kMWU2MTJjMmJlZDgiLCJpYXQiOjE3NjAyNTY3NzMsImV4cCI6MTc2MDM0MzE3M30.B8AaeOOTgge5Y7OpN__ArSojZrVyAfRhT7EHThvWMFcwG6akoxsfWDnnj_wzMhrUW2NkO4XII7IYdCfQpGholA" \
-H "Content-Type:  text/plain " \
-d 'Easy peasy, perfect for quick dinner'

