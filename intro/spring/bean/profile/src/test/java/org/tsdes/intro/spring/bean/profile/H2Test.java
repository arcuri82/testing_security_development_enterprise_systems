package org.tsdes.intro.spring.bean.profile;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tsdes.intro.spring.bean.jpa.Application;

/**
 * Created by arcuri82 on 26-Jan-18.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class H2Test extends DbTestBase {


    @Override
    protected String getExpectedDatabaseName() {
        return "h2";
    }
}
