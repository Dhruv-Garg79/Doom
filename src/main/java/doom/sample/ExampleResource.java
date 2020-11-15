package doom.sample;

import doom.http.Request;
import doom.http.Response;
import doom.http.annotations.GET;
import doom.http.annotations.POST;
import doom.http.annotations.Path;
import doom.middleware.MiddleWare;
import org.json.JSONObject;

import java.io.File;

@MiddleWare({LogMiddleware.class})
@Path("/api/example")
public class ExampleResource {

    public ExampleResource(){}

    @POST("/resource")
    @MiddleWare({LogMiddleware.class})
    public Response getNames(Request request) {
        return new Response("Hello world!");
    }

    @GET("/resource")
    public Response getNamesWithQuery(Request request) {
        System.out.println("Hola senorita... " + request.getQueryParam("id"));
        return new Response("Hello world!");
    }

    @GET("/resource/{name}/{id}")
    public Response getNamesWithPathParam(Request request) {
        System.out.println("Hola senorita... " + request.getPathParam("name"));
        JSONObject json = new JSONObject();
        json.put("name", request.getPathParam("name"));
        json.put("id", request.getPathParam("id"));
        Response response = new Response(json);
        response.setContentType("application/json");
        return response;
    }

    @GET("/resource/file")
    public Response file(Request request) {
        File file = new File("/home/dhruv/IdeaProjects/Framework/java-doom/src/main/java/doom/sample/Application.java");
        return new Response(file);
    }

    @GET("/resource/{name}/family/{members}")
    public Response getNamesWithPathParam2(Request request) {
        System.out.println("Hola senorita... " + request.getPathParam("name"));
        System.out.println(request.getPathParam("members") + ' ' + request.getQueryParam("id"));
        return new Response("Hello world!");
    }
}
