package intentservicefilmes.exemplo.com.intentservicefilmes.remote;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.ResultReceiver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by BPardini on 21/12/2016.
 */

public class SampleIntentService extends IntentService {
    public static final int DOWNLOAD_ERROR = 10;
    public static final int DOWNLOAD_SUCCESS = 11;

    public SampleIntentService(){
        super(SampleIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Baixa a imagem e envia o resultado através do ResultReceiver para a MainActivity
        String url = intent.getStringExtra("url");
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        Bundle bundle = new Bundle();

        File downloadFile = new File(Environment.getExternalStorageDirectory().getPath() + "/IntentService_Example.png");
        if(downloadFile.exists()){
            downloadFile.delete();
            try{
                downloadFile.createNewFile();
                URL downloadURL = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) downloadURL
                        .openConnection();
                int responseCode = conn.getResponseCode();
                if (responseCode != 200)
                    throw new Exception("Error in connection");
                InputStream is = conn.getInputStream();
                FileOutputStream os = new FileOutputStream(downloadFile);
                byte buffer[] = new byte[1024];
                int byteCount;
                while ((byteCount = is.read(buffer)) != -1) {
                    os.write(buffer, 0, byteCount);
                }
                os.close();
                is.close();

                String filePath = downloadFile.getPath();
                bundle.putString("filePath", filePath);
                receiver.send(DOWNLOAD_SUCCESS, bundle);
            }
            catch (Exception e){
                receiver.send(DOWNLOAD_ERROR, Bundle.EMPTY);
                e.printStackTrace();
            }
        }
    }
}
