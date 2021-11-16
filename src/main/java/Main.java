import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
// Creado Sabodo 13 nov 2021
/* @author Ailer */
public class Main {
    // Atributos
    public static LinkedList<Rompecabezas> rompecabezas;
    public static Rompecabezas puzzle = new Rompecabezas();
    public static Map<Integer, List<ResultadoCruce>> resultadosPoblacion = new HashMap(){{ 
        put(3, new ArrayList<Resultados>());
        put(5, new ArrayList<Resultados>());
        put(7, new ArrayList<Resultados>());
    }};
    public static Map<Integer, ArrayList<String>> crucesYMutaciones = new HashMap(){{ // Almacena cada cruce y mutacion realizada, para su furuta impreción
        put(3, new ArrayList<String>());
        put(5, new ArrayList<String>());
        put(7, new ArrayList<String>());
    }};
    public static Resultados resultadosTemp = new Resultados();
    public static String movimientosCruce;
    
    public static void main(String[] args) throws CloneNotSupportedException {
        Map<Integer, int[]> valores = new HashMap() {{ 
            put(3, new int[] {3,30,12,50});// dimension nPoblacion valorResuelto pesoGeneracion
            put(5, new int[] {5,60,40,60});
            put(7, new int[] {7,90,84,70});
        }};
  
        for (int dimension = 3; dimension < 8; dimension += 2) {
            puzzle.crearRompecabezas(dimension, 9); //Lista con el rompecabezas desordenado.
            ArrayList<Rompecabezas> poblacionOriginal = poblar(puzzle, valores.get(dimension)[1]);
            
            ArrayList<Rompecabezas> poblacionTemp = (ArrayList<Rompecabezas>) poblacionOriginal.clone();
            poblacionTemp = (ArrayList<Rompecabezas>) poblacionOriginal.clone();
            resultadosPoblacion.get(dimension).add(cruce(poblacionTemp, valores.get(dimension)[0], valores.get(dimension)[1], valores.get(dimension)[2], valores.get(dimension)[3], "BasadoPunto"));
            crucesYMutaciones.get(dimension).add(movimientosCruce);
            
            poblacionTemp = (ArrayList<Rompecabezas>) poblacionOriginal.clone();
            resultadosPoblacion.get(dimension).add(cruce(poblacionTemp, valores.get(dimension)[0], valores.get(dimension)[1], valores.get(dimension)[2], valores.get(dimension)[3], "MultiPunto"));
            crucesYMutaciones.get(dimension).add(movimientosCruce);  
            
            poblacionTemp = (ArrayList<Rompecabezas>) poblacionOriginal.clone();
            resultadosPoblacion.get(dimension).add(cruce(poblacionTemp, valores.get(dimension)[0], valores.get(dimension)[1], valores.get(dimension)[2], valores.get(dimension)[3], "Uniforme"));
            crucesYMutaciones.get(dimension).add(movimientosCruce);    
        }
        
        System.out.println("---------------------------");
        System.out.println("Mutaciones y cruces");
        System.out.println("---------------------------");
        for (int dimension = 3; dimension < 8; dimension+=2) {
            System.out.println(crucesYMutaciones.get(dimension).get(0));
            System.out.println(crucesYMutaciones.get(dimension).get(1));
            System.out.println(crucesYMutaciones.get(dimension).get(2));  
            System.out.println("---------------------------\n");          
        }   
        
        for (int dimension = 3; dimension < 8; dimension += 2) {
            System.out.println("---------------------------");
            System.out.println("Dimension: " + dimension);
            System.out.println(resultadosPoblacion.get(dimension).get(0).toString());
            System.out.println(resultadosPoblacion.get(dimension).get(1).toString());
            System.out.println(resultadosPoblacion.get(dimension).get(2).toString());  
            System.out.println("---------------------------\n");          
        }
    }  
    
