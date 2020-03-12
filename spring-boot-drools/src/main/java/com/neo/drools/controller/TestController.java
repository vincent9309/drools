package com.neo.drools.controller;

import com.neo.drools.model.Address;
import com.neo.drools.model.MyAgendaFilter;
import com.neo.drools.model.fact.AddressCheckResult;
//import com.neo.drools.service.DroolsBusiness;
import com.neo.drools.service.ReloadDroolsRules;
import com.neo.drools.unit.KieUtils;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;


@RestController
public class TestController {

    @Resource
    private ReloadDroolsRules rules;

    @RequestMapping("/address")
    public void test(){
        KieSession kieSession = KieUtils.getKieSession();

        Address address = new Address();
        address.setPostcode("99425");

        AddressCheckResult result = new AddressCheckResult();
        kieSession.insert(address);
        kieSession.insert(result);
        kieSession.getAgenda().getAgendaGroup("address").setFocus();
        int ruleFiredCount = kieSession.fireAllRules();
        System.out.println("触发了" + ruleFiredCount + "条规则");

        if(result.isPostCodeResult()){
            System.out.println("规则校验通过");
        }

    }

    @RequestMapping("/reload")
    public String reload(String drlName) throws Exception {
        rules.reload(drlName);
        return "ok";
    }

    @RequestMapping("/updateDrlFile")
    public String updateDrlFile(MultipartFile file) throws Exception {
        //更新drl后，再调用reload方法重载。即可热部署
        return "ok";
    }

    @RequestMapping("/address-test")
    public void addressTest(int num){
        Address address = new Address();
        address.setPostcode(generateRandom(num));

        KieServices ks = KieServices.Factory.get();
        KieContainer kc = ks.getKieClasspathContainer();
        KieSession kieSession = kc.newKieSession("address-testKS");

        //选择agenda-group
        kieSession.getAgenda().getAgendaGroup("address-test").setFocus();
        //增加自定义AgendaFilter
        AgendaFilter filter = new MyAgendaFilter("[Address-Test]");

        AddressCheckResult result = new AddressCheckResult();
        FactHandle faceHandle = kieSession.insert(address);
        kieSession.insert(result);

        //更新work memory 中的对象信息
//        address.setPostcode("123");
//        kieSession.update(faceHandle,address);

        int ruleFiredCount = kieSession.fireAllRules(filter);
        kieSession.destroy();
        System.out.println("触发了" + ruleFiredCount + "条规则");

        if(result.isPostCodeResult()){
            System.out.println("规则校验通过");
        }
    }

    /**
     * 生成随机数
     * @param num
     * @return
     */
    public String generateRandom(int num) {
        String chars = "0123456789";
        StringBuffer number=new StringBuffer();
        for (int i = 0; i < num; i++) {
            int rand = (int) (Math.random() * 10);
            number=number.append(chars.charAt(rand));
        }
        return number.toString();
    }

}
