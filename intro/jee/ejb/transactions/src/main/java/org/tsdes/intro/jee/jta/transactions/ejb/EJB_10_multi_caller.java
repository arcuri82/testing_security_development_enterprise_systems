package org.tsdes.intro.jee.jta.transactions.ejb;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;


@Stateless
public class EJB_10_multi_caller {

    @Resource
    private SessionContext ctx;

    @EJB
    private EJB_10_multi_base base;



    public void exe(boolean inNewTransaction, String first, String second){

        base.add(first);

        if(inNewTransaction){
            base.addInNewTransaction(second);
        } else {
            base.add(second);
        }

        if(! base.isPresentByCheckingOnNewTransaction(second)){ //note: called in new transaction
            ctx.setRollbackOnly();
        }
    }

}
