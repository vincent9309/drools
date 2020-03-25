# drools

版权声明：本文为CSDN博主「1Vincent」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/wuzhiwei549/article/details/104813821

一、为什么要使用规则引擎
 有一天运营想弄一个积分策略，计算额外积分金额 ，规则如下:

订单原价金额 100以下, 不加分；
100-500 加100分；
500-1000 加500分；
1000 以上 加1000分；

传统java业务实现如下：

public class JavaScoreExample {
      
    public static void main(String[] args) throws Exception {  
          
        List<Order> orderList = getInitData();
        for (int i=0; i<orderList.size(); i++){  
            Order order = orderList.get(i);  
            if (order.getAmout() <= 100){  
                order.setScore(0);  
                addScore(order);  
            }else if(order.getAmout() > 100 && order.getAmout() <= 500){  
                order.setScore(100);  
                addScore(order);  
            }else if(order.getAmout() > 500 && order.getAmout() <= 1000){  
                order.setScore(500);  
                addScore(order);  
            }else{  
                order.setScore(1000);  
                addScore(order);  
            }  
        }  
          
    }  
      
    private static void addScore(Order o){  
        System.out.println("用户" + o.getUser().getName() + "享受额外增加积分: " + o.getScore());  
    }  
      
    private static List<Order> getInitData() throws Exception {  
        List<Order> orderList = new ArrayList<Order>();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        {
            Order order = new Order();  
            order.setAmout(80);  
            order.setBookingDate(df.parse("2015-07-01"));  
            User user = new User();
            user.setLevel(1);  
            user.setName("Name1");  
            order.setUser(user);  
            orderList.add(order);  
        }
        {
            Order order = new Order();  
            order.setAmout(200);  
            order.setBookingDate(df.parse("2015-07-02"));  
            User user = new User();
            user.setLevel(2);  
            user.setName("Name2");  
            order.setUser(user);  
            orderList.add(order);  
        }  
       
        return orderList;  
    }  
}  
这时候由于市场需求变化，又要调整规则时候，则又要进行业务层面的代码修改、部署，十分麻烦。

如果我们可以把决策规则从应用程序中分离出来，将对系统提供很大的便利！

 

由此，诞生了规则引擎！如下如所示：      



规则引擎优势：

对系统的使用人员

把业务策略（规则）的创建、修改和维护的权利交给业务经理
提高业务灵活性
加强业务处理的透明度，业务规则可以被管理
减少对IT人员的依赖程度
避免将来升级的风险


对IT开发人员

简化系统架构，优化应用
提高系统的可维护性和维护成本
方便系统的整合
减少编写“硬代码”业务规则的成本和风险

 

目前市面上比较热门的规则引擎有几款：

Ilog JRules 是最有名的商用BRMS；
Drools 是最活跃的开源规则引擎；
Jess 是Clips的java实现，就如JRuby之于Ruby，是AI系的代表；
 Visual Rules（旗正规则引擎）国内商业规则引擎品牌。

本文将着重介绍Drools。

 

二、什么是Drools
Drools 是一个基于Charles Forgy’s的RETE算法的，易于访问企业策略、易于调整以及易于管理的开源业务规则引擎，符合业内标准，速度快、效率高。

业务分析师人员或审核人员可以利用它轻松查看业务规则，从而检验是否已编码的规则执行了所需的业务规则。

Drools 是用Java语言编写的开放源码规则引擎，使用Rete算法对所编写的规则求值。Drools允许使用声明方式表达业务逻辑。可以使用非XML的本地语言编写规则，从而便于学习和理解。并且，还可以将Java代码直接嵌入到规则文件中，这令Drools的学习更加吸引人。

 



