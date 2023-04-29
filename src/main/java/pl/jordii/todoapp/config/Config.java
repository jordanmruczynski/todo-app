package pl.jordii.todoapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.Validator;

@Configuration
public class Config  {

    @Bean
    Validator validator() {
        return new LocalValidatorFactoryBean();
    }
//
//    @Override
//    public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
//        validatingListener.addValidator("beforeCreate", validator());
//        validatingListener.addValidator("beforeSave", validator());
//    }

}
