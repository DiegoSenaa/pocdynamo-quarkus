package br.itau.pocdynamo.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

@ConfigMapping(prefix = "aws.dynamodb")
public interface DynamoDBConfigProperties {
    @WithName("endpoint")
    String endpoint();

    @WithName("region")
    String region();

    @WithName("credentials")
    DynamoDbCredentials credentials();
    interface DynamoDbCredentials {
        @WithName("access-key")
        String accessKey();

        @WithName("secret-key")
        String secretKey();
    }
}
