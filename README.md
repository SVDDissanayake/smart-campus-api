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
  "contact": {
    "name": "Smart Campus API Support",
    "email": "support@smartcampus.com"
  },
  "resources": {
    "rooms": "/rest/rooms",
    "sensors": "/rest/sensors"
  }
}
```

Note: The implementation uses `/rest` instead of `/api/v1` as per tutorial guidelines.

---

## Room Management

Rooms support CRUD operations:

* GET `/rest/rooms`
* POST `/rest/rooms`
* GET `/rest/rooms/{id}`
* DELETE `/rest/rooms/{id}`

Rooms require client-provided IDs as per module guidelines.

Rooms cannot be deleted if they contain sensors (409 Conflict).

---

## Sensor Management

Sensors are linked to rooms.

Endpoints:

* GET `/rest/sensors`
* POST `/rest/sensors`
* GET `/rest/sensors?type=CO2`

Validation:

* If room does not exist → 422 error

---

## Sensor Readings

Readings are implemented as a sub-resource:

* GET `/rest/sensors/{id}/readings`
* POST `/rest/sensors/{id}/readings`

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

POST `/rest/rooms`

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
http://localhost:8080/<project-name>/rest/
```

---

## Conclusion

This project demonstrates RESTful API design including resource management, validation, sub-resources, and error handling using JAX-RS.
