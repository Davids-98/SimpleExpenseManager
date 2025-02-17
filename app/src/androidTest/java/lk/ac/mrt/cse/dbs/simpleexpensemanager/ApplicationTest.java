/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;

import androidx.test.core.app.ApplicationProvider;

import org.junit.BeforeClass;
import  org.junit.Test;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.PersistentExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest {
    private static ExpenseManager expenseManager;

    @BeforeClass
    public static void testingAddAccount(){
        Context con = ApplicationProvider.getApplicationContext();
        try {
            expenseManager = new PersistentExpenseManager(con);
        } catch (ExpenseManagerException e) {
            e.printStackTrace();
        }
        expenseManager.addAccount("190545H", "BOC", "Dasith", 5000);
    }

    @Test
    public void AddedAccountChecking(){
        assertTrue(expenseManager.getAccountNumbersList().contains("190545H"));
    }

    @Test
    public void checkingTransaction() {
        int current_size = expenseManager.getTransactionsDAO().getAllTransactionLogs().size();
        try {
            expenseManager.updateAccountBalance("190545H", 10, 5, 2022, ExpenseType.valueOf("EXPENSE"), "1000");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(expenseManager.getTransactionsDAO().getAllTransactionLogs().size() - current_size == 1);
    }
}