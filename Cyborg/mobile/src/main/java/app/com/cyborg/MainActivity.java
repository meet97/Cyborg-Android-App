package app.com.cyborg;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.ParcelUuid;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private TextView txtSpeechInput;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private BluetoothAdapter bluetoothAdapter;
    private OutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtSpeechInput = findViewById(R.id.speechOutput);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(turnOn, 0);
      /*  Intent intentOpenBluetoothSettings = new Intent();
        intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
        startActivity(intentOpenBluetoothSettings);*/
    }

    public void recordSpeech(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Voice command");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "speech not supported",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                    if (bluetoothAdapter != null) {
                        if (bluetoothAdapter.isEnabled()) {
                            Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();

                            if(bondedDevices.size() > 0) {
                                try {
                                    Object[] devices = (Object []) bondedDevices.toArray();
                                    BluetoothDevice device = (BluetoothDevice) devices[0];
                                    Log.i("RESULT",device.toString());
                                    ParcelUuid[] uuids = device.getUuids();
                                    BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                                    try {
                                        socket.connect();
                                        Log.e("","Connected");
                                    } catch (IOException e) {
                                        Log.e("",e.getMessage());
                                        try {
                                            Log.e("","trying fallback...");

                                            socket =(BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(device,1);
                                            socket.connect();

                                            Log.e("","Connected");
                                        }
                                        catch (Exception e2) {
                                            Log.e("", "Couldn't establish Bluetooth connection!");
                                        }
                                    }
                                    outputStream = socket.getOutputStream();
                                 //   outputStream.write(result.get(0).getBytes());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            Log.e("error", "No appropriate paired devices.");
                        } else {
                            Log.e("error", "Bluetooth is disabled.");
                        }
                    }
                }
                break;
            }

        }
    }
}
