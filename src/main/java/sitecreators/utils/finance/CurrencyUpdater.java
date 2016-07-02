package sitecreators.utils.finance;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CurrencyUpdater {
	
	private Map<String,Double> currencyMap = new HashMap<>();;
	
	private URL url;
	
	private CurrencyDAO currencyDao;
	
	public CurrencyUpdater(){
		try{
			url = new URL("http://resources.finance.ua/ru/public/currency-cash.xml");
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public void parse(){
		try( InputStream ins = url.openStream()){
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(ins);
			doc.getDocumentElement().normalize();
			Element orgs =(Element) doc.getElementsByTagName("organizations").item(0);
			NodeList orgsList = orgs.getElementsByTagName("organization");
			Element bank = null;
			for(int i = 0; i<orgsList.getLength();i++){
				Element e = (Element) orgsList.item(i);
				Element title = (Element) e.getElementsByTagName("title").item(0);
				if(title.getAttribute("value").equals("ÓêðÑèááàíê")) bank = e;
			}
			Node curr = bank.getElementsByTagName("currencies").item(0);
			Element currEl = (Element) curr;
			NodeList currs = currEl.getElementsByTagName("c");
			for(int ind = 0; ind < currs.getLength();ind++){
				Element temp = (Element) currs.item(ind);
				for(Country c : Country.values()){
					String code = c.getStringValue();
					if(code.equals(temp.getAttribute("id"))){
						String chart = temp.getAttribute("ar");
						double val = Double.parseDouble(chart);
						currencyMap.put(code, 1/val);
					}
				}
			}
			currencyMap.put("UAH", Double.valueOf(1));
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void update(){
		List<Currency> currencies = currencyDao.getAllCurrencies();
		for(Currency curr : currencies){
			Double val = currencyMap.get(curr.getCountryCode().getStringValue());
			if(val != null) curr.setKoef(val);
		}
		currencyDao.updateCurrencies(currencies);
		Country[] countries = Country.values();
		for(Country country : countries){
			boolean present = false;
			for(Currency curr : currencies){
				if(country.equals(curr.getCountryCode())){ 
					present = true;
					break;
				}
			}	
			if(present) continue;
			Currency newCurrency = new Currency();
			newCurrency.setCountryCode(country);
			double koef = 0;
			switch(country){
				case UAH: koef = 1; break;
				case USD: koef = 0.040349; break;
				case EUR: koef = 0.0362248059 ; break;
				case RUB: koef = 2.57821086 ; break;
				case GBP: koef = 0.0303993069 ; break;
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
