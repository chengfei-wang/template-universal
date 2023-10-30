package template.universal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class TemplateUniversalApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemplateUniversalApplication.class, args);
    }

}
