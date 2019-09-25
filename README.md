# Endpoints-monitoring-service
Endpoints monitoring service
 
Create REST API JSON Java microservice, which allows to monitor particular http/https urls.  
 
The service should allow to: 
1. Create, Edit, Delete monitored URLs and list them for particular user. (CRUD) 
2. Monitor URL(s) on background and log status codes + returned payload 
3. For each particular monitored URL to be able to list last 10  monitored results. 


For run: 

- set mysql connection at the application.properties

-  run as usual spring web app 

- open Swagger-ui on: http://localhost:8080/swagger-ui.html

- *RMR* user endpoints are not secrued, but monitored endpoints so
- You must save hash-token which returned after create user, or read at the databse table user in the field token
- Use this raw token in top part of Swagger-ui on the right is button authorize with lock logo



