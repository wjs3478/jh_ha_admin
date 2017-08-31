package com.jeon.heartbeat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class AbstractDAO {

    
    @Autowired
    private SqlSessionTemplate sqlSession;
     

     
    public Object insert(String queryId, Object params){
        
        return sqlSession.insert(queryId, params);
    }
     
    public Object update(String queryId, Object params){
        
        return sqlSession.update(queryId, params);
    }
     
    public Object delete(String queryId, Object params){
        
        return sqlSession.delete(queryId, params);
    }
     
    public Object selectOne(String queryId){
        
        return sqlSession.selectOne(queryId);
    }
     
    public Object selectOne(String queryId, Object params){
        
        return sqlSession.selectOne(queryId, params);
    }
     
    @SuppressWarnings("rawtypes")
    public List selectList(String queryId){
        
        return sqlSession.selectList(queryId);
    }
     
    @SuppressWarnings("rawtypes")
    public List<HashMap> selectList(String queryId, Map params){
        
        return sqlSession.selectList(queryId,params);
    }

	
}
