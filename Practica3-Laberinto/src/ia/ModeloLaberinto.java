/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ia;

import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

/**
 *
 * @author default
 */
public class ModeloLaberinto{
      // Tamaño de celdas a lo largo y ancho de la cuadrícula.
      int ancho, alto; 
      //La pila nos ayudara a implementar backtrack
      Stack<Celda> visitados;
      // Tamaño en pixeles de cada celda.
      int tamanio;
      // Mundo de celdas donde habitan las astillas.
      Celda[][] mundo;
      //Objeto que nos ayudara a seguir el recorrido
      Actual actual;
      // Auxiliar para decisiones aleatorias.
      Random rnd = new Random();
      
      //Comstructor del modelo laberinto
      ModeloLaberinto(int ancho, int alto, int tamanio){
        this.ancho = ancho;
        this.alto = alto;
        this.tamanio = tamanio;
        //En nuestro mundo cada cuadro será un objeto celda
        mundo = new Celda[alto][ancho];
        for (int i = 0; i < alto; i++)
		for (int j = 0; j < ancho; j++)
		    mundo[i][j] = new Celda (j, i, true);
                    
        
        //Elegimos una celda aleatoria para comenzar el recorrido
        
        int anc = rnd.nextInt(ancho);
        int alt = rnd.nextInt(alto);
        actual = new Actual(anc,alt);
        //La marcamos como visitada
        mundo[alt][anc].estado = false;
        visitados = new Stack<>();
        //Insertamos la celda elegida en la pila
        visitados.push(mundo[alt][anc]);
        
      }
      
      
      boolean CeldaNoVisitada(int posX , int posY, int direccion){
         switch (direccion){
	    case 0: if (posY-1 < 0) return false;
		return mundo[posY-1][posX].estado;
                
	    case 1: if (posX+1 >= ancho) return false;
		return mundo[posY][posX+1].estado;
                
	    case 2: if (posY+1 >= alto) return false;
		return mundo[posY+1][posX].estado;
                
            case 3: if (posX-1 < 0) return false;
		return mundo[posY][posX-1].estado;
	    }
         return false;
         
     }
      
      
      
     void QuitaParedes(int posX, int posY, int direccion){
         switch(direccion){
             case 0: mundo[posY][posX].arr = false;
                     mundo[posY-1][posX].abj = false;
                     break;
             case 1: mundo[posY][posX].der = false;
                     mundo[posY][posX+1].izq = false;
                     break;
             case 2: mundo[posY][posX].abj = false;
                     mundo[posY+1][posX].arr = false;
                     break;
             case 3: mundo[posY][posX].izq = false;
                     mundo[posY][posX-1].der = false;
                     break;
            }
     }
     
     void Mover(Actual actual, int direccion){
            //Guardamos la celda actual
            visitados.push (mundo[actual.posY][actual.posX]);
	    switch (direccion) {
		//movimiento hacia arriba
	    case 0:  actual.posY--;
		break;
		//movimiento hacia derecha
	    case 1: actual.posX++;
		break;
		//movimiento hacia abajo
	    case 2:  actual.posY++;
		break;
		//movimiento hacia izquierda
	    case 3:  actual.posX--;
                break;
	    }
	    //La nueva celda ya es visitada (no disponible)
	    mundo[actual.posY][actual.posX].estado = false;
     }
      
      
      void CreaLaberinto(){
            //Bandera que nos indica cuendo movernos
            boolean mover = false;
            //Lista de las direcciones ya vistas
            LinkedList<Integer> visto = new LinkedList<> ();
            //Entero donde se almacena la direccion aleatoria
            int pos = 0;
            while (visto.size() < 4 && !mover){
                //Random que nos dice la posicion aleatoria a la que nos moveremos
                pos = rnd.nextInt(4);
                
                //Despues checamos que no hayamos visitado la pos y cambiamos 
                //la bandera a cierto
                if (CeldaNoVisitada (actual.posX, actual.posY, pos))
		    mover = true;
                
                //Si aun no hemos visto la posicion la agregamos a la lista
                if (!visto.contains(pos))
                    visto.add(pos);
                
	    }
	    if (mover){
		//Dejamos de dibujar las paredes
		QuitaParedes(actual.posX, actual.posY, pos);
		//Movemos la celda actual a la posicion resultante
                Mover(actual, pos);
	    }else if (!visitados.empty()){
		//Si no hay celdas vecinas a las que movernos nos regresamos a la
                //anterior aqui es donde usamos la tecnica de backtrack
		Celda a = visitados.pop();
		actual.posY = a.celdaY;
		actual.posX = a.celdaX;                
	    }
        } 
     
    }
