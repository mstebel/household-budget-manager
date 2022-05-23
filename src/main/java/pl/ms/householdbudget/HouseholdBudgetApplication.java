package pl.ms.householdbudget;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import pl.ms.householdbudget.dao.TransactionDao;

@SpringBootApplication
public class HouseholdBudgetApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(HouseholdBudgetApplication.class, args);
        TransactionDao transactionDao = context.getBean(TransactionDao.class);
      //  transactionDao.close();
    }

}
