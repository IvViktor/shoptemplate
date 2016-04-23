/**
 * 
 */
package sitecreators.utils.image;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author User
 *
 */
@Entity
public class Image {
	
	@Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
	private long id;
	
	private String imagePath;
	
	private String imgDecs;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getImgDecs() {
		return imgDecs;
	}

	public void setImgDecs(String imgDecs) {
		this.imgDecs = imgDecs;
	}
	
	

}
