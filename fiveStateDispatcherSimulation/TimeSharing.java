package fiveStateDispatcherSimulation;

import java.util.Random;

//Dispatcher
class TimeSharing {

	boolean flow = randomize.nextBoolean();
	public TimeSharing() throws Exception {

		int timerInterrupt = 0; // Simula el Interrupt

		while ( true ) {
			if( timerInterrupt++ > 20 ) break; // System.out.println("timeshare " + timerInterrupt);
			try {

				OperatingSystem.showQueue( OperatingSystem.newQueue );

				if ( !OperatingSystem.newQueue.isEmpty() ) {

					admit( OperatingSystem.newQueue.remove( ), OperatingSystem.address);

					OperatingSystem.address += 5000;

				} // final del if

				OperatingSystem.showQueue( OperatingSystem.readyQueue );	

				if ( !OperatingSystem.readyQueue.isEmpty() ) {

					dispatch( ); // simular Running, proceso va al CPU

					timeOut( ); // simular Timesharing, regresa a Ready

				} // final del if !OperatingSystem.readyQueue.isEmpty()

                boolean gate = randomize.nextBoolean();
			    if(gate){
                    if (!OperatingSystem.readySuspendedQueue.isEmpty()){
                        activate( OperatingSystem.readySuspendedQueue.remove());

                        OperatingSystem.showQueue( OperatingSystem.readySuspendedQueue);

				}else {
					if (!OperatingSystem.blockSuspendedQueue.isEmpty())
						activate(OperatingSystem.blockSuspendedQueue.remove());
					    OperatingSystem.showQueue(OperatingSystem.blockSuspendedQueue);
                    }
				}


				if(randomize.nextInt(5 ) == 0){
			        suspend();
                }


				if (randomize.nextInt(4) == 0 ) // simular Event Occurs
					throw new EventOccursException( "I/O interrupt event occurs" );

			} catch (EventWaitException e) { // simular Event Wait
				// TODO: handle exception
				eventWait( );

			} // final del catch particular EventWait 

			catch (ExitException e) {

				release(); // kill( OperatingSystem.runningPcb );

			} // final del catch particular ExitException

			catch (EventOccursException e) {
				// TODO: handle exception
				eventOccurs();
			} // catch de EventOccursException


		} // final del while( true )

		System.out.println("\nfinal del método timeSharing()");

		OperatingSystem.showQueue(OperatingSystem.newQueue);
		OperatingSystem.showQueue(OperatingSystem.readyQueue);

		if( OperatingSystem.runningPcb != null )
			System.out.println(OperatingSystem.runningPcb.toString() );

		OperatingSystem.showQueue(OperatingSystem.readySuspendedQueue);
		OperatingSystem.showQueue(OperatingSystem.blockSuspendedQueue);
		OperatingSystem.showQueue(OperatingSystem.blockedQueue);
		OperatingSystem.showQueue(OperatingSystem.exitQueue);

		System.exit(0);
	}

	static Random randomize = new Random( );

	void eventOccurs( ) throws Exception{

		if( OperatingSystem.blockedQueue.isEmpty() && OperatingSystem.blockSuspendedQueue.isEmpty() )
			return; // si nadie espera, ignorar el i/o interrupt

        if(OperatingSystem.blockedQueue.isEmpty()){
            PCB process = OperatingSystem.blockSuspendedQueue.remove();
            process.eventOccurs();
            OperatingSystem.readySuspendedQueue.add(process);
        }else {
            PCB process = OperatingSystem.blockedQueue.remove( );
            process.eventOccurs();
            OperatingSystem.readyQueue.add( process );
			}


	} // final del método eventOccurs

	//TODO LOGICAL PROBLEM HERE
	void kill( PCB process ) throws Exception {
		if( process == null)
			throw new Exception("Error en kill, proceso es null.");

		process.release();

		OperatingSystem.exitQueue.add( process );
		OperatingSystem.runningPcb = null;
		OperatingSystem.showQueue( OperatingSystem.exitQueue );
		return; // return opcional en métodos de tipo void
	} // final del método kill

	void release( ) throws Exception {

		if( OperatingSystem.runningPcb == null )
			throw new Exception("Error en release(), running PCB es null.");

		kill( OperatingSystem.runningPcb );

		OperatingSystem.runningPcb = null;

		return; // return opcional en métodos de tipo void
	} // final del método release

	void admit( PCB process, int programCounter ) throws Exception{

		if (flow){
			process.admit( programCounter );
			OperatingSystem.readyQueue.add( process );
		}else {
			process.admitSuspended(programCounter);
			OperatingSystem.readySuspendedQueue.add(process);
		}

	} // final del método Admit()

	void activate(PCB process) throws Exception {

		if(process.getProcessStatus() == ProcessStatusType.BLOCKED_SUSPEND){
			process.activate();
			OperatingSystem.blockedQueue.add(process);
			OperatingSystem.showQueue(OperatingSystem.blockedQueue);
		}else {
			process.activate();
			OperatingSystem.readyQueue.add(process);
			OperatingSystem.showQueue(OperatingSystem.readyQueue);
		}

	} // final del metodo activate

	void timeOut( ) throws Exception {	
		OperatingSystem.runningPcb.timeOut();
		OperatingSystem.readyQueue.add( OperatingSystem.runningPcb );

		OperatingSystem.runningPcb = null;

		OperatingSystem.showQueue( OperatingSystem.readyQueue );
	} // final del método timeOut()

	void eventWait ( ) throws Exception{

			OperatingSystem.runningPcb.eventWait();
			OperatingSystem.blockedQueue.add(OperatingSystem.runningPcb);
			OperatingSystem.runningPcb = null;
			OperatingSystem.showQueue(OperatingSystem.blockedQueue);
	} // final del método eventWait()

	void dispatch( ) throws Exception{

		( OperatingSystem.runningPcb = OperatingSystem.readyQueue.remove() ).dispatch();

		OperatingSystem.runningPcb.setProgramCounter( OperatingSystem.runningPcb.getProgramCounter() + 6 );

		System.out.println( "\n" + OperatingSystem.runningPcb );

		if (randomize.nextInt(10) == 0 ) // simular Close, Exit
			throw new ExitException( "Close, Cancel, Exit" );

		if ( randomize.nextInt( 5 ) == 0 ) // simular Event I/O, se va a blocked
			throw new EventWaitException( "Event i/o process: " + OperatingSystem.runningPcb );

		return; // return opcional en mEtodos de tipo void
	} // final del método dispatch

	void suspend() throws  Exception{

	    if(OperatingSystem.runningPcb != null){
            OperatingSystem.runningPcb.suspend();

            switch(OperatingSystem.runningPcb.getProcessStatus()){
                case BLOCKED_SUSPEND:
                    OperatingSystem.blockSuspendedQueue.add(OperatingSystem.runningPcb);
                    OperatingSystem.showQueue(OperatingSystem.blockSuspendedQueue);
                    break;
                case READY_SUSPEND:
                    OperatingSystem.readySuspendedQueue.add(OperatingSystem.runningPcb);
                    OperatingSystem.showQueue(OperatingSystem.readySuspendedQueue);
                    break;

            }
            OperatingSystem.runningPcb = null;
	    }
	}

} // final de la class TimeSharing
