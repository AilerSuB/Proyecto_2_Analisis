import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/* @author Ailer */
public class JavaApplication160 {
    public static LinkedList<Rompecabezas> rompecabezas;
    public static Rompecabezas puzzle = new Rompecabezas();
    public static Map<Integer, List<ResultadoCruce>> resultadosPoblacion = new HashMap(){{ 
        put(3, new ArrayList<Resultados>());
        put(5, new ArrayList<Resultados>());
        put(7, new ArrayList<Resultados>());
    }};
    public static Resultados resultadosTemporal = new Resultados();
    
    public static void main(String[] args) throws CloneNotSupportedException {
        Map<Integer, int[]> valores = new HashMap() {{ 
            put(3, new int[] {3,30,12,50});// dimension nPoblacion valorResuelto pesoGeneracion
            put(5, new int[] {5,60,40,60});
            put(7, new int[] {7,90,84,70});
        }};
        
        puzzle.crearRompecabezas(7, 9); //Lista con el rompecabezas desordenado.
        ArrayList<Rompecabezas> poblacionOriginal = poblar(puzzle, valores.get(3)[1]);
        resultadosPoblacion.get(7).add(cruceUniforme((ArrayList<Rompecabezas>) poblacionOriginal.clone(), valores.get(3)[0], valores.get(3)[1], valores.get(3)[2], valores.get(3)[3]));
        System.out.println(resultadosPoblacion.get(7).get(0).toString());
        /*
        for (int dimension = 3; dimension < 8; dimension += 2) {
            puzzle.crearRompecabezas(dimension, 9); //Lista con el rompecabezas desordenado.
            ArrayList<Rompecabezas> poblacionOriginal = poblar(puzzle, valores.get(dimension)[1]);

            resultadosPoblacion.get(dimension).add(cruceUniforme((ArrayList<Rompecabezas>) poblacionOriginal.clone(), valores.get(dimension)[0], valores.get(dimension)[1], valores.get(dimension)[2], valores.get(dimension)[3]));
            resultadosPoblacion.get(dimension).add(cruceBasadoPunto((ArrayList<Rompecabezas>) poblacionOriginal.clone(), valores.get(dimension)[0], valores.get(dimension)[1], valores.get(dimension)[2], valores.get(dimension)[3]));
            resultadosPoblacion.get(dimension).add(cruceMultiPunto((ArrayList<Rompecabezas>) poblacionOriginal.clone(), valores.get(dimension)[0], valores.get(dimension)[1], valores.get(dimension)[2], valores.get(dimension)[3]));
        }     
        for (int dimension = 3; dimension < 8; dimension+=2) {
            System.out.println("---------------------------");
            System.out.println("Dimension: " + dimension);
            System.out.println(resultadosPoblacion.get(dimension).get(0).toString());
            System.out.println(resultadosPoblacion.get(dimension).get(1).toString());
            System.out.println(resultadosPoblacion.get(dimension).get(2).toString());  
            System.out.println("---------------------------\n");          
        }
        */
    }  
    
    public static boolean contieneUnion (List<int[]> indicesUniones, int[] unionVerificar){
        for(int[] tempUnion : indicesUniones)
            if( tempUnion[0] == unionVerificar[0] && tempUnion[1] == unionVerificar[1])//Arrays.equals(tempUnion, unionVerificar))
                return true;
        return false;
    }

