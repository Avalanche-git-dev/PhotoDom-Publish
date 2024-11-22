package com.photodom.keycloak;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.photodom.keycloak.service.UserService;

public class Main {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class)) {
            // Sostituisci con il tuo bean principale o test
            UserService userService= context.getBean(UserService.class);
//            
//            
            System.out.println(userService.findById(4).get().toString());
        	System.out.println("Beans registrati:");
    		for (String beanName : context.getBeanDefinitionNames()) {
			System.out.println("Bean --->" + beanName);
		}
		

        }
    }
}
