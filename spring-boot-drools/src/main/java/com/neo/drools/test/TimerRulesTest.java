package com.neo.drools.test;

import com.neo.drools.model.Server;
import com.neo.drools.model.fact.ResultEvent;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.Globals;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;

/**
 * 定时任务
 */
public class TimerRulesTest {

    @Test
    public void timerTest() throws InterruptedException {

        final KieSession kieSession = createKnowledgeSession();
        ResultEvent event = new ResultEvent();

        Globals globals = kieSession.getGlobals();
        System.out.println( globals.getGlobalKeys() );

        kieSession.setGlobal("event", event);

        final Server server = new Server(1);

        new Thread(new Runnable() {
            public void run() {
                kieSession.fireUntilHalt();
            }
        }).start();

        FactHandle serverHandle = kieSession.insert(server);

        for (int i = 8; i <= 15; i++) {
            Thread.sleep(2000);
            server.setTimes(++i);
            kieSession.update(serverHandle, server);
        }

        Thread.sleep(3000);
        kieSession.halt();
        System.out.println(event.getEvents());
    }

    private KieSession createKnowledgeSession() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        KieSession kSession = kieContainer.newKieSession("ksession-rule");
        return kSession;
    }

}
