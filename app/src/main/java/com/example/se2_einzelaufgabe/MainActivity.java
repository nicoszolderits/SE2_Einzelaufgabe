package com.example.se2_einzelaufgabe;

import android.graphics.text.TextRunShaper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
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
        String matrNr = getMatrNrFromField();
        Log.d("Input",matrNr);

        if(matrNr.isEmpty()) {
            Log.e("Info","Bitte geben sie eine Matrikelnummer ein");
            return;
        }

        try {
            Socket socket = connectSocket();

            if (socket == null || !socket.isConnected()) {
                throw new Exception("Fehler beim Verbinden.");
            }

            //if connected matrNr will be send
            Log.d("Info", "Matrikelnumber has been send to server");

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            writer.write(matrNr);
            writer.newLine();
            writer.flush();

            String response = reader.readLine();
            Log.d("Info", "response:"+response);
            writeToResponseField(response);

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

    private String getMatrNrFromField(){
        EditText inputField = findViewById(R.id.matrikelNumber_editText);
        return inputField.getText().toString();
    }

    private void writeToResponseField(String response){
        TextView responseField = findViewById(R.id.serverResponse_textView);
        responseField.setText(response);
    }
}