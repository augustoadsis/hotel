# Last Hotel API

### Considerations
In development of this challenge, I have used (Spring WebFLux) on service layer at list of available dates, cause it is non-blocking. 
As JPA Data with relational databases is a standard, I've decided to not use reactive mysql database, since R2DBC connector does not have all ORM functions. 
Since we have a lot of user for just one room, we don't have to check the database everytime to check availabilities, just when it is modified (save/update/delete), then we can caching this searchs on redis for future queries using date parameter as key.
The rules of "the stay can’t be longer than 3 days and can’t be reserved more than 30 days in advance." are validated by respective annotations @DateRangeValidation and @DateValidation.
I assumed as a premise that this application going to be deployed on a K8s cluster to help to achieve these 99.99 to 100% => no downtime.


### Reference Documentation
For further reference, please consider the following sections:

* [Postman Collection](./hotel.postman_collection.json)
* [Swagger UI](http://localhost:8080/hotel/api/swagger-ui/)

### Guides
The following guides illustrate how to use/run project:

* run docker-compose to start redis and mysql constainers
```
docker-compose up -d
```
* If you want to manipulate initial data, you can change the file: [data.sql](./src/main/resources/data.sql)
```
src/main/resources/data.sql
```
* build and run application using a docker image
```
docker build -t hotel .
docker run -d --name hotel -e TZ='America/Sao_Paulo' hotel
```
* if you don't wanna use docker, you can also run using maven and jdk18
```
mvn clean install
java -Djava.security.egd=file:/dev/./urandom -jar target/booking-0.0.1-SNAPSHOT.jar
```