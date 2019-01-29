package com.example.joan.practica2ame_joajara_i_aleixiglesias;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;


public class Main5Activity extends AppCompatActivity {

    int cont = 0, days, goal;
    Thread t= null;
    float cont2 = 0;
    int bytes;
    float feet;
    String name, id ;
    Boolean stat = true, objectiu =false;
    int passos;
    String day, date, obj;
    String username ;
    String password ;
    Button increase , configuration, saveMain, resume;
    String[] t_feet, t_footBefore, t_stepsDone, t_date;
    DatabaseHelper my_db;
    InputStream input = null;

    private BluetoothAdapter btAdapter;
    private boolean btConnected = false;
    private ArrayAdapter<String> pairedDevicesArray;
    private String DEVICE_ADDRESS;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    BluetoothDevice arduino;
    ArrayList<String> mArrayAdapter = new ArrayList<>();

    BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        t_stepsDone = new String[15];
        t_feet = new String[15];
        t_footBefore = new String[15];
        t_date = new String[15];
        stat=true;

        my_db = new DatabaseHelper(this);
        Bundle parameters = this.getIntent().getExtras();

        if (parameters != null) {
            id = parameters.getString("id");
            obj = parameters.getString("objective");
            name = parameters.getString("name");
            username = parameters.getString("user");
            password = parameters.getString("pass");
            day = parameters.getString("days");
            String foot = parameters.getString("feet").replace("[","");
            foot = foot.replace("]","");
            String footBefore = parameters.getString("feetBefore").replace("[","");
            footBefore=footBefore.replace("]","");
            date = parameters.getString("dayOfTheWeek").replace("[","");
            date = date.replace("]","");
            String stepsDone = parameters.getString("steps").replace("[","");
            stepsDone=stepsDone.replace("]","");

            if (day.equals("")) days = 1;
            else days = Integer.parseInt(day);


            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ITALY);
            Date d = Calendar.getInstance().getTime();
            String dayOfTheWeek = sdf.format(d);
            if(date!=null){
                if (!date.equals("")) {
                    t_date = date.split(", ");
                } else {
                    t_date[days - 1] = "";
                }
            }else{
                t_date[days - 1] = "";
            }

            if(obj!=null){
                if (!obj.equals("")) {
                    goal = Integer.parseInt(obj);
                } else {
                    goal=500;
                }
            }else{
               goal=500;
            }

            if(foot!=null) {
                if (!foot.equals("")) {
                    t_feet = foot.split(", ");
                    feet = Float.parseFloat(t_feet[days - 1])/100;
                } else {
                    feet = (float) 0.0;
                }
            }else {
                feet = (float) 0.0;
            }

            if(footBefore!=null) {
                if (!footBefore.equals("")) {
                    t_footBefore = footBefore.split(", ");
                    cont2 = Float.parseFloat(t_footBefore[days - 1]);
                } else {
                    t_footBefore[days - 1] = "0";
                    cont2 = 0;
                }
            }else {
                t_footBefore[days - 1] = "0";
                cont2 = 0;
            }

            if(stepsDone!=null) {
                if (!stepsDone.equals("")) {
                    t_stepsDone = stepsDone.split(", ");
                    cont = Integer.parseInt(t_stepsDone[days - 1]);
                } else {
                    t_stepsDone[days - 1] = "0";
                    cont = 0;
                }
            }else {
                t_stepsDone[days - 1] = "0";
                cont=0;
            }

