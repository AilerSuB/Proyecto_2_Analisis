import java.util.ArrayList;
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
        
//        int d = 3; //borrar
//        puzzle.crearRompecabezas(d, 9); //Lista con el rompecabezas desordenado.
//        ArrayList<Rompecabezas> poblacionOriginal = poblar(puzzle, valores.get(d)[1]);
//        resultadosPoblacion.get(d).add(cruce((ArrayList<Rompecabezas>) poblacionOriginal.clone(), valores.get(d)[0], valores.get(d)[1], valores.get(d)[2], valores.get(d)[3], "MultiPunto"));
//        System.out.println(movimientosCruce);
//        System.out.println(resultadosPoblacion.get(d).get(0).toString());
        
        for (int dimension = 3; dimension < 8; dimension += 2) {
            puzzle.crearRompecabezas(dimension, 9); //Lista con el rompecabezas desordenado.
            ArrayList<Rompecabezas> poblacionOriginal = poblar(puzzle, valores.get(dimension)[1]);

            resultadosPoblacion.get(dimension).add(cruce((ArrayList<Rompecabezas>) poblacionOriginal.clone(), valores.get(dimension)[0], valores.get(dimension)[1], valores.get(dimension)[2], valores.get(dimension)[3], "Uniforme"));
            crucesYMutaciones.get(dimension).add(movimientosCruce);
            resultadosPoblacion.get(dimension).add(cruce((ArrayList<Rompecabezas>) poblacionOriginal.clone(), valores.get(dimension)[0], valores.get(dimension)[1], valores.get(dimension)[2], valores.get(dimension)[3], "BasadoPunto"));
            crucesYMutaciones.get(dimension).add(movimientosCruce);
            resultadosPoblacion.get(dimension).add(cruce((ArrayList<Rompecabezas>) poblacionOriginal.clone(), valores.get(dimension)[0], valores.get(dimension)[1], valores.get(dimension)[2], valores.get(dimension)[3], "MultiPunto"));
            crucesYMutaciones.get(dimension).add(movimientosCruce);
        }      
        System.out.println("Mutaciones y cruces");
        for (int dimension = 3; dimension < 8; dimension+=2) {
            System.out.println("---------------------------");
            System.out.println(crucesYMutaciones.get(dimension).get(0));
            System.out.println(crucesYMutaciones.get(dimension).get(1));
            System.out.println(crucesYMutaciones.get(dimension).get(2));  
            System.out.println("---------------------------\n");          
        }   
        for (int dimension = 3; dimension < 8; dimension+=2) {
            System.out.println("---------------------------");
            System.out.println("Dimension: " + dimension);
            System.out.println(resultadosPoblacion.get(dimension).get(0).toString());
            System.out.println(resultadosPoblacion.get(dimension).get(1).toString());
            System.out.println(resultadosPoblacion.get(dimension).get(2).toString());  
            System.out.println("---------------------------\n");          
        }
        
    }  
    
    public static boolean contieneUnion (List<int[]> indicesUniones, int[] unionVerificar){
        for(int[] tempUnion : indicesUniones)
            if( tempUnion[0] == unionVerificar[0] && tempUnion[1] == unionVerificar[1])//Arrays.equals(tempUnion, unionVerificar))
                return true;
        return false;
    }
        
    public static ResultadoCruce cruce(List<Rompecabezas> poblacion, 
            int dimension, int nPoblacion, int valorResuelto, int pesoGeneracion, String stringCruce) {
        movimientosCruce = "Cruce " + stringCruce +": ";
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
                
                generacion.add(tempRompecabezas);
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
                
                agregarMovimientoCruce(padre, madre, hijo0, hijo1, "MultiPunto", tmpPts);
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
            
            if (generacionActual == 50) {
                resultados.aumentarComparaciones(1);
                
                loop = false;
                resultados.aumentarAsignaciones(1);
            }
            resultados.aumentarLineas(1);
            
            generacionActual++;
            resultados.aumentarLineas(1);
        }
        
        for (Rompecabezas tempR: generacion){
            resultados.aumentarAsignaciones(1);
            
            tempR.setPuntuacion(aptitud(tempR.getRompecabezas(), dimension));
            resultados.aumentarLineas(1);
        }
        resultados.aumentarLineas(1);
        
        Collections.sort(generacion, Rompecabezas.ptsComparador);
        poblacion = poblacion.subList(0, nPoblacion); // Selecciona la poblacion con mejor puntuación
        resultados.aumentarAsignaciones(1);
        
        Collections.sort(poblacion, Rompecabezas.ptsComparador); // Lo ordena de mañor a menor   
        resultados.setTiempo((System.currentTimeMillis() - tiempoInicio) / 1000.0F);
        resultados.aumentarAsignaciones(1);
        
        resultados.aumentarLineas(4);
        return new ResultadoCruce(resultados, poblacion.subList(0, 5), tipoCruce.valueOf(stringCruce), dimension);
    }
        
   /* public static ResultadoCruce cruceBasadoPunto(List<Rompecabezas> poblacion, 
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
        int indice = (dimension*dimension)/2;
        resultados.aumentarMemoria(32);
        int generacionActual = 1;
        resultados.aumentarMemoria(32);
        
        int[] tempC = new int[2]; // variable temporal para almacenar indices
        resultados.aumentarMemoria(64);
        resultados.aumentarAsignaciones(12);
        resultados.aumentarLineas(12);
        
        while (loop){
            indicesUniones = new ArrayList();
            resultados.aumentarLineas(1);
            resultados.aumentarAsignaciones(1);
            resultados.aumentarComparaciones(1);
            
            for (Rompecabezas tempRompecabezas: poblacion){
                resultados.aumentarComparaciones(1);
                
                generacion.add(tempRompecabezas);
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
            
            for(int[] tempUnion : indicesUniones){ // Segmento donde se realiza el cruce
                resultados.aumentarComparaciones(1);
                
                padre = poblacion.get(tempUnion[0]).getRompecabezas();
                madre = poblacion.get(tempUnion[1]).getRompecabezas();
                hijo0 = new LinkedList();
                hijo1 = new LinkedList();
                resultados.aumentarLineas(4);
                resultados.aumentarAsignaciones(4);
                
                for (int i = 0; i < dimension*dimension; i++){
                    resultados.aumentarComparaciones(1);
                    resultados.aumentarAsignaciones(1);
					
                    if (i <= indice) {
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
                resultados.aumentarLineas(1);
                
                resultadosTemp = resultados; // Guardamos los valores medidos hasta el momento, para aumentarlo durante la medicion de puntos
                if (aptitud(hijo0, dimension) == aptitud(hijo1, dimension)) {
                    resultados.aumentarComparaciones(1);
                    
                    hijo0 = mutar(hijo0, dimension);
                    resultadosTemp = resultados; // Guardamos los valores medidos hasta el momento, para aumentarlo durante la mutacion
                    resultados.aumentarLineas(1);
                    resultados.aumentarAsignaciones(1);
                    
                    if (aptitud(hijo0, dimension) == aptitud(hijo1, dimension)){
                        resultados.aumentarComparaciones(1);
                        
                        hijo1 = mutar(hijo1, dimension); 
                        resultadosTemp = resultados; // Guardamos los valores medidos hasta el momento, para aumentarlo durante la mutacion
                        resultados.aumentarLineas(1);
                        resultados.aumentarAsignaciones(1);
                    }
                    resultados.aumentarLineas(1);
                }
                resultados.aumentarLineas(1);
                
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
            
            if (generacionActual == 50) {
                resultados.aumentarComparaciones(1);
                
                loop = false;
                resultados.aumentarAsignaciones(1);
            }
            resultados.aumentarLineas(1);
            
            generacionActual++;
            resultados.aumentarLineas(1);
        }
        
        for (Rompecabezas tempR: generacion){
            resultados.aumentarAsignaciones(1);
            
            tempR.setPuntuacion(aptitud(tempR.getRompecabezas(), dimension));
            resultados.aumentarLineas(1);
        }
        resultados.aumentarLineas(1);
        
        Collections.sort(generacion, Rompecabezas.ptsComparador);
        poblacion = poblacion.subList(0, nPoblacion); // Selecciona la poblacion con mejor puntuación
        resultados.aumentarAsignaciones(1);
        
        Collections.sort(poblacion, Rompecabezas.ptsComparador); // Lo ordena de mañor a menor   
        resultados.setTiempo((System.currentTimeMillis() - tiempoInicio) / 1000.0F);
        resultados.aumentarAsignaciones(1);
        
        resultados.aumentarLineas(4);
        return new ResultadoCruce(resultados, poblacion.subList(0, 5), tipoCruce.BasadoPunto);
    }
        
    public static ResultadoCruce cruceUniforme(List<Rompecabezas> poblacion, 
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
        
        int[] tempC = new int[2]; // variable temporal para almacenar indices
        resultados.aumentarMemoria(64);
        resultados.aumentarAsignaciones(11);
        resultados.aumentarLineas(11);
        
        while (loop){
            indicesUniones = new ArrayList();
            resultados.aumentarLineas(1);
            resultados.aumentarAsignaciones(1);
            resultados.aumentarComparaciones(1);
            
            for (Rompecabezas tempRompecabezas: poblacion){
                resultados.aumentarComparaciones(1);
                
                generacion.add(tempRompecabezas);
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
            
            for(int[] tempUnion : indicesUniones){ // Segmento donde se realiza el cruce
                resultados.aumentarComparaciones(1);
                
                padre = poblacion.get(tempUnion[0]).getRompecabezas();
                madre = poblacion.get(tempUnion[1]).getRompecabezas();
                hijo0 = new LinkedList();
                hijo1 = new LinkedList();
                resultados.aumentarLineas(4);
                resultados.aumentarAsignaciones(4);
                
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
                resultados.aumentarLineas(1);
                
                resultadosTemp = resultados; // Guardamos los valores medidos hasta el momento, para aumentarlo durante la medicion de puntos
                if (aptitud(hijo0, dimension) == aptitud(hijo1, dimension)) {
                    resultados.aumentarComparaciones(1);
                    
                    hijo0 = mutar(hijo0, dimension);
                    resultadosTemp = resultados; // Guardamos los valores medidos hasta el momento, para aumentarlo durante la mutacion
                    resultados.aumentarLineas(1);
                    resultados.aumentarAsignaciones(1);
                    
                    if (aptitud(hijo0, dimension) == aptitud(hijo1, dimension)){
                        resultados.aumentarComparaciones(1);
                        
                        hijo1 = mutar(hijo1, dimension); 
                        resultadosTemp = resultados; // Guardamos los valores medidos hasta el momento, para aumentarlo durante la mutacion
                        resultados.aumentarLineas(1);
                        resultados.aumentarAsignaciones(1);
                    }
                    resultados.aumentarLineas(1);
                }
                resultados.aumentarLineas(1);
                
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
            
            if (generacionActual == 50) {
                resultados.aumentarComparaciones(1);
                
                loop = false;
                resultados.aumentarAsignaciones(1);
            }
            resultados.aumentarLineas(1);
            
            generacionActual++;
            resultados.aumentarLineas(1);
        }
        
        for (Rompecabezas tempR: generacion){
            resultados.aumentarAsignaciones(1);
            
            tempR.setPuntuacion(aptitud(tempR.getRompecabezas(), dimension));
            resultados.aumentarLineas(1);
        }
        resultados.aumentarLineas(1);
        
        Collections.sort(generacion, Rompecabezas.ptsComparador);
        poblacion = poblacion.subList(0, nPoblacion); // Selecciona la poblacion con mejor puntuación
        resultados.aumentarAsignaciones(1);
        
        Collections.sort(poblacion, Rompecabezas.ptsComparador); // Lo ordena de mañor a menor   
        resultados.setTiempo((System.currentTimeMillis() - tiempoInicio) / 1000.0F);
        resultados.aumentarAsignaciones(1);
        
        resultados.aumentarLineas(4);
        return new ResultadoCruce(resultados, poblacion.subList(0, 5), tipoCruce.Uniforme);
    }
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
        //System.out.println();
    }
    
    public static void agregarMovimientoCruce(LinkedList<Pieza> padre, LinkedList<Pieza> madre, 
            LinkedList<Pieza> hijo0, LinkedList<Pieza> hijo1, String tipoCruce, int[] pts){
        //Rompecabezas tempR = new Rompecabezas();
        movimientosCruce += "\nTipo de cruce: " + tipoCruce;
        movimientosCruce += "\nPadre: " + " Puntuacion: " + pts[0] + " | " + padre; 
        movimientosCruce += "\nMadre: " + " Puntuacion: " + pts[1] + " | "+ madre; 
        movimientosCruce += "\nHijo0: " + " Puntuacion: " + pts[2] + " | "+ hijo0; 
        movimientosCruce += "\nHijo1: " + " Puntuacion: " + pts[3] + " | "+ hijo1; 

        movimientosCruce += "\n";
    }
    
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
