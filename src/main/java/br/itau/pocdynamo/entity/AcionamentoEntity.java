package br.itau.pocdynamo.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.Data;

@Data
@DynamoDBTable(tableName="acionamento_bem")
public class AcionamentoEntity {

    @DynamoDBAutoGeneratedKey
    @DynamoDBHashKey(attributeName = "codigo_solicitacao")
    private String codigoSolicitacao;

    @DynamoDBRangeKey(attributeName = "numero_operacao")
    @DynamoDBIndexHashKey(attributeName = "numero_operacao", globalSecondaryIndexName = "gsi_busca_bens")
    private String numeroOperacao;

    @DynamoDBIndexRangeKey(attributeName = "produto", globalSecondaryIndexName = "gsi_busca_bens")
    private String codigoProduto;

    @DynamoDBTypeConvertedEnum
    @DynamoDBAttribute(attributeName = "status_solicitacao")
    private String statusSolicitacao;

    @DynamoDBAttribute(attributeName = "codigo_operacao")
    private String codigoOperacao;
}
