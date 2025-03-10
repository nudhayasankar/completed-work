package com.amica.billing.db.mongo;

import com.amica.billing.db.CustomerRepository;
import com.amica.billing.db.InvoiceRepository;
import com.amica.billing.db.Migration;
import com.amica.billing.parse.ParserPersistence;
import lombok.extern.java.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@ComponentScan(basePackageClasses={CustomerRepository.class, ParserPersistence.class})
@EnableAutoConfiguration
@EnableMongoRepositories(basePackageClasses=CustomerRepository.class)
@PropertySource(value=
        {"classpath:DB.properties","classpath:migration.properties"})
@Log
public class MigrateCSVToMongo {
    public static void main(String[] args) {
        try(ConfigurableApplicationContext context = SpringApplication.run(MigrateCSVToMongo.class, args)) {
            Migration migrationBean = context.getBean(Migration.class);
            CustomerRepository cRepo = context.getBean(CustomerRepository.class);
            InvoiceRepository iRepo = context.getBean(InvoiceRepository.class);
            log.info(String.format("Before Migrate customer count is %d", cRepo.count()));
            log.info(String.format("Before Migrate invoice count is %d", iRepo.count()));
            migrationBean.migrate();
            log.info(String.format("After Migrate customer count is %d", cRepo.count()));
            log.info(String.format("After Migrate invoice count is %d", iRepo.count()));

        }
    }
}
