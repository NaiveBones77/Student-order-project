package edu.studentorder.dao;

import junit.framework.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class SimpleRunner {
    public static void main(String[] args) {
        SimpleRunner sr = new SimpleRunner();

        sr.runTests();
    }

    private void runTests() {
        try {
            Class c1 = Class.forName("edu.studentorder.dao.DictionaryDaoImplTest");

            Constructor cst = c1.getConstructor();
            Object entity = cst.newInstance();

            Method[] methods = getClass().getMethods();
            for (Method m : methods)
            {
                // Здесь не работает метод из урока 67
                m.invoke(entity);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
