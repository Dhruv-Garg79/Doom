package doom.models;

public class RequestBody <T>{
    private final T body;

    public RequestBody(T requestBody){
        this.body = requestBody;
    }

    public T getBody(){
        return body;
    }

    @Override
    public String toString() {
        return "RequestBody{" +
                "body=" + body.toString() +
                '}';
    }
}
