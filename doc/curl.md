# ADMIN PROFILE

## Get list of Users

### Request

`GET /rest/admin/users`

    curl -i "http://localhost:8080/topjava/rest/admin/users"

### Response

    HTTP/1.1 200
    Content-Type: application/json;charset=UTF-8
    Transfer-Encoding: chunked
    Date: Sat, 23 Nov 2019 09:58:25 GMT
    
    [{"id":100001,"name":"Admin","email":"admin@gmail.com","password":"admin","enabled":true,"registered":"2019-11-23T07:37:21.226+0000","roles":["ROLE_USER","ROLE_ADMIN"],"caloriesPerDay":2000,"meals":null},{"id":100000,"name":"User","email":"user@yandex.ru","password":"password","enabled":true,"registered":"2019-11-23T07:37:21.226+0000","roles":["ROLE_USER"],"caloriesPerDay":2000,"meals":null}]

## Create a new User

### Request

`POST /rest/admin/users`

    curl -i -H "Content-Type: application/json;" -d "{\"name\":\"New user\",\"email\":\"newUser@gmail.com\",\"password\":\"12345678\",\"roles\":[\"ROLE_USER\"]}" "http://localhost:8080/topjava/rest/admin/users"

### Response

    HTTP/1.1 201
    Location: http://localhost:8080/topjava/rest/admin/users/100012
    Content-Type: application/json;charset=UTF-8
    Transfer-Encoding: chunked
    Date: Sat, 23 Nov 2019 10:00:42 GMT

    {"id":100012,"name":"New user","email":"newUser@gmail.com","password":"12345678","enabled":true,"registered":"2019-11-23T10:00:42.540+0000","roles":["ROLE_USER"],"caloriesPerDay":2000}

## Get a specific User

### Request

`GET /rest/admin/users/:id`

    curl -i "http://localhost:8080/topjava/rest/admin/users/100000"

### Response

    HTTP/1.1 200
    Content-Type: application/json;charset=UTF-8
    Transfer-Encoding: chunked
    Date: Sat, 23 Nov 2019 10:02:02 GMT

    {"id":100000,"name":"User","email":"user@yandex.ru","password":"password","enabled":true,"registered":"2019-11-23T07:37:21.226+0000","roles":["ROLE_USER"],"caloriesPerDay":2000,"meals":null}

## Change a User

### Request

`PUT /rest/admin/users/:id`

    curl -i -X PUT -H "Content-Type: application/json;" -d "{\"name\":\"User updated\",\"email\":\"user@gmail.com\",\"password\":\"12345678\",\"roles\":[\"ROLE_USER\"]}" "http://localhost:8080/topjava/rest/admin/users/100000"

### Response

    HTTP/1.1 204
    Date: Sat, 23 Nov 2019 10:03:18 GMT

# USER PROFILE

## Get User data

### Request

`GET /rest/profile`

    curl -i "http://localhost:8080/topjava/rest/profile"

### Response

    HTTP/1.1 200
    Content-Type: application/json;charset=UTF-8
    Transfer-Encoding: chunked
    Date: Sat, 23 Nov 2019 10:05:04 GMT
    
    {"id":100000,"name":"User","email":"user@yandex.ru","password":"password","enabled":true,"registered":"2019-11-23T07:37:21.226+0000","roles":["ROLE_USER"],"caloriesPerDay":2000,"meals":null}
    
## Change User data

### Request

`PUT /rest/profile`

    curl -i -X PUT -H "Content-Type: application/json;" -d "{\"name\":\"new777\",\"email\":\"new777@gmail.com\",\"password\":\"12345678\",\"roles\":[\"ROLE_USER\"]}" "http://localhost:8080/topjava/rest/profile"

### Response

    HTTP/1.1 204
    Date: Sat, 23 Nov 2019 10:09:16 GMT
       
## Delete User profile

### Request

`DELETE /rest/profile`

    curl -i -X DELETE "http://localhost:8080/topjava/rest/profile"

### Response

    HTTP/1.1 204
    Date: Sat, 23 Nov 2019 10:10:46 GMT
    

# MEALS

## Get list of Meals

### Request

`GET /rest/meals`

    curl -i "http://localhost:8080/topjava/rest/meals"

