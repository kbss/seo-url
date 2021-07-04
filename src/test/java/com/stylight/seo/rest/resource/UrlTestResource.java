package com.stylight.seo.rest.resource;


import org.springframework.web.bind.annotation.RequestBody;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

public interface UrlTestResource {

    @POST
    @Path("/pretty")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Map<String, String> getPrettyUrls(@RequestBody List<String> urls);

    @POST
    @Path("/parametrized")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Map<String, String> getParametrizedUrls(@RequestBody List<String> urls);
}
