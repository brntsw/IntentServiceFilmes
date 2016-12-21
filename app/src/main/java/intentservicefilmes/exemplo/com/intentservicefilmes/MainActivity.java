package intentservicefilmes.exemplo.com.intentservicefilmes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import intentservicefilmes.exemplo.com.intentservicefilmes.remote.SampleIntentService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText urlText;
    ProgressBar pd;
    ImageView imgView;
    SampleResultReceiver resultReceiver;
    String defaultUrl = "http://developer.android.com/assets/images/dac_logo.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultReceiver = new SampleResultReceiver(new Handler());
        urlText = (EditText) findViewById(R.id.urlText);
        pd = (ProgressBar) findViewById(R.id.downloadPD);
        imgView = (ImageView) findViewById(R.id.imgView);
    }

    private class SampleResultReceiver extends ResultReceiver {

        public SampleResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            switch (resultCode) {
                case SampleIntentService.DOWNLOAD_ERROR:
                    Toast.makeText(getApplicationContext(), "error in download",
                            Toast.LENGTH_SHORT).show();
                    pd.setVisibility(View.INVISIBLE);
                    break;

                case SampleIntentService.DOWNLOAD_SUCCESS:
                    String filePath = resultData.getString("filePath");
                    Bitmap bmp = BitmapFactory.decodeFile(filePath);
                    if (imgView != null && bmp != null) {
                    imgView.setImageBitmap(bmp);
                    Toast.makeText(getApplicationContext(),
                            "image download via IntentService is done",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "error in decoding downloaded file",
                            Toast.LENGTH_SHORT).show();
                }
                pd.setIndeterminate(false);
                pd.setVisibility(View.INVISIBLE);

                break;
            }
            super.onReceiveResult(resultCode, resultData);
        }

    }

    @Override
    public void onClick(View view) {
        Intent startIntent = new Intent(MainActivity.this, SampleResultReceiver.class);
        startIntent.putExtra("url", TextUtils.isEmpty(urlText.getText()) ? defaultUrl : urlText.getText().toString());
        startService(startIntent);

        pd.setVisibility(View.VISIBLE);
        pd.setIndeterminate(true);
    }
}
