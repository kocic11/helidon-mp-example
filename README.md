Build and run docker image:

docker build -t user-mp target

docker run --rm -p 8080:8080 --name user user-mp:latest

Test

Create user:

curl -X PUT -H "Content-Type: application/json" -d '{"name":"Abc", "age":21}' localhost:8080/user/add

Get user by name:

curl -X GET -H "Content-Type: application/json"  localhost:8080/user/get/{name} && echo

Get user by Id:

curl -X GET -H "Content-Type: application/json"  localhost:8080/user/id/{id} && echo

Get all users:

curl -X GET -H "Content-Type: application/json"  localhost:8080/user/getall

Delete all users:

curl -X DELETE -H "Content-Type: application/json"  localhost:8080/user/deleteall
