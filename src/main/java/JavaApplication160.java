import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author lvalerio
 */
public class JavaApplication160 {
    public static LinkedList<Pieza> rompecabezas;
    public static Rompecabezas puzzle = new Rompecabezas();
    
    public static void main(String[] args) throws CloneNotSupportedException {
        // TODO code application logic here
        
        LinkedList<Pieza> tempRompecabezas;
        int dimension = 3;
        int nPoblacion = 50;
        
        rompecabezas = puzzle.crearRompecabezas(dimension, 9); //Lista con el rompecabezas desordenado.
        
        ArrayList<LinkedList<Pieza>> poblacion = new ArrayList();
        tempRompecabezas = (LinkedList<Pieza>) rompecabezas.clone();
        poblacion.add(tempRompecabezas);
        while (poblacion.size() < nPoblacion){
            tempRompecabezas = (LinkedList<Pieza>) rompecabezas.clone();
            Collections.shuffle(tempRompecabezas);
            while (poblacion.contains(tempRompecabezas)){
                Collections.shuffle(tempRompecabezas);
            }
            poblacion.add(tempRompecabezas);
        } 
        System.out.println("Valor  : " + calificar(poblacion.get(10), dimension));
        imprimirLineal(poblacion.get(10));
        
        mutar(poblacion.get(10), dimension);
        System.out.println("Valor M: " + calificar(poblacion.get(10), dimension));
        imprimirLineal(poblacion.get(10));;
    }   
    
    public static void mutar(LinkedList<Pieza> lista, int n){
        int count = 0;
        int tamaño = n*n;
        for (int i = 0; i < tamaño-1; i++){
            count++;
            if (count == n && i < tamaño - 1) {
                if (lista.get(i).getPiezaAbajo() != lista.get(i+n).getPiezaArriba() ) {
                    Collections.swap(lista, i, i+n); return;}
                count = 0;
            } else if ( i < tamaño-n) {
                if (lista.get(i).getPiezaDerecha() == lista.get(i+1).getPiezaIzquierda() ){
                    Collections.swap(lista, i, i+1); return;}
                if (lista.get(i).getPiezaAbajo() == lista.get(i+n).getPiezaArriba() ){
                    Collections.swap(lista, i, i+n); return;}
            }  else
                if (lista.get(i).getPiezaDerecha() == lista.get(i+1).getPiezaIzquierda() ) {
                    Collections.swap(lista, i, i+1); return;}
        }
    }
        
    public static void imprimir(ArrayList<LinkedList<Pieza>> poblacion, int nPoblacion){
        for (int i = 1; i < nPoblacion; i++){
            System.out.println(poblacion.get(i));
        } 
    }
    
    public static void imprimir(LinkedList<Pieza> lista){
        lista.forEach((pieza) -> {
            System.out.println(pieza.toString());
        });
    }
    
    public static void imprimirLineal(LinkedList<Pieza> lista){
        lista.forEach((pieza) -> {
            System.out.print(pieza.toString() + "---");
        });
        System.out.println();
    }
    
    public static int calificar(LinkedList<Pieza> lista, int n){
        int valor = 0;
        int count = 0;
        int tamaño = n*n;
        for (int i = 0; i < tamaño-1; i++){
            count++;
            if (count == n && i < tamaño - 1){
                if (lista.get(i).getPiezaAbajo() == lista.get(i+n).getPiezaArriba() ) valor++;
                count = 0;
            } else if ( i < tamaño-n) {
                if (lista.get(i).getPiezaDerecha() == lista.get(i+1).getPiezaIzquierda() ) valor++;
                if (lista.get(i).getPiezaAbajo() == lista.get(i+n).getPiezaArriba() ) valor++;
            } else {
                if (lista.get(i).getPiezaDerecha() == lista.get(i+1).getPiezaIzquierda() ) valor++;
            }
        }
        return valor;
    }
}
