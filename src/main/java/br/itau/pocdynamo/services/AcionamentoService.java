package br.itau.pocdynamo.services;

import br.itau.pocdynamo.domain.dto.AcionamentoDTO;
import br.itau.pocdynamo.domain.exception.AcionamentoNotFoundException;
import br.itau.pocdynamo.domain.mappers.AcionamentoMapper;
import br.itau.pocdynamo.entity.AcionamentoEntity;
import br.itau.pocdynamo.repository.AcionamentoRepository;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;

import static net.logstash.logback.argument.StructuredArguments.kv;

import java.util.List;

@ApplicationScoped
@Slf4j
public class AcionamentoService {

    private final AcionamentoMapper acionamentoMapper;
    private final AcionamentoRepository acionamentoRepository;

    public AcionamentoService(AcionamentoMapper acionamentoMapper, AcionamentoRepository acionamentoRepository) {
        this.acionamentoMapper = acionamentoMapper;
        this.acionamentoRepository = acionamentoRepository;
    }

    public AcionamentoEntity create(AcionamentoDTO acionamentoDTO) {

        log.info("Creating new Acionamento: {}", kv("acionamento",acionamentoDTO));
        log.info("Mapping Acionamento to DTO: {}", kv("acionamento",acionamentoDTO));
        var acionamentoEntity = this.acionamentoMapper.mapToEntity(acionamentoDTO);
        AcionamentoEntity aciomentoSaved = acionamentoRepository.save(acionamentoEntity);
        return aciomentoSaved;
    }

    public AcionamentoEntity findByIdAndNumOpe(String id, String numOpe) {
        log.info("Searching for Acionamento id: {}, {}", kv("id",id), kv("numeroOperacao", numOpe));

        AcionamentoEntity acionamentoEntity = this.acionamentoRepository.findByIdAndNumOperation(id, numOpe)
        .orElseThrow(() -> new AcionamentoNotFoundException(String.format("Acionamento for ID %s and Operacao %s does not exist", id, numOpe)));

        return acionamentoEntity;
    }

    public AcionamentoEntity updateAcionamento(AcionamentoDTO acionamentoDTO) {
        log.info("Searching for Acionamento id: {}, {}", kv("id",acionamentoDTO.getCodigoSolicitacao()), kv("numeroOperacao", acionamentoDTO.getNumeroOperacao()));
        findByIdAndNumOpe(acionamentoDTO.getCodigoSolicitacao(), acionamentoDTO.getNumeroOperacao());
        log.info("Mapping Acionamento to new DTO: {}", kv("acionamento",acionamentoDTO));
        AcionamentoDTO acionamentoDomain = AcionamentoDTO.builder()
            .withCodigoOperacao(acionamentoDTO.getCodigoOperacao())
            .withCodigoProduto(acionamentoDTO.getCodigoProduto())
            .withCodigoSolicitacao(acionamentoDTO.getCodigoSolicitacao())
            .withNumeroOperacao(acionamentoDTO.getNumeroOperacao())
            .withStatusSolicitacao(acionamentoDTO.getStatusSolicitacao())
            .build();
        log.info("Updating Acionamento: {}", kv("acionamento",acionamentoDomain));
        AcionamentoEntity create = create(acionamentoDomain);
        return create;
    }

    public void delete(String id, String numOpe) {
        log.info("Deleting for Acionamento id: {}, {}", kv("id",id), kv("numeroOperacao", numOpe));
        this.acionamentoRepository.delete(id, numOpe);
    }

    public List<AcionamentoDTO> findByIdAndPartNumOpe(String id, String partNumOpe) {
        log.info("Searching for Acionamento id: {}, {}", kv("id",id), kv(" partial numeroOperacao", partNumOpe));
        List<AcionamentoEntity> acionamentos = this.acionamentoRepository.findByIdAndPartNumeroOperacao(id, partNumOpe);
        if(acionamentos.isEmpty()) {
           throw new AcionamentoNotFoundException(String.format("Acionamentos does not exist for combination id %s and partial NumeroOpe %s ", id, partNumOpe));
        }
        return this.acionamentoMapper.mapToDtoList(acionamentos);
    }

    public List<AcionamentoDTO> findAll() {

        List<AcionamentoEntity> acionamentos = this.acionamentoRepository.findAll();
        if(acionamentos.isEmpty()) {
           throw new AcionamentoNotFoundException(String.format("Acionamentos does not exist"));
        }
        return this.acionamentoMapper.mapToDtoList(acionamentos);
    }
}
