The following readme is for an imaginary sports weather service that would return the weather for a given sports event.
Each team is given a sports id and each venue is given a venue Id. Sports category is irrelevent though I admit it would
make for a better design. This was meant to be a simple example of writing a weather event in a functional manner using
the reactive library in spring. If you create another service that would retrieve the weather events from a db
and have this service call it, it would work as expected. 

# Directions to run
1. Start the application in your editor of choice
2. Go into postman
3. Make a call to one of the following
> POST call to http://localhost:8088/sports/weather/team
{
    "team":112,
    "date": "2022-02-07"
}

> Response Example 


> POST call to http://localhost:8088/sports/weather/venue
{
    "venue":112
}

> Response Example [
{
"message": "Tropicana Field,Florida\\nCurrent Weather\\nSunny, with a high near 67. North northeast wind around 10 mph."
}
]

# SideNotes
TODO: To be implemented at a later time
1. caching of responses from the sports api
2. caching of responses from weather api
3. testing of all the classes
4. integration test of both end points
5. better error handling
6. First run will always be slower, after initial call
7. Adding logging
8. Create a service to show that the code works as intended 