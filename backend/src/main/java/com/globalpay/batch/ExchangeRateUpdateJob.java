package com.globalpay.batch;

import com.globalpay.entity.ExchangeRate;
import com.globalpay.repository.ExchangeRateRepository;
import com.globalpay.service.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
public class ExchangeRateUpdateJob {

    private final ExchangeRateRepository exchangeRateRepository;
    private final CurrencyService currencyService;

    public ExchangeRateUpdateJob(ExchangeRateRepository exchangeRateRepository, CurrencyService currencyService) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.currencyService = currencyService;
    }

    @Bean
    public Job updateExchangeRatesJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("updateExchangeRatesJob", jobRepository)
            .incrementer(new RunIdIncrementer())
            .start(updateExchangeRatesStep(jobRepository, transactionManager))
            .build();
    }

    @Bean
    public Step updateExchangeRatesStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("updateExchangeRatesStep", jobRepository)
            .<ExchangeRateUpdate, ExchangeRate>chunk(10, transactionManager)
            .reader(exchangeRateItemReader())
            .processor(exchangeRateProcessor())
            .writer(exchangeRateWriter())
            .build();
    }

    @Bean
    public ItemReader<ExchangeRateUpdate> exchangeRateItemReader() {
        // In production, fetch this from API
        List<ExchangeRateUpdate> updates = new ArrayList<>();
        updates.add(new ExchangeRateUpdate("USD", "EUR", new BigDecimal("0.92")));
        updates.add(new ExchangeRateUpdate("USD", "GBP", new BigDecimal("0.79")));
        updates.add(new ExchangeRateUpdate("USD", "JPY", new BigDecimal("149.50")));
        return new ListItemReader<>(updates);
    }

    @Bean
    public ItemProcessor<ExchangeRateUpdate, ExchangeRate> exchangeRateProcessor() {
        return update -> {
            log.info("Processing exchange rate: {} -> {} = {}", update.fromCurrency, update.toCurrency, update.rate);
            return ExchangeRate.builder()
                .fromCurrency(update.fromCurrency)
                .toCurrency(update.toCurrency)
                .midMarketRate(update.rate)
                .rate(update.rate)
                .ourRate(update.rate.multiply(new BigDecimal("1.02"))) // 2% margin
                .margin(new BigDecimal("0.02"))
                .rateTimestamp(LocalDateTime.now())
                .build();
        };
    }

    @Bean
    public ItemWriter<ExchangeRate> exchangeRateWriter() {
        return items -> {
            log.info("Writing {} exchange rates", items.size());
            exchangeRateRepository.saveAll(items);
        };
    }

    @Scheduled(fixedDelay = 3600000) // Run every hour
    public void scheduleExchangeRateUpdate() {
        log.info("Scheduled exchange rate update job");
    }

    public static class ExchangeRateUpdate {
        public String fromCurrency;
        public String toCurrency;
        public BigDecimal rate;

        public ExchangeRateUpdate(String fromCurrency, String toCurrency, BigDecimal rate) {
            this.fromCurrency = fromCurrency;
            this.toCurrency = toCurrency;
            this.rate = rate;
        }
    }
}
