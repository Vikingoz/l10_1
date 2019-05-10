package services.DAO;

import datasets.DataSet;
import org.hibernate.Session;

public class HibernateDAO {
    private Session session;

    public HibernateDAO(Session session) {
        this.session = session;
    }

    public <T extends DataSet> void save(T user){
        session.save(user);
    }

    public <T extends DataSet> T load(long id, Class<T> clazz) {
            return session.load(clazz, id);
    }
}
