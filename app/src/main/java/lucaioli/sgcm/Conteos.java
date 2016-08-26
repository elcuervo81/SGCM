package lucaioli.sgcm;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.w3c.dom.Text;

public class Conteos extends AppCompatActivity {
    private Button scan_btn;
    private EditText txt_resultado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conteos);
        scan_btn = (Button) findViewById(R.id.scan_btn);
        txt_resultado = (EditText) findViewById(R.id.txt_resultado);
        final Activity activity = this;
        scan_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.PRODUCT_CODE_TYPES);
                integrator.setPrompt("Escanear");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.setOrientationLocked(false);
                integrator.initiateScan();

            }
        });
    }
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if  (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Escaneo Cancelado", Toast.LENGTH_LONG).show();
            }
            else {
                txt_resultado.setText(result.getContents());
            }
        }
        else {
              super.onActivityResult(requestCode,resultCode,data);
        }
    }
}