### Response

    HTTP/1.1 200
    Content-Type: application/json;charset=UTF-8
    Transfer-Encoding: chunked
    Date: Sat, 23 Nov 2019 09:20:42 GMT

    [{"id":100008,"dateTime":"2015-05-31T20:00:00","description":"Dinner","calories":510,"excess":true},{"id":100007,"dateTime":"2015-05-31T13:00:00","description":"lunch","calories":1000,"excess":true},{"id":100006,"dateTime":"2015-05-31T10:00:00","description":"Breakfast","calories":500,"excess":true},{"id":100005,"dateTime":"2015-05-31T00:00:00","description":"Meal with limit value of date","calories":100,"excess":true},{"id":100004,"dateTime":"2015-05-30T20:00:00","description":"Dinner","calories":500,"excess":false},{"id":100003,"dateTime":"2015-05-30T13:00:00","description":"lunch","calories":1000,"excess":false},{"id":100002,"dateTime":"2015-05-30T10:00:00","description":"Breakfast","calories":500,"excess":false}]

## Get list of Meals using date filter

### Request

`GET /rest/meals/filter`

    curl -i "http://localhost:8080/topjava/rest/meals/filter?startDate=2015-05-30&endDate=2015-05-30"

### Response

    HTTP/1.1 200
    Content-Type: application/json;charset=UTF-8
    Transfer-Encoding: chunked
    Date: Sat, 23 Nov 2019 09:44:07 GMT

    [{"id":100004,"dateTime":"2015-05-30T20:00:00","description":"Dinner","calories":500,"excess":false},{"id":100003,"dateTime":"2015-05-30T13:00:00","description":"lunch","calories":1000,"excess":false},{"id":100002,"dateTime":"2015-05-30T10:00:00","description":"Breakfast","calories":500,"excess":false}]

## Get list of Meals using time filter

### Request

`GET /rest/meals/filter`

    curl -i "http://localhost:8080/topjava/rest/meals/filter?startTime=11%3A00&endTime=14%3A00"

### Response

    HTTP/1.1 200
    Content-Type: application/json;charset=UTF-8
    Transfer-Encoding: chunked
    Date: Sat, 23 Nov 2019 09:46:26 GMT

    [{"id":100007,"dateTime":"2015-05-31T13:00:00","description":"lunch","calories":1000,"excess":true},{"id":100003,"dateTime":"2015-05-30T13:00:00","description":"lunch","calories":1000,"excess":false}]

## Get list of Meals using date and time filter

### Request

`GET /rest/meals/filter`

    curl -i "http://localhost:8080/topjava/rest/meals/filter?startDate=2015-05-30&endDate=2015-05-30&startTime=11%3A00&endTime=14%3A00"

### Response

    HTTP/1.1 200
    Content-Type: application/json;charset=UTF-8
    Transfer-Encoding: chunked
    Date: Sat, 23 Nov 2019 09:48:31 GMT
    
    [{"id":100003,"dateTime":"2015-05-30T13:00:00","description":"lunch","calories":1000,"excess":false}]

## Create a new Meal

### Request

`POST /rest/meals`

    curl -i -H "Content-Type: application/json;" -d "{\"dateTime\":\"2015-06-03T19:00:00\",\"description\":\"new Meal\",\"calories\":300}"  "http://localhost:8080/topjava/rest/meals"

### Response

    HTTP/1.1 201
    Location: http://localhost:8080/topjava/rest/meals/100011
    Content-Type: application/json;charset=UTF-8
    Transfer-Encoding: chunked
    Date: Sat, 23 Nov 2019 09:23:23 GMT

    {"id":100011,"dateTime":"2015-06-03T19:00:00","description":"new Meal","calories":300,"user":null}

## Get a specific Meal

### Request

`GET /rest/meals/:id`

    curl -i "http://localhost:8080/topjava/rest/meals/100002"

### Response

    HTTP/1.1 200
    Content-Type: application/json;charset=UTF-8
    Transfer-Encoding: chunked
    Date: Sat, 23 Nov 2019 09:26:45 GMT

    {"id":100002,"dateTime":"2015-05-30T10:00:00","description":"Breakfast","calories":500,"user":null}

## Change a Meal

### Request

`PUT /rest/meals/:id`

    curl -i -X PUT -H "Content-Type: application/json;" -d "{\"dateTime\":\"2015-05-30T11:00:00\",\"description\":\"Refreshed breakfast\",\"calories\":100}" "http://localhost:8080/topjava/rest/meals/100002"

### Response

    HTTP/1.1 204
    Date: Sat, 23 Nov 2019 09:32:52 GMT
   
   
## Delete a Meal

### Request

`DELETE /rest/meals/:id`

    curl -i -X DELETE "http://localhost:8080/topjava/rest/meals/100002"

### Response

    HTTP/1.1 204
    Date: Sat, 23 Nov 2019 09:35:39 GMT
