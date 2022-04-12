package com.example.db;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnAdd, btnClear;
    EditText instr, type_of_tool, price;

    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        instr = (EditText) findViewById(R.id.instr);
        type_of_tool = (EditText) findViewById(R.id.type_of_tool);
        price = (EditText) findViewById(R.id.price);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        UpdateTable();

        instr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    instr.setHint("");
                else
                    instr.setHint("Название инструмента");
            }
        });
        type_of_tool.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    type_of_tool.setHint("");
                else
                    type_of_tool.setHint("Вид инструмента");
            }
        });
        price.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    price.setHint("");
                else
                    price.setHint("Цена товара");
            }
        });
    }
    public void UpdateTable(){
        Cursor cursor = database.query(DBHelper.TABLE_INFORMATIONONBUYS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nazvinstrumenta = cursor.getColumnIndex(DBHelper.KEY_INSTR);
            int vidinstrumenta = cursor.getColumnIndex(DBHelper.KEY_VID);
            int pricecheck = cursor.getColumnIndex(DBHelper.KEY_CENA);
            TableLayout dbOutput = findViewById(R.id.db);
            dbOutput.removeAllViews();
            do {
                TableRow dbOutputRow = new TableRow(this);
                dbOutputRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

                TextView outputID = new TextView(this);
                params.weight = 1.0f;
                outputID.setLayoutParams(params);
                outputID.setText(cursor.getString(idIndex));
                outputID.setTextSize(12);
                dbOutputRow.addView(outputID);

                TextView outputNazv = new TextView(this);
                params.weight = 3.0f;
                outputNazv.setLayoutParams(params);
                outputNazv.setText(cursor.getString(nazvinstrumenta));
                outputNazv.setTextSize(12);
                dbOutputRow.addView(outputNazv);

                TextView outputVid = new TextView(this);
                params.weight = 3.0f;
                outputVid.setLayoutParams(params);
                outputVid.setText(cursor.getString(vidinstrumenta));
                outputVid.setTextSize(12);
                dbOutputRow.addView(outputVid);

                TextView outputPrice = new TextView(this);
                params.weight = 3.0f;
                outputPrice.setLayoutParams(params);
                outputPrice.setText(cursor.getString(pricecheck));
                outputPrice.setTextSize(12);
                dbOutputRow.addView(outputPrice);

                Button deleteBtn = new Button(this);
                deleteBtn.setOnClickListener(this);
                params.weight = 1.0f;
                deleteBtn.setLayoutParams(params);
                deleteBtn.setText("Удалить запись");
                deleteBtn.setTextSize(12);
                deleteBtn.setId(cursor.getInt(idIndex));
                dbOutputRow.addView(deleteBtn);

                dbOutput.addView(dbOutputRow);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                String in = instr.getText().toString();
                String vid = type_of_tool.getText().toString();
                String buy = price.getText().toString();
                contentValues = new ContentValues();

                contentValues.put(DBHelper.KEY_INSTR, in);
                contentValues.put(DBHelper.KEY_VID, vid);
                contentValues.put(DBHelper.KEY_CENA, buy);

                database.insert(DBHelper.TABLE_INFORMATIONONBUYS, null, contentValues);
                instr.setText("");
                type_of_tool.setText("");
                price.setText("");
                UpdateTable();
                break;
            case R.id.btnClear:
                database.delete(DBHelper.TABLE_INFORMATIONONBUYS, null, null);
                TableLayout dbOutput = findViewById(R.id.db);
                dbOutput.removeAllViews();
                UpdateTable();
                break;
            default:
                View outputDBRow = (View) v.getParent();
                ViewGroup outputDB = (ViewGroup) outputDBRow.getParent();
                outputDB.removeView(outputDBRow);
                outputDB.invalidate();

                database.delete(DBHelper.TABLE_INFORMATIONONBUYS, DBHelper.KEY_ID + " = ?", new String[]{String.valueOf(v.getId())});

                contentValues = new ContentValues();
                Cursor cursorUpdater = database.query(DBHelper.TABLE_INFORMATIONONBUYS, null, null, null, null, null, null);
                if (cursorUpdater.moveToFirst()) {
                    int idIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_ID);
                    int nazvindex = cursorUpdater.getColumnIndex(DBHelper.KEY_INSTR);
                    int vidindex = cursorUpdater.getColumnIndex(DBHelper.KEY_VID);
                    int indexcena = cursorUpdater.getColumnIndex(DBHelper.KEY_CENA);
                    int realID = 1;
                    do{
                        if(cursorUpdater.getInt(idIndex) > idIndex){
                            contentValues.put(DBHelper.KEY_ID, realID);
                            contentValues.put(DBHelper.KEY_INSTR, cursorUpdater.getString(nazvindex));
                            contentValues.put(DBHelper.KEY_VID, cursorUpdater.getString(vidindex));
                            contentValues.put(DBHelper.KEY_CENA, cursorUpdater.getString(indexcena));
                            database.replace(DBHelper.TABLE_INFORMATIONONBUYS, null, contentValues);
                        }
                        realID++;
                    } while(cursorUpdater.moveToNext());
                    if(cursorUpdater.moveToLast() && (cursorUpdater.getInt(idIndex) == realID)){
                        database.delete(DBHelper.TABLE_INFORMATIONONBUYS, DBHelper.KEY_ID + " = ?", new String[]{cursorUpdater.getString(idIndex)});
                    }
                    UpdateTable();
                }
                break;
        }
    }

}