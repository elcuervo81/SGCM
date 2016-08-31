package lucaioli.sgcm;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
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
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.net.URL;
import java.net.HttpURLConnection;
import android.os.AsyncTask;

public class Conteos extends AppCompatActivity {
    private Button scan_btn;
    private EditText txt_resultado;
    private final static String URL ="http://hoteles.xml";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        new TareaDescargaXml().execute(URL);
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

    private class TareaDescargaXml extends AsyncTask<String, Void, List<Producto>> {

        @Override
        protected List<Producto> doInBackground(String... urls) {
            try {
                return levantarProductos (urls[0]);
            } catch (IOException e) {
                return null; // null si hay error de red
            } catch (XmlPullParserException e) {
                return null; // null si hay error de parsing XML
            }
        }

   /*     @Override
        protected void onPostExecute(List<Producto> result) {
            // Actualizar contenido del proveedor de datos
            Producto.Productos = result;

        }*/
    }

    private List<Producto> levantarProductos  (String urlString) throws XmlPullParserException, IOException {
        File tarjeta = Environment.getExternalStorageDirectory();
        File file = new File(tarjeta.getAbsolutePath(),"codigos.xml");

        InputStream stream = null;
        ParserXML parserXML = new ParserXML();
        List<Producto> entries = null;

        try {
            stream = new FileInputStream (file);/*("/sdcard/DCIM/codigos.xml");*/
            entries = parserXML.parsear(stream);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            /* Toast.makeText(this, "Archivo no encontrado", Toast.LENGTH_LONG).show();*/
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            /*Toast.makeText(this, "Error Ent/Sal", Toast.LENGTH_LONG).show();*/
        }
        finally {
            if (stream != null) {
                stream.close();
            }

        }
        return entries;

    }
/*
    private InputStream descargarContenido(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Iniciar la petición
        conn.connect();
        return conn.getInputStream();
    }*/
}
