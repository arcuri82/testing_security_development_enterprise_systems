package org.tsdes.intro.exercises.quizgame.service;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by arcuri82 on 14-Dec-17.
 */
public class ServiceTestBase {

    @Autowired
    private ResetService deleteService;


    @BeforeEach
    public void cleanDatabase(){
        deleteService.resetDatabase();
    }
}
