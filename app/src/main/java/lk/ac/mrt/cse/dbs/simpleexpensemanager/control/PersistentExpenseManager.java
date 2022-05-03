package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.SQLiteAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.SQLiteTransactionDAO;

public class PersistentExpenseManager extends ExpenseManager{

    public PersistentExpenseManager(Context context) throws ExpenseManagerException {
        setup(context);
    }
    @Override
    public void setup(Context context) throws ExpenseManagerException {
        SQLiteAccountDAO sqLiteAccountDAO = new SQLiteAccountDAO(context);
        SQLiteTransactionDAO sqLiteTransactionDAO = new SQLiteTransactionDAO(context);

        setAccountsDAO(sqLiteAccountDAO);
        setTransactionsDAO(sqLiteTransactionDAO);
    }

    @Override
    public void setup() throws ExpenseManagerException {

    }
}
