/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gatos;

import java.util.LinkedList;

/**
 * Clase para representar un estado del juego del gato. 
 * Cada estado sabe como generar a sus sucesores.
 * @author Vero
 */
public class Gato {
    
    public static final int MARCA1 = 1;             // Numero usado en el tablero del gato para marcar al primer jugador.
    public static final int MARCA2 = 4;             // Se usan int en lugar de short porque coincide con el tamaño de la palabra, el codigo se ejecuta ligeramente mas rapido.
    
    int[][] tablero = new int[3][3];     // Tablero del juego
    Gato padre;                          // Quien genera este estado.
    LinkedList<Gato> sucesores;          // Posibles jugadas desde este estado.
    boolean jugador1 = false;            // Jugador que tira en este tablero.
    boolean hayGanador = false;          // Indica si la ultima tirada produjo un ganador.
    int tiradas = 0;                     // Numero de casillas ocupadas.
  
    /** Constructor del estado inicial. */
    Gato() {}

    /** Constructor que copia el tablero de otro gato y el numero de tiradas */
    Gato(Gato g){
      for(int i = 0; i < 3; i++){
        for(int j = 0; j < 3; j++){
          tablero[i][j] = g.tablero[i][j];
        }
      }
      tiradas = g.tiradas;
    }

    /** Indica si este estado tiene sucesores expandidos. */
    int getNumHijos(){
      if(sucesores != null) return sucesores.size();
      else return 0;
    }

    /* Funcion auxiliar.
     * Dada la ultima posicion en la que se tira y la marca del jugador
     * calcula si esta jugada produjo un ganador y actualiza el atributo correspondiente.
     * 
     * Esta funcion debe ser lo mas eficiente posible para que la generacion del arbol no sea demasiado lenta.
     */
    void hayGanador(int x, int y, int marca){
      // Horizontal
      if (tablero[y][(x + 1) % 3] == marca && tablero[y][(x + 2) % 3] == marca) { hayGanador = true; return; }
      // Vertical
      if (tablero[(y + 1) % 3][x] == marca && tablero[(y + 2) % 3][x] == marca) { hayGanador = true; return; }
      // Diagonal
      if((x == 1 && y != 1) || (y == 1 && x!= 1)) return; // No pueden hacer diagonal
      // Centro y esquinas
      if(x == 1 && y == 1){
        // Diagonal \
        if(tablero[0][0] == marca && tablero[2][2] == marca) { hayGanador = true; return; }
        if(tablero[2][0] == marca && tablero[0][2] == marca) { hayGanador = true; return; }
      } else if (x == y){
        // Diagonal \
        if (tablero[(y + 1) % 3][(x + 1) % 3] == marca && tablero[(y + 2) % 3][(x + 2) % 3] == marca) { hayGanador = true; return; }
      } else {
        // Diagonal /
        if (tablero[(y + 2) % 3][(x + 1) % 3] == marca && tablero[(y + 1) % 3][(x + 2) % 3] == marca) { hayGanador = true; return; }
      }
    }

    /* Funcion auxiliar.
     * Coloca la marca del jugador en turno para este estado en las coordenadas indicadas.
     * Asume que la casilla esta libre.
     * Coloca la marca correspondiente, verifica y asigna la variable si hay un ganador.
     */
    void tiraEn(int x, int y){
      tiradas++;
      int marca = (jugador1) ? MARCA1 : MARCA2;
      tablero[y][x] = marca;
      hayGanador(x,y, marca);
    }

    /** ------- *** ------- *** -------
     * Este es el metodo que se debera dejar como practica.
     * ------- *** ------- *** -------
     * Crea la lista sucesores y agrega a todos los estados que sujen de tiradas validas.
     * Se consideran tiradas validas a aquellas en una casilla libre.
     * Ademas, se optimiza el proceso no agregando estados con jugadas simetricas.
     * Los estados nuevos tendran una tirada mas y el jugador en turno sera el jugador contrario.
     */
    LinkedList<Gato> generaSucesores(){
      //Primero debemos preguntar si ya hay un ganador o se agotaron los tiros, 
      //ya que en estos casos no existen sucesores.
      if(hayGanador || tiradas == 9){
          return null;
      }else{
          //Lista donde guardaremos los sucesores
          this.sucesores = new LinkedList<>();
          //Iteramos el gato (cuadricula de 3x3)
          for(int i = 0; i < 3; i++){
              for(int j = 0; j < 3; j++){
                  //Como se indica en la descripcion del metodo para que sea
                  //un estado valido la casilla no es vacia
                  if(tablero[i][j] == 0){
                    //Creamos un objeto de tipo gato
                    Gato g = new Gato(this);
                    //El jugador cambia
                    g.jugador1 = !jugador1;
                    //Sera el nuevo padre de los sucesores
                    g.padre = this;
                    //Marcamos el tiro
                    g.tiraEn(j,i);
                    //Usaremos una bandera para identificar cuando los gatos son iguales
                    boolean iguales = false;
                    //Iteramos sobre la lista de sucesores
                    for(Gato gat : sucesores){
                      //Preguntamos si g es igual a algun sucesor
                      if(gat.equals(g)){
                          iguales = true;
                        }
                    }
                    // Si el gato g es autentico lo agregamos a la lista de sucesores
                    if(!iguales){
                      sucesores.add(g);
                    } 
                }
              }
          }
            return sucesores;
        }
    }


    // ------- *** ------- *** -------
    // Serie de funciones que revisan la equivalencia de estados considerando las simetrias de un cuadrado.
    // ------- *** ------- *** -------
    // http://en.wikipedia.org/wiki/Examples_of_groups#The_symmetry_group_of_a_square_-_dihedral_group_of_order_8
    // ba es reflexion sobre / y ba3 reflexion sobre \.