    /*
    * Funcion que se encarga de verificar si los indices dados 'unionVerificar' ya se encuentran en uso.
    * Recorre la lista 'indicesUniones' y en caso de encontrar que los indices 'unionVerificar' se estan usando, retorna 'true'
    * en caso contrario retorna 'false'
    * Puede ser un caso donde se de '0,3' y luego un '3,0' esto se da como 'false', dado que a la hora de realizar el cruce
    * el primer valor es padre y el segundo madre, por lo que solo se retorna true cuando las uninoes son excatamente igual a una previamente realizada.
    */
    public static boolean contieneUnion (List<int[]> indicesUniones, int[] unionVerificar){
        for(int[] tempUnion : indicesUniones)
            if( tempUnion[0] == unionVerificar[0] && tempUnion[1] == unionVerificar[1])//Arrays.equals(tempUnion, unionVerificar))
                return true;
        return false;
    }    
    
    /*
    * Funcion encargada de realizar los cruces, dados estos por 'stringCruce'
    * 
    * Retorna un objecto 'ResultadoCruce', esta contiene todos los datos para el analis del algoritmo
    */    
    public static ResultadoCruce cruce(ArrayList<Rompecabezas> poblacionBase, 
            int dimension, int nPoblacion, int valorResuelto, int pesoGeneracion, String stringCruce) throws CloneNotSupportedException {
        System.out.println("Realizando cruce " + stringCruce + ", dimension " + dimension + "x" + dimension + "...");
        movimientosCruce = "Cruce " + stringCruce + ": "; // Limpia la variable global para su uso posterior.
        Resultados resultados = new Resultados();
        ArrayList<Rompecabezas> poblacion = (ArrayList<Rompecabezas>) poblacionBase.clone();
        
        resultadosTemp = new Resultados(); // Limpia la variable global para su uso posterior.
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
        
        int[] tempC = new int[2]; // variable temporal para almacenar indices
        resultados.aumentarMemoria(64);
        
        int[] tmpPts = new int[4]; // variable temporal para almacenar puntuaciones
        resultados.aumentarMemoria(128);
        
        resultados.aumentarAsignaciones(13);
        resultados.aumentarLineas(13);
        
        while (loop){
            indicesUniones = new ArrayList();
            resultados.aumentarLineas(1);
            resultados.aumentarAsignaciones(1);
            resultados.aumentarComparaciones(1);
            
            for (Rompecabezas tempRompecabezas: poblacion){
                resultados.aumentarComparaciones(1);
                
                generacion.add(tempRompecabezas.clone());
                resultados.aumentarLineas(1);
                resultados.aumentarAsignaciones(1);
            }
            resultados.aumentarLineas(1);
            
            while (indicesUniones.size() != pesoGeneracion/2) { // Lista con los indices a tomar para las uniones
                resultados.aumentarComparaciones(1);
                tempC = new int[] {(int) ((Math.random() * (nPoblacion/2))), (int) ((Math.random() * (nPoblacion/2)) + (nPoblacion/2))};
                resultados.aumentarAsignaciones(1);
                
                if (!contieneUnion(indicesUniones, tempC)) {
                    indicesUniones.add(tempC);
                    resultados.aumentarAsignaciones(1);
                    resultados.aumentarComparaciones(1);
                }
            }
            
            resultados.aumentarLineas(1);
            
            for(int[] tempUnion : indicesUniones){ 
                resultados.aumentarComparaciones(1);
                padre = poblacion.get(tempUnion[0]).getRompecabezas();
                madre = poblacion.get(tempUnion[1]).getRompecabezas();
                hijo0 = new LinkedList();
                hijo1 = new LinkedList();
                tmpPts[0] = aptitudSinImpacto(padre, dimension); // Se califica al padre, sin aumentar las variables de analisis, ya que solo se usara para imprimir su puntuacion del cruce
                tmpPts[1] = aptitudSinImpacto(madre, dimension);
                
                resultados.aumentarLineas(6);
                resultados.aumentarAsignaciones(6);
                
                switch (stringCruce){ // Segmento donde se realiza el cruce
                    case "MultiPunto":
                        resultados.aumentarComparaciones(1);
                        for (int i = 0; i < dimension*dimension; i++){
                            resultados.aumentarComparaciones(1);
                            resultados.aumentarAsignaciones(1);

                            if (i%2 == 0) {
                                resultados.aumentarComparaciones(1);

                                hijo0.add(padre.get(i));
                                hijo1.add(madre.get(i));
                                resultados.aumentarLineas(2);
                            } else {
                                hijo0.add(madre.get(i));
                                hijo1.add(padre.get(i));
                                resultados.aumentarLineas(2);
                            }
                            resultados.aumentarLineas(1);
                        }
                    break;
                case "BasadoPunto":
                    resultados.aumentarComparaciones(1);
                    for (int i = 0; i < dimension*dimension; i++){
                        resultados.aumentarComparaciones(1);
                        resultados.aumentarAsignaciones(1);

                        if (i <= (dimension*dimension)/2) {
                            resultados.aumentarAsignaciones(1);
                            resultados.aumentarComparaciones(1);

                            hijo0.add(padre.get(i));
                            hijo1.add(madre.get(i));
                            resultados.aumentarLineas(2);
                        } else {
                            hijo0.add(madre.get(i));
                            hijo1.add(padre.get(i));
                            resultados.aumentarLineas(2);
                        }
                    }
                    break;
                case "Uniforme":
                    resultados.aumentarComparaciones(1);
                    for (int i = 0; i < dimension*dimension; i++){
                        resultados.aumentarComparaciones(1);
                        resultados.aumentarAsignaciones(1);

                        if ((int) (Math.random() * 2) == 0) {
                            resultados.aumentarComparaciones(1);
                            resultados.aumentarAsignaciones(1);

                            hijo0.add(padre.get(i));
                            hijo1.add(madre.get(i));
                            resultados.aumentarLineas(2);
                        } else {
                            hijo0.add(madre.get(i));
                            hijo1.add(padre.get(i));
                            resultados.aumentarLineas(2);
                        }
                        resultados.aumentarLineas(1);
                    }
                    break;
                    default: break;
                }
                tmpPts[2] = aptitud(hijo0, dimension);
                tmpPts[3] = aptitud(hijo1, dimension);
                resultados.aumentarLineas(3);
                resultados.aumentarAsignaciones(2);
                
                resultadosTemp = resultados; // Guardamos los valores medidos hasta el momento, para aumentarlo durante la medicion de puntos
                if (tmpPts[2] == tmpPts[3]) {
                    resultados.aumentarComparaciones(1);
                    
                    movimientosCruce += "\nPoblación empatada 1: " + hijo0 + " | Puntuación: " + tmpPts[2];
                    hijo0 = mutar(hijo0, dimension);
                    tmpPts[2] = aptitud(hijo0, dimension);
                    movimientosCruce += "\nMutacion: " + hijo0 + " | Puntuación: " + tmpPts[2] + "\n";
                    
                    resultadosTemp = resultados; // Guardamos los valores medidos hasta el momento, para aumentarlo durante la mutacion
                    resultados.aumentarLineas(2);
                    resultados.aumentarAsignaciones(2);
                    
                    if (tmpPts[2] == tmpPts[3]){
                        resultados.aumentarComparaciones(1);

                        movimientosCruce += "\nPoblación empatada 2: " + hijo1 + " | Puntuación: " + tmpPts[3];
                        hijo1 = mutar(hijo1, dimension); 
                        tmpPts[3] = aptitud(hijo1, dimension);
                        movimientosCruce += "\nMutacion: " + hijo1 + " | Puntuación: " + tmpPts[3] + "\n";

                        resultadosTemp = resultados; // Guardamos los valores medidos hasta el momento, para aumentarlo durante la mutacion
                        resultados.aumentarLineas(1);
                        resultados.aumentarAsignaciones(1);
                    }
                    resultados.aumentarLineas(1);
                }
                resultados.aumentarLineas(1);
                
                agregarMovimientoCruce(padre, madre, hijo0, hijo1, stringCruce, tmpPts);
                resultadosTemp = resultados; // Guardamos los valores medidos hasta el momento, para aumentarlo durante la medicion de puntos
                if (aptitud(hijo0, dimension) == valorResuelto || aptitud(hijo1, dimension) == valorResuelto) {
                    loop = false;
                    
                    resultados.aumentarAsignaciones(1);
                    resultados.aumentarComparaciones(1);
                }
                
                generacion.add(new Rompecabezas(hijo0));
                generacion.add(new Rompecabezas(hijo1));
                resultados.aumentarLineas(2);
                resultados.aumentarAsignaciones(2);
            }
            resultados.aumentarLineas(1);
            generacionActual++;
            if (generacionActual == 50) {
                resultados.aumentarComparaciones(1);
                
                loop = false;
                resultados.aumentarAsignaciones(1);
            }
            resultados.aumentarLineas(2);
        }
        
        for (Rompecabezas tempR: generacion){
            resultados.aumentarAsignaciones(1);
            
            tempR.setPuntuacion(aptitud(tempR.getRompecabezas(), dimension));
            resultados.aumentarLineas(1);
        }
        resultados.aumentarLineas(1);
        Collections.sort(generacion, Rompecabezas.ptsComparador); // Se acomoda la generacion de mayor a menor
        poblacion = new ArrayList();
        resultados.aumentarAsignaciones(1);
        
        for (int i = 0; i < 5; i++) // Selecciona la 5 poblaciones con mejor puntuación
            poblacion.add(generacion.get(i));
        Collections.sort(poblacion, Rompecabezas.ptsComparador); // Lo ordena de mañor a menor   
        resultados.setTiempo((System.currentTimeMillis() - tiempoInicio) / 1000.0F);
        
        resultados.aumentarLineas(4);
        System.out.println("\tCruce " + stringCruce + " finalizado.");
        return new ResultadoCruce(resultados, poblacion, tipoCruce.valueOf(stringCruce), dimension, generacionActual);
    }
    
