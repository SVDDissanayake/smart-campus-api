# Smart Campus REST API
# By Sanuja Dissanayake
## UOW ID : w2120248 | IIT ID : 20231806

## Introduction

This project implements a RESTful web service for a Smart Campus system using JAX-RS (Jersey) deployed on Apache Tomcat. The system manages rooms, sensors, and sensor readings.

---

## Discovery Endpoint

The API provides a discovery endpoint at:

`GET /rest/`

This returns metadata including version, contact details, and available resources.

Example:

```
{
  "version": "v1",
  "rooms": "/api/v1/rooms",
  "sensors": "/api/v1/sensors"
}
```

Note: The implementation uses `/rest` instead of `/api/v1` as per tutorial guidelines.

---

## Room Management

Rooms support CRUD operations:

* GET `/api/v1/rooms`
* POST `/api/v1/rooms`
* GET `/api/v1/rooms/{id}`
* DELETE `/api/v1/rooms/{id}`

Rooms require client-provided IDs as per module guidelines.

Rooms cannot be deleted if they contain sensors (409 Conflict).

---

## Sensor Management

Sensors are linked to rooms.

Endpoints:

* GET `/api/v1/sensors`
* POST `/api/v1/sensors`
* GET `/api/v1/sensors?type=CO2`

Validation:

* If room does not exist → 422 error

---

## Sensor Readings

Readings are implemented as a sub-resource:

* GET `/api/v1/sensors/{id}/readings`
* POST `/api/v1/sensors/{id}/readings`

Each reading updates the sensor’s current value.

---

## Exception Handling

| Scenario              | Status Code               |
| --------------------- | ------------------------- |
| Room has sensors      | 409 Conflict              |
| Invalid room ID       | 422 Unprocessable Entity  |
| Sensor in maintenance | 403 Forbidden             |
| Unexpected error      | 500 Internal Server Error |

---

## Logging

Request and response logging is implemented using JAX-RS filters.

---

## API Testing (Postman)

Example request:

POST `/api/v1/rooms`

```
{
  "id": 1,
  "name": "Lab 1",
  "capacity": 30,
  "sensorIds": []
}
```

---

## Technologies Used

* Java
* JAX-RS (Jersey)
* Apache Tomcat
* Maven

---

## How to Run

1. Build project:

```
mvn clean install
```

2. Deploy WAR to Tomcat

3. Access:

```
http://localhost:8080/<project-name>/api/v1/
```

---

## Conclusion

This project demonstrates RESTful API design including resource management, validation, sub-resources, and error handling using JAX-RS.


# Answers For the Report Questions

# Coursework Part 1 Questions
## QUESTION 01
### Answer:
Typically, JAX-RS resource classes are instantiated in a per-request manner, which means that each HTTP request results in a new resource class object being instantiated, which enables independent processing of requests and minimizes possible problems related to shared states in the resource class.
However, in this particular implementation, several requests might access the shared data structures, such as in-memory storage represented by the HashMap object in the service layer. In this case, multiple clients may communicate with the API simultaneously, which could result in race condition problems.
The approach to handling such issues should be properly designed to maintain consistency in the data stored within the API. While complete synchronization is not included in this course work, this is considered crucial in application development in general.

## QUESTION 02
### Answer:
Hypermedia (HATEOAS) refers to a REST architectural style that incorporates links within API responses to point the client toward the possible resource operations that are available. Unlike using static documentation, hypermedia enables clients to explore the API through links provided.
This helps the client developers by avoiding the need for hardcoding endpoint URLs and also makes the API more flexible. The discovery endpoint in this project utilizes hypermedia to provide links to resources such as "/rooms" and "/sensors".

# Coursework Part 2 Questions
## QUESTION 01
### Answer:
Limiting responses to room IDs would minimize network usage because less data is sent, resulting in better efficiency when working on a larger scale. On the other hand, it will be harder for clients to access complete information about rooms through extra requests.
Sending whole room objects would maximize network usage because more data is sent, but it would make things easier for clients since all information is sent at once. In the current implementation, the application sends room objects for better user experience.

