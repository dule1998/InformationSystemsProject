/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import entities.Korisnik;
import entities.Pesma;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

/**
 *
 * @author dusan
 */
@Path("uredjaj")
@Stateless
public class Uredjaj {
    
    @PersistenceContext(unitName = "my_persistence_unit")
    EntityManager em;
    
    @Resource(lookup="myConnFactory")
    private ConnectionFactory connectionFactory;
    
    @Resource(lookup="receiveQueueZvuk")
    private Queue receiveQueueZvuk;
    
    @Resource(lookup="sendQueueZvuk")
    private Queue sendQueueZvuk;
    
    @GET
    @Path("{naziv}")
    public Response dohvatiPesmu(@Context HttpHeaders httpHeaders, @PathParam("naziv") String naziv){
        List<String> authHeaderValues = httpHeaders.getRequestHeader("Authorization");
        
        Korisnik korisnik = null;
        
        if(authHeaderValues != null && authHeaderValues.size() > 0){
            String authHeaderValue = authHeaderValues.get(0);
            String decodedAuthHeaderValue = new String(Base64.getDecoder().decode(authHeaderValue.replaceFirst("Basic ", "")),StandardCharsets.UTF_8);
            StringTokenizer stringTokenizer = new StringTokenizer(decodedAuthHeaderValue, ":");
            String username = stringTokenizer.nextToken();
            String password = stringTokenizer.nextToken();
            
            korisnik = em.createNamedQuery("Korisnik.findByKorIme", Korisnik.class).setParameter("korIme", username).getSingleResult();
            
            if (korisnik == null) {
                return Response.status(Response.Status.NO_CONTENT).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Posaljite kredencijale.").build();
        }
        
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        
        naziv = naziv.replace('_', ' ');
        
        List<Pesma> pesme = em.createNamedQuery("Pesma.findByNaziv", Pesma.class).setParameter("naziv", naziv).getResultList();
        if (pesme.isEmpty()) {
            return Response.status(Response.Status.OK).entity("Ne postoji pesma sa datim nazivom").build();
        }
        try {
            ObjectMessage objMsg = context.createObjectMessage(pesme.get(0));
            objMsg.setIntProperty("idK", korisnik.getIdK());
            objMsg.setStringProperty("operacija", "pesma");
            producer.send(receiveQueueZvuk, objMsg);
        } catch (JMSException ex) {
            Logger.getLogger(Uredjaj.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(Response.Status.OK).entity("Pusta se pesma " + pesme.get(0).getNaziv()).build();
    }
    
    @GET
    public Response dohvatiPesme(@Context HttpHeaders httpHeaders){
        List<String> authHeaderValues = httpHeaders.getRequestHeader("Authorization");
        
        Korisnik korisnik = null;
        
        if(authHeaderValues != null && authHeaderValues.size() > 0){
            String authHeaderValue = authHeaderValues.get(0);
            String decodedAuthHeaderValue = new String(Base64.getDecoder().decode(authHeaderValue.replaceFirst("Basic ", "")),StandardCharsets.UTF_8);
            StringTokenizer stringTokenizer = new StringTokenizer(decodedAuthHeaderValue, ":");
            String username = stringTokenizer.nextToken();
            String password = stringTokenizer.nextToken();
            
            korisnik = em.createNamedQuery("Korisnik.findByKorIme", Korisnik.class).setParameter("korIme", username).getSingleResult();
            
            if (korisnik == null) {
                return Response.status(Response.Status.NO_CONTENT).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Posaljite kredencijale.").build();
        }
        
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(sendQueueZvuk);
        
        String pesme = null;
        
        try {
            ObjectMessage objMsg = context.createObjectMessage(korisnik.getIdK());
            objMsg.setStringProperty("operacija", "pesme");
            producer.send(receiveQueueZvuk, objMsg);
            Message msg = consumer.receive();
            if (msg instanceof TextMessage) {
                TextMessage txtMsg = (TextMessage)msg;
                pesme = txtMsg.getText();
            }
        } catch (JMSException ex) {
            Logger.getLogger(Uredjaj.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Response.status(Response.Status.OK).entity(pesme).build();
    }
}