    public static ResultadoCruce cruceMultiPunto(ArrayList<Rompecabezas> poblacion, 
            int dimension, int nPoblacion, int valorResuelto, int pesoGeneracion) {
        ArrayList<Rompecabezas> generacion = new ArrayList();
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
        int memoria = 0;
        double tiempo = 0;
        int asignaciones = 0;
        int comparaciones = 0;
        int totalInstrucciones = 0;
        
        while (loop){
            //System.out.println("Generacion: " + generacionActual);
            indicesUniones = new ArrayList();
            for (Rompecabezas tempRompecabezas: poblacion)
                generacion.add(tempRompecabezas);
            
            while (indicesUniones.size() != pesoGeneracion/2) { // Lista con los indices a tomar para las uniones
                indicePadre = (int) ((Math.random() * (nPoblacion/2)));
                indiceMadre = (int) ((Math.random() * (nPoblacion/2)) + (nPoblacion/2));
                int[] tempC = {indicePadre, indiceMadre};
                if (!contieneUnion(indicesUniones, tempC)) indicesUniones.add(tempC);
            }
            for(int[] tempUnion : indicesUniones){ // Segmento donde se realiza el cruce
                padre = poblacion.get(tempUnion[0]).getRompecabezas();
                madre = poblacion.get(tempUnion[1]).getRompecabezas();
                hijo0 = new LinkedList(); hijo1 = new LinkedList();
                
                for (int i = 0; i < dimension*dimension; i++){
                    if (i%2 == 0) {
                        hijo0.add(padre.get(i));
                        hijo1.add(madre.get(i));
                    } else {
                        hijo0.add(madre.get(i));
                        hijo1.add(padre.get(i));
                    }
                }
                
                if (aptitud(hijo0, dimension) == aptitud(hijo1, dimension)) {
                    hijo0 = mutar(hijo0, dimension);
                    if (aptitud(hijo0, dimension) == aptitud(hijo1, dimension))
                        hijo1 = mutar(hijo1, dimension); 
                }
                if (aptitud(hijo0, dimension) == valorResuelto || aptitud(hijo1, dimension) == valorResuelto)
                    loop = false;
                Rompecabezas tempPuzzle;
                tempPuzzle = new Rompecabezas(hijo0);
                generacion.add(tempPuzzle);
                tempPuzzle = new Rompecabezas(hijo1);
                generacion.add(tempPuzzle);
            }
            if (generacionActual == 50) {
                loop = false;
            }
            generacionActual++;
        }
        for (Rompecabezas tempR: generacion){
            tempR.setPuntuacion(aptitud(tempR.getRompecabezas(), dimension));
        }
        Collections.sort(generacion, Rompecabezas.ptsComparador);
        poblacion = new ArrayList();
        for (int i = 0; i < nPoblacion; i++) // Selecciona la poblacion con mejor puntuación
            poblacion.add(generacion.get(i));
        Collections.sort(poblacion, Rompecabezas.ptsComparador); // Lo ordena de mañor a menor
        return new ResultadoCruce(memoria, tiempo, asignaciones, comparaciones, totalInstrucciones, poblacion.subList(0, 5), tipoCruce.multiPunto);
    }
    
    public static ResultadoCruce cruceBasadoPunto(ArrayList<Rompecabezas> poblacion, 
            int dimension, int nPoblacion, int valorResuelto, int pesoGeneracion) {
        ArrayList<Rompecabezas> generacion = new ArrayList();
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
        int memoria = 0;
        double tiempo = 0;
        int asignaciones = 0;
        int comparaciones = 0;
        int totalInstrucciones = 0;
        
        while (loop){
            //System.out.println("Generacion: " + generacionActual);
            indicesUniones = new ArrayList();
            for (Rompecabezas tempRompecabezas: poblacion)
                generacion.add(tempRompecabezas);
            
            while (indicesUniones.size() != pesoGeneracion/2) { // Lista con los indices a tomar para las uniones
                indicePadre = (int) ((Math.random() * (nPoblacion/2)));
                indiceMadre = (int) ((Math.random() * (nPoblacion/2)) + (nPoblacion/2));
                int[] tempC = {indicePadre, indiceMadre};
                if (!contieneUnion(indicesUniones, tempC)) indicesUniones.add(tempC);
            }
            
            for(int[] tempUnion : indicesUniones){ // Segmento donde se realiza el cruce
                padre = poblacion.get(tempUnion[0]).getRompecabezas();
                madre = poblacion.get(tempUnion[1]).getRompecabezas();
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
                
                if (aptitud(hijo0, dimension) == aptitud(hijo1, dimension)) {
                    hijo0 = mutar(hijo0, dimension);
                    if (aptitud(hijo0, dimension) == aptitud(hijo1, dimension))
                        hijo1 = mutar(hijo1, dimension); 
                }
                if (aptitud(hijo0, dimension) == valorResuelto || aptitud(hijo1, dimension) == valorResuelto)
                    loop = false;
                Rompecabezas tempPuzzle;
                tempPuzzle = new Rompecabezas(hijo0);
                generacion.add(tempPuzzle);
                tempPuzzle = new Rompecabezas(hijo1);
                generacion.add(tempPuzzle);
            }
            if (generacionActual == 50) {
                loop = false;
            }
            generacionActual++;
        }
        for (Rompecabezas tempR: generacion){
            tempR.setPuntuacion(aptitud(tempR.getRompecabezas(), dimension));
        }
        Collections.sort(generacion, Rompecabezas.ptsComparador);
        poblacion = new ArrayList();
        for (int i = 0; i < nPoblacion; i++) // Selecciona la poblacion con mejor puntuación
            poblacion.add(generacion.get(i));
                Collections.sort(poblacion, Rompecabezas.ptsComparador); // Lo ordena de mañor a menor
        return new ResultadoCruce(memoria, tiempo, asignaciones, comparaciones, totalInstrucciones, poblacion.subList(0, 5), tipoCruce.basadoPunto);
    }
    
