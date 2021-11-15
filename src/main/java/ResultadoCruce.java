
import java.util.List;
/* @author Ailer */
public class ResultadoCruce  extends Resultados{
    public List<Rompecabezas> rompecabezas;
    public tipoCruce cruce;
    public int dimension;
    
    public ResultadoCruce(int memoria, double tiempo, int asignaciones, 
            int comparaciones, int totalInstrucciones, List<Rompecabezas> rompecabezas,tipoCruce cruce, int dimension) {
        super(memoria, tiempo, asignaciones, comparaciones, totalInstrucciones);
        this.rompecabezas = rompecabezas;
        this.cruce = cruce;
        this.dimension = dimension;
    }

    public ResultadoCruce(Resultados resultados, List<Rompecabezas> rompecabezas,tipoCruce cruce, int dimension) {
        super(resultados.getMemoria(), resultados.getTiempo(), resultados.getAsignaciones(), resultados.getComparaciones(), resultados.getLineas());
        this.rompecabezas = rompecabezas;
        this.cruce = cruce;
        this.dimension = dimension;
    }
    
    public String imprimirRompecabezas(){
        String strPuzzle = "\n";
        for (Rompecabezas puzzle: rompecabezas)
            strPuzzle += "\t" + puzzle.toString() + "\n";
        return strPuzzle;
    }
    
    @Override
    public String toString() {
        return "Tipo de cruce: " + cruce +
                "\nDimension: " + dimension + "x" + dimension +
                "\nMemoria: " + super.getMemoria() + 
                "\nTiempo: " +  super.getTiempo() + 
                "\nAsignaciones: " +  super.getAsignaciones() + 
                "\nComparaciones: " +  super.getComparaciones() + 
                "\nLineas: " +  super.getLineas() + 
                "\n5 mejores: " + imprimirRompecabezas();
    }
}
