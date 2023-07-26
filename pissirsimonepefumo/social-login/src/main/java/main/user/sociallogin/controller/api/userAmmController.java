package main.user.sociallogin.controller.api;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Optional;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import main.user.sociallogin.database.Database;
import main.user.sociallogin.model.Parcheggio;
import main.user.sociallogin.manager.ParkingManager;



@RestController
public class userAmmController {


    private Database db;
    private ParkingManager pk;

    public userAmmController(){
        db=new Database();
        pk=new ParkingManager();
    }


    @RequestMapping("/admin/api/parkings")
    public Iterable<Parcheggio> getAll() {
        return db.getAllParcheggi();
    }


    @RequestMapping("/admin/api/parkings/{id}")
    public Parcheggio getParkingById(@PathVariable("id") int idParcheggio) {
        Parcheggio parcheggio = db.getParcheggioById(idParcheggio);
        if (parcheggio.getIdParcheggio()==-1) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "item not found");
        }
        return parcheggio;
    }


    @RequestMapping(value = "/admin/api/parkings", method = RequestMethod.POST)
    public void creaParcheggio(@RequestBody Object parcheggio) throws IOException, MqttException, KeyManagementException, NoSuchAlgorithmException, CertificateException, KeyStoreException {
       Parcheggio p= pk.creaParcheggio(parcheggio);
       db.insertUpdateParcheggio(p);
    }


    @RequestMapping(value = "/admin/api/parkings/{id}", method = RequestMethod.DELETE)
    public void rimuoviParcheggio(@PathVariable("id") int idParcheggio) {
        db.deleteParcheggioFromDB(idParcheggio);
    }


    @RequestMapping(value = "/admin/api/parkings/{id}", method = RequestMethod.PUT)
    public void updateParcheggio(@PathVariable("id") int idP) {
        Parcheggio p=db.getParcheggioById(idP);
        if(p.isOpen==true) p.setClosed();
        else p.setOpened();
        db.insertUpdateParcheggio(p);
    }

    @RequestMapping("/user/api/parkings")
    public Iterable<Parcheggio> getAll2() {
        return db.getAllParcheggi();
    }


    @RequestMapping("/api/parkings/{id}")
    public Parcheggio getParkingById(@PathVariable Integer id) {
        Parcheggio parcheggio = db.getParcheggioById(id);
        return parcheggio;
    }
}





    
    
 