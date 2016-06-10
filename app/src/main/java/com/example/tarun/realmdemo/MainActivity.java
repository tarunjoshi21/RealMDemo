package com.example.tarun.realmdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.tarun.realmdemo.bean.EmployeeBean;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private Realm mRealm;
    private final static String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRealm = Realm.getDefaultInstance();

        // add employee
      /*  mRealm.beginTransaction();
        EmployeeBean employee = mRealm.createObject(EmployeeBean.class);
        employee.setEmployeeId("EMP001");
        employee.setEmployeeName("Tarun Joshi");
        employee.setEmployeeSalary(100000);
        mRealm.commitTransaction();*/

        // Transaction block
       /* mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                EmployeeBean employee = mRealm.createObject(EmployeeBean.class);
                employee.setEmployeeId("EMP002");
                employee.setEmployeeName("Amit Rai");
                employee.setEmployeeSalary(60000);
            }
        });*/


        assert ((findViewById(R.id.insert))) != null;
        ((findViewById(R.id.insert))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Asynchronous Transactions
                mRealm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm bgRealm) {
                        Log.e("start time", ""+System.currentTimeMillis());
                        double startTime, endTime = 0;
                        startTime = System.currentTimeMillis();
                        for (int i =0; i<10000; i++){
                            EmployeeBean employee = bgRealm.createObject(EmployeeBean.class);
                            employee.setEmployeeId("EMP"+i);
                            employee.setEmployeeName("Devesh Bhandari "+i);
                            employee.setEmployeeSalary(10000+i);
                        }

                        Log.e("end time", ""+System.currentTimeMillis());
                        endTime = System.currentTimeMillis();

                        double time_diff =  (endTime - startTime);

                        Log.e("time diff", ""+time_diff);

                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        // Transaction was a success.

                        Log.i(TAG, "OnSuccess");
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        // Transaction failed and was automatically canceled.
                        Log.e(TAG, error.getMessage());
                    }
                });
            }
        });



        ((findViewById(R.id.button))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // query all employee
                RealmResults<EmployeeBean> employees = mRealm.where(EmployeeBean.class).greaterThan("employeeSalary", 15000).
                        lessThan("employeeSalary", 15100).findAll();
                Log.e("start time", ""+System.currentTimeMillis());
                for (EmployeeBean employee : employees) {
                    Log.i(TAG, "EmployeeId: "+employee.getEmployeeId());
                    Log.i(TAG, "EmployeeName: "+employee.getEmployeeName());
                    Log.i(TAG, "EmployeeSalary: "+employee.getEmployeeSalary());
                }
                Log.e("End time", ""+System.currentTimeMillis());
            }
        });

        // retrieve all employees whose sal is greater than 50000
        RealmResults<EmployeeBean> employees = mRealm.where(EmployeeBean.class).greaterThan("employeeSalary", 50000).findAll();

       // You can also group conditions with “parentheses” to specify order of evaluation: beginGroup() is your “left parenthesis” and endGroup() your “right parenthesis”:


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }
}