    public static ResultadoCruce cruceUniforme(ArrayList<Rompecabezas> poblacion, 
            int dimension, int nPoblacion, int valorResuelto, int pesoGeneracion) {
        Resultados resultados = new Resultados();
        double tiempoInicio = System.currentTimeMillis(); // No se cuenta como uso de memoria ya que esta se usa para evaluacion de tiempo de ejecucion
        
        LinkedList<Pieza> padre = new LinkedList();
        resultados.aumentarMemoria(128 * (dimension*dimension)); // El uso de memoria de cada pieza es 128 esto lo multiplicamos por el total de piezas
        
        LinkedList<Pieza> madre = new LinkedList();
        resultados.aumentarMemoria(128 * (dimension*dimension));
        
        LinkedList<Pieza> hijo0;
        resultados.aumentarMemoria(128 * (dimension*dimension));
        
        LinkedList<Pieza> hijo1;
        resultados.aumentarMemoria(128 * (dimension*dimension));
        
        ArrayList<Rompecabezas> generacion = new ArrayList(); // El uso de memoria, es el uso total de cada rompecabezas mutiplicado por el peso de generacion siendo (50, 60, 70) dependiendo de su dimension
        resultados.aumentarMemoria((128 * (dimension*dimension)) * pesoGeneracion);
        
        List<int[]> indicesUniones;
        boolean loop = true;
        int generacionActual = 1;
        resultados.aumentarMemoria(32);
        
        int indice = (dimension*dimension)/2;
        resultados.aumentarMemoria(32);
        
        int indicePadre = 0;
        resultados.aumentarMemoria(32);
        
        int indiceMadre = 0;
        resultados.aumentarMemoria(32);
        int[] tempC = new int[2]; // variable temporal para almacenar indices
        resultados.aumentarMemoria(64);

        while (loop){
            indicesUniones = new ArrayList();
            for (Rompecabezas tempRompecabezas: poblacion){
                generacion.add(tempRompecabezas);
            }
            
            while (indicesUniones.size() != pesoGeneracion/2) { // Lista con los indices a tomar para las uniones
                indicePadre = (int) ((Math.random() * (nPoblacion/2)));
                indiceMadre = (int) ((Math.random() * (nPoblacion/2)) + (nPoblacion/2));
                tempC = new int[] {indicePadre, indiceMadre};
                if (!contieneUnion(indicesUniones, tempC)) indicesUniones.add(tempC);
            }
            
            for(int[] tempUnion : indicesUniones){ // Segmento donde se realiza el cruce
                padre = poblacion.get(tempUnion[0]).getRompecabezas();
                madre = poblacion.get(tempUnion[1]).getRompecabezas();
                hijo0 = new LinkedList();
                hijo1 = new LinkedList();
                for (int i = 0; i < dimension*dimension; i++){
                    if ((int) (Math.random() * 2) == 0) {
                        hijo0.add(padre.get(i));
                        hijo1.add(madre.get(i));
                    } else {
                        hijo0.add(madre.get(i));
                        hijo1.add(padre.get(i));
                    }
                }
                resultadosTemporal = resultados; // Guardamos los valores medidos hasta el momento, para aumentarlo durante la medicion de puntos
                if (aptitud(hijo0, dimension) == aptitud(hijo1, dimension)) {
                    hijo0 = mutar(hijo0, dimension);
                    resultadosTemporal = resultados; // Guardamos los valores medidos hasta el momento, para aumentarlo durante la mutacion
                    if (aptitud(hijo0, dimension) == aptitud(hijo1, dimension)){
                        hijo1 = mutar(hijo1, dimension); 
                        resultadosTemporal = resultados; // Guardamos los valores medidos hasta el momento, para aumentarlo durante la mutacion
                    }
                }
                
                resultadosTemporal = resultados; // Guardamos los valores medidos hasta el momento, para aumentarlo durante la medicion de puntos
                if (aptitud(hijo0, dimension) == valorResuelto || aptitud(hijo1, dimension) == valorResuelto)
                    loop = false;
                
                Rompecabezas tempPuzzle;
                tempPuzzle = new Rompecabezas(hijo0);
                generacion.add(tempPuzzle);
                tempPuzzle = new Rompecabezas(hijo1);
                generacion.add(tempPuzzle);
            }
            if (generacionActual == 50) {
                loop = false;
            }
            generacionActual++;
        }
        
        for (Rompecabezas tempR: generacion){
            tempR.setPuntuacion(aptitud(tempR.getRompecabezas(), dimension));
        }
        Collections.sort(generacion, Rompecabezas.ptsComparador);
        poblacion = new ArrayList();
        
        for (int i = 0; i < nPoblacion; i++) // Selecciona la poblacion con mejor puntuación
            poblacion.add(generacion.get(i));
        Collections.sort(poblacion, Rompecabezas.ptsComparador); // Lo ordena de mañor a menor   
        resultados.setTiempo((System.currentTimeMillis() - tiempoInicio) / 1000.0F);
        return new ResultadoCruce(resultados, poblacion.subList(0, 5), tipoCruce.uniforme);
    }
        
