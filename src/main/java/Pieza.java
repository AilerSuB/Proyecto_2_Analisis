/**        
. La clase Pieza contiene los atributos y funciones de get para la posterior formación del rompecabezas. 
* Cada pieza se representa por cuatro atributos: piezaArriba (índice 0), piezaDerecha (índice 1), piezaAbajo (índice 2) y piezaIzquierda (índice 3),
* las cuales contendrán un número aleatorio, que cambiará según la secuencia conforme se va creando el rompecabezas. 
* Por tanto está clase fue creado con el fin de poder obtener de forma rápida y siguiendo las caracteristicas de  POO los valores de cada pieza según su posición.
* Ya que la lista que contiene el rompecabezas esta formado por un Linkedlist de Piezas
*/

public class Pieza implements Cloneable {
    //Atributos
    private int piezaArriba;
    private int piezaDerecha;
    private int piezaAbajo;
    private int piezaIzquierda;

    public Pieza(int piezaArriba, int piezaDerecha, int piezaAbajo, int piezaIzquierda) {
        this.piezaArriba = piezaArriba;
        this.piezaDerecha = piezaDerecha;
        this.piezaAbajo = piezaAbajo;
        this.piezaIzquierda = piezaIzquierda;
    }

    public Pieza() {
    }
    //Funciones get que retornan el número correspondiente a cada indice de la pieza, sea está derecha, izquierda, arriba y abajo.
    public int getPiezaArriba() {
        return piezaArriba;
    }

    public void setPiezaArriba(int piezaArriba) {
        this.piezaArriba = piezaArriba;
    }

    public int getPiezaDerecha() {
        return piezaDerecha;
    }

    public void setPiezaDerecha(int piezaDerecha) {
        this.piezaDerecha = piezaDerecha;
    }

    public int getPiezaAbajo() {
        return piezaAbajo;
    }

    public void setPiezaAbajo(int piezaAbajo) {
        this.piezaAbajo = piezaAbajo;
    }

    public int getPiezaIzquierda() {
        return piezaIzquierda;
    }

    public void setPiezaIzquierda(int piezaIzquierda) {
        this.piezaIzquierda = piezaIzquierda;
    }

    @Override
    public String toString() {
        return piezaArriba + "," + piezaDerecha + "," + piezaAbajo + "," + piezaIzquierda;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }
    
}
