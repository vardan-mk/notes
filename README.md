# notes
The task for assessment.

## initial setup for project at local environment
1. In notes/docker/postgres/ directory please find and edit database.env file for postgres and set your username and password.
2. The same values set in application.yml, application-dev.yml file, any other case this values will be read from env if exist.
3. Run docker-compose file in notes/docker-compose.yml to run containers of postgres DB, build notesApi docker image and
    ETL Batch processing for local.
NOTE: it will automatically create DB notesapp.
NOTE: for table creation and populating users tables with few users used flyway with corresponding sql script.

## Notes API endpoints

API can be accessed by this url either locally or in AWS FARGATE: 

    local -  http://localhost:8080/docs/swagger-ui
    AWS ECS Fargate - http://3.125.48.207:8080/docs/swagger-ui

commands of curl to check endpoints.

    1. for login via jwt, will retrieve the jwt token in case of correct credentials.
        curl --location --request POST 'localhost:8080/login' \
        --header 'Content-Type: application/json' \
        --data-raw '{"username": "vardanmk@gmail.com", "password":"12345678"}'
            
    2. for retrieving all notes of current logged in user.
        curl --location --request GET 'http://localhost:8080/api/v1/notes' \
            --header 'Authorization: Bearer <put the token retrieved from login here>'
            
     3. for retrieving one not with corresponding id for current user.
        curl --location --request GET 'http://localhost:8080/api/v1/notes/3' \
            --header 'Authorization: Bearer <put the token retrieved from login here>'
     
     4. for adding new note for current user.
     curl --location --request POST 'http://localhost:8080/api/v1/addnote' \
     --header 'Authorization: Bearer <put the token retrieved from login here>' \
     --header 'Content-Type: application/json' \
     --data-raw '{
        "title":"new note for test",
        "note":"sdfkjsdhjfsdhf sdfjhksdkjfhsdkjf hsdfsdf"}'
     
     5. for updating the note of current user with provided note id. Note: if one of fields missing (title or note), then 
        it will keeped from existing data
        curl --location --request PUT 'http://localhost:8080/api/v1/notes/update/3' \
            --header 'Authorization: Bearer <put the token retrieved from login here>' \
            --header 'Content-Type: application/json' \
            --data-raw '{
            "title": "changed title",
            "note": "asdasdasdasasd poxac" }'
     
     6. for deleting the note of current user with provided note id.
            curl --location --request DELETE 'http://localhost:8080/api/v1/notes/delete/4' \
            --header 'Authorization: Bearer <put the token retrieved from login here>'
         
## ETL process endpoints
commands of curl to check endpoint and run batch process to export data from database to .json and .csv files.
In project directory it will create etlResult folder if not exist
and after batch processing write output files to it.
    
    1. curl --location --request GET 'localhost:8085/batch/load'
    
    In project directory it will create etlResult folder if not exist
    and after batch processing write output files to it. 

ETL Batch job results now are writing to AWS S3 bucket: notes-etl-result

### Fake Data Generator
created fake data generator which creates about 15 user and store them to DB
then create about 1500 notes, with corresponding requirements and assign them
randomly to existing users and store to DB.

    1. curl --location --request GET 'localhost:8085/batch/generate'