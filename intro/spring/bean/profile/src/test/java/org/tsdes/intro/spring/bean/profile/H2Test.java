package org.tsdes.intro.spring.bean.profile;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.tsdes.intro.spring.bean.jpa.Application;

/**
 * Created by arcuri82 on 26-Jan-18.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class H2Test extends DbTestBase {


    @Override
    protected String getExpectedDatabaseName() {
        return "h2";
    }
}
