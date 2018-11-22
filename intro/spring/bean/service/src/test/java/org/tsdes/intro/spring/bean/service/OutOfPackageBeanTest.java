package org.tsdes.intro.spring.bean.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tsdes.intro.spring.bean.service.root.Application;
import org.tsdes.intro.spring.bean.service.root.BaseSingleton;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


/**
 * Created by arcuri82 on 25-Jan-18.
 */
@ExtendWith(SpringExtension.class)
//as Application is in a inner package, I need to
//explicitly state which is my SpringBoot entry-point
@SpringBootTest(classes = Application.class)
public class OutOfPackageBeanTest {

    @Autowired
    private BaseSingleton inPackage;

    /*
        This bean will not be found, because the scan will
        start from the package in which Application is located,
        and recursively in its subpackages.

        "required=false" means that Spring should keep going
        on even if this bean is not found.
        Otherwise, Spring would just throw an exception at runtime.
     */
    @Autowired(required = false)
    private OutOfPackageBean outOfPackageBean;

    @Test
    public void testBeanScanning(){

        //bean was not found, and so not wired
        assertNull(outOfPackageBean);

        //this bean was found and wired
        assertNotNull(inPackage);
    }
}