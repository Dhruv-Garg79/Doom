package doom;

import doom.annotations.GET;
import doom.annotations.Path;
import doom.http.Request;
import doom.http.Response;

@Path("/api/example")
public class ExampleResource {

    @GET("/resource")
    public Response getNames(Request request) {
        System.out.println("Holla senorita " + request.getPath());
        return new Response("Hello world!");
    }
}
