package com.neo.drools.service;

import com.neo.drools.config.DroolsAutoConfig;
import com.neo.drools.unit.KieUtils;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.kie.spring.KModuleBeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;



@Service
public class ReloadDroolsRules {

    public void reload(String drlName) throws Exception {
        KieServices kieServices = KieUtils.getKieServices();
        KieFileSystem kfs = kieServices.newKieFileSystem();
        loadDBRules(drlName, kfs);
//        loadFileRules(drlName, kfs);
        KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();
        Results results = kieBuilder.getResults();
        if (results.hasMessages(Message.Level.ERROR)) {
            System.out.println(results.getMessages());
            throw new IllegalStateException("### errors ###");
        }

        KieUtils.setKieContainer(kieServices.newKieContainer(KieUtils.getKieServices().getRepository().getDefaultReleaseId()));
        System.out.println("新规则重载成功");
    }

    private void loadDBRules(String drlName, KieFileSystem kfs) {
        //        String path = "src/main/resources/rules/address.drl";
        String path = "src/main/resources/"+ DroolsAutoConfig.RULES_PATH + "/"+ drlName + ".drl";
        // 从数据库加载的规则
        kfs.write(path, "package plausibcheck.adress\n\n import com.neo.drools.model.Address;\n import com.neo.drools.model.fact.AddressCheckResult;\n\n rule \"Postcode 6 numbers\"\n\n    when\n  then\n        System.out.println(\"打印日志：更新rules成功!\");\n end");
    }

    private void loadFileRules(String drlName, KieFileSystem kfs) throws IOException{
        // 从classess/rules加载的规则
        for (Resource file : getRuleFiles(drlName)) {
            kfs.write(ResourceFactory.newClassPathResource(DroolsAutoConfig.BASE_RULES_PATH + DroolsAutoConfig.RULES_PATH + file.getFilename(), "UTF-8"));
        }
    }

    private Resource[] getRuleFiles(String drlName) throws IOException {
        if(StringUtils.isEmpty(drlName)){
            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            return resourcePatternResolver.getResources(DroolsAutoConfig.BASE_RULES_PATH + DroolsAutoConfig.RULES_PATH + "**/*.*");
        }
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        return resourcePatternResolver.getResources(DroolsAutoConfig.BASE_RULES_PATH + DroolsAutoConfig.RULES_PATH + "**/"+ drlName + ".*");
    }
}

