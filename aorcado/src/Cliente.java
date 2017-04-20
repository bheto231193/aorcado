import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;
public class Cliente {

	private static final int INACTIVO = 0; 
	private static final int JUGANDO = 1;
	private static final int GANO = 2;
	private static final int PERDIO = 3;
	private static final int SOLICITANDO_JUEGO_NUEVO = 4;
	private static final int JUEGO_TERMINADO = 5;


	private static final int puerto = 6112;
	private static BufferedReader in = null;
	private static DataOutputStream out = null;
	private static Socket conexion;


	private static int estadoJuego = INACTIVO;
	private static int intentosRestantes = 6;
	private static char letra = '*';
	private static String palabraEnProgreso = "*";
	private static Integer nroMensaje = 0;

	public static void main(String[] args) {
		try {
			System.out.println("1");
			conexion = new Socket("192.168.133.1", 6112);
			System.out.println("2");
			BufferedReader entradaUsuario = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("3");
			System.out.println("iniciando Conexion con el servidor...");
			System.out.println("4");
			in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
			System.out.println("5");
			out = new DataOutputStream(conexion.getOutputStream());
			System.out.println("6");
			System.out.println("conexion realizada...");
			boolean jugando = true;
			while (jugando == true) {
				if (estadoJuego == INACTIVO) {
					setEstadoJuego(SOLICITANDO_JUEGO_NUEVO);
					enviarMensaje();

				} else {

					leerMensaje(in.readLine());

					if (estadoJuego == JUGANDO) {

						imprimirMensajeEnPantalla();
						System.out.println("ingrese una letra: ");
						setLetra(entradaUsuario.readLine().charAt(0));
						enviarMensaje();
					} else {
						if (estadoJuego == GANO) {
							setEstadoJuego(JUEGO_TERMINADO);

							imprimirMensajeEnPantalla();
							jugando = false;
							System.out.println("GANASTE");
						}
						if (estadoJuego == PERDIO) {
							setEstadoJuego(JUEGO_TERMINADO);
							imprimirMensajeEnPantalla();
							jugando = false;

							System.out.println("HASPERDIDO ");
						}
						System.out.println("");

					}
				}
			}
		} catch (IOException ex) {
			System.out.println("ERROR DE ENRADA/SALIDA");
			System.out.println("No fue posible realizar la conexion, posiblemente el servidor esteinactivo.");
		}
	}

	private static void enviarMensaje() {
		try {
			if (estadoJuego == SOLICITANDO_JUEGO_NUEVO) {
				out.writeBytes(crearMensajeRespuesta());
			} else {
				out.writeBytes(crearMensajeRespuesta());
			}
		} catch (IOException iOException) {
			System.out.println("ERROR AL ENVIAR EL MENSAJE");
		}
	}
	public static String crearMensajeRespuesta() {
		return estadoJuego + "#" + intentosRestantes + "#" + letra + "#" + palabraEnProgreso + "#" +nroMensaje + "\n";
	}
	public static int getEstadoJuego() {
		return estadoJuego;
	}
	public static char getLetra() {
		return letra;
	}
	public static int getNroIntentos() {
		return intentosRestantes;
	}
	public static String getPalabraEnProgreso() {
		return palabraEnProgreso;
	}
	public static void setEstadoJuego(int estadoJuego) {
		Cliente.estadoJuego = estadoJuego;
	}
	public static void setLetra(char letra) {
		Cliente.letra = letra;
	}
	public static void leerMensaje(String mensaje) {
		StringTokenizer stk = new StringTokenizer(mensaje, "#");
		while (stk.hasMoreTokens()) {
			estadoJuego = Integer.valueOf(stk.nextToken());
			intentosRestantes = Integer.valueOf(stk.nextToken());
			letra = stk.nextToken().charAt(0);
			palabraEnProgreso = stk.nextToken();
			nroMensaje = Integer.valueOf(stk.nextToken());
		}
	}
	private static void imprimirMensajeEnPantalla() {
		
		System.out.println("");
		imprimirAhorcado(intentosRestantes);
		System.out.println("\nPALABRA ACTUAL: " + getPalabraActualGuionBajo());
		System.out.println("INTENTOS RESTANTES: " + intentosRestantes);
	}
	private static String getPalabraActualGuionBajo() {
		String[] a = palabraEnProgreso.split("");
		String impr = "";
		for (int i = 0; i < a.length; i++) {
			impr += a[i] + " ";
		}
		return impr;
	}
	private static String mostrarEstado() {
		if (estadoJuego == 0) {
			return "INACTIVO";
		} else {
			if (estadoJuego == 1) {
				return "JUGANDO";
			} else {
				if (estadoJuego == 2) {
					return "GANO";
				} else {
					if (estadoJuego == 3) {
						return "PERDIO";
					} else {
						if (estadoJuego == 4) {
							return "SOLICITANDO_JUEGO_NUEVO";
						} else {
							return "JUEGO_TERMINADO";
						}
					}
				}
			}
		}
	}
	private static void imprimirEntrada() {
		String a = estadoJuego + "#" + intentosRestantes + "#" + letra + "#" + palabraEnProgreso +"#" + nroMensaje;
		System.out.println("LEIDO POR CLIENTE: " + a + "\n" + mostrarEstado());
	}
	private static void imprimirSalida() {
		String a = estadoJuego + "#" + intentosRestantes + "#" + letra + "#" + palabraEnProgreso +"#" + nroMensaje;
		System.out.println("ENVIADO POR CLIENTE: " + a + "\n" + mostrarEstado());
	}

	private static void imprimirAhorcado(int intentosRestantes) {
		System.out.println(" -----------\n | |");
		if (intentosRestantes < 1) {
			System.out.println(" |\n |\n |");
		} else {
			if (intentosRestantes < 2) {
				System.out.println(" o |\n |\n |");
			} else {
				if (intentosRestantes < 3) {
					System.out.println(" o_ |\n l |\n|");
				} else {
					if (intentosRestantes < 4) {
						System.out.println(" _o_ |\n l| |\n|");
					} else {
						if (intentosRestantes < 5) {
							System.out.println(" _o_ |\n l|l |\n|");
						} else {
							if (intentosRestantes < 6) {
								System.out.println(" _o_ |\n l|l |\n) |");
							} else {
								if (intentosRestantes < 7) {
									System.out.println(" _o_ |\n l|l |\n( ) |");

								}
							}
						}
					}
				}
			}
		}
		System.out.print(" |\n |\n____________"+ "______l_____\n");
	}
}

