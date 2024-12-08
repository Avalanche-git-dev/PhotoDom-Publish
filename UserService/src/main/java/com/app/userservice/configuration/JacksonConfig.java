package com.app.userservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class JacksonConfig {

//    @Bean
//    ObjectMapper objectMapper() {
//        return new ObjectMapper()
//                .activateDefaultTyping(
//                    BasicPolymorphicTypeValidator.builder()
//                        //.allowIfSubType("com.app.userservice.model")
//                        //.allowIfSubType("com.app.userservice.entity")// Modifica con il tuo package base
//                        .build(),
//                    ObjectMapper.DefaultTyping.NON_FINAL
//                )
//                .registerModule(new JavaTimeModule())
//                .findAndRegisterModules() // Per supportare tipi come LocalDate
//   	            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // U;
//    }

//	@Bean(name="user")
//	ObjectMapper objectMapperModel() {
//
//		ObjectMapper objectMapper = new ObjectMapper();
//		// Rimuovi il supporto per il tipo esplicito
//		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//		objectMapper.registerModule(new JavaTimeModule());
//		objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL,
//				JsonTypeInfo.As.WRAPPER_OBJECT // Usa una struttura compatibile
//		);
//		return objectMapper;
//	}

	@Bean
	ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();

		// Aggiungi supporto per Java 8 date/time
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		// Disabilita il tipo esplicito nella serializzazione
		objectMapper.deactivateDefaultTyping();
//		objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL
//				 // Usa una struttura compatibile
//		);

		return objectMapper;
	}

}
