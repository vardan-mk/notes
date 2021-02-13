# notes
The task for assessment.

## initial setup for project
1. In notes/postgres/ directory please find and edit database.env file for postgres and set your username and password.
2. The same values set in application-dev.yml file, any other case this values will be read from env if exist.
3. Run docker-compose file in notes/postgres/docker-compose.yml to up postgres DB in docker.
NOTE: it will automatically create DB notesapp.

## Notes API endpoints
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
         
     