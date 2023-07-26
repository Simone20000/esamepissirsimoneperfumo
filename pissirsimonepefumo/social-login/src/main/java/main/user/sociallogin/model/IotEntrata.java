package main.user.sociallogin.model;

import main.user.sociallogin.manager.ParkingManager;

public class IotEntrata extends Thread{
    
    private final int idParcheggio;
    private ParkingManager p;
    private int iot;
	

	public IotEntrata(int idParcheggio) {
        this.idParcheggio = idParcheggio;
        iot=0;
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
