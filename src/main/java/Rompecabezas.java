// Creado Sabodo 13 nov 2021
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class Rompecabezas implements Cloneable{
    //Está clase obtiene una función qeu se basa en la ejecución de 3 CASOS:
    //Caso#1: Se crea la primera pieza totalmente aleatoria, por tanto la fila y la columna es igual a 0
    //Caso #2: Cuando la fila es igual a cero pero la columna no, Se crea la primera fila donde todos sus números son aleatorios excepto el último, que es semejante al número aleatorio de la pieza anterior.
    // En todos los casos anteriores y proximos, cuando se crea el número de la pieza en la posición 3 se guarda esta en una lista para posteriormente usarse como el primer numero de la pieza en todas las filas diferentes a 0
    //Caso#3: Cuanto tanto la fila como la columna son distintos de cero, entonces su primer número sale de la lista el cuál posteriormente es eliminado de está, y su último número se obtiene de la pieza creada con anterioridad excepto
    // cuando esta pieza forma parte de la columna = 0, y los demás números son generados aleatoriamente.
    //
    // En caso de que el rompecabezas tenga un pieza repetida, se procede a rehacer el rompecabezas.
    private LinkedList<Pieza> rompecabezas = new LinkedList<>();// Lista con el rompecabezas ordenado.
    private LinkedList<Integer> inicio = new LinkedList<>();  // Lista que obtiene los  primeros números que van en todas piezas que no pertenecen a la fila 1.
    private int puntuacion;

    public Rompecabezas() {
    }
    
    public Rompecabezas (LinkedList<Pieza> rompecabezas){
        this.rompecabezas = rompecabezas;
    }
    
    public void crearRompecabezas (int cont, int num){ // Se encarga de crear el rompecabezas ordenado insertandolo en una lista.
        for(int i=0; i<cont; i++){ // Guía la fila
            for(int j=0; j<cont; j++){ //Guía la columna
                if(i==0 ){
                    if(j==0){ // CASO 1
                        Pieza trozo = new Pieza((int) ((Math.random()*num)+1),(int) ((Math.random()*num)+1),(int) ((Math.random()*num)+1),(int) ((Math.random()*num)+1));
                        inicio.add(trozo.getPiezaAbajo()); rompecabezas.add(trozo);}
                    else{ //CASO 2
                        Pieza trozo = new Pieza((int) ((Math.random()*num)+1),(int) ((Math.random()*num)+1),(int) ((Math.random()*num)+1),rompecabezas.get(j-1).getPiezaDerecha());
                        inicio.add(trozo.getPiezaAbajo()); rompecabezas.add(trozo);}
                    continue;}
                //CASO 3    
                if(j==0){ 
                    Pieza trozo = new Pieza(inicio.get(0),(int) ((Math.random()*num)+1),(int) ((Math.random()*num)+1),(int) ((Math.random()*num)+1));
                    inicio.add(trozo.getPiezaAbajo()); rompecabezas.add(trozo);}
                else{
                    int tamaño = rompecabezas.size(); //Guía la cantidad de datos que va teniendo el linkedList
                    Pieza trozo = new Pieza(inicio.get(0),(int) ((Math.random()*num)+1),(int) ((Math.random()*num)+1),rompecabezas.get(tamaño-1).getPiezaDerecha());
                    inicio.add(trozo.getPiezaAbajo()); rompecabezas.add(trozo);}
                inicio.remove(0);}
        }
        if (verificarPiezas()){
            rompecabezas = new LinkedList<>();
            inicio = new LinkedList<>(); 
            this.crearRompecabezas(cont, num);
        }
        Collections.shuffle(rompecabezas);
    }
    
    /*
    * Funciones 'get' retornan  los atributos
    * Funciones 'set' cambian los atributos
    */
    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public LinkedList<Pieza> getRompecabezas() {
        return rompecabezas;
    }

    public void setRompecabezas(LinkedList<Pieza> rompecabezas) {
        this.rompecabezas = rompecabezas;
    }
    
    /*
    * Está funcion se encarga de verificar que el rompecabezas no contenga ninguna pieza repetida.
    * En caso de encontrar una pieza repetida retorna 'true', caso contrario retorna 'false'
    */
    public boolean verificarPiezas(){
        LinkedList<String> listaPiezas = new LinkedList();
        for(Pieza piezaTemporal: rompecabezas){
            String piezaString = piezaTemporal.toString();
            if (listaPiezas.contains(piezaString))
                return true;
            listaPiezas.add(piezaTemporal.toString());
        }
        return false;
    }
    
    // Interfaz usada para comparar los rompecabezas y ordenarlas por su puntiación
    public static Comparator<Rompecabezas> ptsComparador = new Comparator<Rompecabezas>() {
        public int compare(Rompecabezas puzzle1, Rompecabezas puzzle2) {
	   return puzzle2.getPuntuacion()-puzzle1.getPuntuacion();
    }};
    
    // Funcion encargada de retornar la puntuacion junto a su rompecabezas, para imprimirlos posteriormente.
    @Override
    public String toString() {
        return "Puntuacion: " + puntuacion + " | Rompecabezas: " + rompecabezas.toString();
    }
    
    // Metodo usado para clonar un objecto
    @Override
    protected Rompecabezas clone() throws CloneNotSupportedException {
        return (Rompecabezas) super.clone(); 
    }
}
