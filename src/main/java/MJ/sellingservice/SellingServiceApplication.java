package MJ.sellingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SellingServiceApplication {

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(SellingServiceApplication.class);
    //app.setAdditionalProfiles("dev");
    app.run(args);
  }

}
