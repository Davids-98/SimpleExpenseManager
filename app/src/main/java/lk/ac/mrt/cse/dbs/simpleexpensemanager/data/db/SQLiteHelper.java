package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String ACCOUNT_TABLE = "account";
    public static final String COLUMN_ACCOUNT_NO = "accountNo";
    public static final String COLUMN_BANK_NAME = "bankName";
    public static final String COLUMN_ACCOUNT_HOLDER_NAME = "accountHolderName";
    public static final String COLUMN_BALANCE = "balance";
    public static final String TRANSACTION_TABLE = "Tranaction";
    public static final String COLUMN_TRANSACTION_ID = "ID";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_RELAVANT_ACCOUNT_NO = "relavantAccountNo";
    public static final String COLUMN_EXPENSE_TYPE = "ExpenseType";
    public static final String COLUMN_AMOUNT = "AMOUNT";

    private static SQLiteHelper instance;

    public static SQLiteHelper getInstance(Context context){
        if(instance == null){
            instance = new SQLiteHelper(context);
        }
        return instance;
    }


    public SQLiteHelper(@Nullable Context context) {
        super(context, "190545H.db" , null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createAccountTableStatement = "CREATE TABLE IF NOT EXISTS " + ACCOUNT_TABLE + " (" + COLUMN_ACCOUNT_NO + " VARCHAR PRIMARY KEY," + COLUMN_BANK_NAME + " TEXT," + COLUMN_ACCOUNT_HOLDER_NAME + " TEXT," + COLUMN_BALANCE + " double)";
        String createTransactionTableStatement = "CREATE TABLE IF NOT EXISTS " + TRANSACTION_TABLE + " (" + COLUMN_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_DATE + " DATE," + COLUMN_RELAVANT_ACCOUNT_NO + " INTEGER," + COLUMN_EXPENSE_TYPE + " TEXT," + COLUMN_AMOUNT + " double, FOREIGN KEY (ID) REFERENCES account(accountNo))";

        sqLiteDatabase.execSQL(createAccountTableStatement);
        sqLiteDatabase.execSQL(createTransactionTableStatement);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
