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
@Path("alarm")
@Stateless
public class Alarm {
    
    @PersistenceContext(unitName = "my_persistence_unit")
    EntityManager em;
    
    @Resource(lookup="myConnFactory")
    private ConnectionFactory connectionFactory;
    
    @Resource(lookup="projectQueue")
    private Queue projectQueue;
    
    @GET
    @Path("navijanje/{idM}/{period}/{vreme}")
    public Response dohvatiAlarm(@Context HttpHeaders httpHeaders, @PathParam("idM") int idM, @PathParam("period") int period, @PathParam("vreme") String vreme){
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
        
        List<Melodija> melodije = em.createNamedQuery("Melodija.findByIdM", Melodija.class).setParameter("idM", idM).getResultList();
        if (melodije.isEmpty()) {
            return Response.status(Response.Status.OK).entity("Ne postoji melodija sa datim indeksom").build();
        }
        try {
            ObjectMessage objMsg = context.createObjectMessage(melodije.get(0));
            objMsg.setIntProperty("idK", korisnik.getIdK());
            objMsg.setIntProperty("period", period);
            objMsg.setStringProperty("operacija", "navijanje");
            objMsg.setStringProperty("vreme", vreme);
            producer.send(projectQueue, objMsg);
            return Response.status(Response.Status.OK).entity("Alarm je navijen").build();
        } catch (JMSException ex) {
            Logger.getLogger(Uredjaj.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Alarm nije navijen").build();
    }
    
    @GET
    @Path("melodija/{naziv}")
    public Response dohvatiAlarme(@Context HttpHeaders httpHeaders, @PathParam("naziv") String naziv){
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
        
        List<Melodija> melodije = em.createNamedQuery("Melodija.findByNaziv", Melodija.class).setParameter("naziv", naziv).getResultList();
        if (melodije.isEmpty()) {
            return Response.status(Response.Status.OK).entity("Ne postoji melodija sa datim indeksom").build();
        }
        
        try {
            ObjectMessage objMsg = context.createObjectMessage(melodije.get(0));
            objMsg.setStringProperty("operacija", "konfiguracija");
            producer.send(projectQueue, objMsg);
            return Response.status(Response.Status.OK).entity(melodije.get(0).getIdM() + "").build();
        } catch (JMSException ex) {
            Logger.getLogger(Uredjaj.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Alarm nije navijen").build();
    }
}
