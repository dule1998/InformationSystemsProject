/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import entities.Korisnik;
import entities.Melodija;
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
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

/**
 *
 * @author dusan
 */
@Path("planer")
@Stateless
public class Planer {
    
    @PersistenceContext(unitName = "my_persistence_unit")
    EntityManager em;
    
    @Resource(lookup="myConnFactory")
    private ConnectionFactory connectionFactory;
    
    @Resource(lookup="planerQueue")
    private Queue planerQueue;
    
    /*@Resource(lookup="planerQueueSend")
    private Queue planerQueueSend;*/
    
    @POST
    @Path("{dest}/{vreme1}/{vreme2}")
    public Response ubaciDogadjaj(@Context HttpHeaders httpHeaders, @PathParam("dest") String dest, @PathParam("vreme1") String vreme1, @PathParam("vreme2") String vreme2){
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
        
        vreme1 = vreme1.replace('_', ' ');
        vreme2 = vreme2.replace('_', ' ');
        
        try {
            TextMessage objMsg = context.createTextMessage();
            objMsg.setText("dodavanje");
            objMsg.setIntProperty("idK", korisnik.getIdK());
            objMsg.setStringProperty("destinacija", dest);
            objMsg.setStringProperty("vreme1", vreme1);
            objMsg.setStringProperty("vreme2", vreme2);
            producer.send(planerQueue, objMsg);
            return Response.status(Response.Status.OK).entity("Dogadjaj je navijen").build();
        } catch (JMSException ex) {
            Logger.getLogger(Uredjaj.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Dogadjaj nije navijen").build();
    }
    
    @PUT
    @Path("datum/{vreme}/{idD}")
    public Response promeniDatum(@Context HttpHeaders httpHeaders, @PathParam("vreme") String vreme, @PathParam("idD") int idD){
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
        
        vreme = vreme.replace('_', ' ');
        
        try {
            TextMessage objMsg = context.createTextMessage();
            objMsg.setText("menjanje");
            objMsg.setIntProperty("idK", korisnik.getIdK());
            objMsg.setIntProperty("idD", idD);
            objMsg.setStringProperty("vreme", vreme);
            objMsg.setStringProperty("promena", "datum");
            producer.send(planerQueue, objMsg);
            return Response.status(Response.Status.OK).entity("Dogadjaj je navijen").build();
        } catch (JMSException ex) {
            Logger.getLogger(Uredjaj.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Dogadjaj nije navijen").build();
    }
    
    @PUT
    @Path("destinacija/{dest}/{idD}")
    public Response promeniDest(@Context HttpHeaders httpHeaders, @PathParam("dest") String dest, @PathParam("idD") int idD){
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
        
        dest = dest.replace('_', ' ');
        
        try {
            TextMessage objMsg = context.createTextMessage();
            objMsg.setText("menjanje");
            objMsg.setIntProperty("idK", korisnik.getIdK());
            objMsg.setIntProperty("idD", idD);
            objMsg.setStringProperty("destinacija", dest);
            objMsg.setStringProperty("promena", "destinacija");
            producer.send(planerQueue, objMsg);
            return Response.status(Response.Status.OK).entity("Dogadjaj je navijen").build();
        } catch (JMSException ex) {
            Logger.getLogger(Uredjaj.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Dogadjaj nije navijen").build();
    }
    
    @DELETE
    @Path("{idD}")
    public Response izbrisiDogadjaj(@Context HttpHeaders httpHeaders, @PathParam("idD") int idD){
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
        
        try {
            TextMessage objMsg = context.createTextMessage();
            objMsg.setText("brisanje");
            objMsg.setIntProperty("idK", korisnik.getIdK());
            objMsg.setIntProperty("idD", idD);
            producer.send(planerQueue, objMsg);
            return Response.status(Response.Status.OK).entity("Dogadjaj je navijen").build();
        } catch (JMSException ex) {
            Logger.getLogger(Uredjaj.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Dogadjaj nije navijen").build();
    }
    
    @PUT
    @Path("navijanje/{idD}/{idM}")
    public Response navijAlarm(@Context HttpHeaders httpHeaders, @PathParam("dest") String dest, @PathParam("idD") int idD, @PathParam("idM") int idM){
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
        
        List<Melodija> melodije = em.createNamedQuery("Melodija.findByIdM", Melodija.class).setParameter("idM", idM).getResultList();
        if (melodije.isEmpty()) {
            return Response.status(Response.Status.OK).entity("Ne postoji melodija sa datim indeksom").build();
        }
        
        try {
            TextMessage objMsg = context.createTextMessage();
            objMsg.setText("navijanje");
            objMsg.setIntProperty("idK", korisnik.getIdK());
            objMsg.setIntProperty("idD", idD);
            objMsg.setIntProperty("idM", idM);
            producer.send(planerQueue, objMsg);
            return Response.status(Response.Status.OK).entity("Dogadjaj je navijen").build();
        } catch (JMSException ex) {
            Logger.getLogger(Uredjaj.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Dogadjaj nije navijen").build();
    }
    
    @GET
    @Path("racunanje/{dest}/{dest1}")
    public Response racunanjeVremena(@Context HttpHeaders httpHeaders, @PathParam("dest") String dest, @PathParam("dest1") String dest1){
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
        //JMSConsumer consumer = context.createConsumer(planerQueueSend);
        
        try {
            TextMessage objMsg = context.createTextMessage();
            objMsg.setText("racunanje");
            objMsg.setIntProperty("idK", korisnik.getIdK());
            objMsg.setStringProperty("dest", dest);
            objMsg.setStringProperty("dest1", dest1);
            producer.send(planerQueue, objMsg);
            /*String vreme = "";
            Message msg = consumer.receive();
            if (msg instanceof TextMessage) {
                TextMessage txtMsg = (TextMessage)msg;
                vreme = txtMsg.getText();
            }*/
            return Response.status(Response.Status.OK).entity("Proveri planer").build();
        } catch (JMSException ex) {
            Logger.getLogger(Uredjaj.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Dogadjaj nije navijen").build();
    }
    
    @GET
    @Path("poslednje/{dest}")
    public Response racunanjeVremenaPropo(@Context HttpHeaders httpHeaders, @PathParam("dest") String dest){
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
        //JMSConsumer consumer = context.createConsumer(planerQueueSend);
        
        try {
            TextMessage objMsg = context.createTextMessage();
            objMsg.setText("racunanje1");
            objMsg.setIntProperty("idK", korisnik.getIdK());
            objMsg.setStringProperty("dest", dest);
            producer.send(planerQueue, objMsg);
            /*String vreme = "";
            Message msg = consumer.receive();
            if (msg instanceof TextMessage) {
                TextMessage txtMsg = (TextMessage)msg;
                vreme = txtMsg.getText();
            }*/
            return Response.status(Response.Status.OK).entity("Proveri planer").build();
        } catch (JMSException ex) {
            Logger.getLogger(Uredjaj.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Dogadjaj nije navijen").build();
    }
    
    @GET
    @Path("lista")
    public Response lista(@Context HttpHeaders httpHeaders){
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
        //JMSConsumer consumer = context.createConsumer(planerQueueSend);
        
        try {
            TextMessage objMsg = context.createTextMessage();
            objMsg.setText("lista");
            objMsg.setIntProperty("idK", korisnik.getIdK());
            producer.send(planerQueue, objMsg);
            /*String vreme = "";
            Message msg = consumer.receive();
            if (msg instanceof TextMessage) {
                TextMessage txtMsg = (TextMessage)msg;
                vreme = txtMsg.getText();
            }*/
            return Response.status(Response.Status.OK).entity("Proveri planer").build();
        } catch (JMSException ex) {
            Logger.getLogger(Uredjaj.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Dogadjaj nije navijen").build();
    }
}
