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
                codeList = editor.getText().toString().split("\n");

                for (int i = 0; i < codeList.length; i++) {
                    currentStatement = codeList[i].replace("\r", "");
                    currentStatement = currentStatement.replace("\n", "");

                    ck = currentStatement.split(" ");

                    String tag = ck[0];

                    if (tag.equals("Number")) {
                        processNumber(0);
                    } else if (tag.equals("Text")) {
                        processText(0);
                    } else if (tag.equals("Letter")) {
                        processLetter(0);
                    } else if (tag.equals("Cond")) {
                        processCond(0);
                    } else if (tag.equals("Print")) {
                        processPrint(0);
                    } else if (tag.equals("Change")) {
                        processChange(0);
                    } else if (tag.equals("If")) {
                        processIf();
                    } else if (tag.equals("Loop")) {
                        processLoop(i + 1);
                    } /*else if (tag.equals("Input")) {
                        processInput(0);
                    }*/
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
        if (cond.contains(">=")) {
            op = ">=";
            opNum = 3;
        } else if (cond.contains("<=")) {
            op = "<=";
            opNum = 4;
        } else if (cond.contains(">")) {
            op = ">";
            opNum = 1;
        } else if (cond.contains("<")) {
            op = "<";
            opNum = 2;
        } else if (cond.contains("==")) {
            op = "==";
            opNum = 5;
        } else if (cond.contains("!=")) {
            op = "!=";
            opNum = 6;
        }

        String s1 = cond.substring(0, cond.indexOf(op)).trim(),
                s2 = cond.substring(cond.indexOf(op) + op.length()).trim();
        boolean s1hardCoded = true, s2hardCoded = true;

        for (int i = 0; i < s1.length(); i++) {
            if (!Character.isDigit(s1.charAt(i)) && s1.charAt(i) != '.')
                s1hardCoded = false;
        }

        for (int i = 0; i < s2.length(); i++) {
            if (!Character.isDigit(s2.charAt(i)) && s2.charAt(i) != '.') {
                s2hardCoded = false;
            }
        }

        if (s1hardCoded) {
            d1 = Double.parseDouble(s1);
        } else {
            for (int i = 0; i < numbers.size(); i++) {
                if (s1.contains(numbers.get(i).getName()))
                    d1 = numbers.get(i).getValue();
            }
        }

        if (s2hardCoded) {
            d2 = Double.parseDouble(s2);
        } else {
            for (int i = 0; i < numbers.size(); i++) {
                if (s2.contains(numbers.get(i).getName()))
                    d2 = numbers.get(i).getValue();
            }
        }

        switch (opNum) {
            case 1:
                return d1 > d2;
            case 2:
                return d1 < d2;
            case 3:
                return d1 >= d2;
            case 4:
                return d1 <= d2;
            case 5:
                return Math.abs(d1 - d2) < 0.00001;
            case 6:
                return d1 != d2;
            default:
                throw new IllegalArgumentException("Invalid operation");
        }
    }

    public void changeNumber(int index) {
        if (ck[2 + index].trim().equals("=")) {
            for (int i = 0; i < numbers.size(); i++) {
                if (numbers.get(i).getName().trim().equals(ck[1 + index]))
                    numbers.get(i).setValue(Double.parseDouble(ck[3 + index]));
            }
        } else if (ck[2 + index].trim().equals("+=")) {
            for (int i = 0; i < numbers.size(); i++) {
                if (numbers.get(i).getName().trim().equals(ck[1 + index]))
                    numbers.get(i).add(Double.parseDouble(ck[3 + index]));
            }
        } else if (ck[2 + index].trim().equals("-=")) {
            for (int i = 0; i < numbers.size(); i++) {
                if (numbers.get(i).getName().trim().equals(ck[1 + index]))
                    numbers.get(i).subtract(Double.parseDouble(ck[3 + index]));
            }
        } else if (ck[2 + index].trim().equals("*=")) {
            for (int i = 0; i < numbers.size(); i++) {
                if (numbers.get(i).getName().trim().equals(ck[1 + index]))
                    numbers.get(i).multiplyBy(Double.parseDouble(ck[3 + index]));
            }
        } else if (ck[2 + index].trim().equals("/=")) {
            for (int i = 0; i < numbers.size(); i++) {
                if (numbers.get(i).getName().trim().equals(ck[1 + index]))
                    numbers.get(i).divideBy(Double.parseDouble(ck[3 + index]));
            }
        }
    }

    public void changeCond(int index) {
        if (ck[3 + index].equals("true")) {
            for (int i = 0; i < conds.size(); i++) {
                if (conds.get(i).getName().trim().equals(ck[1 + index]))
                    conds.get(i).setCond(true);
            }
        } else if (ck[3 + index].equals("false")) {
            for (int i = 0; i < conds.size(); i++) {
                if (conds.get(i).getName().trim().equals(ck[1 + index]))
                    conds.get(i).setCond(false);
            }
        }
    }

    public void changeText(int ind) {
        String s = "";
        boolean isQuote = false;
        int index = 0;
        for (int i = 0; i < texts.size(); i++) {
            if (texts.get(i).getName().trim().equals(ck[1 + ind])) {
                index = i;
                if (ck[2 + ind].equals("=")) {
                    s = "";
                } else if (ck[2 + ind].equals("+=")) {
                    s = texts.get(i).getText();
                }
                isQuote = true;
            }
        }

        if (isQuote) {
            isQuote = false;
            for (int i = 3 + ind; i < ck.length; i++) {
                if (isQuote) {
                    if (ck[i].equals("")) {
                        s += (" ");
                    } else if (ck[i].charAt(ck[i].length() - 1) == '"') {
                        isQuote = !isQuote;
                        s += (ck[i].substring(0, ck[i].length() - 1) + "");
                    } else
                        s += (ck[i] + " ");
                } else {

                    if (!ck[i].equals("") && ck[i].charAt(0) == '"') {
                        isQuote = !isQuote;
                        if (ck[i].charAt(ck[i].length() - 1) == '"' && ck[i].length() != 1) {
                            s += (ck[i].substring(1, ck[i].length() - 1) + "");
                            isQuote = !isQuote;
                        } else
                            s += (ck[i].substring(1) + " ");
                    } else
                        s += getVariable(ck[i]);
                }

            }
            texts.get(index).setText(s);
        }
    }

    public void changeLetter(int index) {
        for (int i = 0; i < letters.size(); i++) {
            if (letters.get(i).getName().trim().equals(ck[1 + index]))
                letters.get(i).setLetter(ck[3 + index].charAt(1));
        }
    }

    public void processNumber(int index) {
        if (isNewVariable(ck[1 + index])) {
            if (!ck[3 + index].contains("."))
                ck[3 + index] += ".0";
            numbers.add(new Number(ck[1 + index], Double.parseDouble(ck[3 + index])));
        }
    }

    public void processText(int index) {
        if (isNewVariable(ck[1 + index])) {
            String txt = "";
            boolean isQuote = false;

            for (int i = 3 + index; i < ck.length; i++) {
                if (isQuote) {
                    if (ck[i].equals("")) {
                        txt += (" ");
                    } else if (ck[i].charAt(ck[i].length() - 1) == '"') {
                        isQuote = !isQuote;
                        txt += (ck[i].substring(0, ck[i].length() - 1) + "");
                    } else
                        txt += (ck[i] + " ");
                } else {

                    if (!ck[i].equals("") && ck[i].charAt(0) == '"') {
                        isQuote = !isQuote;
                        if (ck[i].charAt(ck[i].length() - 1) == '"' && ck[i].length() != 1) {
                            txt += (ck[i].substring(1, ck[i].length() - 1) + "");
                            isQuote = !isQuote;
                        } else
                            txt += (ck[i].substring(1) + " ");
                    } else
                        txt += getVariable(ck[i]);
                }

            }
            texts.add(new Text(ck[1 + index], txt));

        }
    }

    private String getVariable(String name) {
        for (int i = 0; i < conds.size(); i++) {

            if (name.equals(conds.get(i).getName())) {
                return conds.get(i).getCond() + "";
            }
        }
        for (int i = 0; i < texts.size(); i++) {
            if (name.equals(texts.get(i).getName())) {
                return texts.get(i).getText().substring(1, texts.get(i).getText().length() - 1) + "";
            }
        }

        for (int i = 0; i < numbers.size(); i++) {
            if (name.equals(numbers.get(i).getName())) {
                return (numbers.get(i).getValue() + "");
            }
        }

        for (int i = 0; i < letters.size(); i++) {
            if (name.equals(letters.get(i).getName())) {
                return (letters.get(i).getLetter() + "");

            }
        }
        if (console != null)
            console.append("\n");
        console.append(currentStatement + " : The variable name " + name + " does not exist\n\n");

        return "";
    }

    public void processCond(int index) {
        if (isNewVariable(ck[1 + index])) {
            if (ck[3 + index].equals("true"))
                conds.add(new Cond(ck[1 + index], true));
            else
                conds.add(new Cond(ck[1 + index], false));
        }
    }

    public void processLetter(int index) {
        if (isNewVariable(ck[1 + index])) {
            letters.add(new Letter(ck[1 + index], ck[3 + index].charAt(1)));
        }
    }

    public void processPrint(int index) {
        boolean isQuote = false;
        for (int j = 1 + index; j < ck.length; j++) {
            if (isQuote) {
                if (ck[j].equals("")) {
                    console.append(" ");
                } else if (ck[j].charAt(ck[j].length() - 1) == '"') {
                    isQuote = !isQuote;
                    console.append(ck[j].substring(0, ck[j].length() - 1) + "");
                } else
                    console.append(ck[j] + " ");
            } else {
                if (!ck[j].equals("") && ck[j].charAt(0) == '"') {
                    isQuote = !isQuote;
                    if (ck[j].charAt(ck[j].length() - 1) == '"' && ck[j].length() != 1) {
                        console.append(ck[j].substring(1, ck[j].length() - 1) + "");
                        isQuote = !isQuote;
                    } else
                        console.append(ck[j].substring(1) + " ");
                } else {
                    printVariable(ck[j]);
                }
            }
        }

        console.append("\n");
    }

    public void processIf() {

        boolean newVar = true, cond = false;
        for (int i = 0; i < conds.size(); i++) {
            if (ck[1].trim().equals(conds.get(i).getName())) {
                newVar = false;
                cond = conds.get(i).getCond();
            }
        }

        if (!newVar) {
            if (cond) {
                String statementTag = ck[2];
                if (statementTag.equals("Change"))
                    processChange(2);
                else if (ck[2].equals("Print"))
                    processPrint(2);
//                else if (ck[2].equals("Input"))
//                    processInput(2);
                else if (ck[2].equals("Number"))
                    processNumber(2);
                else if (ck[2].equals("Text"))
                    processText(2);
                else if (ck[2].equals("Cond"))
                    processCond(2);
                else if (ck[2].equals("Letter"))
                    processLetter(2);
            }
        } else if (getNumberCondition(ck[1] + " " + ck[2] + " " + ck[3])) {
            String statementTag = ck[4];
            if (statementTag.equals("Change"))
                processChange(4);
            else if (ck[4].equals("Print"))
                processPrint(4);
//            else if (ck[4].equals("Input"))
//                processInput(4);
            else if (ck[4].equals("Number"))
                processNumber(4);
            else if (ck[4].equals("Text"))
                processText(4);
            else if (ck[4].equals("Cond"))
                processCond(4);
            else if (ck[4].equals("Letter"))
                processLetter(4);
        }
    }

    public void processChange(int index) {
        String dataType = "";
        for (int b = 0; b < numbers.size(); b++) {
            if (numbers.get(b).getName().trim().equals(ck[1 + index])) {
                dataType = "number";
                changeNumber(index);
            }
        }
        for (int y = 0; y < conds.size(); y++) {
            if (conds.get(y).getName().trim().equals(ck[1 + index])) {
                dataType = "cond";
                changeCond(index);
            }
        }
        for (int x = 0; x < texts.size(); x++) {
            if (texts.get(x).getName().trim().equals(ck[1 + index])) {
                dataType = "text";
                changeText(index);
            }
        }
        for (int e = 0; e < letters.size(); e++) {
            if (letters.get(e).getName().trim().equals(ck[1 + index])) {
                dataType = "letter";
                changeLetter(index);
            }
        }
    }

    public void processLoop(int j) {
        int times = Integer.parseInt(ck[1]);
        int original = j;
        for (int i = 1; i < times; i++) {
            while (!currentStatement.equals("End")) {
                currentStatement = codeList[j].replace("\r", "");
                currentStatement = currentStatement.replace("\n", "");
                ck = currentStatement.split(" ");

                String tag = ck[0];

                if (tag.equals("Number")) {
                    processNumber(0);
                } else if (tag.equals("Text")) {
                    processText(0);
                } else if (tag.equals("Letter")) {
                    processLetter(0);
                } else if (tag.equals("Cond")) {
                    processCond(0);
                } else if (tag.equals("Print")) {
                    processPrint(0);
                } else if (tag.equals("Change")) {
                    processChange(0);
                } else if (tag.equals("If")) {
                    processIf();
                } /*else if (tag.equals("Input")) {
                    processInput(0);
                } */else {
                    if (console != null)
                        console.append("\n");
                    console.append(currentStatement + " : Invalid starting keyword\n\n");
                }

                j++;
            }

            j = original;
            currentStatement = "";
        }
    }

