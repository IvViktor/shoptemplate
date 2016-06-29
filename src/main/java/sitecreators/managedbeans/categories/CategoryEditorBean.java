package sitecreators.managedbeans.categories;

import java.util.List;

import javax.faces.bean.ManagedBean;

import sitecreators.utils.ApplicationContextUtil;
import sitecreators.utils.category.Category;
import sitecreators.utils.category.CategoryDAO;

//@ManagedBean(name="categoryEditorBean")
public class CategoryEditorBean {
	
	private CategoryDAO categoryDao;
	
	private List<Category> categories;
	
	private String newCategoryTitle;
	
	private Category selectedCategory;
	
	public CategoryEditorBean(){
		categoryDao = (CategoryDAO) ApplicationContextUtil.getApplicationContext().getBean("CategoryDAO");
		try{
			categoryDao.open();
			categories = categoryDao.getAllCategories();
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			categoryDao.close();
		}
	}
	
	public void add(){
		Category category = new Category();
		category.setTitle(newCategoryTitle);
		categories.add(category);
		try{
			categoryDao.open();
			categoryDao.addCategory(category);
		} catch (Exception e){
			e.printStackTrace();
		} finally{
			categoryDao.close();
		}
		newCategoryTitle="";
	}
	
	public void update(){
		selectedCategory.setTitle(newCategoryTitle);
		try{
		categoryDao.open();
		categoryDao.updateCategory(selectedCategory);
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			categoryDao.close();
		}
	}
	
	public void remove(){
		try{
			categoryDao.open();
			categoryDao.removeCategory(selectedCategory);
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			categoryDao.close();
		}
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public String getNewCategoryTitle() {
		return newCategoryTitle;
	}

	public void setNewCategoryTitle(String newCategoryTitle) {
		this.newCategoryTitle = newCategoryTitle;
	}

	public Category getSelectedCategory() {
		return selectedCategory;
	}

	public void setSelectedCategory(Category selectedCategory) {
		this.selectedCategory = selectedCategory;
	}
		
}
