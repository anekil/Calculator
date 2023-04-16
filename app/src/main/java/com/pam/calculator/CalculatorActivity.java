package com.pam.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gloorystudio.fook.calc.Fook;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculatorActivity extends AppCompatActivity {
    private EditText solutionDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        boolean isScientific = bundle.getBoolean("type");

        if(isScientific)
            setContentView(R.layout.activity_scientific_calc);
        else
            setContentView(R.layout.activity_simple_calc);

        solutionDisplay = findViewById(R.id.resultDisplay);
        solutionDisplay.setOnTouchListener( (v, event) -> true);

        View.OnClickListener btnValueListener = view -> {
            androidx.appcompat.widget.AppCompatButton pressed = (androidx.appcompat.widget.AppCompatButton)view;
            String btnValue = pressed.getText().toString();
            String solution = solutionDisplay.getText().toString();
            solution += btnValue;
            solutionDisplay.setText(solution);
        };

        View.OnClickListener btnClearListener = view -> {
            androidx.appcompat.widget.AppCompatButton pressed = (androidx.appcompat.widget.AppCompatButton)view;
            String btnValue = pressed.getText().toString();
            String solution = "";
            if(btnValue.equals("←")){
                solution = solutionDisplay.getText().toString();
                if(solution.length()<1)
                    solution = "";
                else
                    solution = solution.substring(0, solution.length()-1);
            }
            solutionDisplay.setText(solution);
        };

        View.OnClickListener btnEqualsListener = view -> {
            String solution = solutionDisplay.getText().toString();
            try{
                double result = Fook.calc(solution);
                if(Double.toString(result).equals("Infinity") || Double.toString(result).equals("NaN")){
                    errorOccurred();
                    return;
                }
                solution = String.format("%.10f", result);
                solution = solution.replaceAll(",", ".");
                solution = !solution.contains(".") ? solution : solution.replaceAll("0*$", "").replaceAll("\\.$", "");
                solutionDisplay.setText(solution);
            }catch (RuntimeException e){
                errorOccurred();
            }
        };

        View.OnClickListener btnChangeListener = view -> {
            String solution = solutionDisplay.getText().toString();
            Pattern pattern = Pattern.compile("(.*?)(-?\\d+)(\\.?\\d*)$");
            Matcher matcher = pattern.matcher(solution);
            if(!matcher.matches()){
                errorOccurred();
                return;
            }

            String start = matcher.group(1);
            String number = matcher.group(2);
            String end = matcher.group(3);
            if(number != null){
                if(number.startsWith("-"))
                    number = number.replace('-','+');
                else{
                    if(start != null && start.endsWith("+")){
                        start = start.substring(0, start.length() - 1) + "-";
                    }
                    else
                        number = '-' + number;
                }
            }else{
                errorOccurred();
                return;
            }
            solution = start + number + end;
            solutionDisplay.setText(solution);
        };

        findViewById(R.id.btn0).setOnClickListener(btnValueListener);
        findViewById(R.id.btn1).setOnClickListener(btnValueListener);
        findViewById(R.id.btn2).setOnClickListener(btnValueListener);
        findViewById(R.id.btn3).setOnClickListener(btnValueListener);
        findViewById(R.id.btn4).setOnClickListener(btnValueListener);
        findViewById(R.id.btn5).setOnClickListener(btnValueListener);
        findViewById(R.id.btn6).setOnClickListener(btnValueListener);
        findViewById(R.id.btn7).setOnClickListener(btnValueListener);
        findViewById(R.id.btn8).setOnClickListener(btnValueListener);
        findViewById(R.id.btn9).setOnClickListener(btnValueListener);
        findViewById(R.id.btnDot).setOnClickListener(btnValueListener);
        findViewById(R.id.btnAdd).setOnClickListener(btnValueListener);
        findViewById(R.id.btnSub).setOnClickListener(btnValueListener);
        findViewById(R.id.btnMulti).setOnClickListener(btnValueListener);
        findViewById(R.id.btnDiv).setOnClickListener(btnValueListener);
        findViewById(R.id.btnChange).setOnClickListener(btnChangeListener);
        findViewById(R.id.btnC).setOnClickListener(btnClearListener);
        findViewById(R.id.btnBack).setOnClickListener(btnClearListener);
        findViewById(R.id.btnEqual).setOnClickListener(btnEqualsListener);

        if(isScientific){
            findViewById(R.id.btnSin).setOnClickListener(btnValueListener);
            findViewById(R.id.btnCos).setOnClickListener(btnValueListener);
            findViewById(R.id.btnTan).setOnClickListener(btnValueListener);
            findViewById(R.id.btnPer).setOnClickListener(btnValueListener);
            findViewById(R.id.btnSqrt).setOnClickListener(btnValueListener);
            findViewById(R.id.btnPow).setOnClickListener(btnValueListener);
            findViewById(R.id.btnLog).setOnClickListener(btnValueListener);
            findViewById(R.id.btnLn).setOnClickListener(btnValueListener);
        }
    }

    private void errorOccurred(){
        Toast.makeText(getApplicationContext(), "Wrong equation", Toast.LENGTH_LONG).show();
        solutionDisplay.setText("");
    }
}