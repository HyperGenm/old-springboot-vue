package com.weiziplus.springboot;

import com.weiziplus.springboot.common.base.BaseService;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootApplicationTests extends BaseService {

    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

}