    public static ArrayList<Rompecabezas> poblar(Rompecabezas rompecabezas, int nPoblacion){
        ArrayList<Rompecabezas> poblacion = new ArrayList();
        LinkedList<Pieza> tempListPiezas = (LinkedList<Pieza>) rompecabezas.getRompecabezas().clone();
        Rompecabezas tempRompecabezas = new Rompecabezas(tempListPiezas);
        poblacion.add(tempRompecabezas);
        while (poblacion.size() < nPoblacion){
            tempListPiezas = (LinkedList<Pieza>) rompecabezas.getRompecabezas().clone();
            Collections.shuffle(tempListPiezas);
            tempRompecabezas = new Rompecabezas(tempListPiezas);
            while (poblacion.contains(tempRompecabezas)){
                Collections.shuffle(tempListPiezas);
                tempRompecabezas = new Rompecabezas(tempListPiezas);
            }
            poblacion.add(tempRompecabezas);
        } 
        return poblacion;
    }
    
    public static LinkedList<Pieza> mutar(LinkedList<Pieza> lista, int dimesion){
        int count = 0;
        resultadosTemporal.aumentarMemoria(32);
        int tamaño = dimesion*dimesion;
        resultadosTemporal.aumentarMemoria(32);
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
        resultadosTemporal.aumentarMemoria(32);
        int count = 0;
        resultadosTemporal.aumentarMemoria(32);
        int tamaño = dimension*dimension;
        resultadosTemporal.aumentarMemoria(32);
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
