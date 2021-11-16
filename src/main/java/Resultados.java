// Creado Domingo 14 nov 2021
/* @author Ailer */

/*        
. La clase Resultados contiene variables para el analisis de los cruces.
* Está clase fue creada con el fin de poder obtener de forma rápida y siguiendo 
* las caracteristicas de  POO los valores de analisis, para un fácil manejo de datos.
*/
public class Resultados implements Cloneable{
    //Atributos
    public int memoria = 0;
    public double tiempo = 0;
    public int asignaciones = 0;
    public int comparaciones = 0;
    public int lineas = 0;

    public Resultados(int memoria, double tiempo, int asignaciones, int comparaciones, 
            int lineas) {
        this.memoria = memoria;
        this.tiempo = tiempo;
        this.asignaciones = asignaciones;
        this.comparaciones = comparaciones;
        this.lineas = lineas;
    }

    public Resultados() { }
    
    /*
    * Funciones 'get' retornan  los atributos
    * Funciones 'set' cambian los atributos
    * Funciones 'aumentar' aumenta dado el parametro los atributos
    */
    public int getAsignaciones() {
        return asignaciones;
    }

    public void setAsignaciones(int asignaciones) {
        this.asignaciones = asignaciones;
    }

    public void aumentarAsignaciones(int aumento) {
        this.asignaciones += aumento;
    }

    public int getComparaciones() {
        return comparaciones;
    }

    public void setComparaciones(int comparaciones) {
        this.comparaciones = comparaciones;
    }
    
    public void aumentarComparaciones(int aumento) {
        this.comparaciones += aumento;
    }

    public int getMemoria() {
        return memoria;
    }

    public void setMemoria(int memoria) {
        this.memoria = memoria;
    }

    public void aumentarMemoria(int aumento) {
        this.memoria += aumento;
    }

    public double getTiempo() {
        return tiempo;
    }

    public void setTiempo(double tiempo) {
        this.tiempo = tiempo;
    }

    public int getLineas() {
        return lineas;
    }

    public void setLineas(int lineas) {
        this.lineas = lineas;
    }

    public void aumentarLineas(int aumento) {
        this.lineas += aumento;
    }
    
    // Metodo usado para clonar un objecto
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return (Resultados) super.clone();
    }
}
