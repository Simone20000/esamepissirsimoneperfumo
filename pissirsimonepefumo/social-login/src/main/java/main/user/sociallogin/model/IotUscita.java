package main.user.sociallogin.model;


import main.user.sociallogin.manager.ParkingManager;


public class IotUscita extends Thread{
    
    private final int idParcheggio;
    private ParkingManager p;
    private int iot;
	

	public IotUscita(int idParcheggio) {
        this.idParcheggio = idParcheggio;
        iot=2;
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

