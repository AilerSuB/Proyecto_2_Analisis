
import java.util.List;
/* @author Ailer */
public class ResultadoCruce  extends Resultados{
    public List<Rompecabezas> rompecabezas;
    public tipoCruce cruce;

    public ResultadoCruce(int memoria, double tiempo, int asignaciones, int comparaciones, int totalInstrucciones, List<Rompecabezas> rompecabezas,tipoCruce cruce) {
        super(memoria, tiempo, asignaciones, comparaciones, totalInstrucciones);
        this.rompecabezas = rompecabezas;
        this.cruce = cruce;
    }

    public ResultadoCruce(Resultados resultados, List<Rompecabezas> rompecabezas,tipoCruce cruce) {
        super(resultados.getMemoria(), resultados.getTiempo(), resultados.getAsignaciones(), resultados.getComparaciones(), resultados.getTotalInstrucciones());
        this.rompecabezas = rompecabezas;
        this.cruce = cruce;
    }
    
    public String imprimirRompecabezas(){
        String strPuzzle = "\n";
        for (Rompecabezas puzzle: rompecabezas)
            strPuzzle += "\t" + puzzle.toString() + "\n";
        return strPuzzle;
    }
    
    @Override
    public String toString() {
        return "Tipocruce: " + cruce +
                "\nMemoria: " + super.getMemoria() + 
                "\nTiempo: " +  super.getTiempo() + 
                "\nAsignaciones: " +  super.getAsignaciones() + 
                "\nComparaciones: " +  super.getComparaciones() + 
                "\nTotal Instrucciones: " +  super.getTotalInstrucciones() + 
                "\n5 mejores: " + imprimirRompecabezas();
    }
}
