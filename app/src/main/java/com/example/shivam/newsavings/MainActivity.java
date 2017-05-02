package com.example.shivam.newsavings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static SavingDBHelper dbHelper;
    TextView amtTextView;
    Button creditButton, debitButton;
    EditText amtEditText;
    static Context cx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cx = this;
        dbHelper = new SavingDBHelper(cx);
        creditButton = (Button)findViewById(R.id.creditButton);
        debitButton = (Button)findViewById(R.id.debitButton);
        amtTextView = (TextView)findViewById(R.id.amtTextView);
        amtEditText = (EditText)findViewById(R.id.amtEditText);
        int amt = dbHelper.getTotalBalance();
        if(amt != 0){
            amtTextView.setText(String.valueOf(amt));
        }

        amtTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DetailedEntryActivity.class);
                startActivity(intent);
                return true;
            }
        });

        creditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String entryAmt = amtEditText.getText().toString();
                if(entryAmt.length() == 0){
                    Toast.makeText(cx,"Enter a value first",Toast.LENGTH_SHORT).show();
                }
                else {
                    String amt = amtTextView.getText().toString();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(amtEditText.getWindowToken(), 0);
                    int a = Integer.parseInt(amt);
                    int b = Integer.parseInt(entryAmt);
                    if(dbHelper.insertEntry(b,"cr")){
                        Toast.makeText(cx,"Entry Added Successfully",Toast.LENGTH_SHORT).show();
                        amtTextView.setText(String.valueOf(a+b));
                        amtEditText.setText("");
                    }
                    else {
                        Toast.makeText(cx,"Entry Failed!! Try Again",Toast.LENGTH_SHORT).show();
                        amtEditText.setFocusable(true);
                        amtEditText.setFocusableInTouchMode(true);
                    }
                }
            }
        });

        debitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String entryAmt = amtEditText.getText().toString();
                if(entryAmt.length() == 0){
                    Toast.makeText(cx,"Enter a value first",Toast.LENGTH_SHORT).show();
                }
                else {
                    String amt = amtTextView.getText().toString();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(amtEditText.getWindowToken(), 0);
                    int a = Integer.parseInt(amt);
                    int b = Integer.parseInt(entryAmt);
                    if(dbHelper.insertEntry(b,"dbt")){
                        Toast.makeText(cx,"Entry Added Successfully",Toast.LENGTH_SHORT).show();
                        amtTextView.setText(String.valueOf(a-b));
                        amtEditText.setText("");
                    }
                    else {
                        Toast.makeText(cx,"Entry Failed!! Try Again",Toast.LENGTH_SHORT).show();
                        amtEditText.setFocusable(true);
                        amtEditText.setFocusableInTouchMode(true);
                    }
                }
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        int amt = dbHelper.getTotalBalance();
        amtTextView = (TextView)findViewById(R.id.amtTextView);
        if(amt != 0){
            amtTextView.setText(String.valueOf(amt));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.resetMenu:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Delete all Entries??").
                        setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int a = dbHelper.deleteAllEntry();
                                amtTextView.setText("00000");
                                if(a == 0)
                                    Toast.makeText(cx,"There is no data to Reset",Toast.LENGTH_SHORT).show();
                            }
                        }).
                        setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(),"I knew you can't do it.", Toast.LENGTH_SHORT).show();
                        }
                });
                AlertDialog d = builder.create();
                d.setTitle("Reset All Entries");
                d.show();
                return true;

            case R.id.aboutMenu:
                Toast.makeText(cx,"Shivam Saxena 8130804357",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
