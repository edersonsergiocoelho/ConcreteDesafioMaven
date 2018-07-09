package br.com.concrete.repository.generic;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;

public interface GenericRepositoryJPA<T, PK extends Serializable> {

    T create(T t);
    T update(T t);
    void delete(T t);
    T findById(PK id);
    List<T> findAll();
    Session getSession();
    Criteria getCriteria();
    Criteria getCriteriaWithAlias(String alias);
    void saveOrUpdate(T t);
}
