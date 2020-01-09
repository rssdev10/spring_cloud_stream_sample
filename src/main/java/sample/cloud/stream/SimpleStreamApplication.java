package sample.cloud.stream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
@SpringBootApplication
public class SimpleStreamApplication {
    private EmitterProcessor<String> sourceGenerator = EmitterProcessor.create();

    public static void main(String[] args) {
        final ApplicationContext context = SpringApplication.run(SimpleStreamApplication.class, args);
        final SimpleStreamApplication app = context.getBean(SimpleStreamApplication.class);
        for (int i = 0; i < 5; i++) {
            app.emitData(LocalDateTime.now().toString());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignore) {
            }
        }
    }

    public void emitData(String str) {
        sourceGenerator.onNext(str);
        System.out.println("Flux emitted: " + str);
    }

    @Bean
    public Supplier<Flux<String>> generate_flux() {
        return () -> sourceGenerator;
    }

    @Bean
    public Function<String, String> process() {
        return String::toUpperCase;
    }

    @Bean
    public Consumer<String> sink() {
        return x -> System.out.println("Consumed: " + x);
    }

    @Bean
    public Supplier<Message<?>> generate_non_flux() {
        return MessageBuilder.withPayload("Non flux emitter: " + LocalDateTime.now().toString())::build;
    }

}
