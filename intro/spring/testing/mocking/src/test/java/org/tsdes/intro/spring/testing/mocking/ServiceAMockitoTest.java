package org.tsdes.intro.spring.testing.mocking;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by arcuri82 on 14-Feb-18.
 */
//Note: no special runner here
public class ServiceAMockitoTest {

    // no service injection


    @Test
    public void testOK(){

        long okId = 0;
        long notOkId = 1;

        ServiceB serviceB = mock(ServiceB.class);
        when(serviceB.isOK(okId)).thenReturn(true);
        when(serviceB.isOK(notOkId)).thenReturn(false);

        ServiceA serviceA = new ServiceA(serviceB);

        String res = serviceA.check(notOkId);
        assertEquals("NOT OK", res);

        res = serviceA.check(okId);
        assertEquals("OK", res);
    }
}