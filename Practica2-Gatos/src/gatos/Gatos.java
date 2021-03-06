/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gatos;

import processing.core.PApplet;
import processing.core.PFont;

import java.util.LinkedList;
import java.util.Hashtable;

/**
 * Programa para mostrar el Árbol de búsqueda en el juego del gato utilizando BFS.
 * @author varriola
 */
public class Gatos extends PApplet {
    
    PFont fuente;              // Fuente para mostrar texto en pantalla

    int tamanioMosaico = 6;    // Tamanio de cada mosaico cuadrado (en pixeles).
    int profundidad = 0;       // Profundidad a la que se ha expandido el arbol.
    int diametroMaximo = 1;    // Mayor numero de estados expandidos a la misma profundidad.

    int anchoImagen;
    int altoImagen;
    int anchoGato, altoGato;           // Dimensiones en pixeles del dibujo de cada gato.
    boolean modificaVentana = false;   // Indica cuando las dimensiones del arbol se han incrementado.
    boolean genera = false;            // Bandera para solicitar la expansion del siguiente nivel.
    
    Gato gatoRaiz;                     // Estado inicial
    LinkedList<Gato> listaAbierta = new LinkedList();  // Nodos en el nivel mas profundo que no han sido expandidos.

    @Override
    public void setup() {
        anchoGato =  tamanioMosaico * 5;
        altoGato = tamanioMosaico * 10;
        anchoImagen = 100+30;
        altoImagen =  altoGato * 10;
        size(100, altoImagen + 30);
        frame.setResizable(true);
        frame.setSize(anchoImagen + 50, altoImagen + 50);
        setLocation(200, 200);
        background(200);
        fuente = createFont("Arial",12,true);

        gatoRaiz = new Gato();
        listaAbierta.add(gatoRaiz);
    }
    
    @Override
    public void draw() {
        try{
        if(genera) generaSiguienteNivel();
        if (modificaVentana){
          System.out.println("Profundidad " + profundidad + ':');
          System.out.println("Diametro maximo " + diametroMaximo + ". Nodos en el ultimo nivel: " + listaAbierta.size());
          anchoImagen = anchoGato * diametroMaximo + 30;
          frame.setSize(anchoImagen, altoImagen + 70);
          background(200);
          modificaVentana = false;
        }
        Gato actual;
        LinkedList<Gato> listaGatos = new LinkedList();
        listaGatos.add(gatoRaiz);
        int profundidadActual = 0;
        while(!listaGatos.isEmpty()) {
          int numGatos = listaGatos.size();  // Todos los gatos a esta profundidad
          int hijosIzquierda = 0; // Descendientes que iran siendo dibujados.
          for(int i = 0; i < numGatos; i++){
            actual = listaGatos.remove();
            if(actual.sucesores != null){
              listaGatos.addAll(actual.sucesores);
              int numHijos = actual.sucesores.size();
              for(int h = 0; h < numHijos; h++){
                line((int)((0.5+i)*anchoGato), altoGato * profundidadActual + 4 * tamanioMosaico,
                    (int)((0.5+hijosIzquierda)*anchoGato), altoGato * (profundidadActual + 1) + tamanioMosaico);
                    hijosIzquierda++;
              }
            }
              dibujaGato(actual, anchoGato * i, altoGato * profundidadActual);
          }
          profundidadActual++;
        }

        // Pintar informacion del modelo en la parte inferior de la ventana.
        stroke(0);
        fill(50);
        rect(0, altoImagen, anchoImagen, 32);
        fill(255);
        textFont(fuente,10);
        text("Profundidad: " + profundidad, 5, altoImagen + 12);
        text("Diametro: " + diametroMaximo, 5, altoImagen + 24);
      } catch (Exception e) {/*e.printStackTrace();*/}
    }
    
    protected void dibujaGato(Gato g, int x, int y){
        x += tamanioMosaico;
        y += tamanioMosaico;

        if(g.hayGanador){
          if(g.jugador1){
            fill(255,0,0);
            stroke(155,0,0);
          } else {
            fill(0,200,0);
            stroke(0,100,0);
          }
        } else if (g.tiradas == 9){
          fill(10,10,10);
          stroke(10,10,10);
        } else {
          fill(50);
          stroke(0);
        }

        // Dibuja el tablero
        int ancho = 3 * tamanioMosaico;
        for(int i = 0, y1 = y + tamanioMosaico, x1 = x + tamanioMosaico; i < 2; i++, y1 += tamanioMosaico, x1 += tamanioMosaico) {
          line(x, y1, x + ancho, y1);
          line(x1, y, x1, y + ancho);
        }
        for(int i = 0; i < 3; i++){
          for(int j = 0; j < 3; j++){
            int jugada = g.tablero[i][j]; 
            if(jugada == 0) continue;
            if(jugada == Gato.MARCA1) ellipse(x + tamanioMosaico/2 + (j * tamanioMosaico),
                                    y + tamanioMosaico/2 + (i * tamanioMosaico),
                                    tamanioMosaico/2, tamanioMosaico/2);
            else {
              int x1 = x + (j * tamanioMosaico);
              int y1 = y + (i * tamanioMosaico);
              line(x1, y1, x1 + tamanioMosaico, y1 + tamanioMosaico);
              line(x1, y1 + tamanioMosaico, x1 + tamanioMosaico, y1);
            }
          } 
        }
        fill(50);
        stroke(0);
    }
    
    /** Indica que se desea expandir el siguiente nivel. */
    @Override
    public void mouseClicked() {
      genera = true;
    }
    
    /**
    * Funcion encargada de generar el siguiente nivel en BFS.
    * Para todos los estados en la lista abierta:
    * los extrae, genera sus sucesores y agrega los estados nuevos al final de la lista.
    */
    protected void generaSiguienteNivel(){
        // Si ya se alcanza la profundidad maxima (9 jugadas) no hace nada.
        if(profundidad >= 9) return;

        // Genera sucesores.
        int numGatos = listaAbierta.size();
        for(int i = 0; i < numGatos; i++){
            Gato actual = listaAbierta.remove();
            LinkedList<Gato> sucesores = actual.generaSucesores();
            if(sucesores != null) listaAbierta.addAll(sucesores);
        }

        // Actualiza variables de estado para saber en que punto se encuentra el proceso
        // y si hay que hacer ajustes al lienzo de dibujo.
        profundidad++;
        numGatos = listaAbierta.size();
        if(numGatos > diametroMaximo) diametroMaximo = numGatos;
        genera = false;
        modificaVentana = true;
    }
    
    public static void main(String args[]) {
        PApplet.main(new String[] { "gatos.Gatos" });
    }
}
