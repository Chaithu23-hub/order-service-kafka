package com.MicroService.OrderService.runner; // <-- The package declaration is crucial

import com.MicroService.OrderService.service.OrderCreationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component // This annotation makes it a Spring-managed bean
public class AppRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(AppRunner.class);

    private final OrderCreationService orderCreationService;

    // Spring will automatically inject the service here
    public AppRunner(OrderCreationService orderCreationService) {
        this.orderCreationService = orderCreationService;
    }

    @Override
    public void run(String... args) throws Exception {
        // This 'run' method is executed automatically after the application starts.
        logger.info("AppRunner started. Triggering file processing...");

        // Specify the path to your local text file.
        // Use forward slashes for better cross-platform compatibility.
        String filePath = "C:/data/orders.txt";

        List<String> processingResults=orderCreationService.placeOrdersFromFile(filePath);
        writeSummaryReport(processingResults);

        logger.info("AppRunner has finished its task.");

    }

    private void writeSummaryReport(List<String> results) {
        String reportHeader = "--- Order Processing Summary: " + LocalDateTime
                .now().format(DateTimeFormatter.ISO_DATE_TIME) + " ---\n";
        String reportFilePath = "C:/data/processing_summary.txt";
        try {
            // Prepend the header to the results
            results.add(0, reportHeader);
            Files.write(Paths.get(reportFilePath), results);
            logger.info("Successfully wrote summary report to:{} ", reportFilePath);
        } catch (IOException e) {
            logger.error("Error writing summary report:{} ",e.getMessage());
        }
    }
}