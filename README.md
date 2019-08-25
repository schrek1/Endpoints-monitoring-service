# Endpoints-monitoring-service
Endpoints monitoring service


For run: 

- set mysql connection at the application.properties

-  run as usual spring web app 

- open swagger ui on: http://localhost:8080/swagger-ui.html

- *RMR* user endpoints are not secrued, but monitored endpoints so
- You must save hash-token which returned after create user, or read at the databse table user in the field token
- Use this raw token in top part of swagger-ui on the right is button authorize with lock logo



