package ethereumfetcher.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties
public class EthereumFetcherProperties {

    @Value("${ethereum.node.url}")
    private String ethereumNodeUrl;
}
