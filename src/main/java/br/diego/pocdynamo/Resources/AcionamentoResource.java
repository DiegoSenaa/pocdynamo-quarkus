package br.itau.pocdynamo.Resources;

import br.itau.pocdynamo.domain.dto.AcionamentoDTO;
import br.itau.pocdynamo.domain.mappers.AcionamentoMapper;
import br.itau.pocdynamo.entity.AcionamentoEntity;
import br.itau.pocdynamo.services.AcionamentoService;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import static net.logstash.logback.argument.StructuredArguments.kv;

import java.util.List;

@Path("/acionamento")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class AcionamentoResource {

    private final AcionamentoService service;
    private final AcionamentoMapper acionamentoMapper;

    public AcionamentoResource(AcionamentoService service, AcionamentoMapper acionamentoMapper) {
        this.service = service;
        this.acionamentoMapper = acionamentoMapper;
    }


    @POST
    public Response create(@Valid AcionamentoDTO acionamentoDTO, @Context UriInfo uriInfo) {

         AcionamentoEntity savedAcionamento = this.service.create(acionamentoDTO);
        log.info("Acionamento was saved successfully: {}", savedAcionamento);
        AcionamentoDTO acionamentoFinal = acionamentoMapper.mapToDomain(savedAcionamento);
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        return Response.created(uriBuilder.build()).entity(acionamentoFinal).build();
    }
    @GET
    @Path("/{id}/{numeroOperacao}")
    public Response findByIdAndNumOpe(@PathParam("id") String id, @PathParam("numeroOperacao") String numOpe, @Context UriInfo uriInfo) {

        log.info("Start for Acionamento id: {}", kv("id",id), kv("numeroOperacao", numOpe));
        AcionamentoEntity acionamento = this.service.findByIdAndNumOpe(id, numOpe);
        AcionamentoDTO acionamentoFinal = acionamentoMapper.mapToDomain(acionamento);
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        log.info("Returning result: {}", acionamento);
        return Response.ok(uriBuilder.build()).entity(acionamentoFinal).build();
    }

    @PUT()
    public Response updateAcionamento(@Valid AcionamentoDTO acionamentoDTO, @Context UriInfo uriInfo) {

        log.info("Start for Acionamento id: {}", kv("AcionamentoDTO",acionamentoDTO));
        AcionamentoEntity acionamento = this.service.updateAcionamento(acionamentoDTO);
        AcionamentoDTO acionamentoFinal = acionamentoMapper.mapToDomain(acionamento);
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        log.info("Update result: {}", acionamento);
        return Response.ok(uriBuilder.build()).entity(acionamentoFinal).build();
    }

    @DELETE
    @Path("/{id}/{numeroOpercao}")
    public Response delete(@PathParam("id") String id, @PathParam("numeroOpercao") String numeroOpercao) {
        this.service.delete(id, numeroOpercao);
        log.info("Acionamento was deleted successfully ...");
        return Response.ok().build();
    }

    @GET
    public Response findByCriteria(@QueryParam("id") String id, @QueryParam("partNumOpe") String partNumOpe){
        if((id == null || id.isEmpty()) || (partNumOpe == null || partNumOpe.isEmpty())){
            log.info("Searching for Acionamentos (FULL SCAN) ...");
            List<AcionamentoDTO> acionamentos = this.service.findAll();
            return Response.ok(acionamentos).build();
        }
        log.info("Searching for Acionamentos QUERY BY {}) ...", kv("id: ", id), kv("partNumOpe: ", partNumOpe));
        List<AcionamentoDTO> acionamentos = this.service.findByIdAndPartNumOpe(id, partNumOpe);
        return Response.ok(acionamentos).build();
    }
}