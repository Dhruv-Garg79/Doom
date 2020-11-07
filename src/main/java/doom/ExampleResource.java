package doom;

import doom.http.annotations.GET;
import doom.middleware.MiddleWare;
import doom.http.annotations.Path;
import doom.http.annotations.QueryParam;
import doom.http.Request;
import doom.http.Response;

@MiddleWare({LogMiddleware.class})
@Path("/api/example")
public class ExampleResource {

    @GET("/resource")
    @MiddleWare({LogMiddleware.class})
    public Response getNames(Request request, @QueryParam("id") String id) {
        System.out.println("Holla senorita " + request.getPath());
        return new Response("Hello world!");
    }
}
