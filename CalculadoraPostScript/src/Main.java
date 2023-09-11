import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;
public class Main {
    static BitacoraDeErrores bitacoraDeErrores = new BitacoraDeErrores(); //Clase BitacoraDeErrores
    public static void main(String[] args) {

        //Interfaz de la calculadora
        System.out.println("\n\t\t\tBIENVENIDO A SU CALCULADORA POSTSCRIPT\n" +
                "\t\t\t Las operaciones disponibles incluyen:\n" +
                "add (+) || sub (-) || mul (*) || div (/) || dup || exch || eq\n");
        System.out.println("El formato para las operaciones es: \n" +
                "Operandos y Operadores separados por espacios, y los Operadores al final de la Expresion.\n"+
                "e.g.: 8 8 add\n");
        System.out.println("Para definir un simbolo con un valor se requiere introducir un \"/\" anterior a el.\n"+
                "e.g.: /pi 3.1416\n");
        Scanner scanner = new Scanner(System.in); //Input de datos
        HashMap<String, Double> mainSymbolTable = new HashMap<>();
        while (true) { //Calculadora funcionará hasta que esto se haga false
            System.out.print("Ingrese una expresión PostScript (o 'salir' para cerrar): \n");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("salir")) {
                System.out.println("Cerrando calculadora...");
                break;
            }
            try {
                double resultado = Double.parseDouble(ExpresionPostScript(input, mainSymbolTable));

            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage()); //Msg de error en ejecución de operación
                bitacoraDeErrores.errorLog(e.getMessage()); //Escribir error en logs.txt
            }
        }
        scanner.close(); //Cerrar Input Scanner
        
    }//end main

    //Método para el ingreso de operaciones tipo PostScript
    public static String ExpresionPostScript(String expression, HashMap<String, Double> symbolTable) {
        String resultado = "";
        String errores = "";
        String symbol = "";
        String tempsymbol = "";
        double symbolValue;
        String[] tokens = expression.split("\\s+"); //Tokens
        Stack<String> pila = new Stack<>(); //Stack
        boolean symbolToBeDef = false;
        for (String token : tokens) {
            if (!symbolToBeDef) {
                // DIGITOS: expresion regular -?\d+(\.\d+)? que identifica digitos numericos sin importar signo
                if (token.matches("-?\\d+(\\.\\d+)?")) {
                    pila.push((token));
                }


                //SUMA
                else if (token.equals("add")) {
                    if (pila.size() < 2) {
                        errores = "No hay suficientes operandos para efectuar la suma.";
                        //bitacoraDeErrores.errorLog(errores);
                        throw new IllegalArgumentException(errores);

                    }
                    double operando1 = Double.parseDouble(pila.pop());
                    double operando2 = Double.parseDouble(pila.pop());
                    resultado = String.valueOf(operando1 + operando2);  //Resultado Suma
                    pila.push(resultado);
                }
                //RESTA
                else if (token.equals("sub")) {
                    if (pila.size() < 2) {
                        errores = "No hay suficientes operandos para efectuar la resta.";
                        //bitacoraDeErrores.errorLog(errores);
                        throw new IllegalArgumentException(errores);

                    }
                    double operando1 = Double.parseDouble(pila.pop());
                    double operando2 = Double.parseDouble(pila.pop());
                    resultado = String.valueOf(operando1 - operando2); //Resultado Resta
                    pila.push(resultado);
                }
                //MULTIPLICACION
                else if (token.equals("mul")) {
                    if (pila.size() < 2) {
                        errores = "No hay suficientes operandos para efectuar la multiplicacion.";
                        //bitacoraDeErrores.errorLog(errores);
                        throw new IllegalArgumentException(errores);
                    }
                    double operando1 = Double.parseDouble(pila.pop());
                    double operando2 = Double.parseDouble(pila.pop());
                    resultado = String.valueOf(operando1 * operando2); //Resultado multiplicacion
                    pila.push(resultado);
                }
                //DIVISION
                else if (token.equals("div")) {
                    if (pila.size() < 2) {
                        errores = "No hay suficientes operandos para efectuar la division.";
                        //bitacoraDeErrores.errorLog(errores);
                        throw new IllegalArgumentException(errores);
                    }
                    double operando1 = Double.parseDouble(pila.pop());
                    double operando2 = Double.parseDouble(pila.pop());
                    if (operando1 == 0) {
                        errores = "Imposible dividir por 0";
                        //bitacoraDeErrores.errorLog(errores);
                        throw new IllegalArgumentException(errores);
                    }
                    resultado = String.valueOf(operando2 / operando1); //Resultado división
                    pila.push(resultado);
                }
                //DUPLICATE
                else if (token.equals("dup")) {
                    if (pila.isEmpty()) {
                        errores = "No hay operando a duplicar";
                        //bitacoraDeErrores.errorLog(errores);
                        throw new IllegalArgumentException(errores);
                    }
                    String operandoDup = pila.peek();
                    pila.push(operandoDup);
                }
                //EXCHANGE
                else if (token.equals("exch")) {
                    if (pila.size() < 2) {
                        errores = "No hay suficientes operandos para efectuar el intercambio";
                        //bitacoraDeErrores.errorLog(errores);
                        throw new IllegalArgumentException(errores);
                    }
                    String operando1 = pila.pop();
                    String operando2 = pila.pop();
                    pila.push(operando2);
                    pila.push(operando1);
                }
                //EQUALS: Coloca el resultado en la pila (1 siendo true, 0 siendo false)
                else if (token.equals("eq")) {
                    if (pila.size() < 2) {
                        errores = "No hay suficientes operandos para efectuar la igualdad";
                        //bitacoraDeErrores.errorLog(errores);
                        throw new IllegalArgumentException(errores);
                    }
                    double operando1 = Double.parseDouble(pila.pop());
                    double operando2 = Double.parseDouble(pila.pop());
                    if (operando2 == operando1) {
                        pila.push("true");
                    } else {
                        pila.push("false");
                    }
                }
                //PSTACK: Imprime los componentes de la pila
                else if (token.equals("pstack")) {
                    System.out.println("Resultado: ");
                    for (String item : pila) {
                        System.out.println(item);
                    }
                }
                //POP: Elimina el token al tope de la pila
                else if (token.equals("pop")) {
                    if (pila.isEmpty()) {
                        errores = "No hay suficientes operando a eliminar";
                        //bitacoraDeErrores.errorLog(errores);
                        throw new IllegalArgumentException(errores);
                    }
                    //double operando1 = pila.peek();
                    pila.pop();
                }
                //DEF: Define un símbolo con un valor double.
                if (token.startsWith("/")) {
                    symbolToBeDef = true;
                    symbol = token.substring(1, token.length());

                }
                tempsymbol = token;
                boolean found = false;
                // Iterate through the keys in the HashMap
                for (String key : symbolTable.keySet()) {
                    if (key.equals(tempsymbol)) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    token = String.valueOf(symbolTable.get(token));
                    pila.push(token);
                }
            }
            else {
                symbolValue = Double.parseDouble(token);
                symbolTable.put(symbol, symbolValue);
                symbolToBeDef = false;
            }
        }
        return pila.pop();

    }//end método ExpressionPostScript
}