            if(!dayOfTheWeek.equals(t_date[days-1])){
                days++;
                if(days!=8) {
                    t_date[days - 1] = dayOfTheWeek;
                    t_stepsDone[days - 1] = "0";
                    t_footBefore[days - 1] = "0";
                    t_feet[days - 1] = t_feet[days - 2];
                    day = String.valueOf(days);
                    cont = 0;
                    cont2 = 0;
                }else{
                    int i =0;
                    while(i<days-2){
                        t_date[i] = t_date[i+1];
                        t_stepsDone[i] = t_stepsDone[i+1];
                        t_feet[i] = t_feet[i+1];
                        t_footBefore[i] = t_footBefore[i+1];
                        i++;
                    }

                    t_footBefore[i]=String.valueOf(cont2);
                    t_stepsDone[i]=String.valueOf(cont);
                    days--;
                    t_date[days-1]=dayOfTheWeek;
                    t_stepsDone[days-1]="0";
                    t_footBefore[days-1]="0";
                    t_feet[days-1]=t_feet[days-2];
                    day = String.valueOf(days);
                    cont=0;
                    cont2=0;
                }
                day=String.valueOf(days);
                my_db.updateData(id,name,username,password,day, EncryptionPass.transformArraytoString(t_feet), EncryptionPass.transformArraytoString(t_stepsDone), EncryptionPass.transformArraytoString(t_footBefore),EncryptionPass.transformArraytoString(t_date), obj);
            }

        }else {
            id="";
            name="";
            username="";
            password="";
            day="1";
            t_date[0]="";
            t_stepsDone[0]="0";
            t_footBefore[0]="0";
            cont=0;
            cont2=0;
        }


       if(feet==0.0) feet = (float) 0.43;



        increase = findViewById(R.id.increaseStep);
        configuration = findViewById(R.id.confBtn);
        saveMain = findViewById(R.id.saveBtnMain);
        resume = findViewById(R.id.resumBtn);

        if(username.equals("")){
            saveMain.setVisibility(View.INVISIBLE);
            resume.setVisibility(View.INVISIBLE);
        }else{
            saveMain.setVisibility(View.VISIBLE);
            resume.setVisibility(View.VISIBLE);
        }

        final TextView steps = findViewById(R.id.stepCounter);
        final TextView km = findViewById(R.id.kilometerCounter);
        steps.setText(String.format(Locale.ENGLISH,"Steps:%d",cont));
        km.setText(String.format(Locale.ENGLISH,"Meters:%.2f", cont2));

        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ITALY);
                Date d = Calendar.getInstance().getTime();
                String auxDate = sdf.format(d);

                if (!auxDate.equals(t_date[days - 1]) && !t_date[days - 1].equals("")) {
                    days++;
                    objectiu=false;
                    if (days != 8) {
                        t_date[days - 1] = auxDate;
                        t_stepsDone[days - 1] = "0";
                        t_footBefore[days - 1] = "0";
                        t_feet[days - 1] = t_feet[days - 2];
                        day = String.valueOf(days);
                        cont = 0;
                        cont2 = 0;
                    } else {
                        int i = 0;
                        while (i < days - 2) {
                            t_date[i] = t_date[i + 1];
                            t_stepsDone[i] = t_stepsDone[i + 1];
                            t_feet[i] = t_feet[i + 1];
                            t_footBefore[i] = t_footBefore[i + 1];
                            i++;
                        }

                        t_footBefore[i] = String.valueOf(cont2);
                        t_stepsDone[i] = String.valueOf(cont);
                        days--;
                        t_date[days - 1] = auxDate;
                        t_stepsDone[days - 1] = "0";
                        t_footBefore[days - 1] = "0";
                        t_feet[days - 1] = t_feet[days - 2];
                        day = String.valueOf(days);
                        cont = 0;
                        cont2 = 0;
                    }
                    day = String.valueOf(days);
                } else {
                    t_footBefore[days - 1] = String.valueOf(cont2);
                    t_stepsDone[days - 1] = String.valueOf(cont);
                }

                /*class readingThread extends  Thread{
                    private InputStream input;

                    public readingThread(){
                        InputStream tmpIn = null;
                        OutputStream tmpOut = null;

                        try {
                            //Create I/O streams for connection
                            tmpIn = btSocket.getInputStream();
                            tmpOut = btSocket.getOutputStream();
                        } catch (IOException e) { }

                        input = tmpIn;
                    }

                    public void run(){

                        try {
                            btSocket.connect();
                            increase.setVisibility(View.INVISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        byte[] buffer = new byte[256];
                        int bytes;
                        int passos;

                        while (true) {
                            try {
                                bytes = input.read(buffer);
                                km.setText(String.format(Locale.ENGLISH, "%d", (int)bytes));
                                System.out.println(bytes);
                                if (bytes!=-1){
                                    String[] pas = steps.getText().toString().split(":");
                                    passos=Integer.parseInt(pas[1]);
                                    passos+=bytes;
                                    steps.setText(String.format(Locale.ENGLISH, "Steps:%d", (int)passos));
                                    km.setText(String.format(Locale.ENGLISH, "Meters:%.2f", ((float)passos*feet)));
                                    passos=-1;
                                    bytes=-1;
                                }
                                Thread.sleep(100);

                            } catch (IOException e) {
                                break;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                cont++;
                steps.setText(String.format(Locale.ENGLISH, "Steps:%d ", cont));
                cont2 += feet;
                String aux = String.format(Locale.ENGLISH, "Meters:%.2f ", cont2);
                km.setText(aux);*/
                String[] pas = steps.getText().toString().split(":");
                cont=Integer.parseInt(pas[1]);
                cont2=cont*feet;
                t_footBefore[days - 1] = String.valueOf(cont2);
                t_stepsDone[days - 1] = String.valueOf(cont);
                my_db.updateData(id, name, username, password, day, EncryptionPass.transformArraytoString(t_feet), EncryptionPass.transformArraytoString(t_stepsDone), EncryptionPass.transformArraytoString(t_footBefore), EncryptionPass.transformArraytoString(t_date), obj);
                if (cont >= goal && !objectiu ) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Main5Activity.this);
                    builder.setTitle("CONGRATULATIONS");
                    builder.setMessage("CONGRATULATIONS, YOU ACHIEVED YOUR GOAL.");
                    objectiu=true;
                    // add a button
                    builder.setPositiveButton("OK", null);

                    // create and show the alert dialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

                btAdapter = BluetoothAdapter.getDefaultAdapter();
                if (btAdapter == null) {
                    Toast.makeText(getApplicationContext(),"Your device don't have bluetooth",Toast.LENGTH_SHORT).show();
                    btConnected = false;
                } else if (!btAdapter.isEnabled()) {
                    Toast.makeText(getApplicationContext(),"You have to enable your bluetooth",Toast.LENGTH_SHORT).show();
                    btConnected = false;
                } else {

                   /* mReceiver= new BroadcastReceiver() {
                        public void onReceive(Context context, Intent intent) {
                            String action = intent.getAction();
                            // When discovery finds a device
                            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                                // Get the BluetoothDevice object from the Intent
                                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                                // Add the name and address to an array adapter to show in a ListView
                                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                            }
                        }
                    };
                    // Register the BroadcastReceiver
                    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    registerReceiver(mReceiver, filter);


                    for (String s : mArrayAdapter) {
                        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
                    }*/

                   arduino = btAdapter.getRemoteDevice("10:14:05:23:11:75");

                    Toast.makeText(getApplicationContext(),arduino.getName(),Toast.LENGTH_SHORT).show();
                    Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
                    btConnected = true;
                    if (arduino.getAddress().equals("10:14:05:23:11:75")) {
                        try{
                            btSocket = arduino.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));   //Creation of a socket
                            try {
                                btSocket.connect();
                                input =btSocket.getInputStream();
                                t = new Thread(new Runnable() {
                                    @Override
                                    public void run() {

                                        while (stat) {
                                            try {
                                                bytes =input.read();
                                                if (bytes!=-1){
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            String[] pas = steps.getText().toString().split(":");
                                                            passos=Integer.parseInt(pas[1]);
                                                            passos+=bytes;
                                                            increase.setVisibility(View.INVISIBLE);
                                                            steps.setText(String.format(Locale.ENGLISH, "Steps:%d", (int) passos));
                                                            km.setText(String.format(Locale.ENGLISH, "Meters:%.2f", ((float) passos * feet)));
                                                            cont = passos;
                                                            cont2=cont*feet;
                                                            passos=-1;
                                                            bytes=-1;
                                                        }});
                                                }
                                                Thread.sleep(100);

                                            } catch (IOException e) {
                                                break;
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                });
                                t.start();
                            } catch (IOException e) {

                                e.printStackTrace();
                            }

                            //btSocket,feet,increase,steps,km).start();    //starting a thrad so as to handle connection and messages

                            Toast.makeText(getApplicationContext(),"Bluetooth Connected!", Toast.LENGTH_SHORT).show();
                        } catch(IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        configuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stat = false;
                try {
                    btSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ITALY);
                Date d = Calendar.getInstance().getTime();
                String auxDate = sdf.format(d);

                if(!auxDate.equals(t_date[days-1]) && !t_date[days-1].equals("") ) {
                    days++;
                    objectiu=false;
                    if(days!=8) {
                        t_date[days - 1] = auxDate;
                        t_stepsDone[days - 1] = "0";
                        t_footBefore[days - 1] = "0";
                        t_feet[days - 1] = t_feet[days - 2];
                        day = String.valueOf(days);
                        cont = 0;
                        cont2 = 0;
                    }else{
                        int i =0;
                        while(i<days-2){
                            t_date[i] = t_date[i+1];
                            t_stepsDone[i] = t_stepsDone[i+1];
                            t_feet[i] = t_feet[i+1];
                            t_footBefore[i] = t_footBefore[i+1];
                            i++;
                        }

                        t_footBefore[i]=String.valueOf(cont2);
                        t_stepsDone[i]=String.valueOf(cont);
                        days--;
                        t_date[days-1]=auxDate;
                        t_stepsDone[days-1]="0";
                        t_footBefore[days-1]="0";
                        t_feet[days-1]=t_feet[days-2];
                        day = String.valueOf(days);
                        cont=0;
                        cont2=0;
                    }
                    day = String.valueOf(days);
                }else{
                    t_footBefore[days-1] = String.valueOf(cont2);
                    t_stepsDone[days-1] = String.valueOf(cont);
                }
                my_db.updateData(id,name,username,password,day, EncryptionPass.transformArraytoString(t_feet), EncryptionPass.transformArraytoString(t_stepsDone), EncryptionPass.transformArraytoString(t_footBefore),EncryptionPass.transformArraytoString(t_date), obj);
                Intent conf = new Intent(getApplicationContext(), Main4Activity.class);
                Bundle information = new Bundle();
                information.putString("id", id);
                information.putString("name", name);
                information.putString("user", username);
                information.putString("pass", password);
                information.putString("days", day);
                information.putString("steps", EncryptionPass.transformArraytoString(t_stepsDone));
                information.putString("feet", EncryptionPass.transformArraytoString(t_feet));
                information.putString("feetBefore", EncryptionPass.transformArraytoString(t_footBefore));
                information.putString("dayOfTheWeek",EncryptionPass.transformArraytoString(t_date));
                information.putString("objective", obj);
                conf.putExtras(information);
                startActivity(conf);
                finish() ;
            }
        });

        saveMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ITALY);
                Date d = Calendar.getInstance().getTime();
                String auxDate = sdf.format(d);

                if(!auxDate.equals(t_date[days-1]) && !t_date[days-1].equals("") ) {
                    days++;
                    objectiu=false;
                    if(days!=8) {
                        t_date[days - 1] = auxDate;
                        t_stepsDone[days - 1] = "0";
                        t_footBefore[days - 1] = "0";
                        t_feet[days - 1] = t_feet[days - 2];
                        day = String.valueOf(days);
                        cont = 0;
                        cont2 = 0;
                    }else{
                        int i =0;
                        while(i<days-2){
                            t_date[i] = t_date[i+1];
                            t_stepsDone[i] = t_stepsDone[i+1];
                            t_feet[i] = t_feet[i+1];
                            t_footBefore[i] = t_footBefore[i+1];
                            i++;
                        }

                        t_footBefore[i]=String.valueOf(cont2);
                        t_stepsDone[i]=String.valueOf(cont);
                        days--;
                        t_date[days-1]=auxDate;
                        t_stepsDone[days-1]="0";
                        t_footBefore[days-1]="0";
                        t_feet[days-1]=t_feet[days-2];
                        day = String.valueOf(days);
                        cont=0;
                        cont2=0;
                    }
                    day = String.valueOf(days);
                }else{
                    t_footBefore[days-1] = String.valueOf(cont2);
                    t_stepsDone[days-1] = String.valueOf(cont);
                }
                my_db.updateData(id,name,username,password,day, EncryptionPass.transformArraytoString(t_feet), EncryptionPass.transformArraytoString(t_stepsDone), EncryptionPass.transformArraytoString(t_footBefore),EncryptionPass.transformArraytoString(t_date), obj);
                Toast.makeText(Main5Activity.this, "Data inserted correctly.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ITALY);
                Date d = Calendar.getInstance().getTime();
                String auxDate = sdf.format(d);
                stat=false;
                try {
                    btSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(!auxDate.equals(t_date[days-1]) && !t_date[days-1].equals("") ) {
                    days++;
                    objectiu=false;
                    if(days!=8) {
                        t_date[days - 1] = auxDate;
                        t_stepsDone[days - 1] = "0";
                        t_footBefore[days - 1] = "0";
                        t_feet[days - 1] = t_feet[days - 2];
                        day = String.valueOf(days);
                        cont = 0;
                        cont2 = 0;
                    }else{
                        int i =0;
                        while(i<days-2){
                            t_date[i] = t_date[i+1];
                            t_stepsDone[i] = t_stepsDone[i+1];
                            t_feet[i] = t_feet[i+1];
                            t_footBefore[i] = t_footBefore[i+1];
                            i++;
                        }

                        t_footBefore[i]="0";
                        t_stepsDone[i]="0";
                        days--;
                        t_date[days-1]=auxDate;
                        t_stepsDone[days-1]="0";
                        t_footBefore[days-1]="0";
                        t_feet[days-1]=t_feet[days-2];
                        day = String.valueOf(days);
                        cont=0;
                        cont2=0;
                    }
                    day = String.valueOf(days);
                }else{
                    t_footBefore[days-1] = String.valueOf(cont2);
                    t_stepsDone[days-1] = String.valueOf(cont);
                }
                my_db.updateData(id,name,username,password,day, EncryptionPass.transformArraytoString(t_feet), EncryptionPass.transformArraytoString(t_stepsDone), EncryptionPass.transformArraytoString(t_footBefore),EncryptionPass.transformArraytoString(t_date), obj);

                Intent conf = new Intent(getApplicationContext(), Main6Activity.class);
                Bundle information = new Bundle();
                information.putString("id", id);
                information.putString("name", name);
                information.putString("user", username);
                information.putString("pass", password);
                information.putString("days", day);
                information.putString("steps", EncryptionPass.transformArraytoString(t_stepsDone));
                information.putString("feet", EncryptionPass.transformArraytoString(t_feet));
                information.putString("feetBefore", EncryptionPass.transformArraytoString(t_footBefore));
                information.putString("dayOfTheWeek",EncryptionPass.transformArraytoString(t_date));
                information.putString("objective", obj);
                conf.putExtras(information);
                startActivity(conf);
                finish();
            }
        });
    }
}
