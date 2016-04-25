/**
 * 
 */
package sitecreators.utils.image;

/**
 * @author viktor
 *
 */
public interface ImageDAO {
	
	public Image getImage(long id);
	
	public Image getImage(String path);
	
	public void addImage(Image image);
	
	public void removeImage(Image image);
	
}
