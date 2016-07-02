package sitecreators.utils;


import org.springframework.scheduling.annotation.Scheduled;

import sitecreators.utils.finance.CurrencyDAO;
import sitecreators.utils.finance.CurrencyUpdater;

public class SchedulerService {
	
	private CurrencyDAO currencyDao;
	
			
	@Scheduled(fixedDelay = (1000 * 60 * 60 * 24))
	public void currencyUpdater(){
		CurrencyUpdater cu = new CurrencyUpdater();
		cu.setCurrencyDao(currencyDao);
		cu.parse();
		cu.update();
	}


	public CurrencyDAO getCurrencyDao() {
		return currencyDao;
	}


	public void setCurrencyDao(CurrencyDAO currencyDao) {
		this.currencyDao = currencyDao;
	}


	
}
