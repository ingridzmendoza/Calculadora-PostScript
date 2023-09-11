import java.io.FileWriter;
import java.io.IOException;

public class BitacoraDeErrores { //Clase para método de escribir errores en un .txt

    public void errorLog(String errorMsg) {
        String ARCHIVO_DE_ERRORES = "logs.txt"; //Archivo donde se guardan los errores
        try (FileWriter writer = new FileWriter(ARCHIVO_DE_ERRORES, true)) {
            writer.write(errorMsg + "\n");  //Escribir el mensaje de error en el archivo
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//end errorLog
}
