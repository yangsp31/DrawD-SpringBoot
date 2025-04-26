package SJ_Project.DrawD.config;

import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;

@Configuration
public class serviceConfig {
    @Bean
    public JsonSchema jsonSchema() {
        try (InputStream inputStream = getClass().getResourceAsStream("/schema/diagram.json")) {
            return JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909)
                    .getSchema(inputStream);
        }
        catch(Exception e) {
            System.out.println(e);
            throw new RuntimeException("Failed to load schema", e);
        }
    }
}
