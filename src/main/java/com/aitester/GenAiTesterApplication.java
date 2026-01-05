package com.aitester;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GenAiTesterApplication {

	public static void main(String[] args) {
        //verifica se as variáveis estão sendo carregadas antes do Spring tentar ler o System.getenv()
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));

        SpringApplication.run(GenAiTesterApplication.class, args);
	}

}
