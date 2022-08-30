package br.itau.pocdynamo.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
@Getter
@Setter
@ToString
public class AcionamentoDTO {

    private String codigoSolicitacao;

    private String numeroOperacao;

    private String codigoProduto;

    private String statusSolicitacao;

    private String codigoOperacao;
}
