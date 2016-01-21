package com.example.carlosnavarrete.reactiveandroid;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.List;
import java.util.regex.Pattern;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by carlos.navarrete on 1/20/16.
 */
public class RxAndroidActivity extends AppCompatActivity {

    EditText userNameEdit,emailEdit;
    Button registerButton;
    private CompositeSubscription compoSubs;

    public static final String RX = "RX";
    public String[] array = {"uno","dos","tres","1","2","3","Alemania","Argentina","Belice","Brasil","Canada","Colombia"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userNameEdit = (EditText) findViewById(R.id.useredit);
        emailEdit = (EditText) findViewById(R.id.emailedit);
        registerButton = (Button) findViewById(R.id.button);

        //Observable

        Observable<String> myObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello, world!");
                subscriber.onCompleted();
            }
        });

        Subscriber<String> mySubscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onNext(String s) {
                Log.d(RX, s);

            }
        };

        myObservable.subscribe(mySubscriber);

        Observable<String> anotherObservable = Observable.just("Hello, world!");
        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(RX, s);
            }
        };

        anotherObservable.subscribe(onNextAction);

        Observable.just("Hello, world!")
            .map(new Func1<String, Integer>() {
                @Override
                public Integer call(String s) {
                    return s.hashCode();
                }
            }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer s) {
                Log.d(RX, Integer.toString(s));
            }
        });
        Observable.just("Hello, world!").flatMap(new Func1<String , Observable<String>>() {
            @Override
            public Observable<String> call(String s) {
                return Observable.from(array);
            }

        }).takeLast(5).doOnNext(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(RX,"PAIS: "+ s);
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(RX, s);
            }
        });

       /* Observable.from(array).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(RX, s);
            }
        });*/

        Observable<String> blockUI = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                for(double i = 0; i < 1000000000 ; i++) {
                    double y = i*i;
                }
                for (int j=0; j < 10000000; j++){
                    subscriber.onNext(j+"");
                }

                subscriber.onCompleted();
            }
        });

        blockUI.subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.d(RX,"Completed");
            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onNext(String s) {
                Log.d(RX, "ELEMENT: "+ s);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        startRX();


    }

    private void startRX(){
        final Pattern emailPattern = Pattern
            .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        compoSubs = new CompositeSubscription();

        /*
            Observable edit username and subscriber
         */

        Observable<Boolean> userNameValid = RxTextView.textChanges(userNameEdit).map(new Func1<CharSequence, Boolean>() {
            @Override
            public Boolean call(CharSequence charSequence) {
                return charSequence.length()>4;
            }
        });

        compoSubs.add(userNameValid.distinctUntilChanged().doOnNext(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                Log.d("RX","Username: "+ (aBoolean ? "Valid":"Invalid"));
            }
        }).map(new Func1<Boolean, Integer>() {
            @Override
            public Integer call(Boolean aBoolean) {
                return aBoolean ? Color.BLACK : Color.RED;
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                userNameEdit.setTextColor(integer);
            }
        }));

        /*
            Observable edit email and subscriber
         */
        Observable<Boolean> emailValid = RxTextView.textChanges(emailEdit).map(new Func1<CharSequence, Boolean>() {
            @Override
            public Boolean call(CharSequence charSequence) {
                return emailPattern.matcher(charSequence).matches();
            }
        });

        compoSubs.add(emailValid.distinctUntilChanged().doOnNext(new Action1<Boolean>() {
            @Override
            public void call(Boolean bool) {
                Log.d("RX","EMAIL: " + (bool ? "Valid":"Invalid"));
            }
        }).map(new Func1<Boolean, Integer>() {
            @Override
            public Integer call(Boolean bool) {
                return bool ? Color.BLACK : Color.RED;
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                emailEdit.setTextColor(integer);
            }
        }));

        final Observable<Boolean> registerEnabled = Observable.combineLatest(userNameValid, emailValid, new Func2<Boolean, Boolean, Boolean>() {
            @Override
            public Boolean call(Boolean aBoolean, Boolean aBoolean2) {
                return aBoolean && aBoolean2;
            }
        });

        compoSubs.add(registerEnabled.distinctUntilChanged().doOnNext(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                Log.d("RX","Register Button" + (aBoolean ? "Enabled" : "Disabled" ));
            }
        }).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                registerButton.setEnabled(aBoolean);
            }
        }));

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

    @Override
    protected void onPause() {
        super.onPause();
    }
}
