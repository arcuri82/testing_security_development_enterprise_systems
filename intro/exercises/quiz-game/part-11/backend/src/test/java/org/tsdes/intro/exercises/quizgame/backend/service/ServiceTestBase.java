package org.tsdes.intro.exercises.quizgame.backend.service;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by arcuri82 on 14-Dec-17.
 */
public class ServiceTestBase {

    @Autowired
    private ResetService deleteService;


    @Before
    public void cleanDatabase(){
        deleteService.resetDatabase();
    }
}
