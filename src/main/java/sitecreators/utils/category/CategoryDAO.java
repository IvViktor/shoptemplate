package sitecreators.utils.category;

import java.util.List;

public interface CategoryDAO {

	public Category getCategory(String title);
	
	public void addCategory(Category category);
	
	public void removeCategory(Category category);
	
	public List<Category> getAllCategories();
	
	public void updateCategory(Category category);
	
}