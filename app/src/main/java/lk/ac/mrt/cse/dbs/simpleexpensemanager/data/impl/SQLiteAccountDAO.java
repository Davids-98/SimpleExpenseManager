package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.SQLiteHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class SQLiteAccountDAO implements AccountDAO {
    private SQLiteHelper sqLiteHelper;

    public SQLiteAccountDAO(Context context) {
        sqLiteHelper = SQLiteHelper.getInstance(context);
    }

    @Override
    public List<String> getAccountNumbersList() {

        //Get all account number from database
        List<String> accountNumbers = new LinkedList<>();
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        String queryString = "SELECT "+ SQLiteHelper.COLUMN_ACCOUNT_NO+" FROM "+SQLiteHelper.ACCOUNT_TABLE;
        Cursor cursor = db.rawQuery(queryString,null);

        if (cursor.moveToFirst()) {
            do {
                String accountNo = cursor.getString(cursor.getColumnIndexOrThrow("accountNo"));
                accountNumbers.add(accountNo);
            } while (cursor.moveToNext());
        }else {

        }
        return accountNumbers;
    }

    @Override
    public List<Account> getAccountsList() {
        //Get all accounts from database
        List<Account> accounts = new LinkedList<>();
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        String queryString = "SELECT * FROM "+SQLiteHelper.ACCOUNT_TABLE;
        Cursor cursor = db.rawQuery(queryString,null);
        if (cursor.moveToFirst()) {
            do {
                String accountNo = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_ACCOUNT_NO));
                String bankName = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_BANK_NAME));
                String accountHolderName = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_ACCOUNT_HOLDER_NAME));
                double balance = cursor.getDouble(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_BALANCE));

                Account account = new Account(accountNo, bankName, accountHolderName,balance);
                accounts.add(account);
            } while (cursor.moveToNext());
        }else {

        }
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        //Get the account with accountNo
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        String queryString = "SELECT * FROM " + SQLiteHelper.ACCOUNT_TABLE + " WHERE " + SQLiteHelper.COLUMN_ACCOUNT_NO + " =? ";
        String [] arguments = {accountNo};
        Cursor cursor = db.rawQuery(queryString, arguments);

        if (cursor.moveToFirst()) {

            String NoOfAccount = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_ACCOUNT_NO));
            String bankName = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_BANK_NAME));
            String accountHolderName = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_ACCOUNT_HOLDER_NAME));
            double balance = cursor.getDouble(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_BALANCE));
            Account account = new Account(NoOfAccount, bankName, accountHolderName, balance);
            return account;
        }else {
            throw new InvalidAccountException("Invalid account");
        }


    }
    @Override
    public void addAccount(Account account) {
        //Add a account to DB
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.COLUMN_ACCOUNT_NO,account.getAccountNo());
        contentValues.put(SQLiteHelper.COLUMN_ACCOUNT_HOLDER_NAME,account.getAccountHolderName());
        contentValues.put(SQLiteHelper.COLUMN_BANK_NAME,account.getBankName());
        contentValues.put(SQLiteHelper.COLUMN_BALANCE,account.getBalance());
        db.insert(SQLiteHelper.ACCOUNT_TABLE,null,contentValues);
    }

    @Override
    public void removeAccount(String noOfAccount) throws InvalidAccountException {
        //Remove an account from DB
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        String [] arguments = {noOfAccount};
        db.delete(SQLiteHelper.ACCOUNT_TABLE, "accountNo=?",arguments);
    }

    @Override
    //Not finished
    public void updateBalance(String noOfAccount, ExpenseType expenseType, double amount) throws InvalidAccountException {
        //update balanace of a given account in DB
        Account account = getAccount(noOfAccount);
        SQLiteDatabase  db = sqLiteHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        switch (expenseType){
            case INCOME:
                account.setBalance(account.getBalance() + amount);
                break;
            case EXPENSE:
                account.setBalance(account.getBalance() - amount);
                break;
            default:
                break;
        }
        contentValues.put(SQLiteHelper.COLUMN_BALANCE,  account.getBalance());
        db.update(SQLiteHelper.ACCOUNT_TABLE,contentValues," accountNo=? ",new String[] {noOfAccount});
    }
}
