![License MIT](https://img.shields.io/badge/license-MIT-blue.svg)
![Java CI with Gradle](https://github.com/Dhruv-Garg79/Doom/workflows/Java%20CI%20with%20Gradle/badge.svg?branch=main)

# Doom
A simple minimal java web framework. It's a web framework which I have created solely for learning purposes.

### Table of content
- [Getting Started](#getting-started)
- [Installation](#installation)
- [Routing](#routing)
- [HTTP Request Method Annotations](#http-request-method-annotations)
- [How to define Handler](#how-to-define-handler)
- [URL Path Parameters](#url-path-parameters)
- [URL Query Parameters](#url-query-parameters)
- [Form Data](#form-data)
- [Request Class](#request-class)
- [Response Class](#response-class)
- [Middleware](#middleware)


## Getting Started
```java
class Application {
    public static void main(String[] args) {
        DoomServer server = new DoomServer();
        server.start();
    }
}
```

## Installation
Download the JAR from release tab of github and add it to your project directly or using gradle.

using gradle:

Add downloaded JAR to libs folder and below snippet to build.gradle
```groovy
dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
}
```

## Routing
To Define API end-points in this framework mark class with **@PATH**(Here, @PATH is like controller) annotation and give a base url to it, now methods defined within this class with HttpMethod annotations will be API end-points.

```java
@Path("/api/example")
public class ExampleResource {
    @POST("/resource")
    public Response getNames(Request request) {
        System.out.println("Hello world" + request.getPath());
        return new Response("Hello world!");
    }
}
```

You just need to mark class with @PATH and everything will be taken care of by the Doom.

## HTTP Request Method Annotations
Mark methods inside controller with these annotations to tell framework what type of http request this handler should cater to.

- @GET
- @POST
- @PUT
- @PATCH
- @DELETE
- @HEAD

All of these annotations take path as value and default value of path is ` \ `. This path is appended after base path provided in controller.

 ## How to define Handler
 
The structure of all Handlers should follow these rulse:

1. The function should be marked with one of the HttpMethod annotations.
2. The function should accept only one parameter i.e. of type Request.
3. The function should return only one parameter i.e. of type Response.

 ```java
 @POST("/path")
 public Response functionNamr(Request request) {
     return new Response("response");
 }
```

## URL Path Parameters
The Path parameter can be defined by using curly braces `{}` inside value of `HttpMethod` Annotations.

```
@GET("/example/{name}/{id}")
 public Response functionNamr(Request request) {
     System.out.println(request.getPathParam("name"));
     return new Response("response");
 }
```

To access path param use this method of Request class
```
Request.getPathParam("param") //returns value of path param if it exists else null is returned
```


## URL Query Parameters
The URL after `?` contains query parameters.

e.g. if url is /example?name=dhruv
 
```
@GET("/example")
 public Response functionNamr(Request request) {
     System.out.println(request.getQueryParam("name"));
     return new Response("response");
 }
```

To access query param use this method of Request class
```
Request.getQueryParam("param") //returns value of query param if it exists else null is returned
```

## Form Data

## Request Class
This class is used to access:
- Query Parameters
- Path Parameters
- Body
- Form data
- Path information
- Headers
- and all things related to incoming request

## Response Class
This class in used to return Response to the client. Set status code, body and header of your response using this class.

## Middleware
It is used to call a method before request handler.

It can be defined:
- Globally for all calls
- Controller Scoped i.e. applied before all calls defined inside specific controller
- Request Scoped

`@Middleware` annotation is used to declare middlewares to be appied. It takes list of Middleware class as value.

To define a middleware implement `MiddlewareHandler` interface.
```java
public class ExampleMiddleware implements MiddlewareHandler {
    @Override
    public Response handle(Request req) {
        System.out.println("Middleware executed");
        return new Response("ok");
    }
}

@MiddleWare({ExampleMiddleware.class})
@Path("/api/example")
public class ExampleResource {
    @POST("/resource")
    @MiddleWare({ExampleMiddleware.class})
    public Response getNames(Request request) {

        System.out.println("Hello world" + request.getPath());
        return new Response("Hello world!");
    }
}
```

To define middleware globally:
```java
DoomServer server = new DoomServer();

server.addGlobalMiddleWare(new ExampleMiddleware());

//or using lambda
server.addGlobalMiddleWare(req -> {
            
});
```

**Note:**
Middleware will only allow request to go further if 200 response is returned in middleware.