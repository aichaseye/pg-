package mfpai.gouv.sn;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import mfpai.gouv.sn.JhipsterProjectBaseJwtApp;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = JhipsterProjectBaseJwtApp.class)
public @interface IntegrationTest {
}
