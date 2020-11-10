package doom.sample;

import doom.http.Request;
import doom.http.Response;
import doom.http.annotations.GET;
import doom.http.annotations.POST;
import doom.http.annotations.Path;
import doom.middleware.MiddleWare;

@MiddleWare({LogMiddleware.class})
@Path("/api/example")
public class ExampleResource {

    public ExampleResource(){}

    @POST("/resource")
    @MiddleWare({LogMiddleware.class})
    public Response getNames(Request request) {
        System.out.println("Hola senorita... " + request.getPath());
        return new Response("Hello world!");
    }

    @GET("/resource")
    public Response getNamesWithQuery(Request request) {
        System.out.println("Hola senorita... " + request.getQueryParam("id"));
        return new Response("Hello world!");
    }

    @GET("/resource/{name}")
    public Response getNamesWithPathParam(Request request) {
        System.out.println("Hola senorita... " + request.getPathParam("name"));
        return new Response("Hello world!");
    }

    @GET("/resource/{name}/family/{members}")
    public Response getNamesWithPathParam2(Request request) {
        System.out.println("Hola senorita... " + request.getPathParam("name"));
        System.out.println(request.getPathParam("members") + ' ' + request.getQueryParam("id"));
        return new Response("Hello world!");
    }
}
