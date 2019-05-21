package services.DAO;

import datasets.DataSet;
import executors.Executor;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

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

    public <T extends DataSet> List<T> loadAll(Class<T> clazz) {
        //Query query = session.createNamedQuery("from my_table", clazz);

        //return query.getResultList();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> root = criteriaQuery.from(clazz);
        criteriaQuery.select(root);
        Query<T> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }
}
