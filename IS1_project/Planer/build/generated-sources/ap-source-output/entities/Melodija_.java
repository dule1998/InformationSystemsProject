package entities;

import entities.AlarmNavijen;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.0.v20170811-rNA", date="2021-07-11T13:27:25")
@StaticMetamodel(Melodija.class)
public class Melodija_ { 

    public static volatile SingularAttribute<Melodija, Integer> idM;
    public static volatile SingularAttribute<Melodija, String> naziv;
    public static volatile SingularAttribute<Melodija, String> lokacija;
    public static volatile ListAttribute<Melodija, AlarmNavijen> alarmNavijenList;

}