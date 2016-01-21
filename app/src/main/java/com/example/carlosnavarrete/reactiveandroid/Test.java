package com.example.carlosnavarrete.reactiveandroid;

import java.util.Observable;

/**
 * Created by carlos.navarrete on 1/18/16.
 */
public class Test extends Observable {

        private String name = "First time i have this Text";

        /**
         *@return the value
         */
        public String getValue() {
            return name;
        }

        public void setValue(String name) {
            this.name = name;
            setChanged();
            notifyObservers();
        }
}