    /*
    * Funcion encargada de completar una lista con rompezabezas dado el tamaño por nPoblacion
    * Cada repeticion clona el rompecabezas en una variable temporal.
    * Luego esta le aplica un shuffle (para acomodar de forma aleatoria el rompezabezas)
    * Se verifica que el rompecabezas no este almacenado previamente en la lista (esto para no tener rompecabezas repetidos)
    * En caso de no estar almacenado, este se almacena en la lista
    * Esto se repite hasta que el tamaño de la lista sea 'nPoblacion'
    * 
    * Retorna la lista de rompecabezas
    */
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
    
    /*
    * Funcion encargada de mmutar los rompecabezas, recorre la lista de piezas y verifica cuando tenga una casilla adyacente con valor diferente
    * Casa recorrido toma en cuenta los siguiente casos
    * Casos: 
    * A = pieza actual | B = -pieza con la que se compara
    *   1 - Verifica la casilla derecha con la casilla izquierda de la siguiente pieza (si esta no es un borde)
    *       Ejemplo: 
    *               A B - | 
    *               - - - | 
    *               - - - |
    *   2 - Verifica la casilla de abajo con la casilla de arriba de la pieza de abajo (indice actual + la dimension del rompecabezas) (si esta no es la ultima fila)
    *       Ejemplo:
    *               A - - | 
    *               B - - | 
    *               - - - |
    *   3 - Verifica las casilla de la ultima fila verifican la casilla derecha con la casilla izquierda de la siguiente pieza
    *       Ejemplo:
    *               - - - | 
    *               - - - | 
    *               A B - |
    * En el momento en que encuentre una casilla adyacente de valor diferente las intercambia siendo los siguientes casos
    * Casos: 
    * A = pieza actual | B = pieza con la que se compara
    *   1 - | Actual| | Nuevo |
    *       | A B - | | B A - |
    *       | - - - | | - - - |
    *       | - - - | | - - - |
    *
    *   2 - | Actual| | Nuevo |
    *       | A - - | | B - - |
    *       | B - - | | A - - |
    *       | - - - | | - - - |
    *
    *   3 - | Actual| | Nuevo |
    *       | - - - | | - - - |
    *       | - - - | | - - - |
    *       | A B - | | B A - |
    */
    public static LinkedList<Pieza> mutar(LinkedList<Pieza> lista, int dimesion){
        int count = 0;
        resultadosTemp.aumentarMemoria(32);
        int tamaño = dimesion*dimesion;
        resultadosTemp.aumentarMemoria(32);
        LinkedList<Pieza> tempLista = (LinkedList<Pieza>) lista.clone();
        resultadosTemp.aumentarLineas(3);
        resultadosTemp.aumentarAsignaciones(3);
        for (int i = 0; i < tamaño-1; i++){
            resultadosTemp.aumentarAsignaciones(1);
            resultadosTemp.aumentarComparaciones(1);
            count++;
            resultadosTemp.aumentarLineas(2);
            if (count == dimesion && i < tamaño - 1) {
                resultadosTemp.aumentarComparaciones(1);
                if (tempLista.get(i).getPiezaAbajo() != tempLista.get(i+dimesion).getPiezaArriba() ) {
                    resultadosTemp.aumentarComparaciones(1);
                    Collections.swap(tempLista, i, i+dimesion); 
                    resultadosTemp.aumentarLineas(1);
                    break;
                }
                count = 0;
                resultadosTemp.aumentarLineas(2);
            } else if ( i < tamaño-dimesion) {
                resultadosTemp.aumentarComparaciones(1);
                if (tempLista.get(i).getPiezaDerecha() == tempLista.get(i+1).getPiezaIzquierda() ){
                    resultadosTemp.aumentarComparaciones(1);
                    Collections.swap(tempLista, i, i+1); 
                    resultadosTemp.aumentarLineas(1);
                    break;
                }
                if (tempLista.get(i).getPiezaAbajo() == tempLista.get(i+dimesion).getPiezaArriba() ){
                    resultadosTemp.aumentarComparaciones(1);
                    Collections.swap(tempLista, i, i+dimesion); 
                    resultadosTemp.aumentarLineas(1);
                    break;
                }
                resultadosTemp.aumentarLineas(2);
            }  else
                if (tempLista.get(i).getPiezaDerecha() == tempLista.get(i+1).getPiezaIzquierda() ) {
                    resultadosTemp.aumentarComparaciones(1);
                    Collections.swap(tempLista, i, i+1); 
                    resultadosTemp.aumentarLineas(1);
                    break;
                }
            resultadosTemp.aumentarLineas(1);
        }
        if (aptitud(tempLista, dimesion) > aptitud(lista, dimesion)) {
            resultadosTemp.aumentarLineas(1);
            resultadosTemp.aumentarComparaciones(1);
            return tempLista;
        } else {
            resultadosTemp.aumentarLineas(1);
            return lista;
        }
    }
    
