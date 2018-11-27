package org.tsdes.intro.jee.jta.transactions.ejb;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.tsdes.misc.testutils.EmbeddedJeeSupport;

public class TestBase {

    protected static QueriesEJB queriesEJB;

    protected static EmbeddedJeeSupport container = new EmbeddedJeeSupport();

    @BeforeAll
    public static void initContainer()  {
        container.initContainer();
        queriesEJB = container.getEJB(QueriesEJB.class);
    }

    @AfterAll
    public static void closeContainer() throws Exception {
        container.closeContainer();
    }

    protected  <T> T getEJB(Class<T> klass){
        return container.getEJB(klass);
    }

    @BeforeEach
    @AfterEach
    public void emptyDatabase(){
        //this is quicker than re-initialize the whole DB / EJB container
        queriesEJB.deleteAll();
    }

}
