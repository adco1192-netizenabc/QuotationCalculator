package com.example.quotationcalculator;

import android.app.*;
import android.os.*;
import android.graphics.Color;
import android.graphics.Typeface;
import android.content.*;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import java.text.DecimalFormat;

public class MainActivity extends Activity {
    final int GREEN=Color.rgb(0,176,80), RED=Color.rgb(210,20,20), ORANGE=Color.rgb(230,120,0), BROWN=Color.rgb(139,63,11), TEXT=Color.rgb(30,35,40);
    LinearLayout results; EditText quotation,purchasing,expense; DecimalFormat df=new DecimalFormat("#,##0.00");

    int dp(int v){return (int)(v*getResources().getDisplayMetrics().density+.5f);}
    TextView tv(String s,int size,int color,boolean bold){TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(color);t.setTypeface(Typeface.DEFAULT,bold?Typeface.BOLD:Typeface.NORMAL);return t;}
    LinearLayout.LayoutParams lp(int w,int h,int mt){LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(w,h);p.topMargin=dp(mt);return p;}

    @Override public void onCreate(Bundle b){
        super.onCreate(b);
        ScrollView scroll=new ScrollView(this); LinearLayout root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(dp(18),dp(18),dp(18),dp(24));scroll.addView(root);
        TextView head=tv("Quotation Calculator",24,Color.WHITE,true); head.setGravity(Gravity.CENTER);head.setPadding(0,dp(18),0,dp(18));head.setBackgroundColor(Color.rgb(0,138,62));root.addView(head,lp(-1,-2,0));
        root.addView(tv("ENTER VALUES",12,Color.DKGRAY,true),lp(-1,-2,18));
        quotation=input(root,"Quotation Total Amount","Enter quotation amount");
        purchasing=input(root,"Purchasing","Enter purchasing amount");
        expense=input(root,"Expense","Enter expense amount");
        Button calc=new Button(this);calc.setText("CALCULATE");calc.setTextColor(Color.WHITE);calc.setTextSize(15);calc.setTypeface(Typeface.DEFAULT,Typeface.BOLD);calc.setBackgroundResource(com.example.quotationcalculator.R.drawable.bg_button);root.addView(calc,lp(-1,dp(54),18));
        Button clear=new Button(this);clear.setText("CLEAR");root.addView(clear,lp(-1,dp(48),8));
        root.addView(tv("CALCULATIONS",12,Color.DKGRAY,true),lp(-1,-2,24));
        results=new LinearLayout(this);results.setOrientation(LinearLayout.VERTICAL);root.addView(results);
        calc.setOnClickListener(v->calculate());clear.setOnClickListener(v->{quotation.setText("");purchasing.setText("");expense.setText("");results.removeAllViews();quotation.requestFocus();});
        setContentView(scroll);
    }
    EditText input(LinearLayout root,String label,String hint){
        TextView l=tv(label,14,TEXT,true);root.addView(l,lp(-1,-2,10));
        EditText e=new EditText(this);e.setSingleLine(true);e.setTextSize(18);e.setHint(hint);e.setInputType(2|8192);e.setPadding(dp(14),0,dp(14),0);e.setBackgroundResource(R.drawable.bg_input);root.addView(e,lp(-1,dp(52),5));return e;
    }
    Double val(EditText e){try{return Double.parseDouble(e.getText().toString().trim().replace(",",""));}catch(Exception x){return null;}}
    void calculate(){
        Double q=val(quotation),p=val(purchasing),ex=val(expense);
        if(q==null||p==null||ex==null){Toast.makeText(this,"Please enter all three green fields.",Toast.LENGTH_SHORT).show();return;}
        double invoice=q*.04, income=q*.055, diff=q*.0362, cins=q*.01, sales=(q-q/1.18)*.20;
        double remaining=q-(invoice+income+diff+cins), profit=remaining-p-ex;
        results.removeAllViews();
        result("Invoice Amount",invoice,RED,"4% of quotation");
        result("Income Tax",income,RED,"5.5% of quotation");
        result("Sales Tax Difference (to be paid)",diff,RED,"3.62% of quotation");
        result("CINS 1%",cins,RED,"1% of quotation");
        result("20% of Sales Tax",sales,ORANGE,"(Quotation - Quotation / 1.18) × 20%");
        result("Remaining Amount",remaining,RED,"Quotation - Invoice - Income Tax - Sales Tax Difference - CINS");
        result("Net Profit",profit,BROWN,"Remaining Amount - Purchasing - Expense");
        resultText("% Save from Purchasing",p==0?"—":df.format((profit*100)/p)+"%",BROWN,"(Net Profit × 100) ÷ Purchasing");
        results.requestFocus();((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(results.getWindowToken(),0);
    }
    void result(String label,double value,int color,String formula){resultText(label,df.format(value),color,formula);}
    void resultText(String label,String value,int color,String formula){
        LinearLayout card=new LinearLayout(this);card.setOrientation(LinearLayout.VERTICAL);card.setPadding(dp(15),dp(12),dp(15),dp(12));
        GradientDrawable bg=new GradientDrawable();bg.setColor(Color.WHITE);bg.setCornerRadius(dp(12));bg.setStroke(dp(1),Color.rgb(225,228,232));card.setBackground(bg);
        LinearLayout top=new LinearLayout(this);top.setGravity(Gravity.CENTER_VERTICAL);TextView dot=tv("●",18,color,true);top.addView(dot,lp(dp(25),-2,0));
        TextView l=tv(label,14,TEXT,true);top.addView(l,new LinearLayout.LayoutParams(0,-2,1));
        TextView v=tv(value,20,color,true);top.addView(v,lp(-2,-2,0));card.addView(top);
        TextView f=tv(formula,11,Color.GRAY,false);f.setPadding(dp(25),dp(4),0,0);card.addView(f);results.addView(card,lp(-1,-2,9));
    }
}
