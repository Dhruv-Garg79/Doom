package doom;

import doom.http.Request;
import doom.http.Response;
import doom.http.annotations.GET;
import doom.http.annotations.POST;
import doom.http.annotations.Path;
import doom.middleware.MiddleWare;

@MiddleWare({LogMiddleware.class})
@Path("/api/example")
public class ExampleResource {

    @POST("/resource")
    @MiddleWare({LogMiddleware.class})
    public Response getNames(Request request) {
        System.out.println("Holla senorita " + request.getPath());
        return new Response("Hello world!");
    }

    @GET("/resource")
    public Response getNamesWithQuery(Request request) {
        System.out.println("Holla senorita " + request.getQueryParam("id"));
        return new Response("Hello world!");
    }

    @GET("/resource/{name}")
    public Response getNamesWithPathParam(Request request) {
        System.out.println("Holla senorita " + request.getQueryParam("id"));
        return new Response("Hello world!");
    }
}
