package com.example.se2_einzelaufgabe;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        final Button sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Info", sendButton.getText() + " Button gedrückt");

                Thread networkThread = new Thread() {
                    @Override
                    public void run() {
                        sendToServer();
                    }
                };

                Log.d("Info", "Thread wurde erstellt");
                networkThread.start();
                Log.d("Info", "Thread wurde gestartet");
            }
        });
    }

    private void sendToServer() {
        try {
            Socket socket = connectSocket();

            if (socket == null || !socket.isConnected()) {
                throw new Exception("Fehler beim Verbinden.");
            }
        } catch (Exception exc) {
            Log.d("Error", "MainActivity.createconnection" + exc.getMessage());
        }
    }

    private Socket connectSocket() {
        Socket socket;
        try {
            socket = new Socket("se2-submission.aau.at", 20080);
            Log.d("Info", "Connected to se2-submission.aau.at.");
            return socket;
        } catch (Exception exc) {
            Log.d("Error", "MainActivity.connectSocket" + exc.getMessage());
        }
        return null;
    }
}