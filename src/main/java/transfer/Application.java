package transfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import transfer.converter.CreateSubscriberDTOToSubscriberConverter;
import transfer.converter.CreateMessageDTOToMessageConverter;
import transfer.converter.MessageToMessageDTOConverter;
import transfer.converter.SubscriberToSubscriberDTOConverter;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ConfigurableConversionService conversionService() {
        ConfigurableConversionService configurableConversionService = new GenericConversionService();
        configurableConversionService.addConverter(new CreateMessageDTOToMessageConverter());
        configurableConversionService.addConverter(new MessageToMessageDTOConverter());
        configurableConversionService.addConverter(new SubscriberToSubscriberDTOConverter());
        configurableConversionService.addConverter(new CreateSubscriberDTOToSubscriberConverter());
        return configurableConversionService;
    }

}
