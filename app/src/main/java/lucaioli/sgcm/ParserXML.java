package lucaioli.sgcm;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ParserXML {

    private static final String ns=null;

    // Constantes del archivo Xml
    private static final String ETIQUETA_PRODUCTOS = "Productos";
    private static final String ETIQUETA_PRODUCTO = "Producto";
    private static final String ETIQUETA_CODIGO = "Codigo";
    private static final String ETIQUETA_DESCRIPCION = "Descripcion";
    private static final String ETIQUETA_BARCODE = "EAN";

    public List<Producto> parsear(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            parser.setInput(in, null);
            parser.nextTag();
            return leerProductos (parser);
        } finally {
            in.close();
        }
    }

    private List<Producto> leerProductos (XmlPullParser parser) throws XmlPullParserException, IOException  {

        List<Producto> listaProductos = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, ns, ETIQUETA_PRODUCTOS);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String nombreEtiqueta = parser.getName();
            // Buscar etiqueta <hotel>
            if (nombreEtiqueta.equals(ETIQUETA_PRODUCTO)) {
                listaProductos.add(leerProducto(parser));
            } else {
                saltarEtiqueta(parser);
            }
        }
        return listaProductos;
    }

    private Producto leerProducto(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, ETIQUETA_PRODUCTO);
        int Codigo = 0;
        String Descripcion = null;
        long Barcode = 0;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            switch (name) {
                case ETIQUETA_PRODUCTO:
                    Codigo = leerCodigo (parser);
                    break;
                case ETIQUETA_DESCRIPCION:
                    Descripcion = leerDescripcion(parser);
                    break;
                case ETIQUETA_BARCODE:
                    Barcode = leerBarcode(parser);
                    break;
                default:
                    saltarEtiqueta(parser);
                    break;
            }
        }
        return new Producto(Codigo,
                Descripcion,
                Barcode);
    }

    private int leerCodigo(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, ETIQUETA_PRODUCTO);
        int Codigo = Integer.parseInt(obtenerTexto(parser));
        parser.require(XmlPullParser.END_TAG, ns, ETIQUETA_PRODUCTO);
        return Codigo;
    }

    private String leerDescripcion(XmlPullParser parser) throws IOException, XmlPullParserException {
        String descripcion = null;
        parser.require(XmlPullParser.START_TAG, ns, ETIQUETA_DESCRIPCION);
        /* String prefijo = parser.getPrefix(); */
        descripcion = obtenerTexto(parser);
        parser.require(XmlPullParser.END_TAG, ns, ETIQUETA_DESCRIPCION);
        return descripcion;
    }

    private long leerBarcode(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, ETIQUETA_PRODUCTO);
        long Barcode = Long.parseLong(obtenerTexto(parser));
        parser.require(XmlPullParser.END_TAG, ns, ETIQUETA_PRODUCTO);
        return Barcode;
    }

    private String obtenerTexto(XmlPullParser parser) throws IOException, XmlPullParserException {
        String resultado = "";
        if (parser.next() == XmlPullParser.TEXT) {
            resultado = parser.getText();
            parser.nextTag();
        }
        return resultado;
    }

    private void saltarEtiqueta(XmlPullParser parser) throws XmlPullParserException, IOException {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                throw new IllegalStateException();
            }
            int depth = 1;
            while (depth != 0) {
                switch (parser.next()) {
                    case XmlPullParser.END_TAG:
                        depth--;
                        break;
                    case XmlPullParser.START_TAG:
                        depth++;
                        break;
                }
            }
        }
}