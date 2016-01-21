package com.example.carlosnavarrete.reactiveandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by carlos.navarrete on 1/18/16.
 */
public class ObjectObserverPattern extends AppCompatActivity implements Observer, View.OnClickListener {
    BaseApp myBase;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstobserver);
        myBase = (BaseApp) getApplication();
        myBase.getObserver().addObserver(this);
        // myBase.getObserver().setValue(10);

        btn = (Button) findViewById(R.id.button);
        btn.setText("value: " + myBase.getObserver().getValue());
        btn.setOnClickListener(this);

    }

    @Override
    public void update(Observable observable, Object data) {
        // This method is notified after data changes.
        Toast.makeText(this, "I am notified" + myBase.getObserver().getValue(), 0).show();
        btn.setText("value: " + myBase.getObserver().getValue());

    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(ObjectObserverPattern.this,
            SecondActivity.class));

    }
}
