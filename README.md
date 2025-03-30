Run Application

* source .env
* ./gradlew bootrun

Access DB

* sudo -i -u postgres
* psql
* psql -U your_username -d your_database
* psql -h localhost -p 5432 -U your_username -d your_database

Endpoints

LOGIN
curl -v -X POST http://localhost:8080/api/login/user \
-H "Content-Type: application/json" \
-d '{"email":"test@example.com", "password":"supersecure123"}'

GET ALL BOOKMARKS
curl -v -X GET http://localhost:8080/api/bookmarks \
-H "Authorization: Bearer
eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI1NDYyNjllYS0yODFlLTQ4MGUtODY1Mi1kODFhMzFmM2JjODEiLCJpYXQiOjE3NDMyODE5NjksImV4cCI6MTc0MzM2ODM2OX0.d2bfVh8wdAycsEPLVnPOpCt7EAWQZc2OzMIy7E2vlxHzGi3DtyB08ATasQyl4PT6u6s7aeY9ekKIOooMwF6euA"

Get BOOKMARK BY TAG

curl -v -X GET "http://localhost:8080/api/bookmarks/filter?tags=dinner" \
-H "Authorization: Bearer
eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI1NDYyNjllYS0yODFlLTQ4MGUtODY1Mi1kODFhMzFmM2JjODEiLCJpYXQiOjE3NDMyODE5NjksImV4cCI6MTc0MzM2ODM2OX0.d2bfVh8wdAycsEPLVnPOpCt7EAWQZc2OzMIy7E2vlxHzGi3DtyB08ATasQyl4PT6u6s7aeY9ekKIOooMwF6euA"

ADD BOOKMARK
curl -v -X POST http://localhost:8080/api/bookmarks \
-H "Authorization: Bearer
eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI1NDYyNjllYS0yODFlLTQ4MGUtODY1Mi1kODFhMzFmM2JjODEiLCJpYXQiOjE3NDMyODE5NjksImV4cCI6MTc0MzM2ODM2OX0.d2bfVh8wdAycsEPLVnPOpCt7EAWQZc2OzMIy7E2vlxHzGi3DtyB08ATasQyl4PT6u6s7aeY9ekKIOooMwF6euA" \
-H "Content-Type: application/json" \
-d '{"name": "Simple Pasta", "referenceType": "URL", "url": "https://example.com/pasta", "picture": null, "
tags": ["easy", "dinner"]}'
