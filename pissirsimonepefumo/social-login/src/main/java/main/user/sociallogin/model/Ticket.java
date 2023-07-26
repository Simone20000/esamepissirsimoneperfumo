package main.user.sociallogin.model;


public class Ticket {

	private int idTicket;
	private int idParcheggio;
	private boolean pagato;
	
	public Ticket() {
		this.idTicket = -1;
		this.pagato = true;
		this.idParcheggio=-1;
	}

	public Ticket(int idParcheggio) {
		this.idTicket = -1;
		this.pagato = true;
		this.idParcheggio=idParcheggio;
	}

	public Ticket(int idticket,int idParcheggio,int stato) {
		this.idTicket = idticket;
		if(stato==1) this.pagato = true;
		this.idParcheggio=idParcheggio;
	}

	public int getIdTicket() {
		return idTicket;
	}

	public int getIdParcheggio() {
		return idParcheggio;
	}
	
	public boolean isPagato() {
        return pagato;
    }

    public void markAsPaid() {
        pagato = true;
    }
}
