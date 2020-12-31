package org.tsdes.intro.jee.jta.transactions.ejb;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;


@Stateless
public class EJB_10_multi_caller {

    @Resource
    private SessionContext ctx;

    /*
        This is the typical case of when we need to be aware of transaction boundaries.
        injecting beans is the common scenario.
        but what happen when dealing with methods that work on the DB?
        is it going to be a single transaction? or each call its own transaction?
        it depends...
     */
    @EJB
    private EJB_10_multi_base base;



    public void exe(boolean inNewTransaction, String first, String second){

        /*
            here we want to add 2 elements. however, for the 2nd element, we
            want to decide if doing it in the same or a new transcation
         */

        base.add(first);

        if(inNewTransaction){
            base.addInNewTransaction(second);
        } else {
            // when this branch is taken, the following check will fail, as second is in cache and not
            // in DB yet (which will happen when transaction is committed, usually at the end of the method)
            base.add(second);
        }

        /*
            if for any reason the second is not in the DB, we rollback the current transaction.
            note: this check is called in a new transaction, as the current active one gets
            put on hold due to REQUIRES_NEW.
            Recall that "add" on "em" is not in the DB yet, as "em" is just a cache, whose content
            will be saved into DB only once the transaction is committed.
         */
        if(! base.isPresentByCheckingOnNewTransaction(second)){
            ctx.setRollbackOnly();
        }
    }

}
