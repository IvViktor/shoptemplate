package sitecreators.managedbeans.products;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import sitecreators.utils.ApplicationContextUtil;
import sitecreators.utils.category.Category;
import sitecreators.utils.category.CategoryDAO;
import sitecreators.utils.product.Product;
import sitecreators.utils.product.ProductDAO;

@ManagedBean(name="productPanelBean")
public class ProductBriefBean {
	
	private int pagesAmount;
	
	private ProductDAO productDao;
	
	private CategoryDAO categoryDao;
	
	private List<Product> products;
	
	private String selectedCategory;
	
	private String minPrice = null;
	
	private String maxPrice = null;
	
	private String searchText = null;
	
	private List<Category> categories;
	
	private String prodPerPage;
	
	private String pageNumber;
	
	private int totalPages;
	
	public ProductBriefBean(){
		this.productDao = (ProductDAO) ApplicationContextUtil.getApplicationContext().getBean("ProductDAO");
		this.categoryDao =(CategoryDAO) ApplicationContextUtil.getApplicationContext().getBean("CategoryDAO");
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		this.prodPerPage = (String) req.getParameter("ppp");
		this.pageNumber = (String) req.getParameter("page");
		this.minPrice = (String) req.getParameter("minprice");
		this.maxPrice = (String) req.getParameter("maxprice");
		this.selectedCategory = (String) req.getParameter("category");
		int ppp = 20;
		int page = 1;
		int productAmount = 0;
		if(prodPerPage != null) ppp = Integer.parseInt(prodPerPage);
		if(pageNumber != null) page = Integer.parseInt(pageNumber);
		Category filterCategory = null;
		double min=0 ,max=0;
		try{
			productDao.open();
			if(selectedCategory != null){
				categoryDao.open();
				filterCategory = categoryDao.getCategory(selectedCategory);
			}
			try{
				max = Double.parseDouble(maxPrice);
				System.out.println("parsed maxPrice "+max);
			} catch (Exception e){ max = 0;}
			try{
				min = Double.parseDouble(minPrice);
				System.out.println("parsed minPrice"+min);
			} catch (Exception e){ min = 0;}
			if(searchText == null){
				productAmount = ((Number) productDao.getProductsNumber(filterCategory, min, max)).intValue();
				this.products = productDao.getProducts(filterCategory,min,max,(page-1)*ppp,ppp);
			} else {
				this.products = productDao.getProducts(searchText);
				productAmount = ((Number) productDao.getProductsNumber(searchText)).intValue();
			}
			this.totalPages = productAmount / ppp;
			if((productAmount % ppp) > 0) totalPages++;
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			productDao.close();
			categoryDao.close();
		}
		try{
			categoryDao.open();
			this.categories = categoryDao.getAllCategories();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			categoryDao.close();
		}
	}
	
	public void filter(){
		return;
	}
	
	public void reset(){
		selectedCategory=null;
		minPrice = null;
		maxPrice = null;
		searchText = null;
	}
	
	public String details(){
		return "productdetails";
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public String getSelectedCategory() {
		return selectedCategory;
	}

	public void setSelectedCategory(String selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

	public String getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(String minPrice) {
		this.minPrice = minPrice;
	}

	public String getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(String maxPrice) {
		this.maxPrice = maxPrice;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}



	public String getProdPerPage() {
		return prodPerPage;
	}



	public void setProdPerPage(String prodPerPage) {
		this.prodPerPage = prodPerPage;
	}



	public String getPageNumber() {
		return pageNumber;
	}



	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPagesAmount() {
		return pagesAmount;
	}

	public void setPagesAmount(int pagesAmount) {
		this.pagesAmount = pagesAmount;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	
}
