Run Application

* source .env
* ./gradlew bootrun

Access DB

* sudo -i -u postgres
* psql
* psql -U your_username -d your_database
* psql -h localhost -p 5432 -U your_username -d your_database

Endpoints

* Register User
    * /api/register/user
    * 