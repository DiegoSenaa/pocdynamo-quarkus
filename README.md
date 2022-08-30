# pocdynamo Project

Projeto para estudo de de Quarkus e DynamoDB.

Foi utilizado localstack para rodar o mesmo.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

## Preparado o localstack

OBS: É necessário ter o docker rodando o localstack.

### Rodando dentro do container localstack 
1. Download [AWS Cli](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-version.html)
2. Configure AWS Cli:
```shell script
$ aws configure
```
- AWS Access Key ID: `xpto`
- AWS Secret Access Key: `xpto`
- Default region name: `sa-east-1`
- Default output format: `json`

### Configurando tabela:
```shell script
$ aws dynamodb create-table --endpoint-url http://127.0.0.1:4566 --region sa-east-1 --table-name acionamento_bem --no-verify-ssl --attribute-definitions AttributeName=codigo_solicitacao,AttributeType=S AttributeName=numero_operacao,AttributeType=S AttributeName=produto,AttributeType=S --key-schema AttributeName=codigo_solicitacao,KeyType=HASH AttributeName=numero_operacao,KeyType=RANGE --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 --global-secondary-indexes IndexName=gsi_busca_bens,KeySchema=[{AttributeName=numero_operacao,KeyType=HASH},{AttributeName=produto,KeyType=RANGE}],Projection={ProjectionType=ALL},ProvisionedThroughput={ReadCapacityUnits=5,WriteCapacityUnits=5} --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5
```

## Modelos de requisções:

#### Post Criar Acionamento:
```shell script
$ curl --request POST \
  --url http://localhost:8080/acionamento \
  --header 'Content-Type: application/json' \
  --data '{
	"numeroOperacao" : "51289",
	"codigoProduto" : 22,
	"statusSolicitacao" : "Aberto",
	"codigoOperacao" : 2022
}'
```
#### Put Atualizar acionamento:
```shell script
curl --request PUT \
  --url http://localhost:8080/acionamento \
  --header 'Content-Type: application/json' \
  --data '{
	"codigoSolicitacao": "30dffb45-fe47-4c55-bf6f-5cdbfa1a516f",
	"numeroOperacao" : "51289",
	"codigoProduto" : 22,
	"statusSolicitacao" : "Aberto",
	"codigoOperacao" : 3333
}'
```
#### Deletar acionamento:
```shell script
curl --request DELETE \
  --url http://localhost:8080/acionamento/{codigoDaSolicitacao}/{codigoProduto}
```
#### Listar todos os acionamento ("Full scan"):
```shell script
curl --request GET \
  --url http://localhost:8080/acionamento
```

#### Buscar acionamento 
```shell script
curl --request GET \
  --url http://localhost:8080/acionamento/{codigoDaSolicitacao}/{codigoProduto}
```

#### Buscar acionamento por PK e SK (QUERY SCAN):
```shell script
curl --request GET \
curl --request GET \
  --url 'http://localhost:8080/acionamento?id=codigoDaSolicitacao&partNumOpe={numeroDaOperacaoParcial}'
```

