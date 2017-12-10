package fiveStateDispatcherSimulation;

class PCB {

	private int pID,// Identifier del proceso.
	ppID,			// Identifier del proceso madre.
	userID,			// Identifier del usuario responsable.
	programCounter, // Address de donde se está ejecutando.
	pageInMemory;	// Lugar en memoria donde reside el programa.

	private ProcessStatusType processStatus = ProcessStatusType.NEW;

	PCB(int a, int b, int c) { // Constructor function
		this.pID =  a;
		this.ppID = b;
		this.userID = c;
		this.processStatus = ProcessStatusType.NEW;
	} // final del constructor

	void admit(int pc ) throws Exception {
		if ( this.getProcessStatus().equals( ProcessStatusType.NEW ) ) {
			this.setProcessStatus( ProcessStatusType.READY ); // simular Ready
			this.setProgramCounter( pc );
			this.setPageInMemory( pc / 1000 );
		} // final del if
		else
			throw new Exception("Error in Admit( ) process: " + this.toString() );
	} // final del método Admit()

	void admitSuspended(int pc) throws Exception{
		if ( this.getProcessStatus().equals( ProcessStatusType.NEW ) ) {
			this.setProcessStatus( ProcessStatusType.READY_SUSPEND ); // simular Ready
			this.setProgramCounter( pc );
			this.setPageInMemory( pc / 1000 );
		} // final del if
		else
			throw new Exception("Error in suspendAdmint( ) process: " + this.toString() );
	}

	void  timeOut() throws Exception {
		if ( this.processStatus == ProcessStatusType.RUNNING ) {
			this.processStatus = ProcessStatusType.READY;
			return;
		}
		else
			throw new Exception("Error in timeOut( ) " + this.toString() ); 
	} // fin de timeout()

	void eventWait() throws Exception {
		if ( this.processStatus == ProcessStatusType.RUNNING ) {
			this.setProcessStatus( ProcessStatusType.BLOCKED );
			return;     
		}
		else
			throw new Exception("Error in eventWait() " + this.toString() ); 
	} // eventWait

	void  dispatch() throws Exception {
		if ( this.processStatus == ProcessStatusType.READY ) {
			this.processStatus = ProcessStatusType.RUNNING;
			return; 
		}
		else
			throw new Exception("Error in dispatch( ) " + this.toString() ); 
	} // dispatch

	void  release() throws Exception {

		if (this.processStatus != null) {
			this.setProcessStatus( ProcessStatusType.EXIT );
			this.setPageInMemory( -1 ); // simulamos que sale de RAM
			this.setProgramCounter( -1 ); // simulamos que sale de RAM
			return;     
		}
		else
			throw new Exception("Error in release() " + this.toString() ); 
	} // release

	void  eventOccurs() throws Exception {
		if ( this.processStatus == ProcessStatusType.BLOCKED  || this.processStatus == ProcessStatusType.BLOCKED_SUSPEND) {
			this.setProcessStatus( this.processStatus == ProcessStatusType.BLOCKED ? ProcessStatusType.READY : ProcessStatusType.READY_SUSPEND);
			return;      
		}
		else
			throw new Exception("Error in eventOccurs() " + this.toString() );
	} // eventOccurs


    void activate() throws Exception{
	    if(this.processStatus == ProcessStatusType.READY_SUSPEND || this.processStatus == ProcessStatusType.BLOCKED_SUSPEND){
	        this.setProcessStatus( this.processStatus == ProcessStatusType.BLOCKED_SUSPEND ? ProcessStatusType.BLOCKED : ProcessStatusType.READY);
	        return;
	    }else {
	        throw new Exception("Error in activate() " + this.toString() );
			}
		}

		void suspend()throws Exception{
			if(	this.processStatus == ProcessStatusType.READY || this.processStatus == ProcessStatusType.BLOCKED || this.processStatus == ProcessStatusType.RUNNING){
				this.setProcessStatus(this.processStatus == ProcessStatusType.BLOCKED ? ProcessStatusType.BLOCKED_SUSPEND : ProcessStatusType.READY_SUSPEND);
				return;
			}else {
				throw new Exception("Error in suspend() " + this.toString() );
			}
		}

	@Override
	public
	String toString() {
		return "PCB [pID=" + pID + ", ppID=" + ppID + ", userID=" + userID
				+ ", programCounter=" + programCounter + ", pageInMemory="
				+ pageInMemory + ", processStatus=" + processStatus + "]";
	}

	void setProgramCounter( int pc ) {
		this.programCounter = pc;
		return; 
	} // setProgramCounter

	void setProcessID(int processID) throws Exception {

		if( processID < 0 )
			throw new Exception("Error in process id: " + processID );

		this.pID = processID;
	}

	void setParentProcessID(int parentProcessID) throws Exception {

		if( parentProcessID < 0 )
			throw new Exception("Error in parent process id: " + parentProcessID );

		this.ppID = parentProcessID;
	}

	void setUserID(int userID) throws Exception {

		if( userID < 0 )
			throw new Exception("Error in user id: " + userID );

		this.userID = userID;
	}

	void setPageInMemory() {
		this.pageInMemory = this.programCounter / 1000;
		return; // return optional in void type methods
	} // setPageInMemory

	void setPageInMemory(int pageInMemory) throws Exception {

		this.pageInMemory = pageInMemory;
	}

	void setProcessStatus(ProcessStatusType processStatus) throws Exception {

		for (ProcessStatusType type : ProcessStatusType.values() ) {

			if( processStatus == type ) {
				this.processStatus = processStatus;
				return;
			} // if

		} // foreach

		throw new Exception("Error en setProcessStatus: " + processStatus.toString() );
	}


	public ProcessStatusType getProcessStatus() {
		// TODO Auto-generated method stub
		return this.processStatus;
	} // getter

	public int getProgramCounter() {
		// TODO Auto-generated method stub
		return this.programCounter;
	} // getter



} // final de la clase