package br.itau.pocdynamo.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

public class DynamoDBConfig {

    private final DynamoDBConfigProperties dynamoDBConfigProperties;

    @Inject
    public DynamoDBConfig(DynamoDBConfigProperties dynamoDBConfigProperties) {
        this.dynamoDBConfigProperties = dynamoDBConfigProperties;
    }

    @Produces
    public AmazonDynamoDB dynamoDbClient() {
        var dynamoDbCredentialsProperties = dynamoDBConfigProperties.credentials();
        var credentials = new BasicAWSCredentials(dynamoDbCredentialsProperties.accessKey(), dynamoDbCredentialsProperties.secretKey());
        var awsCredentialsProvider = new AWSStaticCredentialsProvider(credentials);
        return AmazonDynamoDBClientBuilder.standard()
                .withCredentials(awsCredentialsProvider)
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(dynamoDBConfigProperties.endpoint(), dynamoDBConfigProperties.region()))
                .build();
    }

    @Produces
    public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB){
        return new DynamoDBMapper(amazonDynamoDB);
    }
}
