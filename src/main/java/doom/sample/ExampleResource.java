package doom.sample;

import doom.http.annotations.GET;
import doom.http.annotations.POST;
import doom.http.annotations.Path;
import doom.middleware.MiddleWare;
import doom.models.MultiPart;
import doom.models.Request;
import doom.models.Response;
import org.json.JSONObject;

import java.io.File;

@MiddleWare({LogMiddleware.class})
@Path("/api/example")
public class ExampleResource {

    @POST("/resource")
    @MiddleWare({LogMiddleware.class})
    public Response postExample(Request request) {
        System.out.println(request.getRequestBody());
        return new Response("Hello world!");
    }

    @POST("/resource/multi")
    @MiddleWare({LogMiddleware.class})
    public Response postExampleWithMulti(Request request) {
        MultiPart body = (MultiPart) request.getRequestBody().getBody();
        System.out.println(body.getText("name"));

        File file = body.getFile("file");

        System.out.println(file.getName());
        System.out.println(file.getAbsolutePath());

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

    @GET("/resource/multi")
    public Response getExampleWithForm(Request request) {
        MultiPart form = new MultiPart();
        form.put("firstName", "Dhruv");

        File file = new File("/home/dhruv/IdeaProjects/Framework/java-doom/src/main/java/doom/sample/Application.java");
        form.put("file", file);

        File image = new File("/home/dhruv/Pictures/my_iamge.jpeg");
        form.put("image", image);

        return new Response(form);
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
