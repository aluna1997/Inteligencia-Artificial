/*
 * Practica 3 : Recursión y Backtrack
 */
package ia;
import processing.core.PApplet;
import processing.core.PFont;

/**
 * @author falv
 */
public class Laberinto extends PApplet{
    // Fuente para mostrar texto en pantalla
    PFont fuente;  
    // Altura (en celdas) de la cuadricula
    int alto = 28;
    // Anchura (en celdas) de la cuadricula
    int ancho = 50;
    // Tamanio de cada celda cuadrada (en pixeles)
    int celda = 15;
    // El objeto que representa el modelo de laberinto
    ModeloLaberinto modelo;
    
    
    /**
     * Configuraciones iniciales de la pantalla.
     */
    @Override
    public void setup() {
        //Tamanio de la pantalla que se mostrara, se agrega un cuadro 
        //para informacion adicional en pantalla
        size(ancho*celda, (alto*celda) + 50);
        //Fuente del texto que se muestra
        fuente = createFont("Arial",12,true);
        //Modelo usado dentro del cuadro
        modelo = new ModeloLaberinto(ancho, alto, celda);

    }
    
    /**
     * Pintar el mundo del modelo.
     */
    @Override
    public void draw() {
        
      //Damos un color al fondo y un relleno
      background(12,23,44);
        
      //Dibujamos el rectangulo actual que recorre
      rect(modelo.actual.posX*modelo.tamanio, modelo.actual.posY*modelo.tamanio, modelo.tamanio, modelo.tamanio);
      
      
        for (int fila = 0; fila < ancho; fila++)
	    for (int columna = 0; columna < alto; columna++){
                //Para cada celda de la pantalla
                stroke(221, 10, 110);
                //Pintamos las "paredes" derechas
		if (modelo.mundo[columna][fila].der){                   
		    line ((fila+1)*modelo.tamanio, columna*modelo.tamanio,  (fila+1)*modelo.tamanio,  (columna+1)*modelo.tamanio);
                }
                //Pintamos las "paredes" izquierdas
                if (modelo.mundo[columna][fila].izq){                   
		    line (fila*modelo.tamanio, columna*modelo.tamanio, fila*modelo.tamanio,  (columna+1)*modelo.tamanio); 
                }
                //Pintamos las "paredes" de abajo
                if (modelo.mundo[columna][fila].abj){                 
		    line (fila*modelo.tamanio,  (columna+1)*modelo.tamanio,  (fila+1)*modelo.tamanio,  (columna+1)*modelo.tamanio);
                }
                //Pintamos las "paredes" de arriba
                if (modelo.mundo[columna][fila].arr){
		    line (fila*modelo.tamanio, columna*modelo.tamanio,  (fila+1)*modelo.tamanio, columna*modelo.tamanio);
                }
		             
	    }
     
        // Informacion del modelo en la parte inferior de la ventana.   

        fill(50);
        rect(0, alto*celda, ancho*celda, alto*celda);
        fill(255);
        textFont(fuente,10);
        text("Cuadricula: " + ancho + " x " + alto, 10, (alto*celda) + 15);
        text("Practica 3 : Recursión y Backtrack",10,(alto*celda) + 30);
        frame.setTitle("Laberinto");
        modelo.CreaLaberinto();
       

        
    }
    
  
    public static void main(String[] args) {
        PApplet.main(new String[] { "ia.Laberinto" });
    }
    
}
