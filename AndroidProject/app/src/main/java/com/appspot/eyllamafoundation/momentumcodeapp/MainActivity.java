package com.appspot.eyllamafoundation.momentumcodeapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String code;
    private String[] codeList;
    private String[] ck;
    private String currentStatement;
    private ArrayList<Text> texts;
    private ArrayList<Letter> letters;
    private ArrayList<Cond> conds;
    private ArrayList<Number> numbers;
    private TextView console;
    private EditText editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        numbers = new ArrayList<Number>();
        texts = new ArrayList<Text>();
        conds = new ArrayList<Cond>();
        letters = new ArrayList<Letter>();

        console = findViewById(R.id.console);
        editor = findViewById(R.id.editor);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
                codeList = ("" + editor.getText()).split(":\\)");

                for (int i = 0; i < codeList.length; i++) {
                    currentStatement = codeList[i].replace("\r", "");
                    currentStatement = currentStatement.replace("\n", "");

                    ck = currentStatement.split(" ");

                    String tag = ck[0];

                    if (tag.equals("Container")) {
                        processContainer(0);
                    } else if (tag.equals("Print")) {
                        processPrint(0);
                    } else if (tag.equals("Change")) {
                        processChange();
                    } else if (tag.equals("If")) {
                        processIf();
                    }
                }
            }
        }); //EYLAM IS AN IDDIOT
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    public ArrayList<Text> getText() {
//        return texts;
//    }
//
//    public ArrayList<Letter> getLetters() {
//        return letters;
//    }
//
//    public ArrayList<Cond> getConds() {
//        return conds;
//    }
//
//    public ArrayList<Number> getNumbers() {
//        return numbers;
//    }

    public boolean getNumberCondition(String cond) {
        double d1 = 0, d2 = 0;
        String op = "";
        int opNum = 0;
        if (cond.contains(">")) {
            op = ">";
            opNum = 1;
            System.out.println(1);
        } else if (cond.contains("<")) {
            op = "<";
            opNum = 2;
            System.out.println(2);
        } else if (cond.contains(">=")) {
            op = ">=";
            opNum = 3;
            System.out.println(3);
        } else if (cond.contains("<=")) {
            op = "<=";
            opNum = 4;
            System.out.println(4);
        } else if (cond.contains("==")) {
            op = "==";
            opNum = 5;
            System.out.println(5);
        } else if (cond.contains("!=")) {
            op = "!=";
            opNum = 6;
            System.out.println(6);
        }

        String s1 = cond.substring(0, cond.indexOf(op)).trim(),
                s2 = cond.substring(cond.indexOf(op) + op.length()).trim();
        System.out.println("s1 = " + s1);
        System.out.println("s2 = " + s2);
        boolean s1hardCoded = true, s2hardCoded = true;

        for (int i = 0; i < s1.length(); i++) {
            if (!Character.isDigit(s1.charAt(i)) && s1.charAt(i) != '.')
                s1hardCoded = false;
        }

        System.out.println("s1hc" + s1hardCoded);

        for (int i = 0; i < s2.length(); i++) {
            if (!Character.isDigit(s2.charAt(i)) && s2.charAt(i) != '.') {
                System.out.println("bjafobsnfobsanfobjfsa " + s2.charAt(i));
                s2hardCoded = false;
            }
        }

        System.out.println("s2hc" + s2hardCoded);

        if (s1hardCoded) {
            d1 = Double.parseDouble(s1);
            System.out.println("hd1 = " + d1);
        } else {
            for (int i = 0; i < numbers.size(); i++) {
                if (s1.contains(numbers.get(i).getName()))
                    d1 = numbers.get(i).getValue();
            }
        }

        if (s2hardCoded) {
            d2 = Double.parseDouble(s2);
            System.out.println("hd2 = " + d2);
        } else {
            for (int i = 0; i < numbers.size(); i++) {
                if (s2.contains(numbers.get(i).getName()))
                    d2 = numbers.get(i).getValue();
            }
        }

        System.out.println("d1 = " + d1);
        System.out.println("d2 = " + d2);

        switch (opNum) {
            case 1:
                System.out.println(d1 > d2);
                return d1 > d2;
            case 2:
                System.out.println(d1 < d2);
                return d1 < d2;
            case 3:
                System.out.println(d1 >= d2);
                return d1 >= d2;
            case 4:
                System.out.println(d1 <= d2);
                return d1 <= d2;
            case 5:
                System.out.println(Math.abs(d1 - d2) < 0.00001);
                return Math.abs(d1 - d2) < 0.00001;
            case 6:
                System.out.println(d1 != d2);
                return d1 != d2;
            default:
                throw new IllegalArgumentException("Invalid operation");
        }
    }

    public void changeNumber(String change) {
        double d = Double.parseDouble(change.substring(change.indexOf("=") + 1, change.length() - 1).trim());
        if (change.contains("+=")) {
            String name = change.substring(0, change.indexOf("+")).trim();
            for (int i = 0; i < numbers.size(); i++) {
                if (numbers.get(i).getName().trim().equals(name)) {
                    numbers.get(i).add(d);
                }
            }
        } else if (change.contains("-=")) {
            String name = change.substring(0, change.indexOf("-")).trim();
            for (int i = 0; i < numbers.size(); i++) {
                if (numbers.get(i).getName().trim().equals(name)) {
                    numbers.get(i).subtract(d);
                }
            }
        } else if (change.contains("*=")) {
            String name = change.substring(7, change.indexOf("*")).trim();
            for (int i = 0; i < numbers.size(); i++) {
                if (numbers.get(i).getName().trim().equals(name)) {
                    numbers.get(i).multiplyBy(d);
                }
            }
        } else if (change.contains("/=")) {
            String name = change.substring(0, change.indexOf("/")).trim();
            for (int i = 0; i < numbers.size(); i++) {
                if (numbers.get(i).getName().trim().equals(name)) {
                    numbers.get(i).divideBy(d);
                }
            }
        } else if (change.contains("=")) {
            String name = change.substring(0, change.indexOf("=")).trim();
            for (int i = 0; i < numbers.size(); i++) {
                if (numbers.get(i).getName().trim().equals(name)) {
                    numbers.get(i).setValue(d);
                }
            }
        }
    }

    public void changeCond(String change) {
        boolean b = false;
        String name = change.substring(0, change.indexOf("=")).trim();
        for (int i = 0; i < conds.size(); i++) {
            if (conds.get(i).getName().trim().equals(name)) {
                conds.get(i).setCond(b);
            }
        }
    }

    public void changeText(String change) {
        System.out.println(change);
        String s = change.substring(change.indexOf("\""), change.lastIndexOf("\"")).trim();
        if (change.contains("+=")) {
            String name = change.substring(8, change.indexOf("+")).trim();
            for (int i = 0; i < texts.size(); i++) {
                if (texts.get(i).getName().trim().equals(name)) {
                    texts.get(i).addText(s);
                }
            }
        } else if (change.contains("=")) {
            String name = change.substring(8, change.indexOf("=")).trim();
            for (int i = 0; i < texts.size(); i++) {
                if (texts.get(i).getName().trim().equals(name)) {
                    texts.get(i).setText(s.substring(1));
                }
            }
        }
    }

    public void changeLetter(String change) {
        char c = change.charAt(change.lastIndexOf("\'") - 1);
        String name = change.substring(0, change.indexOf("=")).trim();
        for (int i = 0; i < letters.size(); i++) {
            if (letters.get(i).getName().trim().equals(name)) {
                letters.get(i).setLetter(c);
            }
        }
    }

    public void processContainer(int index) {
        String dataType = ck[1 + index];
        if (isNewVariable(ck[2 + index])) {
            if (dataType.equals("number")) {
                if (!ck[4 + index].contains("."))
                    ck[4 + index] = ck[4 + index] + ".0";
                numbers.add(new Number(ck[2 + index], Double.parseDouble(ck[4 + index])));
            } else if (dataType.equals("text")) {
                String txt = ck[4 + index];
                for (int j = 5; j < ck.length; j++)
                    txt += " " + ck[j + index];
                texts.add(new Text(ck[2 + index], txt));

            } else if (dataType.equals("cond")) {
                if (ck[4 + index].equals("true"))
                    conds.add(new Cond(ck[2 + index], true));
                else
                    conds.add(new Cond(ck[2 + index], false));
            }
            if (dataType.equals("letter"))
                letters.add(new Letter(ck[2 + index], ck[4 + index].charAt(0)));
        }
    }

    public void processPrint(int index) {
        boolean isQuote = false;
        for (int j = 1; j < ck.length; j++) {
            if (isQuote) {
                if (ck[j = index].charAt(ck[j + index].length() - 1) == '"') {
                    isQuote = !isQuote;
                    console.append(ck[j + index].substring(0, ck[j + index].length() - 1) + " ");
                } else
                    console.append(ck[j + index] + " ");
            } else {

                if (ck[j + index].charAt(0) == '"') {
                    isQuote = !isQuote;
                    if (ck[j + index].charAt(ck[j + index].length() - 1) == '"') {
                        console.append(ck[j + index].substring(1, ck[j + index].length() - 1) + " ");
                        isQuote = !isQuote;
                    } else
                        console.append(ck[j + index].substring(1) + " ");
                } else
                    printVariable(ck[j + index]);
            }
        }

        console.append("\n");
    }

    public void processIf() {
        System.out.println(ck[1] + " " + ck[2] + " " + ck[3]);
        System.out.println(getNumberCondition(ck[1] + " " + ck[2] + " " + ck[3]));
        if (getNumberCondition(ck[1] + " " + ck[2] + " " + ck[3])) {
            String statementTag = ck[4];
            if (statementTag.equals("Change"))
                processChange();
            else if (ck[4].equals("Print"))
                processPrint(4);
            else if (ck[4].equals("Container"))
                processContainer(4);
        }
    }

    public void processChange() {
        String dataType = "";
        for (int b = 0; b < numbers.size(); b++) {
            if (numbers.get(b).getName().trim().equals(ck[1])) {
                dataType = "number";
                changeNumber(currentStatement);
            }
        }
        for (int y = 0; y < conds.size(); y++) {
            if (conds.get(y).getName().trim().equals(ck[1])) {
                dataType = "cond";
                changeCond(currentStatement);
            }
        }
        for (int x = 0; x < texts.size(); x++) {
            if (texts.get(x).getName().trim().equals(ck[1])) {
                dataType = "text";
                changeText(currentStatement);
            }
        }
        for (int e = 0; e < letters.size(); e++) {
            if (letters.get(e).getName().trim().equals(ck[1])) {
                dataType = "letter";
                changeLetter(currentStatement);
            }
        }
    }

    private boolean isNewVariable(String name) {
        boolean newVar = true;
        for (int i = 0; i < conds.size(); i++) {
            if (name.equals(conds.get(i).getName())) {
                newVar = false;
            }
        }
        for (int i = 0; i < texts.size(); i++) {
            if (name.equals(texts.get(i).getName())) {
                newVar = false;
            }

        }
        for (int i = 0; i < numbers.size(); i++) {
            if (name.equals(numbers.get(i).getName())) {
                newVar = false;
            }
        }
        for (int i = 0; i < letters.size(); i++) {
            if (name.equals(letters.get(i).getName())) {
                newVar = false;
            }
        }

        if (!newVar) {
            console.append("The variable name " + name + " is already being used \n");

        }
        return newVar;
    }

    private void printVariable(String name) {
        boolean printed = false;
        for (int i = 0; i < conds.size(); i++) {

            if (name.equals(conds.get(i).getName())) {
                console.append(conds.get(i).getCond() + " ");
                printed = true;
            }
        }
        if (!printed) {
            for (int i = 0; i < texts.size(); i++) {
                if (name.equals(texts.get(i).getName())) {
                    console.append(texts.get(i).getText().substring(1, texts.get(i).getText().length() - 1) + " ");
                    printed = true;
                }
            }
        }
        if (!printed) {
            for (int i = 0; i < numbers.size(); i++) {
                if (name.equals(numbers.get(i).getName())) {
                    console.append(numbers.get(i).getValue() + " ");
                    printed = true;
                }
            }
        }
        if (!printed) {
            for (int i = 0; i < letters.size(); i++) {
                if (name.equals(letters.get(i).getName())) {
                    console.append(letters.get(i).getLetter() + " ");

                    printed = true;
                }
            }
        }
    }

    private void reset() {
        conds.clear();
        texts.clear();
        numbers.clear();
        letters.clear();
        console.setText("");
    }
}
