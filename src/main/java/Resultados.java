/* @author Ailer */
public class Resultados {
    public int memoria = 0;
    public double tiempo = 0;
    public int asignaciones = 0;
    public int comparaciones = 0;
    public int totalInstrucciones = 0;

    public Resultados(int memoria, double tiempo, int asignaciones, int comparaciones, 
            int totalInstrucciones) {
        this.memoria = memoria;
        this.tiempo = tiempo;
        this.asignaciones = asignaciones;
        this.comparaciones = comparaciones;
        this.totalInstrucciones = totalInstrucciones;
    }

    public Resultados() {
    }

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

    public int getTotalInstrucciones() {
        return totalInstrucciones;
    }

    public void setTotalInstrucciones(int totalInstrucciones) {
        this.totalInstrucciones = totalInstrucciones;
    }

    public void aumentarTotalInstrucciones(int aumento) {
        this.totalInstrucciones += aumento;
    }
}
