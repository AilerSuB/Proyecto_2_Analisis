import java.util.ArrayList;
import java.util.Arrays;
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
        int dimension = 3;
        int nPoblacion = 30;
        int pesoGeneracion = 50;
        
        rompecabezas = puzzle.crearRompecabezas(dimension, 9); //Lista con el rompecabezas desordenado.
        
        ArrayList<LinkedList<Pieza>> poblacion = poblar(rompecabezas, nPoblacion);
        cruceBasadoPunto(poblacion, nPoblacion, pesoGeneracion, dimension);
    }  
    
    public static boolean contieneUnion (List<int[]> indicesUniones, int[] unionVerificar){
        for(int[] tempUnion : indicesUniones)
            if(Arrays.equals(tempUnion, unionVerificar))
                return true;
        return false;
    }
    
    public static void cruceBasadoPunto(ArrayList<LinkedList<Pieza>> poblacionBase, int nPoblacion, int pesoGeneracion, int dimension){
        ArrayList<LinkedList<Pieza>> poblacion = (ArrayList<LinkedList<Pieza>>) poblacionBase.clone();
        ArrayList<LinkedList<Pieza>> generaciones; 
        List<Integer> aptitudes;
        LinkedList<Pieza> padre = new LinkedList();
        LinkedList<Pieza> madre = new LinkedList();
        LinkedList<Pieza> hijo0;
        LinkedList<Pieza> hijo1;
        List<int[]> indicesUniones;
        boolean loop = true;
        int generacionActual = 1;
        int indice = (dimension*dimension)/2;
        int indicePadre = 0;
        int indiceMadre = 0;
        
        while (loop){
            System.out.println("Generacion: " + generacionActual);
            indicesUniones = new ArrayList();
            aptitudes = new ArrayList();
            generaciones = new ArrayList(); 
            
            for (int i = 0; i < poblacion.size(); i++){
                generaciones.add(poblacion.get(i));
                aptitudes.add(aptitud(poblacion.get(i), dimension));
            }
            
            while (indicesUniones.size() != pesoGeneracion/2) { // Lista con los indices a tomar para las uniones
                indicePadre = (int) ((Math.random()* (nPoblacion/2)));
                indiceMadre = (int) ((Math.random()* (nPoblacion/2))+(nPoblacion/2));
                int[] tempC = {indicePadre, indiceMadre};
                if (!contieneUnion(indicesUniones, tempC)) indicesUniones.add(tempC);
            }
            for(int[] tempUnion : indicesUniones){
                padre = poblacion.get(tempUnion[0]);
                madre = poblacion.get(tempUnion[1]);
                hijo0 = new LinkedList();
                hijo1 = new LinkedList();
                for (int i = 0; i < dimension*dimension; i++){
                    if (i <= indice) {
                        hijo0.add(padre.get(i));
                        hijo1.add(madre.get(i));
                    } else {
                        hijo0.add(madre.get(i));
                        hijo1.add(padre.get(i));
                    }
                }
                generaciones.add(hijo0); generaciones.add(hijo1);
                aptitudes.add(aptitud(hijo0, dimension)); aptitudes.add(aptitud(hijo1, dimension));
            }
            if (generacionActual == pesoGeneracion) {
                loop = false;
                System.out.println(aptitudes.toString());
                Collections.sort(aptitudes);
                Collections.reverse(aptitudes);
                System.out.println(aptitudes.toString());
            }
            generacionActual++;
        }
    }
    
    public static ArrayList<LinkedList<Pieza>> poblar(LinkedList<Pieza> rompecabezas, int nPoblacion){
        ArrayList<LinkedList<Pieza>> poblacion = new ArrayList();
        LinkedList<Pieza> tempRompecabezas = (LinkedList<Pieza>) rompecabezas.clone();
        poblacion.add(tempRompecabezas);
        while (poblacion.size() < nPoblacion){
            tempRompecabezas = (LinkedList<Pieza>) rompecabezas.clone();
            Collections.shuffle(tempRompecabezas);
            while (poblacion.contains(tempRompecabezas)){
                Collections.shuffle(tempRompecabezas);
            }
            poblacion.add(tempRompecabezas);
        } 
        return poblacion;
    }
    
    public static LinkedList<Pieza> mutar(LinkedList<Pieza> lista, int dimesion){
        int count = 0;
        int tamaño = dimesion*dimesion;
        LinkedList<Pieza> tempLista = (LinkedList<Pieza>) lista.clone();
        for (int i = 0; i < tamaño-1; i++){
            count++;
            if (count == dimesion && i < tamaño - 1) {
                if (tempLista.get(i).getPiezaAbajo() != tempLista.get(i+dimesion).getPiezaArriba() ) {
                    Collections.swap(tempLista, i, i+dimesion); break;}
                count = 0;
            } else if ( i < tamaño-dimesion) {
                if (tempLista.get(i).getPiezaDerecha() == tempLista.get(i+1).getPiezaIzquierda() ){
                    Collections.swap(tempLista, i, i+1); break;}
                if (tempLista.get(i).getPiezaAbajo() == tempLista.get(i+dimesion).getPiezaArriba() ){
                    Collections.swap(tempLista, i, i+dimesion); break;}
            }  else
                if (tempLista.get(i).getPiezaDerecha() == tempLista.get(i+1).getPiezaIzquierda() ) {
                    Collections.swap(tempLista, i, i+1); break;}
        }
        if (aptitud(tempLista, dimesion) > aptitud(lista, dimesion)) {
            return tempLista;
        } else {
            return lista;
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
    
    public static int aptitud(LinkedList<Pieza> lista, int dimension){
        int valor = 0;
        int count = 0;
        int tamaño = dimension*dimension;
        for (int i = 0; i < tamaño-1; i++){
            count++;
            if (count == dimension && i < tamaño - 1){
                if (lista.get(i).getPiezaAbajo() == lista.get(i+dimension).getPiezaArriba() ) valor++;
                count = 0;
            } else if ( i < tamaño-dimension) {
                if (lista.get(i).getPiezaDerecha() == lista.get(i+1).getPiezaIzquierda() ) valor++;
                if (lista.get(i).getPiezaAbajo() == lista.get(i+dimension).getPiezaArriba() ) valor++;
            } else {
                if (lista.get(i).getPiezaDerecha() == lista.get(i+1).getPiezaIzquierda() ) valor++;
            }
        }
        return valor;
    }
}
