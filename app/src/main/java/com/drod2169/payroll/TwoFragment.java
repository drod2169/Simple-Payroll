package com.drod2169.payroll;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import static android.R.layout.simple_list_item_1;


public class TwoFragment extends android.support.v4.app.Fragment {

    DatabaseHandler databaseHandler;
    static ArrayAdapter<String> arrayAdapter;
    static ArrayList<Employee> employees = new ArrayList<>();
    private ArrayList<String> results;
    static final List<String> values = new ArrayList<String>();



    public TwoFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ListView listView;
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        listView = (ListView) view.findViewById(R.id.employee_list);
        databaseHandler = new DatabaseHandler(getContext());

        try {
            employees = (ArrayList<Employee>) databaseHandler.getAllEmployees();

            String name;

            for (Employee emps : employees) {

                name = emps.getEmployeeName();

                values.add(name);

            }

            for (Employee emp : employees) {
                /*String log = "Id: " + emp.getId() + " , Name: " + emp.getEmployeeName() + " , Pay Rate: " +
                        emp.getPayRate() + " , Dates: " + emp.getDate() + " , Clock In: " +
                        emp.getClockedInDate() + " , Clock Out: " + emp.getClockedOutDate() + " , Hours Worked: " + emp.getHoursWorked();
                */
                String log = "Id: " + emp.getId() + " , Name: " + emp.getEmployeeName() + " , Pay Rate: " +
                        emp.getPayRate() + " , Clock In: " + emp.getClockedInDate() + " , Clock Out: " +
                        emp.getClockedOutDate() + " , Hours Worked: " + emp.getHoursWorked();

                //MainActivity.employee.setID(emp.getId());
                // Write to the log
                Log.i("DB from TwoFrag: ", log);
            }

            arrayAdapter = new ArrayAdapter<>(getActivity(), simple_list_item_1, values);

            listView.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Intent intent = new Intent(getContext(), EmployeeActivity.class);
                    intent.putExtra("empId", i);
                    startActivity(intent);

                }
            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                    final int itemToDelete = i;

                    new AlertDialog.Builder(getContext())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Are you sure?")
                            .setMessage("Do you want to delete this employee record?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    DatabaseHandler db = new DatabaseHandler(getContext());

                                    Log.i("Employee to delete: ", employees.get(itemToDelete).getEmployeeName());
                                    db.deleteEmployee(employees.get(itemToDelete));
                                    values.remove(itemToDelete);
                                    if (EmployeeSingleton.getInstance() != null) {
                                        EmployeeSingleton.resetInstance();
                                    }
                                    employees.remove(itemToDelete);
                                    MainActivity.employees.remove(itemToDelete);
                                    for (Employee emp : employees) {
                                        String log = "Id: " + emp.getId() + " , Name: " + emp.getEmployeeName() + " , Pay Rate: " +
                                                emp.getPayRate() + " , Dates: " + emp.getDate() + " , Clock In: " +
                                                emp.getClockIn() + " , Clock Out: " + emp.getClockOut() + " , Hours Worked: " + emp.getHoursWorked();
                                        //MainActivity.employee.setID(emp.getId());
                                        // Write to the log
                                        Log.i("DB from TwoFrag ", log);
                                    }


                                    arrayAdapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();

                    return true;

                }

            });

        } catch (Exception e) {

            e.printStackTrace();

        }

        return view;

    }


    public static void updateListView(String name) {

        ArrayList<String> newValues = new ArrayList<>();
        newValues.add(name);
        arrayAdapter.addAll(newValues);
        arrayAdapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        employees = (ArrayList<Employee>) databaseHandler.getAllEmployees();
        for (Employee emp : employees) {
            Log.i("onResume: ", emp.getEmployeeName() + " id: " + emp.getId());
        }
        arrayAdapter.clear();
        String name;
        ArrayList<String> newValues = new ArrayList<>();
        for (Employee emps : employees) {

            name = emps.getEmployeeName();

            newValues.add(name);

        }
        arrayAdapter.addAll(newValues);
        arrayAdapter.notifyDataSetChanged();

        super.onResume();
    }
}