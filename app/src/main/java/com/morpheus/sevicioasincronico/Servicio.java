package com.morpheus.sevicioasincronico;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Morpheus on 06/07/2018.
 */

public class Servicio extends Service
{
    private static final String TAG = "TareaAsincrona";
    private MiTarea miTarea;

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        Toast.makeText(this, "Servicio iniciado", Toast.LENGTH_SHORT).show();

        //Instancia la tarea en segundo plano
        miTarea = new MiTarea();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        miTarea.execute();
        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        Toast.makeText(this, "Servicio parado", Toast.LENGTH_SHORT).show();

        //Se cancela la tarea
        miTarea.cancel(true);
    }

    //Creamos una clase AsynTask que contiene una tarea lenta
    private class MiTarea extends AsyncTask<String, String, String>
    {
        private int i;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            i = 1;
        }

        @Override
        protected String doInBackground(String... strings)
        {
            for (i = 1; i < 100; i++)
            {
                Log.i(TAG, "AsynTask: " + i);
                publishProgress(String.valueOf(i));

                //Se coloca un retardo de 5 segundos
                try
                {
                    Thread.sleep(1000);
                }catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values)
        {
            super.onProgressUpdate(values);
            Toast.makeText(Servicio.this, "Contando hasta: " + values[0], Toast.LENGTH_SHORT).show();
        }

        //En caso de cancelarse
        @Override
        protected void onCancelled(String s)
        {
            super.onCancelled(s);
            i = 101;
        }
    }
}
