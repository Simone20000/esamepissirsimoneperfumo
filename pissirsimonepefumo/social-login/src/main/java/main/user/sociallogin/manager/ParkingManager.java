package main.user.sociallogin.manager;



import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import main.user.sociallogin.database.Database;
import main.user.sociallogin.model.Parcheggio;
import main.user.sociallogin.model.Ticket;
import main.user.sociallogin.model.IotEntrata;
import main.user.sociallogin.model.IotPagamento;
import main.user.sociallogin.model.IotUscita;

import java.nio.charset.StandardCharsets;
import java.security.KeyStoreException;
import java.util.ArrayList;


/**
Configuratore: contiene la tabella di corrispondenza tra pulsanti del pannello di controllo e
 attuatori
 */
public class ParkingManager implements MqttCallback {

    // init the client
    private MqttAsyncClient mqttClient;
    private Database database;
    private Parcheggio parcheggio;
    private Integer a;
    private MemoryPersistence persistence;
    /**
     *  genera client id e instanziamo mqqtclient 
     */
    public ParkingManager() {
        // the broker URL
        String brokerURL = "tcp://localhost:1883";
        try {
            mqttClient = new MqttAsyncClient(brokerURL, MqttClient.generateClientId(),persistence);
        } catch (MqttException e) {
            e.printStackTrace();
        }
            

    }

    @Override
    public void connectionLost(Throwable cause) {
        // what happens when the connection is lost. We could reconnect here, for example.
    }

    //gestisce l'arrivo di un messaggio mqtt
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        try {
            String strmsg = new String(message.getPayload(), StandardCharsets.UTF_8);
        System.out.println("Message arrived for the topic '" + topic + "': " + strmsg);

        String[] componentiTopic = topic.split("/");
        //System.out.println("Scomposizione Topic "+componentiTopic[0]+" "+componentiTopic[1]+" "+componentiTopic[2]);
        //System.out.println("Scomposizione info cliente:"+infocliente[0]+" "+infocliente[1]);
        parcheggio=database.getParcheggioById(Integer.valueOf(componentiTopic[1]));
        //0 posto occupato
        if(componentiTopic[0].equals("parking")){
            if(parcheggio.isOpen()==true){
                    if (componentiTopic[2].equals("entrata")) entrataParcheggio();
                    else if (componentiTopic[2].equals("pagamento")) pagaParcheggio();
                    else if (componentiTopic[2].equals("uscita")) UscitaParcheggio();
            } 
        }
        else // controllare topic sia parking/+/topicsalvati
            {mqttClient.publish("parking/"+componentiTopic[1]+"/log", strmsg.getBytes(), 1, false);}

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //gestisce messaggio in entrata per topic entrata, aggiunge macchina 
    private void entrataParcheggio(){
                parcheggio.addMacchina();
                database.insertUpdateParcheggio(parcheggio);
                IotEntrata entrataDevice = new IotEntrata(parcheggio.getIdParcheggio());
                entrataDevice.setParkingService(this);
                Thread entrata = new Thread(entrataDevice);
                entrata.start();
        }
         

    private void pagaParcheggio(){
            Ticket t=new Ticket(parcheggio.getIdParcheggio());
            database.insertUpdateTicket(t);
            IotPagamento cassaDevice = new IotPagamento(parcheggio.getIdParcheggio());
            cassaDevice.setParkingService(this);
            Thread cassa = new Thread(cassaDevice);
            cassa.start();
        } 
        

    private void UscitaParcheggio(){
                parcheggio.uscitaMacchina();
                database.rimuoviTicket(parcheggio.getIdParcheggio());
                database.insertUpdateParcheggio(parcheggio);
                IotUscita uscitaDevice = new IotUscita(parcheggio.getIdParcheggio());
                uscitaDevice.setParkingService(this);
                Thread uscita = new Thread(uscitaDevice);
                uscita.start();
        }

       
    // richiama thread per mandare messaggio mqtt
    public void MessaggioParcheggio(int idParcheggio,int iot) {
        String testoMessaggio;
        try {
            switch (iot) {
                case 0:
                    testoMessaggio = "macchina entrata nel parcheggio" + idParcheggio;
                    mqttClient.publish("parking/"+idParcheggio+"/log",testoMessaggio.getBytes(),1,false);
                    break;
                case 1:
                    testoMessaggio = "ticket pagato correttamente" + idParcheggio;
                    mqttClient.publish("parking/"+idParcheggio+"/log",testoMessaggio.getBytes(),1,false);
                    break;
                case 2:
                    testoMessaggio = "uscita macchina dal parcheggio" + idParcheggio;
                    mqttClient.publish("parking/"+idParcheggio+"/log",testoMessaggio.getBytes(),1,false);
                    break;
            
                default:
                    break;
            }
        }
        catch (MqttException e) {
                e.printStackTrace();
        }
	    
	}
        
    

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // called when delivery for a message has been completed, and all acknowledgments have been received
        // no-op, here
    }
    /**
     * The method to start the subscriber. It listen to all the homestation-related topics.
     */
    public void start() {
        try { 
            String password = "123456";
            char pwd[] = password.toCharArray();
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName("pissir");
            options.setPassword(pwd);
            options.setCleanSession(false);
            // set a callback and connect to the broker
            IMqttToken token= mqttClient.connect(options);
            token.waitForCompletion();
            
            database=new Database();
            final String topicEntrata = "parking/+/entrata";
            final String topicUscita = "parking/+/uscita";
            final String topicPagamento = "parking/+/pagamento";

            mqttClient.subscribe(topicEntrata,1).waitForCompletion();
            System.out.println("The subscriber is now listening to " + topicEntrata + "...");
            mqttClient.subscribe(topicUscita,1).waitForCompletion();
            System.out.println("The subscriber is now listening to " + topicUscita+ "...");
            mqttClient.subscribe(topicPagamento,1).waitForCompletion();
            System.out.println("The subscriber is now listening to " + topicPagamento + "...");
            Thread.sleep(2000);
            mqttClient.setCallback(this);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Parcheggio creaParcheggio(Object parcheggio){
        System.out.println(parcheggio.toString());
        String[] componenti = parcheggio.toString().split(",");
        System.out.println("\n\n\n"+componenti);
        String[] nomep=componenti[0].split("=");
        String[] numposti=componenti[1].split("=");
        String[] postidisp=componenti[2].split("=");
        postidisp[1]=postidisp[1].substring(0, postidisp[1].length()-1);
        Parcheggio p=new Parcheggio(-1, nomep[1], Integer.valueOf(numposti[1]), Integer.valueOf(postidisp[1]), 0);
        System.out.println("\n\n\n"+p);
        return p;
    }

    /**
     * The main
     */
    public static void main(String[] args) {
        ParkingManager subscriber = new ParkingManager();
        subscriber.start();
    }


}

