package hust.server.app.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class Config {
    @Bean
    public Cloudinary getCloudinary(){
        Map config = new HashMap();
        config.put("cloud_name", "dn1pbep3e");
        config.put("api_key", "231664969325537");
        config.put("api_secret", "dVhvZW_9SxMChGdi4jDGlk_RBO8");
        config.put("secure", true);
        return new Cloudinary(config);
    }
}