    /** Revisa si ambos gatos son exactamente el mismo. */
    boolean esIgual(Gato otro){
      for(int i = 0; i < 3; i++){
        for(int j = 0; j < 3; j++){
          if(tablero[i][j] != otro.tablero[i][j]) return false;
        }
      }
      return true;
    }

    /** Al reflejar el gato sobre la diagonal \ son iguales (ie traspuesta) */
    boolean esSimetricoDiagonalInvertida(Gato otro){
      for(int i = 0; i < 3; i++){
        for(int j = 0; j < 3; j++){
          if(this.tablero[i][j] != otro.tablero[j][i])
              return false;
        }
      }
        return true;
    }

    /** Al reflejar el gato sobre la diagonal / son iguales (ie traspuesta) */
    boolean esSimetricoDiagonal(Gato otro){
        int x = 2;
        int y = 2;
        for(int i = 0; i < 3; i++){
            if (y == -1){
                y = 2;
            }  
        for(int j = 0; j < 3; j++){            
          if(tablero[i][j] != otro.tablero[x][y]){ 
              return false;
          }else{
                x--; 
                if(x ==-1){
                    x = 2;
                }      
            }
        }
         y--;
      }
      
      return true;
    }

    /** Al reflejar el otro gato sobre la vertical son iguales */
    boolean esSimetricoVerticalmente(Gato otro){
      int x = 2;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(tablero[i][j] != otro.tablero[i][x]) {
                    return false;
                }else{
                       x--; 
                       if(x == -1){
                          x = 2;
                }
            }
        }   
      }
    return true;
   }

    /** Al reflejar el otro gato sobre la horizontal son iguales */
    boolean esSimetricoHorizontalmente(Gato otro){
        int x = 0;
        int y = 2;
        for(int i = 0; i < 3; i++){
            if (y == -1){
                y = 2;
            }   
        for(int j = 0; j < 3; j++){                   
          if(tablero[i][j] != otro.tablero[y][x]){
              return false;
          }else{
                x++;
                if(x == 3){
                    x = 0;
                }
            }
        }
        y--;
     }
     return true;
    }

    /** Rota el otro tablero 90Â° en la direcciÃ³n de las manecillas del reloj. */
    boolean esSimetrico90(Gato otro){
        int[][] t = new int[3][3];
        t[0][2] = otro.tablero[0][0];
        t[1][2] = otro.tablero[0][1];
        t[2][2] = otro.tablero[0][2];
        t[0][1] = otro.tablero[1][0];
        t[1][1] = otro.tablero[1][1];
        t[2][1] = otro.tablero[1][2];
        t[0][0] = otro.tablero[2][0];
        t[1][0] = otro.tablero[2][1];
        t[2][0] = otro.tablero[2][2];   
	Gato g = new Gato(otro);
        g.tablero = t;
        return esIgual(g);
    }

    /** Rota el otro tablero 180Â° en la direcciÃ³n de las manecillas del reloj. */
    boolean esSimetrico180(Gato otro){
        int[][] t = new int[3][3];
        t[2][2] = otro.tablero[0][0];
        t[2][1] = otro.tablero[0][1];
        t[2][0] = otro.tablero[0][2];
        t[1][2] = otro.tablero[1][0];
        t[1][1] = otro.tablero[1][1];
        t[1][0] = otro.tablero[1][2];
        t[0][2] = otro.tablero[2][0];
        t[0][1] = otro.tablero[2][1];
        t[0][0] = otro.tablero[2][2];
        Gato g = new Gato(otro);
        g.tablero = t;
        return esIgual(g);
    }

    /** Rota el otro tablero 270Â° en la direcciÃ³n de las manecillas del reloj. */
    boolean esSimetrico270(Gato otro){
      int[][] t = new int[3][3];
      t[2][0] = otro.tablero[0][0];
      t[1][0] = otro.tablero[0][1];
      t[0][0] = otro.tablero[0][2];
      t[2][1] = otro.tablero[1][0];
      t[1][1] = otro.tablero[1][1];
      t[0][1] = otro.tablero[1][2];
      t[2][2] = otro.tablero[2][0];
      t[1][2] = otro.tablero[2][1];
      t[0][2] = otro.tablero[2][2];
      Gato g = new Gato(otro);
      g.tablero = t;
      return esIgual(g);
    }

    /**
     * Indica si dos estados del juego del gato son iguales, considerando simetrÃ­as, 
     * de este modo el problema se vuelve manejable.
     */
    @Override
    public boolean equals(Object o){
        Gato otro = (Gato)o;
        if(esIgual(otro)) return true;

        if(esSimetricoDiagonalInvertida(otro)) return true;
        if(esSimetricoDiagonal(otro)) return true;
        if(esSimetricoVerticalmente(otro)) return true;
        if(esSimetricoHorizontalmente(otro)) return true;
        if(esSimetrico90(otro)) return true;
        if(esSimetrico180(otro)) return true;
        if(esSimetrico270(otro)) return true; // No redujo el diÃ¡metro mÃ¡ximo al agregarlo

        return false;
    }

    /** Devuelve una representaciÃ³n con caracteres de este estado.
     *  Se puede usar como auxiliar al probar segmentos del cÃ³digo. 
     */
    @Override
    public String toString(){
        char simbolo = jugador1 ? 'o' : 'x';
        String gs = "";
        for(int i = 0; i < 3; i++){
          for(int j = 0; j < 3; j++){
            gs += tablero[i][j] + " ";
          }
          gs += '\n';
        }
        return gs;
    }
}
