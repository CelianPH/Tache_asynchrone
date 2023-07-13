package com.example.tcheasynchrone;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Button button;
    private ProgressBar progressBar;
    private MyTask myTask;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);
        progressBar = findViewById(R.id.progressBar);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myTask == null || myTask.getStatus() == AsyncTask.Status.FINISHED) {
                    myTask = new MyTask();
                    myTask.execute();
                }
            }
        });

        // Restaurer le message et l'état de la ProgressBar s'ils existent
        if (savedInstanceState != null) {
            message = savedInstanceState.getString("message");
            textView.setText(message);
            int progress = savedInstanceState.getInt("progress");
            progressBar.setProgress(progress);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("message", message);
        outState.putInt("progress", progressBar.getProgress());
    }

    private class MyTask extends AsyncTask<Void, Integer, String> {
        private long startTime;
        private int sleepTime = 5; // Temps de sommeil total en secondes
        private int progressInterval = 100; // Nombre de tranches pour mettre à jour la ProgressBar

        @Override
        protected void onPreExecute() {
            textView.setText("Lancement de la tâche...");
            button.setEnabled(false);
            progressBar.setMax(progressInterval);
            progressBar.setProgress(0);
        }

        @Override
        protected String doInBackground(Void... voids) {
            startTime = System.currentTimeMillis();

            int sleepInterval = sleepTime * 1000 / progressInterval;

            for (int i = 1; i <= progressInterval; i++) {
                try {
                    Thread.sleep(sleepInterval);
                    publishProgress(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return "Tâche terminée!";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progress = values[0];
            progressBar.setProgress(progress);
        }

        @Override
        protected void onPostExecute(String result) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            message = result + "\nTemps d'exécution: " + elapsedTime + " millisecondes";
            textView.setText(message);
            button.setEnabled(true);
        }
    }
}
