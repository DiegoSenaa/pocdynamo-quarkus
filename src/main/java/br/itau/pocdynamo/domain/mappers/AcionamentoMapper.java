package br.itau.pocdynamo.domain.mappers;

import br.itau.pocdynamo.domain.dto.AcionamentoDTO;
import br.itau.pocdynamo.entity.AcionamentoEntity;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AcionamentoMapper {

    private final AcionamentoMapper acionamentoMapper;

    public AcionamentoMapper(AcionamentoMapper acionamentoMapper) {
        this.acionamentoMapper = acionamentoMapper;
    }

    public AcionamentoEntity mapToEntity(AcionamentoDTO acionamento) {
        var mapper = new ModelMapper();

        return mapper.map(acionamento, AcionamentoEntity.class );
    }

    public AcionamentoDTO mapToDomain(AcionamentoEntity acionamento) {
        var mapper = new ModelMapper();

        return mapper.map(acionamento, AcionamentoDTO.class );
    }

    public List<AcionamentoDTO> mapToDtoList(List<AcionamentoEntity> acionamentos) {
        List<AcionamentoDTO> acionamentosDto = new ArrayList<>();
        
        acionamentos.stream().forEach((acionamento) -> acionamentosDto.add(mapToDomain(acionamento)));
        
        return acionamentosDto;
    }
}