## QUESTION 02
### Answer:
Yes, the DELETE request in this scenario is an idempotent operation. Idempotence refers to performing an action multiple times without changing its end-state.
In the above case, the client can issue a DELETE request on the room resource. The system will delete the room from its memory. However, issuing another DELETE request will have no effect since the resource will no longer exist. In other words, the state of the system remains the same after multiple attempts. As such, this guarantees consistency in the system's state irrespective of multiple requests issued by the client.

# Coursework Part 3 Questions
## QUESTION 01
### Answer:
The @Consumes(MediaType.APPLICATION_JSON) annotation specifies that the API only accepts requests with a JSON payload. If a client sends data in a different format, such as text/plain or application/xml, JAX-RS will not be able to process the request.
In such cases, JAX-RS automatically returns a 415 Unsupported Media Type response, indicating that the server does not support the format of the request body. This ensures that the API enforces consistent data formats and prevents incorrect or incompatible input from being processed.

## QUESTION 02
### Answer:
Using @QueryParam for filtering is generally preferred because it clearly separates resource identification from filtering criteria. The base path (e.g., /sensors) represents the resource collection, while query parameters (e.g., ?type=CO2) are used to refine or filter the results.
In contrast, embedding filters in the URL path (e.g., /sensors/type/CO2) can make the API structure more rigid and less flexible, especially when multiple filters are needed. Query parameters allow combining multiple conditions easily (e.g., /sensors?type=CO2&status=ACTIVE) without complicating the URL structure.
Therefore, the query parameter approach improves flexibility, readability, and scalability when searching or filtering collections.

# Coursework Part 4 Questions
## QUESTION 01
### Answer:
Sub-Resource Locator is an effective improvement technique in the field of API design, which involves splitting logic associated with the sub-resource into separate classes. Rather than implementing all endpoints using a single large controller, a separate class handles every resource (for example, sensor data).
Such a solution contributes to better clarity, facilitates maintenance, and helps developers adhere to the Single Responsibility Principle.
Moreover, it proves to be more beneficial than creating all sub-resources in a single controller, because it prevents complications in terms of coding, debugging, and scalability.

# Coursework Part 5 Questions
## QUESTION 01
### Answer:
The HTTP status code 422 Unprocessable Entity can be used when there is an error with the semantics of the request while the syntax is perfect. In this situation, the syntax of the creation request for the sensor is correct; however, the provided room ID is nonexistent.
The HTTP status code 404 Not Found would suggest that the resource or endpoint requested does not exist. But since the endpoint is valid and the problem is caused by the wrong information provided by the client, a 422 is a better fit for this scenario.

## QUESTION 02
### Answer:
Exposing detailed error messages in API responses can lead to security risks by revealing internal implementation details, such as class names, database structure, or system behavior. Attackers can use this information to identify vulnerabilities and exploit the system.
To reduce this risk, APIs should return controlled and user-friendly error messages while avoiding sensitive details such as stack traces. In this implementation, exception handling is used to provide clear but limited error information, ensuring both usability and security.

## QUESTION 03
### Answer:
Filters are preferred for logging because they provide a centralized mechanism to handle request and response processing. Instead of adding logging code in every resource method, filters allow logging to be implemented once and applied to all endpoints.
This improves maintainability, reduces code duplication, and keeps resource classes clean and focused on business logic. It also ensures consistent logging behavior across the entire API.


## Declaration

This project was developed independently by the author in accordance with the guidelines provided by the module team at the University of Westminster. All work presented is original unless otherwise referenced.

## Author Details
### Name : Dissanayakege Sanuja Vindula Dewmith Dissanayake
### Email : dewmithdissanayake@gmail.com / sanuja.20231806@iit.ac.lk