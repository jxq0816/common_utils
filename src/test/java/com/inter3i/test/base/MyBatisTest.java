package com.inter3i.test.base;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by boxiaotong on 2017/5/16.
 */
public class MyBatisTest {
    @Test
    public void query() {
        String resource = "mybatis-config.xml";
        Reader reader = null;
        try {
            reader = Resources.getResourceAsReader(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SqlSessionFactory ssf = new SqlSessionFactoryBuilder().build(reader);
        SqlSession session = ssf.openSession();
        try {
            Map param=new HashMap();
            param.put("name","admin");
            Map rs = session.selectOne("query",param);
            System.out.println(rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
