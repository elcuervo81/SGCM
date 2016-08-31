package lucaioli.sgcm;



import java.util.ArrayList;
import java.util.List;

public class Producto {
    private int Codigo;
    private String Descripcion;
    private long Barcode;

    public static List<Producto> Productos = new ArrayList<>();

    public Producto (int Codigo,
                     String Descripcion,
                     long Barcode) {
        this.Codigo = Codigo;
        this.Descripcion = Descripcion;
        this.Barcode = Barcode;
    }

    public int getCodigo (){
        return Codigo;
    }

    public String getDescripcion () {
        return Descripcion;
    }

    public long getBarcode() {
        return Barcode;
    }




}
