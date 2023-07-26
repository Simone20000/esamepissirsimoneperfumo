package main.user.sociallogin.model;

import main.user.sociallogin.manager.ParkingManager;

public class IotPagamento extends Thread{
    
    private final int idParcheggio;
    private ParkingManager p;
    private int iot;
	

	public IotPagamento(int idParcheggio) {
        this.idParcheggio = idParcheggio;
        iot=1;
    }

    public void setParkingService(ParkingManager parkingService) {
	    this.p = parkingService;
	}
	

    public void run() {
        try {
			p.MessaggioParcheggio(idParcheggio,iot);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}

