#getAll
`curl -v "http://localhost:8080/topjava/rest/meals"`

#create
`curl -v "http://localhost:8080/topjava/rest/meals" -H "Content-Type: application/json;" -d "{\"dateTime\":\"2015-06-03T19:00:00\",\"description\":\"new Meal\",\"calories\":300}"`

#get
`curl -v "http://localhost:8080/topjava/rest/meals/100002"`

#update
`curl -v "http://localhost:8080/topjava/rest/meals/100002" -X PUT -d "{\"dateTime\":\"2015-05-30T11:00:00\",\"description\":\"Refreshed breakfast\",\"calories\":100}" -H "Content-Type: application/json;"`    

#delete
`curl -v "http://localhost:8080/topjava/rest/meals/100002" -X DELETE`  

#getBetween (Only Date)
`curl -v "http://localhost:8080/topjava/rest/meals/filter?startDate=2015-05-30&endDate=2015-05-30"`

#getBetween (Only Time)
`curl -v "http://localhost:8080/topjava/rest/meals/filter?startTime=11%3A00&endTime=14%3A00"`

#getBetween (Date and Time)
`curl -v "http://localhost:8080/topjava/rest/meals/filter?startDate=2015-05-30&endDate=2015-05-30&startTime=11%3A00&endTime=14%3A00`