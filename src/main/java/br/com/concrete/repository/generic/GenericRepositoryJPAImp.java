package br.com.concrete.repository.generic;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("unchecked")
public abstract class GenericRepositoryJPAImp<T, PK extends Serializable> implements GenericRepositoryJPA<T, PK> {

	@Autowired
    private SessionFactory sessionFactory;

    private final Class<T> entity;

    public GenericRepositoryJPAImp(){
        this.entity =(Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public Session getSession(){
        return sessionFactory.getCurrentSession();
    }

    @Override
    public T update(T entity) {
    	getSession().update(entity);
        return entity;
    }

    @Override
	public T findById(PK id){
    	return (T) getSession().get(entity, id);
    }

    @Override
	public List<T> findAll(){
        return getSession().createQuery("from " + entity.getName()).list();
    }

    @Override
	public Criteria getCriteria(){
		return getSession().createCriteria(entity);
	}

    @Override
	public Criteria getCriteriaWithAlias(String alias){
		return getSession().createCriteria(entity, alias);
	}
    
	@Override
	public T create(T t) {
		return (T) getSession().save(t);
	}

	@Override
	public void delete(T t) {
		getSession().delete(t);
	}
	
	@Override
	public void saveOrUpdate(T t) {
		getSession().saveOrUpdate(t);
	}
}

