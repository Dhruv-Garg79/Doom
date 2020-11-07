package doom;

import doom.annotations.GET;
import doom.annotations.MiddleWare;
import doom.annotations.Path;
import doom.annotations.QueryParam;
import doom.http.Request;
import doom.http.Response;

@MiddleWare({LogMiddleWare.class})
@Path("/api/example")
public class ExampleResource {

    @GET("/resource")
    @MiddleWare({LogMiddleWare.class})
    public Response getNames(Request request, @QueryParam("id") String id) {
        System.out.println("Holla senorita " + request.getPath());
        return new Response("Hello world!");
    }
}
