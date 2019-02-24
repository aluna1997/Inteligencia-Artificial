/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ia;

/**
 *
 * @author default
 */
public class Celda {
    //Coordenadas de la cuadricula
      int celdaX, celdaY;
      //El estado nos ayuda a marcar si ya fue visitado
      boolean estado;
      //Muro abajo
      boolean abj;
      //Muro arriba
      boolean arr;
      //Muro izquierdo
      boolean izq;  
      //Muro derecho
      boolean der;  
        
      //Constructor de objeto celda
      Celda(int celdaX, int celdaY, boolean estado){
        this.celdaX = celdaX;
        this.celdaY = celdaY;
        this.estado = estado;
        arr = true;
        abj = true;
        izq = true;
        der = true;
        
      }
    
}
