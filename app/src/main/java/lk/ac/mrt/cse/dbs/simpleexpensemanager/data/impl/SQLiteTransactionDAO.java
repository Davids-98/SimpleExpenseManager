package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.SQLiteHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class SQLiteTransactionDAO implements TransactionDAO {
    private SQLiteHelper sqLiteHelper;

    public SQLiteTransactionDAO(Context context) {
        sqLiteHelper = SQLiteHelper.getInstance(context);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        //Insert a new transation to DB
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");

        contentValues.put(SQLiteHelper.COLUMN_DATE, d.format(date));
        contentValues.put(SQLiteHelper.COLUMN_RELAVANT_ACCOUNT_NO, String.valueOf(accountNo));
        contentValues.put(SQLiteHelper.COLUMN_EXPENSE_TYPE, String.valueOf(expenseType));
        contentValues.put(SQLiteHelper.COLUMN_AMOUNT, String.valueOf(amount));
        db.insert(SQLiteHelper.TRANSACTION_TABLE, null, contentValues);

    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        //Get all transactions from DB
        List<Transaction> transactionLogs = new LinkedList<>();
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();

        String queryString = "SELECT * FROM " + SQLiteHelper.TRANSACTION_TABLE;
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            do {
                String accountNo = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_RELAVANT_ACCOUNT_NO));

                String dateStr = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_DATE));
                SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = d.parse(dateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                ExpenseType expenceType = ExpenseType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_EXPENSE_TYPE)));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_AMOUNT));

                Transaction transaction = new Transaction(date, accountNo, expenceType, amount);
                transactionLogs.add(transaction);
            } while (cursor.moveToNext());
        } else {

        }
        return transactionLogs;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        //Get all transactions from DB with LIMIT
        List<Transaction> PtransactionLogs = new LinkedList<>();
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        try {
            String queryString = "SELECT * FROM " + SQLiteHelper.TRANSACTION_TABLE + " LIMIT " + limit;
            Cursor cursor = db.rawQuery(queryString, null);
            SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
            if (cursor.moveToFirst()) {
                do {
                    String accountNo = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_RELAVANT_ACCOUNT_NO));
                    String dateStr = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_DATE));
                    ExpenseType expenceType = ExpenseType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_EXPENSE_TYPE)));
                    double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_AMOUNT));

                    Transaction transaction = new Transaction(d.parse(dateStr), accountNo, expenceType, amount);
                    PtransactionLogs.add(transaction);
                } while (cursor.moveToNext());
            } else {

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return PtransactionLogs;


    }
}


