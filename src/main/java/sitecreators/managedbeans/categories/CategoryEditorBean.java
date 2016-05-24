package sitecreators.managedbeans.categories;

import java.util.List;

import javax.faces.bean.ManagedBean;

import sitecreators.utils.ApplicationContextUtil;
import sitecreators.utils.category.Category;
import sitecreators.utils.category.CategoryDAO;

@ManagedBean(name="categoryEditorBean")
public class CategoryEditorBean {
	
	private CategoryDAO categoryDao;
	
	private List<Category> categories;
	
	public CategoryEditorBean(){
		categoryDao = (CategoryDAO) ApplicationContextUtil.getApplicationContext().getBean("CategoryDAO");
		categories = categoryDao.getAllCategories();
	}
	
	public void add(String title){
		Category category = new Category();
		category.setTitle(title);
		categoryDao.addCategory(category);
	}
	
	public void update(Category category){
		categoryDao.updateCategory(category);
	}
	
	public void remove(Category category){
		categoryDao.removeCategory(category);
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
		
}
