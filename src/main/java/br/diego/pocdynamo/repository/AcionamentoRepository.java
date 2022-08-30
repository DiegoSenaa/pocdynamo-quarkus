package br.itau.pocdynamo.repository;

import br.itau.pocdynamo.entity.AcionamentoEntity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class AcionamentoRepository {
    private final DynamoDBMapper dynamoDBMapper;

    @Inject
    public AcionamentoRepository(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public AcionamentoEntity save(AcionamentoEntity acionamento) {
        this.dynamoDBMapper.save(acionamento);
        acionamento.setCodigoOperacao(acionamento.getCodigoOperacao());
        return acionamento;
    }

    public Optional<AcionamentoEntity> findByIdAndNumOperation(String id, String numOpe) {
        AcionamentoEntity acionamentoEntity = this.dynamoDBMapper.load(AcionamentoEntity.class, id, numOpe);
        return Optional.ofNullable(acionamentoEntity);
    }

    public void delete(String id, String numOpe) {
        AcionamentoEntity acionamento = this.dynamoDBMapper.load(AcionamentoEntity.class, id, numOpe);
        Optional.ofNullable(acionamento)
                .ifPresent(this.dynamoDBMapper::delete);
    }

    public PaginatedScanList<AcionamentoEntity> findAll() {
        DynamoDBScanExpression scan = new DynamoDBScanExpression();
        return dynamoDBMapper.scan(AcionamentoEntity.class, scan);
    }

    public PaginatedQueryList<AcionamentoEntity> findByIdAndPartNumeroOperacao(String id, String partNumOpe) {
        AcionamentoEntity acionamento = new AcionamentoEntity();
        acionamento.setCodigoSolicitacao(id);

        Condition condition = new Condition();
        condition.withComparisonOperator(ComparisonOperator.BEGINS_WITH)
                .withAttributeValueList(new AttributeValue().withS(partNumOpe));

        DynamoDBQueryExpression<AcionamentoEntity> queryExpression =
                new DynamoDBQueryExpression<AcionamentoEntity>()
                        .withHashKeyValues(acionamento)
                        .withRangeKeyCondition("numero_operacao", condition)
                        .withLimit(10);
        return dynamoDBMapper.query(AcionamentoEntity.class, queryExpression);
    }
}
