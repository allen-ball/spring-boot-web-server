package application;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@NoArgsConstructor @ToString @Log4j2
public class DictionaryConfiguration {
    @Bean
    public Map<String,String> dictionary() {
        return new ConcurrentSkipListMap<>();
    }
}
