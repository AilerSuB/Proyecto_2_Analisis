// Creado Domingo 14 nov 2021
/*       
. La clase ResultadosCruce, en una clase extendida de resultados
* Está clase fue creada con el fin de poder obtener de forma rápida y siguiendo 
* las caracteristicas de  POO los valores de analisis, para un fácil manejo de datos.
* Agregando una lista de rompecabezas, su dimension, tipo de cruce y el total de generaciones realizadas.
*/
import java.util.List;
/* @author Ailer */
public class ResultadoCruce  extends Resultados implements Cloneable{
    //Atributos
    public List<Rompecabezas> rompecabezas;
    public tipoCruce cruce;
    public int dimension;
    public int generaciones;
    
    public ResultadoCruce(int memoria, double tiempo, int asignaciones, 
            int comparaciones, int totalInstrucciones, List<Rompecabezas> rompecabezas,tipoCruce cruce, int dimension, int generaciones) {
        super(memoria, tiempo, asignaciones, comparaciones, totalInstrucciones);
        this.rompecabezas = rompecabezas;
        this.cruce = cruce;
        this.dimension = dimension;
        this.generaciones = generaciones;
    }

    public ResultadoCruce(Resultados resultados, List<Rompecabezas> rompecabezas,tipoCruce cruce, int dimension, int generaciones) {
        super(resultados.getMemoria(), resultados.getTiempo(), resultados.getAsignaciones(), resultados.getComparaciones(), resultados.getLineas());
        this.rompecabezas = rompecabezas;
        this.cruce = cruce;
        this.dimension = dimension;
        this.generaciones = generaciones;
    }

    ResultadoCruce() {
    }
    
    // Recorre la lista de rompebezas y almacena en un String sus datos para retornarlos posteriormente
    public String imprimirRompecabezas(){
        String strPuzzle = "\n";
        for (Rompecabezas puzzle: rompecabezas)
            strPuzzle += "    " + puzzle.toString() + "\n";
        return strPuzzle;
    }
    
    // Funcion encargada de retornar los valores de analisis, para imprimirlos posteriormente.
    @Override
    public String toString() {
        return "Tipo de cruce: " + cruce +
            "\nDimension: " + dimension + "x" + dimension +
            "\nGeneraciones alcanzadas: " + generaciones +
            "\nMemoria: " + super.getMemoria() + 
            "\nTiempo: " +  super.getTiempo() + " segundos"+
            "\nAsignaciones: " +  super.getAsignaciones() + 
            "\nComparaciones: " +  super.getComparaciones() + 
            "\nLineas: " +  super.getLineas() + 
            "\n5 mejores: " + imprimirRompecabezas();
    }
    
    // Metodo usado para clonar un objecto
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return (ResultadoCruce) super.clone();
    }
}
