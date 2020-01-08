package sample.cloud.stream;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

/**
 * Created by rss on 08/01/2020
 */
@SpringBootTest(
        classes = SimpleStreamApplication.class
)
@Import(TestChannelBinderConfiguration.class)
@ActiveProfiles("test")
public class AbstractTest {
}
