/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logika.sparktests;

/**
 *
 * @author ZT4RM4N
 */
import java.util.ArrayList;
import spark.Spark;

public class main {

    /**
     * @param args the command line arguments
     */
    static ArrayList<Double> alturas;

    public static void main(String[] args) {
//       puerto del servicio
        Spark.port(88);
        /*
        configuracion para intercambio de origen cruzado cors
         */

        Spark.before(((rqst, rspns) -> rspns.header("Access-Control-Allow-Origin", "*"))
        );

        /*
        Entonces.. Spark.get define que vamos a hacer... luego lo que va entre comillas "" es la ruta 
        en este caso la raiz... la parte entre () son los 2 aspectos a utilizar para el flujo de 
        info... la solicitud y la respuesta
        de ulltimo queda el retorno al que puede tener notacion html
         */
        Spark.get("/", (rqst, rspns) -> {
            return "Cadena de texto que puede ser html";
        });

        /*
        en este ejemplo pasamos argumentos ... en primer lugar / que indica que estamos en la raiz /
        y dentro de esa raiz un metodo que se llama saludar y en ese metodo /: un argumento
        
         */
        Spark.get("/saludar/:nombre", (rqst, rspns) -> {
            /*
            aqui declaramos el tipo de argumento y de donde lo saca,,, como se ve se toma el 
            elemento rqst. y se le asigna un valor predefinido que se llama params asi el toma lo 
            que viene de la ruta,,,, creo... 
             */
            String nombre = rqst.params("nombre");
            /*
            en este punto ya sale el return al que le estoy colocando un texto y le concateno el 
            argumento que ha recibido el valor dese el navegador
             */
            return "Hola " + nombre;
        });

        /*
        Een este punto lo mismo que el anterior tenemos una ruta / raiz y dentro de esa raiz
        un metodo /ejercicio1 y dentro de ese metodo el argumento /:numero... el resto de la estructura
        ya la comente arriba
         */
        Spark.get("/ejercicio1/:numero", ((rqst, rspns) -> {

            /*
            igual que arriba declaro una variable para recibir el argumento que viene del navegador
             */
            String num = rqst.params("numero");
            /*
            aqui declaro un integer que me va a permitir operar numericamente ya que lo que viene
            del navegador es un string
             */
            int n1 = 0;
            /*
            por la naturaleza del ejercicio requiero saber si los caracteres ingresados son numeros
            asi que en el try hago un analisis gramatical del argumento intentando asignarlo al 
            ingeger que declare            
             */
            try {
                n1 = Integer.parseInt(num);
            } catch (Exception e) {
                /*
                de no poderse ejecutar el parseInt, se envia el error
                 */
                return "Error : " + e.getMessage();
            }
            /*
            decalro un string html... esta parte porque la respuesta en el navegador no tiene que ser
            solo texto plano, utilizando el html me permite enviar la respuesta con notacion
            se declara vacio y se va haciendo concatenacion +=
             */
            String html = "";
            /*
            entonces si la parte logica solo es un condicional
             */
            if (n1 > 0) {
                /*
                en este punto al darle += coy construyendo el hipertexto con las etiquetas
                y se concatena "+ +" como al colocar argumentos en la consulta JPQL 
                 */
                html += "<div style='background-color:green'>";
                html += "El numero " + n1 + " es positivo";
                html += "</div>";
            } else if (n1 < 0) {
                html += "<div style='background-color:red'>";
                html += "El numero " + n1 + " es negativo";
                html += "</div>";
            } else {
                html += "<div style = 'background-color:blue'>";
                html += "El numero es cero";
                html += "</div>";

            }
            /*
            dado que cada respuesta del if esta adicionando texto al html ps se retorna ese html 
             */
            return html;

        }));

        /* ejercicio 2
        
        Hacer un servicio que calcule el total a pagar por la compra de camisas. Si se
        compran tres camisas o más se aplica un descuento del 20% sobre el total de la
        compra y si son menos de tres camisas un descuento del 10%.
         */
 /*
        en este la cuestion es que no puse atencion ajjajajajaj vamos a ver..
        de entrada son 2 argumentos asi voy a aexperimentear
        
        jejejejej me funciono!! 
        
        "/ejercicio2/:unidades/:precio"
         */
        Spark.get("/ejercicio2/:unidades/:precio", (rqst, rspns) -> {
            /*
            se asignan los datos enviados por el navegador a Strings 
             */
            String unids = rqst.params("unidades");
            String price = rqst.params("precio");
            /*
            doubles (por aquello del descuento) para operar inicializados
             */
            double p = 0;
            double u = 0;
            double v = 0;
            double vn = 0;

            /*
            en el try se hace el parseo y se chequeean que sirvan los caracteres
            recibidos
             */
            try {
                p = Double.parseDouble(price);
                u = Double.parseDouble(unids);

            } catch (Exception e) {
                return "Error : " + e.getMessage();
            }
            /*
            calculo el precio de venta normal solo por dar una salida mas detallada
            pero no esta en el requisito
             */
            vn = p * u;
            /*
            declaro la variable que voy a usar para mi salida html
             */
            String html = "";
            /*
            se evalua la condicion y ...
             */
            if (u <= 3) {
                // esto pues carreta normal matematica
                v = ((u * p) - ((u * p) * (0.10)));
                /*
                se construye el html de salida                 */
                html += "<div style='background-color:#18D7FF'>";
                html += "El precio normal de su compra es : $" + vn + " </br> ";
                html += "El precio de su compra con el 10% de DESCUENTO es : $" + v + "";
                html += "</div>";
            } else {
                v = ((u * p) - ((u * p) * (0.20)));
                html += "<div style='background-color:#FFFF00'>";
                html += "El precio normal de su compra es : $" + vn + "</br>";
                html += "El precio de su compra con el 20% de DESCUENTO es : $" + v + "";
                html += "</div>";
            }
            /*
            se retorna el html con el resultado para renderizarlo
            
             */
            return html;
        });

        /*
        Ejercicio 3
        
        Construir un servicio que determine la cantidad de dinero que recibirá un
        trabajador por concepto de las horas extras trabajadas en una empresa,
        sabiendo que cuando las horas de trabajo exceden de 40, el resto se
        consideran horas extras y que estas se pagan al doble de una hora normal
        cuando no exceden de 8; si las horas extras exceden de 8 se pagan las primeras
        8 al doble de lo que se pagan las horas normales y el resto al triple.
         */
 /*
        Crear el servicio... y el resto no lo voy a comentar porque ya es reiterarivo y 
        porque tengo pereza jajajajjaja
         */
        Spark.get("/ejercicio3/:horas/:precioHora", (rqst, rspns) -> {
            String time = rqst.params("horas");
            String price = rqst.params("precioHora");

            int t = 0;
            int p = 0;
            int he = 0;

            try {
                t = Integer.parseInt(time);
                p = Integer.parseInt(price);
            } catch (Exception e) {
                return "Error : " + e.getMessage();
            }
            String html = "";

            if (t <= 40) {
                html += "<div style = 'background-color:yellow'>";
                html += "El numero de horas trabajadas no califica para horas extras";
                html += "</div>";
            } else if (t <= 48) {
                he = ((t - 40) * (p * 2));
                html += "<div style = 'background-color:#18D7FF'>";
                html += "El valor a pagar por horas extras es $: " + he + "";
                html += "</div>";
            } else {
                he = (((t - 40) * (p * 2)) + ((t - 48) * (p * 3)));
                html += "<div style = 'background-color:pink'>";
                html += "El valor a pagar por horas extras es $: " + he + "";
                html += "</div>";
            }

            return html;
        });
        /*
        Ejercicio 4
        
        Confeccionar un programa que permita ingresar un valor del 1 al 10 y nos
muestre la tabla de multiplicar del mismo (los primeros 13 t�rminos) Ejemplo:
Si ingreso 3 deber� aparecer en pantalla los valores 3, 6, 9, hasta el 39. La
salida del servicio web debe estar escrito en HTML.
         */

        Spark.get("/ejercicio4/:valor", (rqst, rspns) -> {
            String value = rqst.params("valor");
            int val = 0;
            int res = 0;
            try {
                val = Integer.parseInt(value);

            } catch (Exception e) {
                return "Error :" + e.getMessage();
            }

            String html = "";

            for (int i = 1; i <= 13; i++) {
                res = val * i;
                html += " " + res + ", ";
            }
            return html;
        });

        /*
        Ejercicio 5
        
        Imprimir la serie Fibonacci hasta que la serie llegue al valor X.
Ejm: 0,1,1,2,3,5,8,13,21,34,55,X� Fin, Dado que X debe ser un argumento
Request.
         */
        Spark.get("/ejercicio5/:valor", (rqst, rspns) -> {
            String value = rqst.params("valor");
            int val = 0;
            int n1 = 0, n2 = 1, n3, i;
            try {
                val = Integer.parseInt(value);

            } catch (Exception e) {
                return "Error :" + e.getMessage();
            }

            String html = "";

            for (i = 0; i <= val; i++) {
                n3 = n1 + n2;
                html += " " + n3 + ", ";
                n1 = n2;
                n2 = n3;
            }
            return html;
        });

        /*
        Ejercicio 6
        
        Definir un vector de 5 componentes de tipo float que representen las alturas
de 5 personas. Obtener el promedio de las mismas. Contar cuántas personas
son más altas que el promedio y cuántas más bajas. Utilizando una colección
de datos.
         */
 /*
        Vamos a comentar este porque no es que la tenga clara...
        primero declaramos un un list dinamico para ir colocando 
        los datos
         */
 
 
        alturas = new ArrayList<>();

        /*
         ahora si arrancamos el servicio POST que nos permite procesar 
        las solicitudes hechas del navegador (o eso entiendo hasta aqui jejejejjajajaja)
        */
        Spark.post("/Ejercicio6", (rqst, rspns) -> {

            /**
             * Aqui se declara un string que sera en donde guarde lo que viene de
             * la solicitud... la cuestion a tener en cuenta es que body es una parte
             * componente de la api ... los datos se extraen como en jpql (si vio marica que eso era importante ajajajjajaajaj)
             * 
             */
            String body = rqst.body();
            /**
             * tonces... evaluamos si el body esta vacio con .propiedad y seteamos
             * el status (que es otro componente a atener en cuenta) en 500  .... hay que leer sobre los statusessesese jajajja
             * 
             */
            if (body.equals("")) {
                rspns.status(500);
                return "No existen datos en el cuerpo";
            }
            /**
             * ahora del otro lado inicio un double para manehar la info
             * que viene en el body
             */
            double altura = 0;
            try {
                /**
                 *interpreto lo que viene del body como double para entrarlo
                 * a la variable
                 */
                altura = Double.parseDouble(body);
            } catch (Exception e) {
                /**
                 * a la respuesta le asigno el estatus (que si ome que no me los se aun jajjajajaja)
                 */
                rspns.status(500);
                /**
                 * ese chino deje algo a la curiosidad jajajjaa... aqui se trae el mensaje como en c#
                 */
                return e.getMessage();
            }

            /**
             * ahora/// si el array dinamico tiene un tamaño menor a 5 ps ingreso esa altura 
             * de lo contrario saco el mensaje
             */
            if (alturas.size() < 5) {
                alturas.add(altura);
                return "Informacion agregada";
                 /**
                 * bueno normal lo de la respuesta status retorno etc... 
                 * ahora... lo que se hace hasta aqui es guardar la info
                 * como el requisito dice:
                 * 
                 * Obtener el promedio de las mismas. Contar cuántas personas
                   son más altas que el promedio y cuántas más bajas. Utilizando
                 * una colección de datos.
                 * 
                 * yo pienso que aqui invocamos de una vez un metodo que retorne un String que realice
                 * las evaluaciones y retorne el html con los datos... esto solo sucederia 
                 * cuando el quinto dato este ingresado... colocaria un counter y sale
                 * si aquello de "solo se pueden ingresar 5 datos".. cosa que no esta en el
                 * requisito/.... iszhhhh que hpta tan jarto jajajajajajajajja ... vamos a ver que tal
                 * 
                 */
            } else {
               
                rspns.status(403);
                return "Solo se permiten 5 ingresos";
                
            }
        });
        
        
        /**
         * esta parte es como lo hicieron en clase igual me lo voy a explicar porque luego
         * se me "olvida" jajjaja
         * 
         * de entrada un servicio get donde colocamos un path para las request
         */
        
        Spark.get("/alturas", (rqst, rspns) -> {
            return null; //To change body of generated lambdas, choose Tools | Templates.
        });

        
    }sd

}
