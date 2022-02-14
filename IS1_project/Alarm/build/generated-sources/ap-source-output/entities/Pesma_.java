package entities;

import entities.Reprodukcija;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.0.v20170811-rNA", date="2021-07-10T18:45:16")
@StaticMetamodel(Pesma.class)
public class Pesma_ { 

    public static volatile SingularAttribute<Pesma, Integer> idP;
    public static volatile ListAttribute<Pesma, Reprodukcija> reprodukcijaList;
    public static volatile SingularAttribute<Pesma, String> naziv;
    public static volatile SingularAttribute<Pesma, String> link;

}