package fiveStateDispatcherSimulation;

import java.io.*;
import java.util.*;


class OperatingSystem {
	static Queue<PCB> newQueue = new LinkedList<PCB>();
	static Queue<PCB> readyQueue = new LinkedList<PCB>();
	static Queue<PCB> blockedQueue = new LinkedList<PCB>();
	static Queue<PCB> exitQueue = new LinkedList<PCB>();
	static Queue<PCB> blockSuspendedQueue = new LinkedList<>();
	static Queue<PCB> readySuspendedQueue = new LinkedList<>();


	static PCB runningPcb; // El proceso que estA corriendo

	static int address = 5000; //E

	static void showQueue(Queue<PCB> queue) {
		System.out.println( );

		int i = 0;

		for (Iterator<PCB> iterator = queue.iterator(); iterator.hasNext();) {
			System.out.println( String.valueOf( ++i ) 
					+ " " + iterator.next() );
		} // final del for  loop

	} // final del mEtodo

	OperatingSystem(String fileString) throws FileNotFoundException {

		File archivoFile = new File( fileString );

		Scanner arcScanner = new Scanner(archivoFile);

		while (arcScanner.hasNext() ) {
			String[] arregloString = arcScanner.nextLine().split(",");
			int processId = Integer.parseInt(arregloString[0]);
			int parentProcessId = Integer.parseInt(arregloString[1]);
			int userId = Integer.parseInt(arregloString[2]);

			OperatingSystem.newQueue.add( 
					new PCB(processId, parentProcessId, userId) );
		} // final del while
		arcScanner.close();

	} // final del método constructor

	public static void main(String[ ] args) {

		try {
			new OperatingSystem( args[0] );
			//Simulating the Dispatcher
			new TimeSharing();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	} // final del main

} // final de la class Sistema Operativo