//    public void processInput(int index) {
//		if (ck[1 + index].equals("number")) {
//			double d = Double.parseDouble(JOptionPane.showInputDialog("Enter value for " + ck[2 + index]));
//			if (isNewVariable(ck[2 + index])) {
//				numbers.add(new Number(ck[2 + index], d));
//			}
//		} else if (ck[1 + index].equals("text")) {
//			String s = JOptionPane.showInputDialog("Enter value for " + ck[2 + index]);
//			if (isNewVariable(ck[2 + index])) {
//				texts.add(new Text(ck[2 + index], s));
//			}
//		} else if (ck[1 + index].equals("cond")) {
//			boolean b = Boolean.parseBoolean(JOptionPane.showInputDialog("Enter value for " + ck[2 + index]));
//			if (isNewVariable(ck[2 + index])) {
//				conds.add(new Cond(ck[2 + index], b));
//			}
//		} else if (ck[1 + index].equals("letter")) {
//			char l = JOptionPane.showInputDialog("Enter value for " + ck[2 + index]).charAt(0);
//			if (isNewVariable(ck[2 + index])) {
//				letters.add(new Letter(ck[2 + index], l));
//			}
//		}
//	}

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
            if (!console.equals(null)) {
                console.append("\n");
            }
            console.append(currentStatement + " : The variable name " + name + " is already being used \n\n");

        }
        return newVar;
    }

    private void printVariable(String name) {
        boolean printed = false;
        for (int i = 0; i < conds.size(); i++) {

            if (name.equals(conds.get(i).getName())) {
                console.append(conds.get(i).getCond() + "");
                printed = true;
            }
        }
        if (!printed) {
            for (int i = 0; i < texts.size(); i++) {
                if (name.equals(texts.get(i).getName())) {
                    console.append(texts.get(i).getText().substring(0, texts.get(i).getText().length()) + "");
                    printed = true;
                }
            }
        }
        if (!printed) {
            for (int i = 0; i < numbers.size(); i++) {
                if (name.equals(numbers.get(i).getName())) {
                    console.append(numbers.get(i).getValue() + "");
                    printed = true;
                }
            }
        }
        if (!printed) {
            for (int i = 0; i < letters.size(); i++) {
                if (name.equals(letters.get(i).getName())) {
                    console.append(letters.get(i).getLetter() + "");

                    printed = true;
                }
            }
        }
        if (!printed) {
            if (console != null)
                console.append("\n");
            console.append(currentStatement + " : The variable name " + name + " does not exist\n\n");

        }
    }

    private void reset() {
        conds.clear();
        texts.clear();
        numbers.clear();
        letters.clear();
        console.setText(null);
    }
}
