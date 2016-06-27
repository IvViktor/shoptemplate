package sitecreators.utils;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;

import sitecreators.utils.finance.Country;
import sitecreators.utils.finance.Currency;
import sitecreators.utils.finance.CurrencyDAO;

public class SchedulerService {
	
	private CurrencyDAO currencyDao;
	
	@Scheduled(fixedDelay = (1000 * 60 * 60 * 24))
	public void currencyUpdater(){
		List<Currency> currencies = currencyDao.getAllCurrencies();
		Country[] countries = Country.values();
		for(Country country : countries){
			boolean present = false;
			for(Currency curr : currencies){
				if(country.equals(curr.getCountryCode())){ present = true;break;}
			}	
			if(present) continue;
			Currency newCurrency = new Currency();
			newCurrency.setCountryCode(country);
			double koef = 0;
			switch(country){
				case UAH: koef = 24.887; break;
				case USD: koef = 1; break;
				case EUR: koef = 0.905; break;
				case RUB: koef = 65.078; break;
				case GBP: koef = 0.743273376; break;
			}
			newCurrency.setKoef(koef);
			currencyDao.addCurrency(newCurrency);
		}
	}

	public CurrencyDAO getCurrencyDao() {
		return currencyDao;
	}

	public void setCurrencyDao(CurrencyDAO currencyDao) {
		this.currencyDao = currencyDao;
	}

}
