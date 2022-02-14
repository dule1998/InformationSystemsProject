/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import entities.Korisnik;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author dusan
 */
@Provider
public class BasicAuthFilter implements ContainerRequestFilter {

    @PersistenceContext(unitName = "my_persistence_unit")
    EntityManager em;
    
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        List<String> authHeaderValues = requestContext.getHeaders().get("Authorization");
        
        if(authHeaderValues != null && authHeaderValues.size() > 0){
            String authHeaderValue = authHeaderValues.get(0);
            String decodedAuthHeaderValue = new String(Base64.getDecoder().decode(authHeaderValue.replaceFirst("Basic ", "")),StandardCharsets.UTF_8);
            StringTokenizer stringTokenizer = new StringTokenizer(decodedAuthHeaderValue, ":");
            String username = stringTokenizer.nextToken();
            String password = stringTokenizer.nextToken();
            
            List<Korisnik> korisnici = em.createNamedQuery("Korisnik.findByKorIme", Korisnik.class).setParameter("korIme", username).getResultList();
            
            if(korisnici.size() != 1){
                Response response = Response.status(Response.Status.UNAUTHORIZED).entity("Korisnicko ime ili sifra nije ispravno.").build();
                requestContext.abortWith(response);
                return;
            }
            
            Korisnik korisnik = korisnici.get(0);
            
            if(!korisnik.getSifra().equals(password)){
                Response response = Response.status(Response.Status.UNAUTHORIZED).entity("Korisnicko ime ili sifra nije ispravno.").build();
                requestContext.abortWith(response);
                return;
            }
            
            return;
        }
        
        Response response = Response.status(Response.Status.UNAUTHORIZED).entity("Posaljite kredencijale.").build();
        requestContext.abortWith(response);
    }
    
}