    /*
    * Funcion encargada de almacenar en una variable global, los datos de: padre, madre e hijos
    * Almacena la puntuación y su rompecabezas correspondiente.
    * Esto se hace para mantener un control de los movimientos hechos en cada generacion.
    */
    public static void agregarMovimientoCruce(LinkedList<Pieza> padre, LinkedList<Pieza> madre, 
            LinkedList<Pieza> hijo0, LinkedList<Pieza> hijo1, String tipoCruce, int[] pts){
        movimientosCruce += "\nTipo de cruce: " + tipoCruce;
        movimientosCruce += "\nPadre: " + " Puntuacion: " + pts[0] + " | " + padre; 
        movimientosCruce += "\nMadre: " + " Puntuacion: " + pts[1] + " | " + madre; 
        movimientosCruce += "\nHijo0: " + " Puntuacion: " + pts[2] + " | " + hijo0; 
        movimientosCruce += "\nHijo1: " + " Puntuacion: " + pts[3] + " | " + hijo1; 

        movimientosCruce += "\n";
    }
    
    /*
    * Recorre el rompecabezas verificando cuantas piezas tienen adyacente una casilla con una valor igual (pero no tienen impacto en el analisis del algoritmo)
    * Casos: 
    * A = pieza actual | B = pieza con la que se compara
    *   1 - La casilla derecha con la casilla izquierda de la siguiente pieza (si esta no es un borde)
    *       Ejemplo: 
    *               A B - | 
    *               - - - | 
    *               - - - |
    *   2 - La casilla de abajo con la casilla de arriba de la pieza de abajo (indice actual + la dimension del rompecabezas) (si esta no es la ultima fila)
    *       Ejemplo:
    *               A - - | 
    *               B - - | 
    *               - - - |
    *   3 - Las casilla de la ultima fila verifican la casilla derecha con la casilla izquierda de la siguiente pieza
    *       Ejemplo:
    *               - - - | 
    *               - - - | 
    *               A B - |
    * Retorna el valor total de las piezas verificadas, con casillas adyacentes iguales (dados los casos anteriores)
    */ 
    public static int aptitudSinImpacto(LinkedList<Pieza> lista, int dimension){
        int valor = 0;
        int count = 0;
        int tamaño = dimension*dimension;
        for (int i = 0; i < tamaño-1; i++){
            count++;
            if (count == dimension && i < tamaño - 1){
                if (lista.get(i).getPiezaAbajo() == lista.get(i+dimension).getPiezaArriba() )  valor++; 
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
    
    /*
    * Recorre el rompecabezas verificando cuantas piezas tienen adyacente una casilla con una valor igual
    * Casos: 
    * A = pieza actual | B = pieza con la que se compara
    *   1 - La casilla derecha con la casilla izquierda de la siguiente pieza (si esta no es un borde)
    *       Ejemplo: 
    *               A B - | 
    *               - - - | 
    *               - - - |
    *   2 - La casilla de abajo con la casilla de arriba de la pieza de abajo (indice actual + la dimension del rompecabezas) (si esta no es la ultima fila)
    *       Ejemplo:
    *               A - - | 
    *               B - - | 
    *               - - - |
    *   3 - Las casilla de la ultima fila verifican la casilla derecha con la casilla izquierda de la siguiente pieza
    *       Ejemplo:
    *               - - - | 
    *               - - - | 
    *               A B - |
    * Retorna el valor total de las piezas verificadas, con casillas adyacentes iguales (dados los casos anteriores)
    */ 
    public static int aptitud(LinkedList<Pieza> lista, int dimension){
        int valor = 0;
        resultadosTemp.aumentarMemoria(32);
        int count = 0;
        resultadosTemp.aumentarMemoria(32);
        int tamaño = dimension*dimension;
        resultadosTemp.aumentarMemoria(32);
        resultadosTemp.aumentarLineas(3);
        resultadosTemp.aumentarAsignaciones(3);
        for (int i = 0; i < tamaño-1; i++){
            resultadosTemp.aumentarAsignaciones(1);
            count++;
            if (count == dimension && i < tamaño - 1){
                resultadosTemp.aumentarComparaciones(1);
                if (lista.get(i).getPiezaAbajo() == lista.get(i+dimension).getPiezaArriba() )  {
                    resultadosTemp.aumentarComparaciones(1);
                    valor++; 
                    resultadosTemp.aumentarLineas(1);
                }
                count = 0;
                resultadosTemp.aumentarLineas(2);
            } else if ( i < tamaño-dimension) {
                resultadosTemp.aumentarComparaciones(1);
                if (lista.get(i).getPiezaDerecha() == lista.get(i+1).getPiezaIzquierda() ) {
                    resultadosTemp.aumentarComparaciones(1);
                    valor++; 
                    resultadosTemp.aumentarLineas(1);
                }
                if (lista.get(i).getPiezaAbajo() == lista.get(i+dimension).getPiezaArriba() ) {
                    resultadosTemp.aumentarComparaciones(1);
                    valor++; 
                    resultadosTemp.aumentarLineas(1);
                }
                resultadosTemp.aumentarLineas(3);
            } else {
                if (lista.get(i).getPiezaDerecha() == lista.get(i+1).getPiezaIzquierda() ) {
                    resultadosTemp.aumentarComparaciones(1);
                    valor++; 
                    resultadosTemp.aumentarLineas(1);
                }
                resultadosTemp.aumentarLineas(1);
            }
            resultadosTemp.aumentarLineas(4);
        }
        return valor;
    }
}
