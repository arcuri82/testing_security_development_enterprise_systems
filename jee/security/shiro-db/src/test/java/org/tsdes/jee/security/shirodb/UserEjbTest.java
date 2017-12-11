package org.tsdes.jee.security.shirodb;

import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by arcuri82 on 11-Dec-17.
 */
@RunWith(Arquillian.class)
public class UserEjbTest {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(A.class, Data.class)
                .addAsResource("META-INF/persistence.xml");
    }

}