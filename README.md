This project is a simple weather data gatherer. It utilizes
the public endpoint `api.weather.gov/../forecast` to retrieve
the weather data for the current day using Kotlin Spring
Reactive techniques.

MVP of this product only includes weather data for 1 location
and will only retrieve the high temp for the current day along
with a short forecast description.

### Local Project Setup
To run locally you will need a cassandra DB running in a docker
container.

To quickly perform this setup, run the following: 

```
docker run -p 9042:9042 --rm --name cassandra -d cassandra:4.0.7
docker exec -it cassandra bash -c "cqlsh -u cassandra -p cassandra"
CREATE KEYSPACE weather_app WITH replication = {'class' : 'SimpleStrategy', 'replication_factor' : 1};
EXIT;
```

You can now build and run the gradle project with your
preferred method.

The endpoint exposed on port 8080, GET /weather. <br>
This project currently has a primitive oauth2-client in which
a basic auth is required to access the endpoints.
``` 
username = user
password = <look for the log message "Using generated security password:" on startup>
```



