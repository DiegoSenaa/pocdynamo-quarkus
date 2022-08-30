package br.itau.pocdynamo.Resources;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import br.itau.pocdynamo.domain.exception.AcionamentoNotFoundException;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Provider
@Priority(Priorities.AUTHENTICATION)
@Slf4j
public class ExceptionHandlerController implements ExceptionMapper<Exception> {

    @Context
    private ResourceInfo resourceInfo;

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(Exception ex) {
        var absolutePath = uriInfo.getAbsolutePath();
        var resourceMethod = resourceInfo.getResourceMethod();
        log.error("Error processing request: {}, {}, {}", kv("url", absolutePath), kv("method", resourceMethod.getName()), kv("exception", ex));
        ex.printStackTrace();
        var error = new br.itau.pocdynamo.domain.exception.ErrorDto();
        error.setMessage(ex.getMessage());
        if (ex instanceof AcionamentoNotFoundException){
            error.setCode(404);
            log.warn("Error processing request: {}", kv("error", error));
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        error.setCode(500);
        log.error("Error processing request: {}", kv("error", error));
        return Response.serverError().entity(error).build();
    }

}