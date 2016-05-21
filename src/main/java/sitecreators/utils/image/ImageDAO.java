/**
 * 
 */
package sitecreators.utils.image;

import javax.servlet.http.Part;

/**
 * @author viktor
 *
 */
public interface ImageDAO {
	
	public Image getImage(long id);
	
	public Image getImage(String path);
	
	public void addImage(Image image);
	
	public void removeImage(Image image);
	
	public String saveImage(Part part,String directory,long userId);
	
}
