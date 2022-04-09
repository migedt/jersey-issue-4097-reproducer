package reproducer;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/helloWorld")
public class HelloWorldResource {

    private static final Response RESPONSE =
        Response.status(410).entity("Hello World").build();

    @GET
    public Response helloWorld() {
        // returning the same response is obviously wrong
        return RESPONSE;
    }

}