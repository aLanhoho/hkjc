package com.win.big.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType;
import com.win.big.constant.RaceHistSql;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
public class RaceHistRepository {

    @PersistenceContext
    private EntityManager entityManager;


    @Transactional
    public JsonNode getRaceHistOne(String date) {
        JsonNode node = (JsonNode) entityManager.createNativeQuery(RaceHistSql.SQL)
            .setParameter("date", date)
            .unwrap(org.hibernate.query.NativeQuery.class)
            .addScalar("result", JsonNodeBinaryType.INSTANCE)
            .getSingleResult();

        return node;
    }

}
