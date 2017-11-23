package org.senai.mecatronica.dripper.managers;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Created by Felipe on 20/11/2017.
 */

public class BluetoothManager {

    private final String TAG = "Bluetooth Manager";

    private BluetoothAdapter btAdapter;
    private Set<BluetoothDevice> pairedDevices;
    private String macAddress;
    private Context context;
//    private ConnectThread connectThread;
//    private ConnectedThread connectedThread;

    private BluetoothManager(Context context) {
        super();
        this.context = context;
        this.btAdapter = BluetoothAdapter.getDefaultAdapter();
        this.pairedDevices = btAdapter.getBondedDevices();
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

    public boolean startConnection(){
        boolean success = false;
        BluetoothDevice targetDevice = btAdapter.getRemoteDevice(macAddress);
        final BluetoothSocket mmSocket;
        BluetoothSocket tmp = null;
            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
//                tmp = device.createRfcommSocketToServiceRecord(device.getUuids()[0].getUuid());
                //reflection (getclass.getmethod)
                Method m = targetDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
                tmp = (BluetoothSocket) m.invoke(targetDevice, 1);
                Log.i(TAG, "RFCOMM Socket created");
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            mmSocket = tmp;

            if(mmSocket != null){
                try {
                    mmSocket.connect();
                    Log.i(TAG, "Connection successful");
                    success = true;
                } catch (IOException connectException) {
                    Log.i(TAG, connectException.getMessage());
//                    try {
//                        mmSocket.close();
//                    } catch (IOException closeException) { }
                }
            }
        try {
            mmSocket.close();
        } catch (IOException closeException) { }

        return success;
//        connectThread = new ConnectThread(targetDevice);
//        connectThread.start();
    }


//    private void manageConnectedSocket(BluetoothSocket connectedSocket){
//        connectedThread = new ConnectedThread(connectedSocket);
//        connectedThread.start();
//    }
//
//    public void closeConnection(){
//        connectThread.cancel();
//        connectedThread.cancel();
//    }

    public void setMacAddress(String address){
        this.macAddress = address;
    }

//    public void sendIrrigationData(Uri uri){
//        BufferedInputStream inputStream = null;
//        try
//        {
//            inputStream = new BufferedInputStream(context.getContentResolver().openInputStream(uri));
//            int bufferSize = 1024;
//            byte[] buffer = new byte[bufferSize];
//
//            // we need to know how may bytes were read to write them to the byteBuffer
//            int len = 0;
//            while ((len = inputStream.read(buffer)) != -1)
//            {
//                connectedThread.write(buffer);
//            }
//            inputStream.close();
//        }catch (IOException e){
//            Log.e(TAG, "Unable to send irrigation data");
//        }
//    }

//    /**
//     * Class to manage Bluetooth connection
//     * */
//    private class ConnectThread extends Thread {
//        private final BluetoothSocket mmSocket;
//        private final BluetoothDevice mmDevice;
//        private static final String TAG = "Connect Thread";
//
//        public ConnectThread(BluetoothDevice device) {
//            BluetoothSocket tmp = null;
//            mmDevice = device;
//            // Get a BluetoothSocket to connect with the given BluetoothDevice
//            try {
////                tmp = device.createRfcommSocketToServiceRecord(device.getUuids()[0].getUuid());
//                //reflection (getclass.getmethod)
//                Method m = device.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
//                tmp = (BluetoothSocket) m.invoke(device, 1);
//                Log.i(TAG, "RFCOMM Socket created");
//            } catch (Exception e) {
//                Log.e(TAG, e.getMessage());
//            }
//            mmSocket = tmp;
//        }
//
//        public void run() {
//            // Cancel discovery because it will slow down the connection
//            btAdapter.cancelDiscovery();
//
//            //try to connect, if not successful close;
//            try {
//                mmSocket.connect();
//                Log.i(TAG, "Connection successful");
//            } catch (IOException connectException) {
//                Log.i(TAG, connectException.getMessage());
//                try {
//                    mmSocket.close();
//                } catch (IOException closeException) { }
//                return;
//            }
//
//            // Do work to manage the connection (in a separate thread)
//            manageConnectedSocket(mmSocket);
//        }
//
//        /** Will cancel an in-progress connection, and close the socket */
//        public void cancel() {
//            try {
//                mmSocket.close();
//                Log.i(TAG, "Socket closed.");
//            } catch (IOException e) { }
//        }
//
//    }
//
//    private class ConnectedThread extends Thread {
//        private final String TAG = "Connected Thread";
//        private final BluetoothSocket mmSocket;
//        private final InputStream mmInStream;
//        private final OutputStream mmOutStream;
//
//        public ConnectedThread(BluetoothSocket socket) {
//            mmSocket = socket;
//            InputStream tmpIn = null;
//            OutputStream tmpOut = null;
//            Log.i(TAG, "Connected thread started");
//            // Get the input and output streams, using temp objects because
//            // member streams are final
//            try {
//                tmpIn = socket.getInputStream();
//                tmpOut = socket.getOutputStream();
//                Log.i(TAG, "I/O Streams initialized");
//            } catch (IOException e) { }
//
//            mmInStream = tmpIn;
//            mmOutStream = tmpOut;
//        }
//
//        public void run() {
//            byte[] buffer = new byte[1024];  // buffer store for the stream
//            int bytes; // bytes returned from read()
//
//            // Keep listening to the InputStream until an exception occurs
//            while (true) {
//                try {
//                    // Read from the InputStream
//                    bytes = mmInStream.read(buffer);
//                    String message = new String(buffer, 0, bytes);
//                    Log.i(TAG, message);
//                    // Send the obtained bytes to the UI activity
////                    mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
////                            .sendToTarget();
//                } catch (IOException e) {
//                    Log.e(TAG, "Unable to read from InputStream");
//                    break;
//                }
//            }
//        }
//
//        /* Call this from the main activity to send data to the remote device */
//        public void write(byte[] bytes) {
//            try {
//                mmOutStream.write(bytes);
//            } catch (IOException e) { }
//        }
//
//        /* Call this from the main activity to shutdown the connection */
//        public void cancel() {
//            try {
//                mmSocket.close();
//            } catch (IOException e) { }
//        }
//    }

}