package org.senai.mecatronica.dripper.managers;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Felipe on 20/11/2017.
 */

public class BluetoothManager {

    private final String TAG = "Bluetooth Manager";
    private final int MESSAGE_READ = 1;
    private final int MESSAGE_WRITE = 0;

    private BluetoothAdapter btAdapter;
    private Set<BluetoothDevice> pairedDevices;
    private String macAddress;
    private Context context;
    private ConnectThread connectThread;
    private ConnectedThread connectedThread;
    private Handler mHandler;
    private DataManager dataManager;

    private BluetoothManager(Context context) {
        super();
        this.context = context;
        this.btAdapter = BluetoothAdapter.getDefaultAdapter();
        this.pairedDevices = btAdapter.getBondedDevices();
        this.dataManager = DataManager.getInstance(context);
        this.mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                byte[] writeBuf = (byte[]) msg.obj;
//                int begin = (int)msg.arg1;
//                int end = (int)msg.arg2;
                if(msg.what == MESSAGE_READ) {
                    String writeMessage = new String(writeBuf);
                    dataManager.appendSensorData(writeMessage);
//                    writeMessage = writeMessage.substring(begin, end);
                    Log.i(TAG, writeMessage);
                }
            }
        };
    }

    private static BluetoothManager bluetoothManager;

    public static BluetoothManager getInstance(Context context){
        if(bluetoothManager == null){
            bluetoothManager = new BluetoothManager(context);
        }
        return bluetoothManager;
    }

    public boolean isSupported(){
        return btAdapter != null;
    }

    public boolean isEnabled(){
        return btAdapter.isEnabled();
    }

    public boolean isPaired(){
        pairedDevices = btAdapter.getBondedDevices();
        if(pairedDevices.size() > 0){
            Log.i(TAG, "Target address: " + macAddress);
            for (BluetoothDevice device : pairedDevices) {
                Log.i(TAG, "Device address: "+ device.getAddress());
                if(device.getAddress().equals(macAddress)){
                    return true;
                }
            }
        }
        return false;
    }

    public void startConnection(){
        BluetoothDevice targetDevice = btAdapter.getRemoteDevice(macAddress);
        synchronized (this){
            connectThread = new ConnectThread(targetDevice);
            connectThread.start();
        }
//        boolean success = false;
//        final BluetoothSocket mmSocket;
//        BluetoothSocket tmp = null;
//            // Get a BluetoothSocket to connect with the given BluetoothDevice
//            try {
////                tmp = device.createRfcommSocketToServiceRecord(device.getUuids()[0].getUuid());
//                //reflection (getclass.getmethod)
//                Method m = targetDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
//                tmp = (BluetoothSocket) m.invoke(targetDevice, 1);
//                Log.i(TAG, "RFCOMM Socket created");
//            } catch (Exception e) {
//                Log.e(TAG, e.getMessage());
//            }
//            mmSocket = tmp;
//
//            if(mmSocket != null){
//                try {
//                    mmSocket.connect();
//                    Log.i(TAG, "Connection successful");
//                    success = true;
//                } catch (IOException connectException) {
//                    Log.i(TAG, connectException.getMessage());
////                    try {
////                        mmSocket.close();
////                    } catch (IOException closeException) { }
//                }
//            }
//        try {
//            mmSocket.close();
//        } catch (IOException closeException) { }

//        return success;
    }


    public void closeConnection(){
        connectThread.cancel();
        connectedThread.cancel();
    }

    public void setMacAddress(String address){
        this.macAddress = address;
    }

    public void sendIrrigationData(Uri uri){
        BufferedInputStream inputStream = null;
        try
        {
            inputStream = new BufferedInputStream(context.getContentResolver().openInputStream(uri));
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            // we need to know how may bytes were read to write them to the byteBuffer
            int len = 0;
            while (len != -1){

                len = inputStream.read((buffer));
                connectedThread.write(buffer);
            }
            inputStream.close();
        }catch (IOException e){
            Log.e(TAG, "Unable to send irrigation data");
        }
    }

    /**
     * Class to manage Bluetooth connection
     * */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private static final String TAG = "Connect Thread";
        private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mmDevice = device;
            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
//                tmp = device.createRfcommSocketToServiceRecord(device.getUuids()[0].getUuid());
                //reflection (getclass.getmethod)
//                Method m = device.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
//                tmp = (BluetoothSocket) m.invoke(device, 1);

                Log.i(TAG, "RFCOMM Socket created");
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            btAdapter.cancelDiscovery();

            //try to connect, if not successful close;
            try {
                mmSocket.connect();
                Log.i(TAG, "Connection successful");
            } catch (IOException connectException) {
                Log.i(TAG, connectException.getMessage());
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }

            // Do work to manage the connection (in a separate thread)
            connectedThread = new ConnectedThread(mmSocket);
            connectedThread.start();

        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
                Log.i(TAG, "Socket closed.");
            } catch (IOException e) { }
        }

    }

    private class ConnectedThread extends Thread {
        private final String TAG = "Connected Thread";
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            Log.i(TAG, "Connected thread started");
            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
                Log.i(TAG, "I/O Streams initialized");
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    String message = new String(buffer, 0, bytes);
                    Log.i(TAG, message);
                    // Send the obtained bytes to the UI activity
                    mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                            .sendToTarget();
                } catch (IOException e) {
                    Log.e(TAG, "Unable to read from InputStream");
                    break;
                }
            }
            Uri irrigationDataUri = dataManager.getIrrigationDataUri();
            sendIrrigationData(irrigationDataUri);
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Log.e(TAG, "Error writing message");
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

}