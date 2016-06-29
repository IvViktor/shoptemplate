package sitecreators.utils;


import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class FilterServlet implements Filter {

    public FilterServlet() {
    }

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession(false);
		long id = 0;
		try{
			id = (long) session.getAttribute("userID");
		} catch (Exception e){
			id = 0;
		}
		
		if(id != 0) {
			chain.doFilter(request, response);
		}
		else{
			String contextPath = ((HttpServletRequest)request).getContextPath();
	        ((HttpServletResponse) response).sendRedirect(contextPath + "/login.xhtml");
		}
	}
	

	public void init(FilterConfig fConfig) throws ServletException {
		
	}

}
